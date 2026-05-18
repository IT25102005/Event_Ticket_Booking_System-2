package com.example.backend.ticket.service;

import com.example.backend.ticket.dto.PriceCalculationDTO;

public interface PricingService {
    PriceCalculationDTO calculateLivePrice(PriceCalculationDTO calculationDTO);
    PriceCalculationDTO getTicketPricingDetails(Long ticketTypeId);
}
