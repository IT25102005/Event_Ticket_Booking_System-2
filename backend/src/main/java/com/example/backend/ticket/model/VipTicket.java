package com.example.backend.ticket.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("VIP")
public class VipTicket extends TicketType {

    private String vipBenefits;
    private Boolean loungeAccess;
    private String complimentaryItems;
    private Double vipServiceFee;

    @Override
    public void calculateFinalPrice() {
        double discountAmount = getBasePrice() * (getDiscountPercentage() / 100);
        double taxAmount = getBasePrice() * (getTaxPercentage() / 100);
        // Final Price = Base Price + Service Fee + Tax + VIP Service Fee - Discount
        double total = getBasePrice() + getServiceFee() + taxAmount + (vipServiceFee != null ? vipServiceFee : 0.0) - discountAmount;
        setFinalPrice(Math.max(0, total));
    }

    @Override
    public String getTicketBenefits() {
        return "VIP Benefits: " + vipBenefits + (loungeAccess ? " | Lounge Access Included" : "") + " | Items: " + complimentaryItems;
    }

    // Getters and Setters
    public String getVipBenefits() { return vipBenefits; }
    public void setVipBenefits(String vipBenefits) { this.vipBenefits = vipBenefits; }

    public Boolean getLoungeAccess() { return loungeAccess; }
    public void setLoungeAccess(Boolean loungeAccess) { this.loungeAccess = loungeAccess; }

    public String getComplimentaryItems() { return complimentaryItems; }
    public void setComplimentaryItems(String complimentaryItems) { this.complimentaryItems = complimentaryItems; }

    public Double getVipServiceFee() { return vipServiceFee; }
    public void setVipServiceFee(Double vipServiceFee) { this.vipServiceFee = vipServiceFee; }
}
