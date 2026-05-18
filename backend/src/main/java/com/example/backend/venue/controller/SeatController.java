package com.example.backend.venue.controller;

import com.example.backend.venue.dto.SeatLayoutResponseDTO;
import com.example.backend.venue.dto.SeatBulkRequestDTO;
import com.example.backend.venue.dto.SeatRequestDTO;
import com.example.backend.venue.dto.SeatResponseDTO;
import com.example.backend.venue.model.SeatStatus;
import com.example.backend.venue.model.SeatType;
import com.example.backend.venue.service.SeatService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * =====================================================
 * SeatController.java — REST Controller
 * =====================================================
 * OOP Concept: ABSTRACTION — Controller only calls
 *   SeatService interface methods. No business logic here.
 *
 * Two URL patterns:
 *   /api/venues/{venueId}/seats → venue-specific seat operations
 *   /api/seats/{seatId}         → individual seat operations
 * =====================================================
 */
@RestController
@CrossOrigin(origins = "*")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    // =====================================================
    // POST /api/venues/{venueId}/seats
    // Add a single seat to a venue
    // =====================================================
    @PostMapping("/api/venues/{venueId}/seats")
    public ResponseEntity<SeatResponseDTO> addSeat(
            @PathVariable Long venueId,
            @Valid @RequestBody SeatRequestDTO request) {
        SeatResponseDTO created = seatService.addSeat(venueId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // =====================================================
    // POST /api/venues/{venueId}/seats/bulk
    // Add a block of seats (e.g. 20 seats at once)
    // =====================================================
    @PostMapping("/api/venues/{venueId}/seats/bulk")
    public ResponseEntity<List<SeatResponseDTO>> addSeatBlock(
            @PathVariable Long venueId,
            @Valid @RequestBody SeatBulkRequestDTO request) {
        List<SeatResponseDTO> created = seatService.addSeatBlock(venueId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // =====================================================
    // GET /api/venues/{venueId}/seats
    // Get all seats for a venue (flat list)
    // =====================================================
    @GetMapping("/api/venues/{venueId}/seats")
    public ResponseEntity<List<SeatResponseDTO>> getSeatsByVenue(@PathVariable Long venueId) {
        return ResponseEntity.ok(seatService.getSeatsByVenue(venueId));
    }

    // =====================================================
    // GET /api/venues/{venueId}/seats/layout
    // Get seat layout grouped by section and row
    // =====================================================
    @GetMapping("/api/venues/{venueId}/seats/layout")
    public ResponseEntity<SeatLayoutResponseDTO> getSeatLayout(@PathVariable Long venueId) {
        return ResponseEntity.ok(seatService.getSeatLayout(venueId));
    }

    // =====================================================
    // GET /api/seats/{seatId}
    // Get one seat by ID
    // =====================================================
    @GetMapping("/api/seats/{seatId}")
    public ResponseEntity<SeatResponseDTO> getSeatById(@PathVariable Long seatId) {
        return ResponseEntity.ok(seatService.getSeatById(seatId));
    }

    // =====================================================
    // GET /api/seats/search?venueId=&section=&seatType=&seatStatus=
    // Search/filter seats
    // =====================================================
    @GetMapping("/api/seats/search")
    public ResponseEntity<List<SeatResponseDTO>> searchSeats(
            @RequestParam(required = false) Long venueId,
            @RequestParam(required = false) String section,
            @RequestParam(required = false) SeatType seatType,
            @RequestParam(required = false) SeatStatus seatStatus) {
        return ResponseEntity.ok(seatService.searchSeats(venueId, section, seatType, seatStatus));
    }

    // =====================================================
    // PUT /api/seats/{seatId}
    // Update full seat details
    // =====================================================
    @PutMapping("/api/seats/{seatId}")
    public ResponseEntity<SeatResponseDTO> updateSeat(
            @PathVariable Long seatId,
            @Valid @RequestBody SeatRequestDTO request) {
        return ResponseEntity.ok(seatService.updateSeat(seatId, request));
    }

    // =====================================================
    // PATCH /api/seats/{seatId}/status
    // Update seat status only
    // Body: { "status": "BOOKED" }
    // =====================================================
    @PatchMapping("/api/seats/{seatId}/status")
    public ResponseEntity<SeatResponseDTO> updateSeatStatus(
            @PathVariable Long seatId,
            @RequestBody Map<String, String> body) {

        String statusStr = body.get("status");
        if (statusStr == null || statusStr.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        SeatStatus newStatus;
        try {
            newStatus = SeatStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(seatService.updateSeatStatus(seatId, newStatus));
    }

    // =====================================================
    // DELETE /api/seats/{seatId}
    // Delete a single seat
    // =====================================================
    @DeleteMapping("/api/seats/{seatId}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long seatId) {
        seatService.deleteSeat(seatId);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
