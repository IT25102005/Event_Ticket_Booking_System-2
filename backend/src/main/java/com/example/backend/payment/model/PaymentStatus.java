package com.example.backend.payment.model;

public enum PaymentStatus {
    PENDING,
    SUCCESS,
    PAYMENT_SUCCESSFUL,
    REFUND_PROCESSING,
    PAYMENT_CANCELLED,
    COMPLETED, // Kept for backward compatibility
    FAILED
}
