package com.example.backend.ticket.dto;

import com.example.backend.ticket.model.TicketCategory;
import java.time.LocalDateTime;

public class TicketRequestDTO {
    private Long eventId;
    private String ticketName;
    private String description;
    private TicketCategory ticketCategory;
    private Double basePrice;
    private Double discountPercentage;
    private Double serviceFee;
    private Double taxPercentage;
    private Integer totalQuantity;
    private Integer minPurchaseLimit;
    private Integer maxPurchaseLimit;
    private LocalDateTime saleStartDate;
    private LocalDateTime saleEndDate;

    // Subclass specific fields
    private String vipBenefits;
    private Boolean loungeAccess;
    private String complimentaryItems;
    private Double vipServiceFee;
    private Double standardServiceFee;
    private Boolean seatPreferenceAllowed;
    private Double earlyBirdDiscountPercentage;
    private LocalDateTime offerEndDate;

    // Getters and Setters
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public String getTicketName() { return ticketName; }
    public void setTicketName(String ticketName) { this.ticketName = ticketName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
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
    public Integer getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }
    public Integer getMinPurchaseLimit() { return minPurchaseLimit; }
    public void setMinPurchaseLimit(Integer minPurchaseLimit) { this.minPurchaseLimit = minPurchaseLimit; }
    public Integer getMaxPurchaseLimit() { return maxPurchaseLimit; }
    public void setMaxPurchaseLimit(Integer maxPurchaseLimit) { this.maxPurchaseLimit = maxPurchaseLimit; }
    public LocalDateTime getSaleStartDate() { return saleStartDate; }
    public void setSaleStartDate(LocalDateTime saleStartDate) { this.saleStartDate = saleStartDate; }
    public LocalDateTime getSaleEndDate() { return saleEndDate; }
    public void setSaleEndDate(LocalDateTime saleEndDate) { this.saleEndDate = saleEndDate; }
    public String getVipBenefits() { return vipBenefits; }
    public void setVipBenefits(String vipBenefits) { this.vipBenefits = vipBenefits; }
    public Boolean getLoungeAccess() { return loungeAccess; }
    public void setLoungeAccess(Boolean loungeAccess) { this.loungeAccess = loungeAccess; }
    public String getComplimentaryItems() { return complimentaryItems; }
    public void setComplimentaryItems(String complimentaryItems) { this.complimentaryItems = complimentaryItems; }
    public Double getVipServiceFee() { return vipServiceFee; }
    public void setVipServiceFee(Double vipServiceFee) { this.vipServiceFee = vipServiceFee; }
    public Double getStandardServiceFee() { return standardServiceFee; }
    public void setStandardServiceFee(Double standardServiceFee) { this.standardServiceFee = standardServiceFee; }
    public Boolean getSeatPreferenceAllowed() { return seatPreferenceAllowed; }
    public void setSeatPreferenceAllowed(Boolean seatPreferenceAllowed) { this.seatPreferenceAllowed = seatPreferenceAllowed; }
    public Double getEarlyBirdDiscountPercentage() { return earlyBirdDiscountPercentage; }
    public void setEarlyBirdDiscountPercentage(Double earlyBirdDiscountPercentage) { this.earlyBirdDiscountPercentage = earlyBirdDiscountPercentage; }
    public LocalDateTime getOfferEndDate() { return offerEndDate; }
    public void setOfferEndDate(LocalDateTime offerEndDate) { this.offerEndDate = offerEndDate; }
}
