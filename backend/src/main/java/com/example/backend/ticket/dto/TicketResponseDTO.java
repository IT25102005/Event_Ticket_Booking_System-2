package com.example.backend.ticket.dto;

import com.example.backend.ticket.model.TicketCategory;
import com.example.backend.ticket.model.TicketStatus;
import java.time.LocalDateTime;

public class TicketResponseDTO {
    private Long ticketTypeId;
    private Long eventId;
    private String ticketName;
    private String description;
    private TicketCategory ticketCategory;
    private Double basePrice;
    private Double finalPrice;
    private Integer totalQuantity;
    private Integer soldQuantity;
    private Integer availableQuantity;
    private TicketStatus status;
    private String benefits;
    private LocalDateTime saleStartDate;
    private LocalDateTime saleEndDate;

    // Getters and Setters
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
    public Double getFinalPrice() { return finalPrice; }
    public void setFinalPrice(Double finalPrice) { this.finalPrice = finalPrice; }
    public Integer getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }
    public Integer getSoldQuantity() { return soldQuantity; }
    public void setSoldQuantity(Integer soldQuantity) { this.soldQuantity = soldQuantity; }
    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }
    public TicketStatus getStatus() { return status; }
    public void setStatus(TicketStatus status) { this.status = status; }
    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }
    public LocalDateTime getSaleStartDate() { return saleStartDate; }
    public void setSaleStartDate(LocalDateTime saleStartDate) { this.saleStartDate = saleStartDate; }
    public LocalDateTime getSaleEndDate() { return saleEndDate; }
    public void setSaleEndDate(LocalDateTime saleEndDate) { this.saleEndDate = saleEndDate; }
}
