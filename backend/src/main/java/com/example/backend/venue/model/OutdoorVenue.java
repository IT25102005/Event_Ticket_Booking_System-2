package com.example.backend.venue.model;

import jakarta.persistence.*;

/**
 * =====================================================
 * OutdoorVenue.java — Subclass of Venue
 * =====================================================
 * OOP Concept: INHERITANCE — Extends Venue (parent class).
 *   OutdoorVenue inherits all fields from Venue and adds
 *   its own specific fields: weatherProtected, openGroundArea, hasParkingArea.
 *
 * OOP Concept: POLYMORPHISM — Overrides getVenueTypeDescription()
 *   to return an outdoor-specific description.
 *
 * JPA: Uses @DiscriminatorValue("OUTDOOR") so the venue_type
 *   column stores "OUTDOOR" for this subclass in the DB.
 * =====================================================
 */
@Entity
@DiscriminatorValue("OUTDOOR")
public class OutdoorVenue extends Venue {

    // ---- OutdoorVenue-specific fields ----

    private Boolean weatherProtected = false;  // Does it have a roof/cover?

    private Double openGroundArea = 0.0;       // Area in square meters

    private Boolean hasParkingArea = false;    // Is there parking?

    // =====================================================
    // OOP Concept: POLYMORPHISM
    // Overrides parent method to give OutdoorVenue-specific description
    // =====================================================
    @Override
    public String getVenueTypeDescription() {
        return "Outdoor Venue" +
               (weatherProtected != null && weatherProtected ? " | Weather Protected" : " | Open Air") +
               (hasParkingArea != null && hasParkingArea ? " | Parking Available" : "") +
               (openGroundArea != null && openGroundArea > 0 ? " | Area: " + openGroundArea + " sqm" : "");
    }

    // ---- Getters and Setters (Encapsulation) ----

    public Boolean getWeatherProtected() { return weatherProtected; }
    public void setWeatherProtected(Boolean weatherProtected) { this.weatherProtected = weatherProtected; }

    public Double getOpenGroundArea() { return openGroundArea; }
    public void setOpenGroundArea(Double openGroundArea) { this.openGroundArea = openGroundArea; }

    public Boolean getHasParkingArea() { return hasParkingArea; }
    public void setHasParkingArea(Boolean hasParkingArea) { this.hasParkingArea = hasParkingArea; }
}
