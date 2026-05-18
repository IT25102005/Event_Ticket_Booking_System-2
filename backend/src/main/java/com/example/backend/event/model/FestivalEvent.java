package com.example.backend.event.model;

import jakarta.persistence.*;

/**
 * OOP CONCEPT: INHERITANCE — FestivalEvent EXTENDS Event
 * OOP CONCEPT: POLYMORPHISM — Overrides getEventSummary() with festival-specific behavior
 * OOP CONCEPT: ENCAPSULATION — theme, organizerName, highlights are private
 */
@Entity
@DiscriminatorValue("FESTIVAL")
public class FestivalEvent extends Event {

    // Unique fields for Festival events only
    private String theme;
    private String organizerName;
    private String highlights; // Short description of festival highlights

    public FestivalEvent() {
        super();
    }

    /**
     * POLYMORPHISM: Unique behavior specific to festivals.
     */
    @Override
    public String getEventSummary() {
        return "🎉 Festival: " + getTitle() + " | Theme: " + theme + " | Organizer: " + organizerName;
    }

    @Override
    public String getDisplayCategory() {
        return "FESTIVAL - " + (theme != null ? theme : "Celebration");
    }

    // Getters and Setters (Encapsulation)
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public String getOrganizerName() { return organizerName; }
    public void setOrganizerName(String organizerName) { this.organizerName = organizerName; }

    public String getHighlights() { return highlights; }
    public void setHighlights(String highlights) { this.highlights = highlights; }
}
