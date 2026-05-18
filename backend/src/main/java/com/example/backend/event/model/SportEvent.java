package com.example.backend.event.model;

import jakarta.persistence.*;

/**
 * OOP CONCEPT: INHERITANCE — SportEvent EXTENDS Event
 * OOP CONCEPT: POLYMORPHISM — Overrides getEventSummary() with sport-specific behavior
 * OOP CONCEPT: ENCAPSULATION — teamOne, teamTwo, sportType are private
 */
@Entity
@DiscriminatorValue("SPORT")
public class SportEvent extends Event {

    // Unique fields for Sport events only
    private String teamOne;
    private String teamTwo;
    private String sportType;

    public SportEvent() {
        super();
    }

    /**
     * POLYMORPHISM: Different behavior from ConcertEvent.getEventSummary()
     */
    @Override
    public String getEventSummary() {
        return "⚽ Sport: " + getTitle() + " | " + teamOne + " vs " + teamTwo + " | " + sportType;
    }

    @Override
    public String getDisplayCategory() {
        return "SPORT - " + (sportType != null ? sportType : "Sports");
    }

    // Getters and Setters (Encapsulation)
    public String getTeamOne() { return teamOne; }
    public void setTeamOne(String teamOne) { this.teamOne = teamOne; }

    public String getTeamTwo() { return teamTwo; }
    public void setTeamTwo(String teamTwo) { this.teamTwo = teamTwo; }

    public String getSportType() { return sportType; }
    public void setSportType(String sportType) { this.sportType = sportType; }
}

