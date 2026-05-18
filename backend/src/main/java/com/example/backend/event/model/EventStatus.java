package com.example.backend.event.model;

/**
 * OOP CONCEPT: ENCAPSULATION + ABSTRACTION
 * Enum representing event lifecycle statuses.
 */
public enum EventStatus {
    UPCOMING,    // Event is scheduled in the future
    ONGOING,     // Event is currently happening
    COMPLETED,   // Event has ended successfully
    CANCELLED    // Event has been cancelled
}
