package com.example.backend.payment.service;

import com.example.backend.payment.dto.PaymentRequestDTO;
import com.example.backend.payment.dto.PaymentResponseDTO;
import com.example.backend.payment.model.Payment;

import java.util.List;

public interface PaymentService {
    PaymentResponseDTO createPayment(PaymentRequestDTO request);
    PaymentResponseDTO getPaymentById(Long id);
    PaymentResponseDTO getPaymentByPaymentId(String paymentId);
    List<PaymentResponseDTO> getAllPayments();
    List<PaymentResponseDTO> getPaymentsByUserId(Long userId);
    List<PaymentResponseDTO> getPaymentsByEventId(Long eventId);
    List<PaymentResponseDTO> getPaymentsByStatus(String status);
    PaymentResponseDTO updatePaymentStatus(Long id, String status);
    void deletePayment(Long id);
}
