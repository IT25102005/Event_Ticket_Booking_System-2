package com.example.backend.ticket.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ticket_type", discriminatorType = DiscriminatorType.STRING)
public abstract class TicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketTypeId;

    @Column(nullable = false)
    private Long eventId; // Will connect to Event Management module later

    @Column(nullable = false)
    private String ticketName;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketCategory ticketCategory;

    @Column(nullable = false)
    private Double basePrice;

    private Double discountPercentage = 0.0;
    private Double serviceFee = 0.0;
    private Double taxPercentage = 0.0;
    private Double finalPrice;

    @Column(nullable = false)
    private Integer totalQuantity;

    private Integer soldQuantity = 0;
    private Integer availableQuantity;

    private Integer minPurchaseLimit = 1;
    private Integer maxPurchaseLimit;

    @Column(nullable = false)
    private LocalDateTime saleStartDate;

    @Column(nullable = false)
    private LocalDateTime saleEndDate;

    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.ACTIVE;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculateAvailableQuantity();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateAvailableQuantity();
    }

    public void calculateAvailableQuantity() {
        this.availableQuantity = this.totalQuantity - this.soldQuantity;
        if (this.availableQuantity <= 0) {
            this.status = TicketStatus.SOLD_OUT;
        }
    }

    // Abstract method for Polymorphism
    public abstract void calculateFinalPrice();

    public abstract String getTicketBenefits();

    // Getters and Setters (Encapsulation)
    public Long getTicketTypeId() { return ticketTypeId; }
    public void setTicketTypeId(Long ticketTypeId) { this.ticketTypeId = ticketTypeId; }

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

    public Double getFinalPrice() { return finalPrice; }
    public void setFinalPrice(Double finalPrice) { this.finalPrice = finalPrice; }

    public Integer getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }

    public Integer getSoldQuantity() { return soldQuantity; }
    public void setSoldQuantity(Integer soldQuantity) { this.soldQuantity = soldQuantity; }

    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }

    public Integer getMinPurchaseLimit() { return minPurchaseLimit; }
    public void setMinPurchaseLimit(Integer minPurchaseLimit) { this.minPurchaseLimit = minPurchaseLimit; }

    public Integer getMaxPurchaseLimit() { return maxPurchaseLimit; }
    public void setMaxPurchaseLimit(Integer maxPurchaseLimit) { this.maxPurchaseLimit = maxPurchaseLimit; }

    public LocalDateTime getSaleStartDate() { return saleStartDate; }
    public void setSaleStartDate(LocalDateTime saleStartDate) { this.saleStartDate = saleStartDate; }

    public LocalDateTime getSaleEndDate() { return saleEndDate; }
    public void setSaleEndDate(LocalDateTime saleEndDate) { this.saleEndDate = saleEndDate; }

    public TicketStatus getStatus() { return status; }
    public void setStatus(TicketStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
