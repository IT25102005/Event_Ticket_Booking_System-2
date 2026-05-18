package com.example.backend.venue.repository;

import com.example.backend.venue.model.Seat;
import com.example.backend.venue.model.SeatStatus;
import com.example.backend.venue.model.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * =====================================================
 * SeatRepository.java
 * =====================================================
 * OOP Concept: ABSTRACTION — Interface only; Spring provides
 *   the implementation at runtime automatically.
 * =====================================================
 */
@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    // Get all seats for a specific venue
    List<Seat> findByVenue_VenueId(Long venueId);

    // Check if a seat already exists (prevent duplicates)
    boolean existsByVenue_VenueIdAndSectionNameIgnoreCaseAndRowNameIgnoreCaseAndSeatNumber(
            Long venueId, String sectionName, String rowName, String seatNumber);

    // Count total seats for a venue
    long countByVenue_VenueId(Long venueId);

    // Count seats by status for a venue
    long countByVenue_VenueIdAndSeatStatus(Long venueId, SeatStatus status);

    // Count seats by type for a venue
    long countByVenue_VenueIdAndSeatType(Long venueId, SeatType seatType);

    // Advanced filter query
    @Query("SELECT s FROM Seat s WHERE " +
           "(:venueId IS NULL OR s.venue.venueId = :venueId) " +
           "AND (:sectionName IS NULL OR LOWER(s.sectionName) LIKE LOWER(CONCAT('%', :sectionName, '%'))) " +
           "AND (:seatType IS NULL OR s.seatType = :seatType) " +
           "AND (:seatStatus IS NULL OR s.seatStatus = :seatStatus) " +
           "ORDER BY s.sectionName, s.rowName, s.seatNumber")
    List<Seat> searchSeats(
            @Param("venueId") Long venueId,
            @Param("sectionName") String sectionName,
            @Param("seatType") SeatType seatType,
            @Param("seatStatus") SeatStatus seatStatus
    );

    // Delete all seats for a venue (used when deleting venue)
    void deleteByVenue_VenueId(Long venueId);

    // Find distinct section names for a venue (used in frontend filters)
    @Query("SELECT DISTINCT s.sectionName FROM Seat s WHERE s.venue.venueId = :venueId ORDER BY s.sectionName")
    List<String> findDistinctSectionsByVenueId(@Param("venueId") Long venueId);

    // Fetch seat IDs linked to a booking via the join table (avoids lazy loading)
    @Query(value = "SELECT seat_id FROM booking_seats WHERE booking_id = :bookingId", nativeQuery = true)
    List<Long> findSeatIdsByBookingId(@Param("bookingId") Long bookingId);
}
