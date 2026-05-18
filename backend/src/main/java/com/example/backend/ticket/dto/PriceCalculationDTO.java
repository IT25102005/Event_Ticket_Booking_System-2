package com.example.backend.ticket.dto;

import com.example.backend.ticket.model.TicketCategory;

public class PriceCalculationDTO {
    private TicketCategory ticketCategory;
    private Double basePrice;
    private Double discountPercentage;
    private Double serviceFee;
    private Double taxPercentage;
    private Double extraFee; // For VIP or Standard
    private Double discountAmount;
    private Double taxAmount;
    private Double finalPrice;

    // Getters and Setters
    public TicketCategory getTicketCategory() { return ticketCategory; }
    public void setTicketCategory(TicketCategory ticketCategory) { this.ticketCategory = ticketCategory; }
    public Double getBasePrice() { return basePrice; }
    public void setBasePrice(Double basePrice) { this.basePrice = basePrice; }
    public Double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(Double discountPercentage) { this.discountPercentage = discountPercentage; }
    public Double getServiceFee() { return serviceFee; }
    public void setServiceFee(Double serviceFee) { this.serviceFee = serviceFee; }
    public Double getTaxPercentage() { return taxPercentage; }
    public void setTaxPercentage(Double taxPercentage) { this.taxPercentage = taxPercentage; }
    public Double getExtraFee() { return extraFee; }
    public void setExtraFee(Double extraFee) { this.extraFee = extraFee; }
    public Double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Double discountAmount) { this.discountAmount = discountAmount; }
    public Double getTaxAmount() { return taxAmount; }
    public void setTaxAmount(Double taxAmount) { this.taxAmount = taxAmount; }
    public Double getFinalPrice() { return finalPrice; }
    public void setFinalPrice(Double finalPrice) { this.finalPrice = finalPrice; }
}
