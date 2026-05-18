package com.example.backend.ticket.service;

import com.example.backend.ticket.dto.PriceCalculationDTO;
import com.example.backend.ticket.exception.TicketNotFoundException;
import com.example.backend.ticket.model.*;
import com.example.backend.ticket.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PricingServiceImpl implements PricingService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public PriceCalculationDTO calculateLivePrice(PriceCalculationDTO dto) {
        double discountAmount = dto.getBasePrice() * (dto.getDiscountPercentage() / 100);
        double taxAmount = dto.getBasePrice() * (dto.getTaxPercentage() / 100);
        double extraFee = dto.getExtraFee() != null ? dto.getExtraFee() : 0.0;
        
        double finalPrice = dto.getBasePrice() + dto.getServiceFee() + taxAmount + extraFee - discountAmount;
        
        dto.setDiscountAmount(discountAmount);
        dto.setTaxAmount(taxAmount);
        dto.setFinalPrice(Math.max(0, finalPrice));
        
        return dto;
    }

    @Override
    public PriceCalculationDTO getTicketPricingDetails(Long ticketTypeId) {
        TicketType ticket = ticketRepository.findById(ticketTypeId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));
        
        PriceCalculationDTO dto = new PriceCalculationDTO();
        dto.setTicketCategory(ticket.getTicketCategory());
        dto.setBasePrice(ticket.getBasePrice());
        dto.setDiscountPercentage(ticket.getDiscountPercentage());
        dto.setServiceFee(ticket.getServiceFee());
        dto.setTaxPercentage(ticket.getTaxPercentage());
        
        double extra = 0.0;
        if (ticket instanceof VipTicket vip) {
            extra = vip.getVipServiceFee();
        } else if (ticket instanceof StandardTicket standard) {
            extra = standard.getStandardServiceFee();
        }
        
        dto.setExtraFee(extra);
        return calculateLivePrice(dto);
    }
}
