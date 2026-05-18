package com.example.backend.ticket.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("EARLY_BIRD")
public class EarlyBirdTicket extends TicketType {

    private Double earlyBirdDiscountPercentage;
    private LocalDateTime offerEndDate;

    @Override
    public void calculateFinalPrice() {
        double currentDiscount = getDiscountPercentage();
        
        // Apply extra early bird discount if before offer end date
        if (offerEndDate != null && LocalDateTime.now().isBefore(offerEndDate)) {
            currentDiscount += (earlyBirdDiscountPercentage != null ? earlyBirdDiscountPercentage : 0.0);
        }

        double discountAmount = getBasePrice() * (currentDiscount / 100);
        double taxAmount = getBasePrice() * (getTaxPercentage() / 100);
        
        double total = getBasePrice() + getServiceFee() + taxAmount - discountAmount;
        setFinalPrice(Math.max(0, total));
    }

    @Override
    public String getTicketBenefits() {
        return "Early Bird Discount: " + earlyBirdDiscountPercentage + "% valid until " + offerEndDate;
    }

    // Getters and Setters
    public Double getEarlyBirdDiscountPercentage() { return earlyBirdDiscountPercentage; }
    public void setEarlyBirdDiscountPercentage(Double earlyBirdDiscountPercentage) { this.earlyBirdDiscountPercentage = earlyBirdDiscountPercentage; }

    public LocalDateTime getOfferEndDate() { return offerEndDate; }
    public void setOfferEndDate(LocalDateTime offerEndDate) { this.offerEndDate = offerEndDate; }
}
