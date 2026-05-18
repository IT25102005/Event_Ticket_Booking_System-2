package com.example.backend.venue.dto;

import com.example.backend.venue.model.SeatStatus;
import com.example.backend.venue.model.SeatType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * =====================================================
 * SeatBulkRequestDTO.java
 * =====================================================
 * Used to create multiple seats (a block) at once.
 * e.g. Section A, Row B, seats 1 to 20.
 *
 * OOP Concept: ENCAPSULATION — Bundles all block creation
 *   parameters into a single safe object.
 * =====================================================
 */
public class SeatBulkRequestDTO {

    @NotBlank(message = "Section name cannot be empty")
    private String sectionName;

    @NotBlank(message = "Row name cannot be empty")
    private String rowName;

    @NotNull(message = "Starting seat number is required")
    @Min(value = 1, message = "Starting seat number must be at least 1")
    private Integer startingSeatNumber;

    @NotNull(message = "Number of seats is required")
    @Min(value = 1, message = "Number of seats must be at least 1")
    @Max(value = 200, message = "Cannot add more than 200 seats at once")
    private Integer numberOfSeats;

    @NotNull(message = "Seat type is required")
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

    public Integer getStartingSeatNumber() { return startingSeatNumber; }
    public void setStartingSeatNumber(Integer startingSeatNumber) { this.startingSeatNumber = startingSeatNumber; }

    public Integer getNumberOfSeats() { return numberOfSeats; }
    public void setNumberOfSeats(Integer numberOfSeats) { this.numberOfSeats = numberOfSeats; }

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
