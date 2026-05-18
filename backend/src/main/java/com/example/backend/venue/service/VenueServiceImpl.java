package com.example.backend.venue.service;

import com.example.backend.venue.dto.VenueRequestDTO;
import com.example.backend.venue.dto.VenueResponseDTO;
import com.example.backend.venue.exception.VenueNotFoundException;
import com.example.backend.venue.model.*;
import com.example.backend.venue.repository.SeatRepository;
import com.example.backend.venue.repository.VenueRepository;
import com.example.backend.event.repository.EventRepository;
import com.example.backend.event.model.Event;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * =====================================================
 * VenueServiceImpl.java — Service Implementation
 * =====================================================
 * OOP Concept: ABSTRACTION — Implements VenueService interface.
 *   The controller has no idea how this works internally.
 *
 * OOP Concept: POLYMORPHISM — When we call venue.getVenueTypeDescription(),
 *   Java automatically calls the correct override (IndoorVenue or OutdoorVenue).
 *
 * OOP Concept: ENCAPSULATION — Validation and business rules
 *   are enforced here, not in the controller.
 *
 * OOP Concept: INHERITANCE — The service handles both IndoorVenue
 *   and OutdoorVenue objects through their common Venue parent type.
 * =====================================================
 */
@Service
@Transactional
public class VenueServiceImpl implements VenueService {

    // ---- Dependency Injection (Spring injects these automatically) ----
    private final VenueRepository venueRepository;
    private final SeatRepository seatRepository;
    private final EventRepository eventRepository;

    public VenueServiceImpl(VenueRepository venueRepository, SeatRepository seatRepository, EventRepository eventRepository) {
        this.venueRepository = venueRepository;
        this.seatRepository = seatRepository;
        this.eventRepository = eventRepository;
    }

    // =====================================================
    // CREATE: Add a new venue
    // =====================================================
    @Override
    @Transactional
    public VenueResponseDTO createVenue(VenueRequestDTO request) {
        // Strict Validation & Check BEFORE persisting anything
        Event associatedEvent = null;
        if (request.getEventId() != null) {
            associatedEvent = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event with ID " + request.getEventId() + " not found"));
            
            if (associatedEvent.getVenueId() != null) {
                throw new IllegalStateException("This event already has a venue assigned");
            }
        }

        // Determine venue type and create the correct subclass object
        Venue venue;

        if ("INDOOR".equalsIgnoreCase(request.getVenueType())) {
            // OOP: Creating an IndoorVenue instance (subclass of Venue)
            IndoorVenue indoor = new IndoorVenue();
            indoor.setAirConditioned(request.getAirConditioned() != null ? request.getAirConditioned() : false);
            indoor.setNumberOfHalls(request.getNumberOfHalls() != null ? request.getNumberOfHalls() : 1);
            indoor.setHasSoundSystem(request.getHasSoundSystem() != null ? request.getHasSoundSystem() : false);
            venue = indoor;

        } else if ("OUTDOOR".equalsIgnoreCase(request.getVenueType())) {
            // OOP: Creating an OutdoorVenue instance (subclass of Venue)
            OutdoorVenue outdoor = new OutdoorVenue();
            outdoor.setWeatherProtected(request.getWeatherProtected() != null ? request.getWeatherProtected() : false);
            outdoor.setOpenGroundArea(request.getOpenGroundArea() != null ? request.getOpenGroundArea() : 0.0);
            outdoor.setHasParkingArea(request.getHasParkingArea() != null ? request.getHasParkingArea() : false);
            venue = outdoor;

        } else {
            throw new IllegalArgumentException("Venue type must be INDOOR or OUTDOOR. Got: " + request.getVenueType());
        }

        // Set common Venue fields (inherited by both subclasses)
        mapRequestToVenue(request, venue);

        // Save to database
        Venue saved = venueRepository.save(venue);

        // Perform transactional link
        if (associatedEvent != null) {
            associatedEvent.setVenueId(saved.getVenueId());
            eventRepository.save(associatedEvent);
        }

        VenueResponseDTO response = mapVenueToResponse(saved);
        if (request.getEventId() != null) {
            response.setEventId(request.getEventId());
        }
        return response;
    }

    // =====================================================
    // READ: Get all venues
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public List<VenueResponseDTO> getAllVenues() {
        return venueRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapVenueToResponse)
                .collect(Collectors.toList());
    }

    // =====================================================
    // READ: Get one venue by ID
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public VenueResponseDTO getVenueById(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException(venueId));
        return mapVenueToResponse(venue);
    }

    // =====================================================
    // READ: Search venues by keyword, city, status, venueType
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public List<VenueResponseDTO> searchVenues(String keyword, String city, VenueStatus status, String venueType) {
        // Normalize empty strings to null for the query
        String kw = (keyword != null && !keyword.isBlank()) ? keyword.trim() : null;
        String ct = (city != null && !city.isBlank()) ? city.trim() : null;

        List<Venue> results = venueRepository.searchVenues(kw, ct, status);

        // Filter by venueType in Java (discriminator not easily queryable via JPQL in Spring Data)
        if (venueType != null && !venueType.isBlank()) {
            results = results.stream()
                    .filter(v -> {
                        if ("INDOOR".equalsIgnoreCase(venueType)) return v instanceof IndoorVenue;
                        if ("OUTDOOR".equalsIgnoreCase(venueType)) return v instanceof OutdoorVenue;
                        return true;
                    })
                    .collect(Collectors.toList());
        }

        return results.stream()
                .map(this::mapVenueToResponse)
                .collect(Collectors.toList());
    }

    // =====================================================
    // UPDATE: Edit venue details
    // =====================================================
    @Override
    public VenueResponseDTO updateVenue(Long venueId, VenueRequestDTO request) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException(venueId));

        // Update common fields
        mapRequestToVenue(request, venue);

        // Update subclass-specific fields
        if (venue instanceof IndoorVenue indoor) {
            if (request.getAirConditioned() != null) indoor.setAirConditioned(request.getAirConditioned());
            if (request.getNumberOfHalls() != null) indoor.setNumberOfHalls(request.getNumberOfHalls());
            if (request.getHasSoundSystem() != null) indoor.setHasSoundSystem(request.getHasSoundSystem());

        } else if (venue instanceof OutdoorVenue outdoor) {
            if (request.getWeatherProtected() != null) outdoor.setWeatherProtected(request.getWeatherProtected());
            if (request.getOpenGroundArea() != null) outdoor.setOpenGroundArea(request.getOpenGroundArea());
            if (request.getHasParkingArea() != null) outdoor.setHasParkingArea(request.getHasParkingArea());
        }

        Venue updated = venueRepository.save(venue);
        return mapVenueToResponse(updated);
    }

    // =====================================================
    // UPDATE: Deactivate venue (soft delete)
    // =====================================================
    @Override
    public VenueResponseDTO deactivateVenue(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException(venueId));
        venue.setStatus(VenueStatus.INACTIVE);
        return mapVenueToResponse(venueRepository.save(venue));
    }

    // =====================================================
    // UPDATE: Activate venue
    // =====================================================
    @Override
    public VenueResponseDTO activateVenue(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException(venueId));
        venue.setStatus(VenueStatus.ACTIVE);
        return mapVenueToResponse(venueRepository.save(venue));
    }

    // =====================================================
    // DELETE: Permanently remove venue and its seats
    // =====================================================
    @Override
    public void deleteVenue(Long venueId) {
        if (!venueRepository.existsById(venueId)) {
            throw new VenueNotFoundException(venueId);
        }
        // Cascade delete: seats are deleted automatically (CascadeType.ALL in Venue entity)
        venueRepository.deleteById(venueId);
    }

    // =====================================================
    // PRIVATE HELPER: Map DTO fields → Venue entity
    // =====================================================
    private void mapRequestToVenue(VenueRequestDTO request, Venue venue) {
        venue.setVenueName(request.getVenueName());
        venue.setLocation(request.getLocation());
        venue.setAddress(request.getAddress());
        venue.setCity(request.getCity());
        venue.setCapacity(request.getCapacity());
        venue.setDescription(request.getDescription());
        venue.setFacilities(request.getFacilities());
        venue.setImageUrl(request.getImageUrl());
        if (request.getStatus() != null) {
            venue.setStatus(request.getStatus());
        }
    }

    // =====================================================
    // PRIVATE HELPER: Map Venue entity → VenueResponseDTO
    // OOP POLYMORPHISM in action: venue.getVenueTypeDescription()
    //   calls IndoorVenue or OutdoorVenue's overridden method.
    // =====================================================
    private VenueResponseDTO mapVenueToResponse(Venue venue) {
        VenueResponseDTO dto = new VenueResponseDTO();

        dto.setVenueId(venue.getVenueId());
        dto.setVenueName(venue.getVenueName());
        dto.setLocation(venue.getLocation());
        dto.setAddress(venue.getAddress());
        dto.setCity(venue.getCity());
        dto.setCapacity(venue.getCapacity());
        dto.setDescription(venue.getDescription());
        dto.setFacilities(venue.getFacilities());
        dto.setImageUrl(venue.getImageUrl());
        dto.setStatus(venue.getStatus());
        dto.setCreatedAt(venue.getCreatedAt());
        dto.setUpdatedAt(venue.getUpdatedAt());

        // OOP POLYMORPHISM: Java determines at runtime whether to call
        // IndoorVenue.getVenueTypeDescription() or OutdoorVenue.getVenueTypeDescription()
        dto.setVenueTypeDescription(venue.getVenueTypeDescription());

        // Determine venue type and map subclass-specific fields
        if (venue instanceof IndoorVenue indoor) {
            dto.setVenueType("INDOOR");
            dto.setAirConditioned(indoor.getAirConditioned());
            dto.setNumberOfHalls(indoor.getNumberOfHalls());
            dto.setHasSoundSystem(indoor.getHasSoundSystem());

        } else if (venue instanceof OutdoorVenue outdoor) {
            dto.setVenueType("OUTDOOR");
            dto.setWeatherProtected(outdoor.getWeatherProtected());
            dto.setOpenGroundArea(outdoor.getOpenGroundArea());
            dto.setHasParkingArea(outdoor.getHasParkingArea());
        }

        // Add seat summary statistics
        Long venueId = venue.getVenueId();
        dto.setTotalSeats(seatRepository.countByVenue_VenueId(venueId));
        dto.setAvailableSeats(seatRepository.countByVenue_VenueIdAndSeatStatus(venueId, SeatStatus.AVAILABLE));
        dto.setBookedSeats(seatRepository.countByVenue_VenueIdAndSeatStatus(venueId, SeatStatus.BOOKED));
        dto.setReservedSeats(seatRepository.countByVenue_VenueIdAndSeatStatus(venueId, SeatStatus.RESERVED));
        dto.setVipSeats(seatRepository.countByVenue_VenueIdAndSeatType(venueId, SeatType.VIP));
        dto.setRegularSeats(seatRepository.countByVenue_VenueIdAndSeatType(venueId, SeatType.REGULAR));

        // Add associated Event ID if present
        eventRepository.findFirstByVenueId(venueId).ifPresent(event -> {
            dto.setEventId(event.getEventId());
        });

        return dto;
    }
}
