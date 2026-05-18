package com.example.backend.booking.repository;

import com.example.backend.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    java.util.List<Booking> findByUserId(Long userId);

    /**
     * Direct JPQL update — no lazy loading, no service proxy, no cross-transaction
     * rollback risk.
     * Used by PaymentServiceImpl to safely update only status columns after payment
     * success/cancel.
     */
    @Modifying
    @Query("UPDATE Booking b SET b.status = com.example.backend.booking.model.BookingStatus.CONFIRMED, b.paymentStatus = :paymentStatus WHERE b.id = :bookingId")
    int markBookingConfirmed(@Param("bookingId") Long bookingId, @Param("paymentStatus") String paymentStatus);

    @Modifying
    @Query("UPDATE Booking b SET b.paymentStatus = :paymentStatus WHERE b.id = :bookingId")
    int updatePaymentStatusOnly(@Param("bookingId") Long bookingId, @Param("paymentStatus") String paymentStatus);
}
