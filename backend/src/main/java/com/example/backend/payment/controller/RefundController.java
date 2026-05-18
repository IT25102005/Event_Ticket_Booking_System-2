package com.example.backend.payment.controller;

import com.example.backend.payment.dto.RefundRequestDTO;
import com.example.backend.payment.dto.RefundResponseDTO;
import com.example.backend.payment.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
@CrossOrigin(origins = "*")
public class RefundController {
    
    @Autowired
    private RefundService refundService;
    
    @PostMapping
    public ResponseEntity<RefundResponseDTO> createRefund(@RequestBody RefundRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(refundService.createRefund(request));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RefundResponseDTO> getRefundById(@PathVariable Long id) {
        return ResponseEntity.ok(refundService.getRefundById(id));
    }
    
    @GetMapping("/refund-id/{refundId}")
    public ResponseEntity<RefundResponseDTO> getRefundByRefundId(@PathVariable String refundId) {
        return ResponseEntity.ok(refundService.getRefundByRefundId(refundId));
    }
    
    @GetMapping
    public ResponseEntity<List<RefundResponseDTO>> getAllRefunds() {
        return ResponseEntity.ok(refundService.getAllRefunds());
    }
    
    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<List<RefundResponseDTO>> getRefundsByPaymentId(@PathVariable Long paymentId) {
        return ResponseEntity.ok(refundService.getRefundsByPaymentId(paymentId));
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<RefundResponseDTO>> getRefundsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(refundService.getRefundsByStatus(status));
    }
    
    @PutMapping("/{id}/approve")
    public ResponseEntity<RefundResponseDTO> approveRefund(
            @PathVariable Long id,
            @RequestParam String approvedBy) {
        return ResponseEntity.ok(refundService.approveRefund(id, approvedBy));
    }
    
    @PutMapping("/{id}/reject")
    public ResponseEntity<RefundResponseDTO> rejectRefund(@PathVariable Long id) {
        return ResponseEntity.ok(refundService.rejectRefund(id));
    }
    
    @PutMapping("/{id}/complete")
    public ResponseEntity<RefundResponseDTO> completeRefund(@PathVariable Long id) {
        return ResponseEntity.ok(refundService.completeRefund(id));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRefund(@PathVariable Long id) {
        refundService.deleteRefund(id);
        return ResponseEntity.noContent().build();
    }
}
