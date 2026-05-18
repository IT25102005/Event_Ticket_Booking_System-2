package com.example.backend.venue.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * =====================================================
 * Seat.java — Base Entity Class for Seats
 * =====================================================
 * OOP Concept: INHERITANCE — This is the parent class.
 *   RegularSeat and VIPSeat both extend this class.
 *
 * OOP Concept: ENCAPSULATION — All fields are private.
 *   Access only via getters/setters.
 *
 * OOP Concept: POLYMORPHISM — getSeatBenefits() and calculateFinalPrice()
 *   are overridden in subclasses.
 *
 * JPA: SINGLE_TABLE inheritance — all seat types share one DB table.
 *   A discriminator column "seat_category" stores the type.
 *
 * Relationship: Many Seats belong to One Venue (@ManyToOne)
 * =====================================================
 */
@Entity
@Table(name = "seats",
       uniqueConstraints = @UniqueConstraint(
           columnNames = {"venue_id", "section_name", "row_name", "seat_number"},
           name = "uq_venue_section_row_seat"
       ))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "seat_category", discriminatorType = DiscriminatorType.STRING)
public class Seat {

    // ---- Primary Key ----
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    // ---- Many Seats belong to One Venue ----
    @com.fasterxml.jackson.annotation.JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    // ---- Seat Location Fields ----
    @Column(name = "section_name", nullable = false, length = 50)
    private String sectionName;

    @Column(name = "row_name", nullable = false, length = 20)
    private String rowName;

    @Column(name = "seat_number", nullable = false, length = 20)
    private String seatNumber;

    // ---- Seat Classification ----
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatType seatType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus seatStatus = SeatStatus.AVAILABLE;

    // ---- Pricing ----
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal finalPrice = BigDecimal.ZERO;

    // ---- Timestamps ----
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ---- Lifecycle Hooks ----
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        // Auto-calculate final price on creation
        this.finalPrice = calculateFinalPrice();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        // Recalculate final price on update
        this.finalPrice = calculateFinalPrice();
    }

    // =====================================================
    // OOP Concept: POLYMORPHISM
    // These methods are overridden in RegularSeat and VIPSeat
    // to calculate price and describe benefits differently.
    // =====================================================

    /**
     * Returns the final price of the seat.
     * Overridden in RegularSeat and VIPSeat to add service fees.
     */
    public BigDecimal calculateFinalPrice() {
        return basePrice; // Default: final price = base price
    }

    /**
     * Returns a description of seat benefits.
     * Overridden in subclasses for polymorphic behavior.
     */
    public String getSeatBenefits() {
        return "Standard seat access";
    }

    // =====================================================
    // ENCAPSULATION: Getters and Setters
    // =====================================================

    public Long getSeatId() { return seatId; }
    public void setSeatId(Long seatId) { this.seatId = seatId; }

    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }

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
    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
        this.finalPrice = calculateFinalPrice(); // Recalculate when base price changes
    }

    public BigDecimal getFinalPrice() { return finalPrice; }
    public void setFinalPrice(BigDecimal finalPrice) { this.finalPrice = finalPrice; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
