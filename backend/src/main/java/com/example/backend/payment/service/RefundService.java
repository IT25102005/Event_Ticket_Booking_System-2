package com.example.backend.payment.service;

import com.example.backend.payment.dto.RefundRequestDTO;
import com.example.backend.payment.dto.RefundResponseDTO;

import java.util.List;

public interface RefundService {
    RefundResponseDTO createRefund(RefundRequestDTO request);
    RefundResponseDTO getRefundById(Long id);
    RefundResponseDTO getRefundByRefundId(String refundId);
    List<RefundResponseDTO> getAllRefunds();
    List<RefundResponseDTO> getRefundsByPaymentId(Long paymentId);
    List<RefundResponseDTO> getRefundsByStatus(String status);
    RefundResponseDTO approveRefund(Long id, String approvedBy);
    RefundResponseDTO rejectRefund(Long id);
    RefundResponseDTO completeRefund(Long id);
    void deleteRefund(Long id);
}
