package com.example.backend.ticket.service;

import com.example.backend.ticket.dto.TicketAvailabilityDTO;
import com.example.backend.ticket.dto.TicketRequestDTO;
import com.example.backend.ticket.dto.TicketResponseDTO;
import com.example.backend.ticket.model.TicketCategory;
import com.example.backend.ticket.model.TicketStatus;
import java.util.List;

public interface TicketService {
    TicketResponseDTO createTicket(TicketRequestDTO request);
    List<TicketResponseDTO> getAllTickets();
    TicketResponseDTO getTicketById(Long id);
    List<TicketResponseDTO> getTicketsByEventId(Long eventId);
    List<TicketResponseDTO> searchTickets(String keyword, Long eventId, TicketCategory category, TicketStatus status);
    TicketResponseDTO updateTicket(Long id, TicketRequestDTO request);
    TicketResponseDTO updatePrice(Long id, Double newBasePrice);
    TicketResponseDTO updateQuantity(Long id, Integer additionalQuantity);
    TicketResponseDTO activateTicket(Long id);
    TicketResponseDTO deactivateTicket(Long id);
    void deleteTicket(Long id);
    TicketAvailabilityDTO getTicketAvailability(Long id);
}
