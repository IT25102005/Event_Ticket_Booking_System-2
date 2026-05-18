package com.example.backend.event.controller;

import com.example.backend.event.dto.EventRequestDTO;
import com.example.backend.event.dto.EventResponseDTO;
import com.example.backend.event.model.EventCategory;
import com.example.backend.event.model.EventStatus;
import com.example.backend.event.service.EventService;
import com.example.backend.venue.dto.VenueResponseDTO;
import com.example.backend.venue.service.VenueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller — exposes HTTP endpoints for Event Management.
 *
 * OOP CONCEPT: ABSTRACTION
 *   - Controller only calls EventService methods. It does NOT contain business logic.
 *   - Clean separation of concerns: Controller → Service → Repository
 *
 * @CrossOrigin: Allows the frontend HTML pages to make requests to this backend.
 */
@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*") // Allow requests from any origin (frontend HTML)
public class EventController {

    // Injected via constructor (Dependency Injection)
    private final EventService eventService;
    private final VenueService venueService;

    public EventController(EventService eventService, VenueService venueService) {
        this.eventService = eventService;
        this.venueService = venueService;
    }

    /**
     * POST /api/events
     * Create a new event. Returns 201 Created.
     */
    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody EventRequestDTO requestDTO) {
        EventResponseDTO created = eventService.createEvent(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET /api/events
     * Get all events. Returns 200 OK with list.
     */
    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    /**
     * GET /api/events/{id}
     * Get a single event by ID. Returns 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    /**
     * GET /api/events/search?keyword=rock&category=CONCERT&status=UPCOMING
     * Search events by keyword, category, and/or status (all optional).
     */
    @GetMapping("/search")
    public ResponseEntity<List<EventResponseDTO>> searchEvents(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status) {

        // Convert string params to enums safely (null if not provided)
        EventCategory cat = (category != null && !category.isBlank())
                ? EventCategory.valueOf(category.toUpperCase()) : null;
        EventStatus st = (status != null && !status.isBlank())
                ? EventStatus.valueOf(status.toUpperCase()) : null;

        return ResponseEntity.ok(eventService.searchEvents(keyword, cat, st));
    }

    /**
     * PUT /api/events/{id}
     * Update an existing event. Returns updated event.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable Long id,
            @RequestBody EventRequestDTO requestDTO) {
        return ResponseEntity.ok(eventService.updateEvent(id, requestDTO));
    }

    /**
     * PATCH /api/events/{id}/cancel
     * Cancel an event (change status to CANCELLED). Returns updated event.
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<EventResponseDTO> cancelEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.cancelEvent(id));
    }

    /**
     * DELETE /api/events/{id}
     * Permanently delete an event. Returns 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/events/{eventId}/venue
     * Get the associated venue details for a given event.
     */
    @GetMapping("/{eventId}/venue")
    public ResponseEntity<VenueResponseDTO> getVenueForEvent(@PathVariable Long eventId) {
        EventResponseDTO event = eventService.getEventById(eventId);
        if (event.getVenueId() == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(venueService.getVenueById(event.getVenueId()));
    }
}
