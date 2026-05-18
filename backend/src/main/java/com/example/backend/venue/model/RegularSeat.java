package com.example.backend.venue.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * =====================================================
 * RegularSeat.java — Subclass of Seat
 * =====================================================
 * OOP Concept: INHERITANCE — Extends Seat (parent class).
 *   Inherits all seat fields and adds regular-specific fields.
 *
 * OOP Concept: POLYMORPHISM — Overrides calculateFinalPrice()
 *   and getSeatBenefits() to provide Regular-specific behavior.
 *
 * JPA: @DiscriminatorValue("REGULAR") — stored in seat_category column.
 * =====================================================
 */
@Entity
@DiscriminatorValue("REGULAR")
public class RegularSeat extends Seat {

    // ---- RegularSeat-specific fields ----

    private Boolean standardView = true;     // Does it have a standard stage view?

    private BigDecimal regularServiceFee = BigDecimal.ZERO;  // Extra service fee

    // =====================================================
    // OOP Concept: POLYMORPHISM
    // Overrides parent method — RegularSeat adds regularServiceFee
    // =====================================================
    @Override
    public BigDecimal calculateFinalPrice() {
        BigDecimal base = getBasePrice() != null ? getBasePrice() : BigDecimal.ZERO;
        BigDecimal fee = regularServiceFee != null ? regularServiceFee : BigDecimal.ZERO;
        return base.add(fee);  // Final = Base + Service Fee
    }

    @Override
    public String getSeatBenefits() {
        return "Regular seat" +
               (standardView != null && standardView ? " | Standard stage view" : "") +
               " | Service fee: LKR " + (regularServiceFee != null ? regularServiceFee : "0.00");
    }

    // ---- Getters and Setters (Encapsulation) ----

    public Boolean getStandardView() { return standardView; }
    public void setStandardView(Boolean standardView) { this.standardView = standardView; }

    public BigDecimal getRegularServiceFee() { return regularServiceFee; }
    public void setRegularServiceFee(BigDecimal regularServiceFee) { this.regularServiceFee = regularServiceFee; }
}
