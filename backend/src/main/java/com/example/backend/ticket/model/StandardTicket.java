package com.example.backend.ticket.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("STANDARD")
public class StandardTicket extends TicketType {

    private Double standardServiceFee;
    private Boolean seatPreferenceAllowed;

    @Override
    public void calculateFinalPrice() {
        double discountAmount = getBasePrice() * (getDiscountPercentage() / 100);
        double taxAmount = getBasePrice() * (getTaxPercentage() / 100);
        // Final Price = Base Price + Service Fee + Tax + Standard Service Fee - Discount
        double total = getBasePrice() + getServiceFee() + taxAmount + (standardServiceFee != null ? standardServiceFee : 0.0) - discountAmount;
        setFinalPrice(Math.max(0, total));
    }

    @Override
    public String getTicketBenefits() {
        return "Standard Ticket" + (seatPreferenceAllowed ? " | Seat Preference Allowed" : "");
    }

    // Getters and Setters
    public Double getStandardServiceFee() { return standardServiceFee; }
    public void setStandardServiceFee(Double standardServiceFee) { this.standardServiceFee = standardServiceFee; }

    public Boolean getSeatPreferenceAllowed() { return seatPreferenceAllowed; }
    public void setSeatPreferenceAllowed(Boolean seatPreferenceAllowed) { this.seatPreferenceAllowed = seatPreferenceAllowed; }
}
