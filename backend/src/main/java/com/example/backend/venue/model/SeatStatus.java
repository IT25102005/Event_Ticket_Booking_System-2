package com.example.backend.venue.model;

/**
 * SeatStatus Enum
 * Represents the possible states of a seat.
 * OOP Concept: Encapsulation — seat state is managed through this enum.
 */
public enum SeatStatus {
    AVAILABLE,    // Seat is free and can be booked
    RESERVED,     // Seat is temporarily held (e.g., during checkout)
    BOOKED,       // Seat has been confirmed and paid
    BLOCKED,      // Seat is blocked by admin (not for sale)
    MAINTENANCE   // Seat is under repair or unusable
}
