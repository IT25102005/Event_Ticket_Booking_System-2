package com.example.backend.payment.service;

import com.example.backend.payment.dto.RefundRequestDTO;
import com.example.backend.payment.dto.RefundResponseDTO;
import com.example.backend.payment.model.Refund;
import com.example.backend.payment.model.RefundStatus;
import com.example.backend.payment.repository.RefundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RefundServiceImpl implements RefundService {
    
    @Autowired
    private RefundRepository refundRepository;
    
    @Override
    public RefundResponseDTO createRefund(RefundRequestDTO request) {
        Refund refund = new Refund();
        refund.setRefundId("REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        refund.setPaymentId(request.getPaymentId());
        refund.setAmount(request.getAmount());
        refund.setRefundType(request.getRefundType());
        refund.setReason(request.getReason());
        refund.setNotes(request.getNotes());
        refund.setStatus(RefundStatus.PENDING);
        
        Refund saved = refundRepository.save(refund);
        return convertToDTO(saved);
    }
    
    @Override
    public RefundResponseDTO getRefundById(Long id) {
        Refund refund = refundRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Refund not found"));
        return convertToDTO(refund);
    }
    
    @Override
    public RefundResponseDTO getRefundByRefundId(String refundId) {
        Refund refund = refundRepository.findByRefundId(refundId)
            .orElseThrow(() -> new RuntimeException("Refund not found"));
        return convertToDTO(refund);
    }
    
    @Override
    public List<RefundResponseDTO> getAllRefunds() {
        return refundRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<RefundResponseDTO> getRefundsByPaymentId(Long paymentId) {
        return refundRepository.findByPaymentId(paymentId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<RefundResponseDTO> getRefundsByStatus(String status) {
        return refundRepository.findByStatus(RefundStatus.valueOf(status.toUpperCase())).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public RefundResponseDTO approveRefund(Long id, String approvedBy) {
        Refund refund = refundRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Refund not found"));
        refund.setStatus(RefundStatus.APPROVED);
        refund.setApprovedBy(approvedBy);
        refund.setApprovalDate(LocalDateTime.now());
        Refund updated = refundRepository.save(refund);
        return convertToDTO(updated);
    }
    
    @Override
    public RefundResponseDTO rejectRefund(Long id) {
        Refund refund = refundRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Refund not found"));
        refund.setStatus(RefundStatus.REJECTED);
        Refund updated = refundRepository.save(refund);
        return convertToDTO(updated);
    }
    
    @Override
    public RefundResponseDTO completeRefund(Long id) {
        Refund refund = refundRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Refund not found"));
        refund.setStatus(RefundStatus.COMPLETED);
        refund.setRefundDate(LocalDateTime.now());
        Refund updated = refundRepository.save(refund);
        return convertToDTO(updated);
    }
    
    @Override
    public void deleteRefund(Long id) {
        refundRepository.deleteById(id);
    }
    
    private RefundResponseDTO convertToDTO(Refund refund) {
        return new RefundResponseDTO(
            refund.getId(),
            refund.getRefundId(),
            refund.getPaymentId(),
            refund.getAmount(),
            refund.getRefundType(),
            refund.getStatus().toString(),
            refund.getReason(),
            refund.getApprovedBy(),
            refund.getApprovalDate(),
            refund.getRefundDate(),
            refund.getNotes(),
            refund.getCreatedAt(),
            refund.getUpdatedAt()
        );
    }
}
