package com.example.backend.venue.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * =====================================================
 * VIPSeat.java — Subclass of Seat
 * =====================================================
 * OOP Concept: INHERITANCE — Extends Seat (parent class).
 *   Inherits all seat fields and adds VIP-specific fields.
 *
 * OOP Concept: POLYMORPHISM — Overrides calculateFinalPrice()
 *   and getSeatBenefits() to provide VIP-specific behavior.
 *
 * JPA: @DiscriminatorValue("VIP") — stored in seat_category column.
 * =====================================================
 */
@Entity
@DiscriminatorValue("VIP")
public class VIPSeat extends Seat {

    // ---- VIPSeat-specific fields ----

    private Boolean loungeAccess = false;          // Access to VIP lounge?

    private Boolean complimentaryDrink = false;    // Free drinks included?

    private BigDecimal vipServiceFee = BigDecimal.ZERO;  // Premium service fee

    // =====================================================
    // OOP Concept: POLYMORPHISM
    // Overrides parent method — VIPSeat adds vipServiceFee
    // VIP seats always cost more than Regular seats
    // =====================================================
    @Override
    public BigDecimal calculateFinalPrice() {
        BigDecimal base = getBasePrice() != null ? getBasePrice() : BigDecimal.ZERO;
        BigDecimal fee = vipServiceFee != null ? vipServiceFee : BigDecimal.ZERO;
        return base.add(fee);  // Final = Base + VIP Service Fee
    }

    @Override
    public String getSeatBenefits() {
        StringBuilder benefits = new StringBuilder("VIP seat");
        if (loungeAccess != null && loungeAccess) benefits.append(" | Lounge Access");
        if (complimentaryDrink != null && complimentaryDrink) benefits.append(" | Complimentary Drink");
        benefits.append(" | VIP Service Fee: LKR ").append(vipServiceFee != null ? vipServiceFee : "0.00");
        return benefits.toString();
    }

    // ---- Getters and Setters (Encapsulation) ----

    public Boolean getLoungeAccess() { return loungeAccess; }
    public void setLoungeAccess(Boolean loungeAccess) { this.loungeAccess = loungeAccess; }

    public Boolean getComplimentaryDrink() { return complimentaryDrink; }
    public void setComplimentaryDrink(Boolean complimentaryDrink) { this.complimentaryDrink = complimentaryDrink; }

    public BigDecimal getVipServiceFee() { return vipServiceFee; }
    public void setVipServiceFee(BigDecimal vipServiceFee) { this.vipServiceFee = vipServiceFee; }
}
