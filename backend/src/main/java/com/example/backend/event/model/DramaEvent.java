package com.example.backend.event.model;

import jakarta.persistence.*;

/**
 * OOP CONCEPT: INHERITANCE — DramaEvent EXTENDS Event
 * OOP CONCEPT: POLYMORPHISM — Overrides getEventSummary() with drama-specific behavior
 * OOP CONCEPT: ENCAPSULATION — director, cast, language are private
 */
@Entity
@DiscriminatorValue("DRAMA")
public class DramaEvent extends Event {

    // Unique fields for Drama events only
    private String director;
    @Column(name = "event_cast")
    private String cast;
    private String language;

    public DramaEvent() {
        super();
    }

    /**
     * POLYMORPHISM: Different behavior from other subclasses.
     */
    @Override
    public String getEventSummary() {
        return "🎭 Drama: " + getTitle() + " | Director: " + director + " | Language: " + language;
    }

    @Override
    public String getDisplayCategory() {
        return "DRAMA - " + (language != null ? language : "Theatre");
    }

    // Getters and Setters (Encapsulation)
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getCast() { return cast; }
    public void setCast(String cast) { this.cast = cast; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}

