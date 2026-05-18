package com.example.backend.payment.repository;

import com.example.backend.payment.model.Payment;
import com.example.backend.payment.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentId(String paymentId);
    List<Payment> findByUserId(Long userId);
    List<Payment> findByEventId(Long eventId);
    List<Payment> findByStatus(PaymentStatus status);
    List<Payment> findByPaymentType(String paymentType);
}
