package com.example.backend.venue.controller;

import com.example.backend.venue.dto.VenueRequestDTO;
import com.example.backend.venue.dto.VenueResponseDTO;
import com.example.backend.venue.model.VenueStatus;
import com.example.backend.venue.service.VenueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * =====================================================
 * VenueController.java — REST Controller
 * =====================================================
 * OOP Concept: ABSTRACTION — The controller only calls
 *   VenueService methods. It does NOT contain business logic.
 *
 * Follows MVC architecture:
 *   - Controller handles HTTP requests/responses
 *   - Service handles business logic
 *   - Repository handles database access
 *
 * CORS is enabled to allow frontend HTML pages to connect.
 * =====================================================
 */
@RestController
@RequestMapping("/api/venues")
@CrossOrigin(origins = "*")  // Allow all origins (frontend can access from any port)
public class VenueController {

    // Injecting via VenueService interface (not VenueServiceImpl)
    // — this is ABSTRACTION: controller doesn't know the implementation detail
    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    // =====================================================
    // POST /api/venues
    // Create a new venue (Indoor or Outdoor)
    // =====================================================
    @PostMapping
    public ResponseEntity<VenueResponseDTO> createVenue(@Valid @RequestBody VenueRequestDTO request) {
        VenueResponseDTO created = venueService.createVenue(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // =====================================================
    // GET /api/venues
    // Get all venues (newest first)
    // =====================================================
    @GetMapping
    public ResponseEntity<List<VenueResponseDTO>> getAllVenues() {
        return ResponseEntity.ok(venueService.getAllVenues());
    }

    // =====================================================
    // GET /api/venues/{id}
    // Get one venue by its ID
    // =====================================================
    @GetMapping("/{id}")
    public ResponseEntity<VenueResponseDTO> getVenueById(@PathVariable("id") Long venueId) {
        return ResponseEntity.ok(venueService.getVenueById(venueId));
    }

    // =====================================================
    // GET /api/venues/search?keyword=&city=&status=&venueType=
    // Search venues by keyword, city, status, venue type
    // =====================================================
    @GetMapping("/search")
    public ResponseEntity<List<VenueResponseDTO>> searchVenues(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) VenueStatus status,
            @RequestParam(required = false) String venueType) {

        return ResponseEntity.ok(venueService.searchVenues(keyword, city, status, venueType));
    }

    // =====================================================
    // PUT /api/venues/{id}
    // Update all venue details
    // =====================================================
    @PutMapping("/{id}")
    public ResponseEntity<VenueResponseDTO> updateVenue(
            @PathVariable("id") Long venueId,
            @Valid @RequestBody VenueRequestDTO request) {
        return ResponseEntity.ok(venueService.updateVenue(venueId, request));
    }

    // =====================================================
    // PATCH /api/venues/{id}/deactivate
    // Mark venue as INACTIVE (soft delete)
    // =====================================================
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<VenueResponseDTO> deactivateVenue(@PathVariable("id") Long venueId) {
        return ResponseEntity.ok(venueService.deactivateVenue(venueId));
    }

    // =====================================================
    // PATCH /api/venues/{id}/activate
    // Mark venue as ACTIVE again
    // =====================================================
    @PatchMapping("/{id}/activate")
    public ResponseEntity<VenueResponseDTO> activateVenue(@PathVariable("id") Long venueId) {
        return ResponseEntity.ok(venueService.activateVenue(venueId));
    }

    // =====================================================
    // DELETE /api/venues/{id}
    // Permanently delete venue and all its seats
    // =====================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable("id") Long venueId) {
        venueService.deleteVenue(venueId);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
