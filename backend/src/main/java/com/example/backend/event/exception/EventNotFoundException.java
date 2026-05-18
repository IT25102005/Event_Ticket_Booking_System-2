package com.example.backend.event.exception;

/**
 * Custom exception thrown when an event with a given ID is not found.
 *
 * OOP CONCEPT: INHERITANCE — This class EXTENDS RuntimeException (Java
 * built-in)
 * We customize the message to be meaningful for our module.
 */
public class EventNotFoundException extends RuntimeException {

    private final Long eventId;

    public EventNotFoundException(Long eventId) {
        super("Event not found with ID: " + eventId);
        this.eventId = eventId;
    }

    public Long getEventId() {
        return eventId;
    }
}
