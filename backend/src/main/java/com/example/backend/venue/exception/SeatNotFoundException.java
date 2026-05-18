package com.example.backend.venue.exception;

/**
 * SeatNotFoundException
 * Thrown when a seat with the given ID does not exist in the database.
 * Returns HTTP 404 Not Found via GlobalExceptionHandler.
 */
public class SeatNotFoundException extends RuntimeException {

    private final Long seatId;

    public SeatNotFoundException(Long seatId) {
        super("Seat not found with ID: " + seatId);
        this.seatId = seatId;
    }

    public SeatNotFoundException(String message) {
        super(message);
        this.seatId = null;
    }

    public Long getSeatId() { return seatId; }
}
