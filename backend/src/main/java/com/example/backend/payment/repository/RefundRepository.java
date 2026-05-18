package com.example.backend.payment.repository;

import com.example.backend.payment.model.Refund;
import com.example.backend.payment.model.RefundStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {
    Optional<Refund> findByRefundId(String refundId);
    List<Refund> findByPaymentId(Long paymentId);
    List<Refund> findByStatus(RefundStatus status);
    List<Refund> findByRefundType(String refundType);
}
