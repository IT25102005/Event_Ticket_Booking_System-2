package com.example.backend.ticket.service;

import com.example.backend.ticket.dto.*;
import com.example.backend.ticket.exception.*;
import com.example.backend.ticket.model.*;
import com.example.backend.ticket.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public TicketResponseDTO createTicket(TicketRequestDTO request) {
        validateRequest(request);
        
        TicketType ticket;
        switch (request.getTicketCategory()) {
            case VIP:
                VipTicket vip = new VipTicket();
                vip.setVipBenefits(request.getVipBenefits());
                vip.setLoungeAccess(request.getLoungeAccess() != null ? request.getLoungeAccess() : false);
                vip.setComplimentaryItems(request.getComplimentaryItems());
                vip.setVipServiceFee(request.getVipServiceFee() != null ? request.getVipServiceFee() : 0.0);
                ticket = vip;
                break;
            case STANDARD:
                StandardTicket standard = new StandardTicket();
                standard.setStandardServiceFee(request.getStandardServiceFee() != null ? request.getStandardServiceFee() : 0.0);
                standard.setSeatPreferenceAllowed(request.getSeatPreferenceAllowed() != null ? request.getSeatPreferenceAllowed() : false);
                ticket = standard;
                break;
            case EARLY_BIRD:
                EarlyBirdTicket early = new EarlyBirdTicket();
                early.setEarlyBirdDiscountPercentage(request.getEarlyBirdDiscountPercentage() != null ? request.getEarlyBirdDiscountPercentage() : 0.0);
                early.setOfferEndDate(request.getOfferEndDate());
                ticket = early;
                break;
            default:
                // For Student and Group, we just use Standard for now but with their category
                StandardTicket other = new StandardTicket();
                other.setStandardServiceFee(0.0);
                other.setSeatPreferenceAllowed(false);
                ticket = other;
                break;
        }

        mapCommonFields(request, ticket);
        ticket.calculateFinalPrice();
        
        TicketType savedTicket = ticketRepository.save(ticket);
        return mapToResponseDTO(savedTicket);
    }

    @Override
    public List<TicketResponseDTO> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TicketResponseDTO getTicketById(Long id) {
        TicketType ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id: " + id));
        return mapToResponseDTO(ticket);
    }

    @Override
    public List<TicketResponseDTO> getTicketsByEventId(Long eventId) {
        return ticketRepository.findByEventId(eventId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketResponseDTO> searchTickets(String keyword, Long eventId, TicketCategory category, TicketStatus status) {
        return ticketRepository.searchTickets(keyword, eventId, category, status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TicketResponseDTO updateTicket(Long id, TicketRequestDTO request) {
        TicketType ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id: " + id));
        
        validateRequest(request);
        mapCommonFields(request, ticket);
        
        if (ticket instanceof VipTicket vip) {
            vip.setVipBenefits(request.getVipBenefits());
            vip.setLoungeAccess(request.getLoungeAccess());
            vip.setComplimentaryItems(request.getComplimentaryItems());
            vip.setVipServiceFee(request.getVipServiceFee());
        } else if (ticket instanceof StandardTicket standard) {
            standard.setStandardServiceFee(request.getStandardServiceFee());
            standard.setSeatPreferenceAllowed(request.getSeatPreferenceAllowed());
        } else if (ticket instanceof EarlyBirdTicket early) {
            early.setEarlyBirdDiscountPercentage(request.getEarlyBirdDiscountPercentage());
            early.setOfferEndDate(request.getOfferEndDate());
        }

        ticket.calculateFinalPrice();
        TicketType updatedTicket = ticketRepository.save(ticket);
        return mapToResponseDTO(updatedTicket);
    }

    @Override
    public TicketResponseDTO updatePrice(Long id, Double newBasePrice) {
        if (newBasePrice < 0) throw new InvalidTicketPriceException("Price cannot be negative");
        TicketType ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id: " + id));
        ticket.setBasePrice(newBasePrice);
        ticket.calculateFinalPrice();
        return mapToResponseDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponseDTO updateQuantity(Long id, Integer additionalQuantity) {
        TicketType ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id: " + id));
        ticket.setTotalQuantity(ticket.getTotalQuantity() + additionalQuantity);
        ticket.calculateAvailableQuantity();
        return mapToResponseDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponseDTO activateTicket(Long id) {
        TicketType ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id: " + id));
        ticket.setStatus(TicketStatus.ACTIVE);
        return mapToResponseDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponseDTO deactivateTicket(Long id) {
        TicketType ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id: " + id));
        ticket.setStatus(TicketStatus.INACTIVE);
        return mapToResponseDTO(ticketRepository.save(ticket));
    }

    @Override
    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new TicketNotFoundException("Ticket not found with id: " + id);
        }
        ticketRepository.deleteById(id);
    }

    @Override
    public TicketAvailabilityDTO getTicketAvailability(Long id) {
        TicketType ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id: " + id));
        TicketAvailabilityDTO dto = new TicketAvailabilityDTO();
        dto.setTicketTypeId(ticket.getTicketTypeId());
        dto.setTicketName(ticket.getTicketName());
        dto.setTotalQuantity(ticket.getTotalQuantity());
        dto.setSoldQuantity(ticket.getSoldQuantity());
        dto.setAvailableQuantity(ticket.getAvailableQuantity());
        dto.setStatus(ticket.getStatus());
        return dto;
    }

    private void validateRequest(TicketRequestDTO request) {
        if (request.getBasePrice() < 0) throw new InvalidTicketPriceException("Base price cannot be negative");
        if (request.getTotalQuantity() < 0) throw new InvalidTicketPriceException("Quantity cannot be negative");
        if (request.getSaleEndDate().isBefore(request.getSaleStartDate())) {
            throw new InvalidTicketPriceException("Sale end date cannot be before start date");
        }
    }

    private void mapCommonFields(TicketRequestDTO request, TicketType ticket) {
        ticket.setEventId(request.getEventId());
        ticket.setTicketName(request.getTicketName());
        ticket.setDescription(request.getDescription());
        ticket.setTicketCategory(request.getTicketCategory());
        ticket.setBasePrice(request.getBasePrice());
        ticket.setDiscountPercentage(request.getDiscountPercentage() != null ? request.getDiscountPercentage() : 0.0);
        ticket.setServiceFee(request.getServiceFee() != null ? request.getServiceFee() : 0.0);
        ticket.setTaxPercentage(request.getTaxPercentage() != null ? request.getTaxPercentage() : 0.0);
        ticket.setTotalQuantity(request.getTotalQuantity());
        ticket.setMinPurchaseLimit(request.getMinPurchaseLimit() != null ? request.getMinPurchaseLimit() : 1);
        ticket.setMaxPurchaseLimit(request.getMaxPurchaseLimit());
        ticket.setSaleStartDate(request.getSaleStartDate());
        ticket.setSaleEndDate(request.getSaleEndDate());
    }

    private TicketResponseDTO mapToResponseDTO(TicketType ticket) {
        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setTicketTypeId(ticket.getTicketTypeId());
        dto.setEventId(ticket.getEventId());
        dto.setTicketName(ticket.getTicketName());
        dto.setDescription(ticket.getDescription());
        dto.setTicketCategory(ticket.getTicketCategory());
        dto.setBasePrice(ticket.getBasePrice());
        dto.setFinalPrice(ticket.getFinalPrice());
        dto.setTotalQuantity(ticket.getTotalQuantity());
        dto.setSoldQuantity(ticket.getSoldQuantity());
        dto.setAvailableQuantity(ticket.getAvailableQuantity());
        dto.setStatus(ticket.getStatus());
        dto.setBenefits(ticket.getTicketBenefits());
        dto.setSaleStartDate(ticket.getSaleStartDate());
        dto.setSaleEndDate(ticket.getSaleEndDate());
        return dto;
    }
}
