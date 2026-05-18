package com.example.backend.venue.dto;

import com.example.backend.venue.model.SeatStatus;
import com.example.backend.venue.model.SeatType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * =====================================================
 * SeatResponseDTO.java
 * =====================================================
 * OOP Concept: ENCAPSULATION — Exposes only the data needed
 *   by the frontend. Hides JPA entity internals.
 *
 * Includes seatBenefits — the result of the polymorphic
 *   getSeatBenefits() call from RegularSeat or VIPSeat.
 * =====================================================
 */
public class SeatResponseDTO {

    private Long seatId;
    private Long venueId;
    private String venueName;

    private String sectionName;
    private String rowName;
    private String seatNumber;

    private SeatType seatType;
    private SeatStatus seatStatus;

    private BigDecimal basePrice;
    private BigDecimal finalPrice;

    // Polymorphic result — different for RegularSeat vs VIPSeat
    private String seatBenefits;

    // Seat category discriminator: "REGULAR" or "VIP"
    private String seatCategory;

    // ---- RegularSeat fields ----
    private Boolean standardView;
    private BigDecimal regularServiceFee;

    // ---- VIPSeat fields ----
    private Boolean loungeAccess;
    private Boolean complimentaryDrink;
    private BigDecimal vipServiceFee;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ---- Getters and Setters ----

    public Long getSeatId() { return seatId; }
    public void setSeatId(Long seatId) { this.seatId = seatId; }

    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }

    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }

    public String getSectionName() { return sectionName; }
    public void setSectionName(String sectionName) { this.sectionName = sectionName; }

    public String getRowName() { return rowName; }
    public void setRowName(String rowName) { this.rowName = rowName; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public SeatType getSeatType() { return seatType; }
    public void setSeatType(SeatType seatType) { this.seatType = seatType; }

    public SeatStatus getSeatStatus() { return seatStatus; }
    public void setSeatStatus(SeatStatus seatStatus) { this.seatStatus = seatStatus; }

    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public BigDecimal getFinalPrice() { return finalPrice; }
    public void setFinalPrice(BigDecimal finalPrice) { this.finalPrice = finalPrice; }

    public String getSeatBenefits() { return seatBenefits; }
    public void setSeatBenefits(String seatBenefits) { this.seatBenefits = seatBenefits; }

    public String getSeatCategory() { return seatCategory; }
    public void setSeatCategory(String seatCategory) { this.seatCategory = seatCategory; }

    public Boolean getStandardView() { return standardView; }
    public void setStandardView(Boolean standardView) { this.standardView = standardView; }

    public BigDecimal getRegularServiceFee() { return regularServiceFee; }
    public void setRegularServiceFee(BigDecimal regularServiceFee) { this.regularServiceFee = regularServiceFee; }

    public Boolean getLoungeAccess() { return loungeAccess; }
    public void setLoungeAccess(Boolean loungeAccess) { this.loungeAccess = loungeAccess; }

    public Boolean getComplimentaryDrink() { return complimentaryDrink; }
    public void setComplimentaryDrink(Boolean complimentaryDrink) { this.complimentaryDrink = complimentaryDrink; }

    public BigDecimal getVipServiceFee() { return vipServiceFee; }
    public void setVipServiceFee(BigDecimal vipServiceFee) { this.vipServiceFee = vipServiceFee; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
