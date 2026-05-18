package com.example.backend.venue.exception;

/**
 * VenueNotFoundException
 * Thrown when a venue with the given ID does not exist in the database.
 * Returns HTTP 404 Not Found via GlobalExceptionHandler.
 */
public class VenueNotFoundException extends RuntimeException {

    private final Long venueId;

    public VenueNotFoundException(Long venueId) {
        super("Venue not found with ID: " + venueId);
        this.venueId = venueId;
    }

    public VenueNotFoundException(String message) {
        super(message);
        this.venueId = null;
    }

    public Long getVenueId() { return venueId; }
}
