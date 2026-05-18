package com.example.backend.venue.dto;

import com.example.backend.venue.model.SeatStatus;
import com.example.backend.venue.model.SeatType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * =====================================================
 * SeatRequestDTO.java
 * =====================================================
 * OOP Concept: ENCAPSULATION — The controller receives this DTO
 *   instead of the raw Seat entity.
 *   Validation annotations protect internal data integrity.
 * =====================================================
 */
public class SeatRequestDTO {

    @NotBlank(message = "Section name cannot be empty")
    private String sectionName;

    @NotBlank(message = "Row name cannot be empty")
    private String rowName;

    @NotBlank(message = "Seat number cannot be empty")
    private String seatNumber;

    @NotNull(message = "Seat type is required (REGULAR or VIP)")
    private SeatType seatType;

    private SeatStatus seatStatus = SeatStatus.AVAILABLE;

    private BigDecimal basePrice = BigDecimal.ZERO;

    // ---- RegularSeat specific fields ----
    private Boolean standardView;
    private BigDecimal regularServiceFee;

    // ---- VIPSeat specific fields ----
    private Boolean loungeAccess;
    private Boolean complimentaryDrink;
    private BigDecimal vipServiceFee;

    // ---- Getters and Setters ----

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
}
