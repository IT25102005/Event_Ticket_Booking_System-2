package com.example.backend.payment.service;

import com.example.backend.payment.dto.PaymentRequestDTO;
import com.example.backend.payment.dto.PaymentResponseDTO;
import com.example.backend.payment.model.Payment;
import com.example.backend.payment.model.PaymentStatus;
import com.example.backend.payment.model.PaymentType;
import com.example.backend.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private com.example.backend.booking.service.BookingService bookingService;
    
    @Autowired
    private com.example.backend.booking.repository.BookingRepository bookingRepository;
    
    @Autowired
    private com.example.backend.venue.repository.SeatRepository seatRepository;
    
    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO request) {
        Payment payment = new Payment();
        payment.setPaymentId("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setEventId(request.getEventId());
        payment.setUserId(request.getUserId());
        payment.setBookingId(request.getBookingId());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency() != null ? request.getCurrency() : "USD");
        payment.setPaymentType(PaymentType.valueOf(request.getPaymentType().toUpperCase()));
        payment.setDescription(request.getDescription());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setMaskedCardNumber(request.getMaskedCardNumber());
        payment.setCardholderName(request.getCardholderName());
        
        Payment saved = paymentRepository.save(payment);
        return convertToDTO(saved);
    }
    
    @Override
    public PaymentResponseDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        return convertToDTO(payment);
    }
    
    @Override
    public PaymentResponseDTO getPaymentByPaymentId(String paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        return convertToDTO(payment);
    }
    
    @Override
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<PaymentResponseDTO> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<PaymentResponseDTO> getPaymentsByEventId(Long eventId) {
        return paymentRepository.findByEventId(eventId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<PaymentResponseDTO> getPaymentsByStatus(String status) {
        return paymentRepository.findByStatus(PaymentStatus.valueOf(status.toUpperCase())).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @org.springframework.transaction.annotation.Transactional
    @Override
    public PaymentResponseDTO updatePaymentStatus(Long id, String status) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (status.equalsIgnoreCase("SUCCESS") || status.equalsIgnoreCase("COMPLETED")) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaymentDate(LocalDateTime.now());

            if (payment.getBookingId() != null) {
                // Direct JPQL update — zero lazy-loading, zero cross-service transaction risk
                bookingRepository.markBookingConfirmed(
                    payment.getBookingId(), "PAYMENT_SUCCESSFUL");

                // Mark seats BOOKED via native SQL on the join table
                java.util.List<Long> seatIds = seatRepository.findSeatIdsByBookingId(payment.getBookingId());
                if (seatIds != null && !seatIds.isEmpty()) {
                    java.util.List<com.example.backend.venue.model.Seat> seats = seatRepository.findAllById(seatIds);
                    seats.forEach(s -> s.setSeatStatus(com.example.backend.venue.model.SeatStatus.BOOKED));
                    seatRepository.saveAll(seats);
                }
            }
        } else {
            payment.setStatus(PaymentStatus.valueOf(status.toUpperCase()));
        }

        Payment updated = paymentRepository.save(payment);
        return convertToDTO(updated);
    }
    
    @org.springframework.transaction.annotation.Transactional
    @Override
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id).orElse(null);
        if (payment != null) {
            payment.setStatus(PaymentStatus.PAYMENT_CANCELLED);
            if (payment.getBookingId() != null) {
                try {
                    bookingService.cancelBooking(payment.getBookingId());
                    bookingRepository.updatePaymentStatusOnly(
                        payment.getBookingId(), "PAYMENT_CANCELLED");
                } catch (Exception e) {
                    System.err.println("Could not cancel booking: " + e.getMessage());
                }
            }
            paymentRepository.save(payment);
        }
    }
    
    private PaymentResponseDTO convertToDTO(Payment payment) {
        return new PaymentResponseDTO(
            payment.getId(),
            payment.getPaymentId(),
            payment.getEventId(),
            payment.getUserId(),
            payment.getBookingId(),
            payment.getAmount(),
            payment.getCurrency(),
            payment.getPaymentType().toString(),
            payment.getStatus().toString(),
            payment.getDescription(),
            payment.getTransactionId(),
            payment.getPaymentDate(),
            payment.getCreatedAt(),
            payment.getUpdatedAt(),
            payment.getMaskedCardNumber(),
            payment.getCardholderName()
        );
    }
}
