package com.example.backend.venue.model;

import jakarta.persistence.*;

/**
 * =====================================================
 * IndoorVenue.java — Subclass of Venue
 * =====================================================
 * OOP Concept: INHERITANCE — Extends Venue (parent class).
 *   IndoorVenue inherits all fields from Venue and adds
 *   its own specific fields: airConditioned, numberOfHalls, hasSoundSystem.
 *
 * OOP Concept: POLYMORPHISM — Overrides getVenueTypeDescription()
 *   to return an indoor-specific description.
 *
 * JPA: Uses @DiscriminatorValue("INDOOR") so the venue_type
 *   column stores "INDOOR" for this subclass in the DB.
 * =====================================================
 */
@Entity
@DiscriminatorValue("INDOOR")
public class IndoorVenue extends Venue {

    // ---- IndoorVenue-specific fields ----

    private Boolean airConditioned = false;  // Does the venue have AC?

    private Integer numberOfHalls = 1;       // How many halls inside?

    private Boolean hasSoundSystem = false;  // Is there a built-in sound system?

    // =====================================================
    // OOP Concept: POLYMORPHISM
    // Overrides parent method to give IndoorVenue-specific description
    // =====================================================
    @Override
    public String getVenueTypeDescription() {
        return "Indoor Venue" +
               (airConditioned != null && airConditioned ? " | Air Conditioned" : "") +
               (hasSoundSystem != null && hasSoundSystem ? " | Sound System Available" : "") +
               " | Halls: " + (numberOfHalls != null ? numberOfHalls : 1);
    }

    // ---- Getters and Setters (Encapsulation) ----

    public Boolean getAirConditioned() { return airConditioned; }
    public void setAirConditioned(Boolean airConditioned) { this.airConditioned = airConditioned; }

    public Integer getNumberOfHalls() { return numberOfHalls; }
    public void setNumberOfHalls(Integer numberOfHalls) { this.numberOfHalls = numberOfHalls; }

    public Boolean getHasSoundSystem() { return hasSoundSystem; }
    public void setHasSoundSystem(Boolean hasSoundSystem) { this.hasSoundSystem = hasSoundSystem; }
}
