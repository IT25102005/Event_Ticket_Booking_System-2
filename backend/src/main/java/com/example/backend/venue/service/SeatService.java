package com.example.backend.venue.service;

import com.example.backend.venue.dto.SeatLayoutResponseDTO;
import com.example.backend.venue.dto.SeatBulkRequestDTO;
import com.example.backend.venue.dto.SeatRequestDTO;
import com.example.backend.venue.dto.SeatResponseDTO;
import com.example.backend.venue.model.SeatStatus;
import com.example.backend.venue.model.SeatType;

import java.util.List;

/**
 * =====================================================
 * SeatService.java — Service Interface
 * =====================================================
 * OOP Concept: ABSTRACTION — Defines WHAT seat operations
 *   exist. SeatServiceImpl provides the HOW.
 *
 * Controllers call these methods only — business logic
 *   stays inside the implementation, not in controllers.
 * =====================================================
 */
public interface SeatService {

    // Add one seat to a venue
    SeatResponseDTO addSeat(Long venueId, SeatRequestDTO request);

    // Add a block of seats (e.g. Section A, Row B, seats 1-20)
    List<SeatResponseDTO> addSeatBlock(Long venueId, SeatBulkRequestDTO request);

    // Get all seats for a venue
    List<SeatResponseDTO> getSeatsByVenue(Long venueId);

    // Get seat layout (grouped by section and row) + availability summary
    SeatLayoutResponseDTO getSeatLayout(Long venueId);

    // Get one seat by its ID
    SeatResponseDTO getSeatById(Long seatId);

    // Search/filter seats by venue, section, type, or status
    List<SeatResponseDTO> searchSeats(Long venueId, String sectionName, SeatType seatType, SeatStatus seatStatus);

    // Update full seat details
    SeatResponseDTO updateSeat(Long seatId, SeatRequestDTO request);

    // Update only the seat status (quick status change)
    SeatResponseDTO updateSeatStatus(Long seatId, SeatStatus newStatus);

    // Delete a single seat
    void deleteSeat(Long seatId);
}
