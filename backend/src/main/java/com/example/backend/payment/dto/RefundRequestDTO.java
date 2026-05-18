package com.example.backend.payment.dto;

public class RefundRequestDTO {
    private Long paymentId;
    private Double amount;
    private String refundType; // FULL or PARTIAL
    private String reason;
    private String notes;

    public RefundRequestDTO() {
    }

    public RefundRequestDTO(Long paymentId, Double amount, String refundType, String reason) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.refundType = refundType;
        this.reason = reason;
    }

    // Getters and Setters
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
