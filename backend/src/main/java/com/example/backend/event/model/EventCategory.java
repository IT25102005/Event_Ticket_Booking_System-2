package com.example.backend.event.model;

/**
 * OOP CONCEPT: ENCAPSULATION + ABSTRACTION
 * Enum representing event categories.
 * Each category corresponds to a subclass of Event.
 */
public enum EventCategory {
    CONCERT,      // Maps to ConcertEvent
    SPORT,        // Maps to SportEvent
    DRAMA,        // Maps to DramaEvent
    MOVIE,        // Maps to MovieEvent
    FESTIVAL,     // Maps to FestivalEvent
    CONFERENCE,   // General conference (uses base Event)
    WORKSHOP      // General workshop (uses base Event)
}