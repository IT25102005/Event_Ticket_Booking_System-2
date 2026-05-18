package com.example.backend.venue.exception;

/**
 * DuplicateSeatException
 * Thrown when a seat with the same venue + section + row + seatNumber already exists.
 * Returns HTTP 409 Conflict via GlobalExceptionHandler.
 */
public class DuplicateSeatException extends RuntimeException {

    public DuplicateSeatException(String sectionName, String rowName, String seatNumber) {
        super("Seat already exists: Section '" + sectionName +
              "', Row '" + rowName +
              "', Seat '" + seatNumber + "' in this venue.");
    }

    public DuplicateSeatException(String message) {
        super(message);
    }
}
