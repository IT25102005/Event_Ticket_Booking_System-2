package com.example.backend.ticket.controller;

import com.example.backend.ticket.dto.PriceCalculationDTO;
import com.example.backend.ticket.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pricing")
@CrossOrigin(origins = "*")
public class PricingController {

    @Autowired
    private PricingService pricingService;

    @PostMapping("/calculate")
    public ResponseEntity<PriceCalculationDTO> calculatePrice(@RequestBody PriceCalculationDTO request) {
        return ResponseEntity.ok(pricingService.calculateLivePrice(request));
    }

    @GetMapping("/ticket/{ticketTypeId}")
    public ResponseEntity<PriceCalculationDTO> getTicketPricing(@PathVariable Long ticketTypeId) {
        return ResponseEntity.ok(pricingService.getTicketPricingDetails(ticketTypeId));
    }
}
