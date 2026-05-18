package com.example.backend.venue.model;

/**
 * VenueStatus Enum
 * Represents the possible states of a venue.
 * OOP Concept: Encapsulation — status is controlled via this enum, not free strings.
 */
public enum VenueStatus {
    ACTIVE,       // Venue is open and available for events
    INACTIVE,     // Venue is closed / deactivated
    MAINTENANCE   // Venue is temporarily under maintenance
}
