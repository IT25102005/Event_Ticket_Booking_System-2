package com.example.backend.payment.dto;

import java.time.LocalDateTime;

public class RefundResponseDTO {
    private Long id;
    private String refundId;
    private Long paymentId;
    private Double amount;
    private String refundType;
    private String status;
    private String reason;
    private String approvedBy;
    private LocalDateTime approvalDate;
    private LocalDateTime refundDate;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public RefundResponseDTO() {}
    
    public RefundResponseDTO(Long id, String refundId, Long paymentId, Double amount, String refundType,
                            String status, String reason, String approvedBy, LocalDateTime approvalDate,
                            LocalDateTime refundDate, String notes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.refundId = refundId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.refundType = refundType;
        this.status = status;
        this.reason = reason;
        this.approvedBy = approvedBy;
        this.approvalDate = approvalDate;
        this.refundDate = refundDate;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRefundId() {
        return refundId;
    }
    
    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }
    
    public Long getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
    
    public Double getAmount() {
        return amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    public String getRefundType() {
        return refundType;
    }
    
    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public String getApprovedBy() {
        return approvedBy;
    }
    
    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
    
    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }
    
    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }
    
    public LocalDateTime getRefundDate() {
        return refundDate;
    }
    
    public void setRefundDate(LocalDateTime refundDate) {
        this.refundDate = refundDate;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
