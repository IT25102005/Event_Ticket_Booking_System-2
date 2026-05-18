package com.example.backend.venue.service;

import com.example.backend.venue.dto.VenueRequestDTO;
import com.example.backend.venue.dto.VenueResponseDTO;
import com.example.backend.venue.model.VenueStatus;

import java.util.List;

/**
 * =====================================================
 * VenueService.java — Service Interface
 * =====================================================
 * OOP Concept: ABSTRACTION — This interface defines WHAT
 *   operations are available without revealing HOW they work.
 *
 * The controller only calls these method signatures.
 * VenueServiceImpl provides the actual implementation.
 *
 * This is the "contract" between Controller and Service layer.
 * =====================================================
 */
public interface VenueService {

    // Create a new venue (Indoor or Outdoor)
    VenueResponseDTO createVenue(VenueRequestDTO request);

    // Get all venues ordered by newest first
    List<VenueResponseDTO> getAllVenues();

    // Get a single venue by its ID
    VenueResponseDTO getVenueById(Long venueId);

    // Search venues by keyword, city, status, or venueType
    List<VenueResponseDTO> searchVenues(String keyword, String city, VenueStatus status, String venueType);

    // Update all venue details
    VenueResponseDTO updateVenue(Long venueId, VenueRequestDTO request);

    // Mark venue as INACTIVE (soft delete / deactivate)
    VenueResponseDTO deactivateVenue(Long venueId);

    // Mark venue as ACTIVE again
    VenueResponseDTO activateVenue(Long venueId);

    // Permanently delete venue and all its seats
    void deleteVenue(Long venueId);
}
