package com.example.backend.venue.dto;

import com.example.backend.venue.model.VenueStatus;
import jakarta.validation.constraints.*;

/**
 * =====================================================
 * VenueRequestDTO.java
 * =====================================================
 * OOP Concept: ENCAPSULATION — This DTO protects the
 *   Venue entity from direct exposure. The controller
 *   receives this object, not the raw entity.
 *
 * Validation annotations ensure data integrity before
 *   the service layer processes any data.
 * =====================================================
 */
public class VenueRequestDTO {

    // ---- Common Venue Fields ----

    @NotBlank(message = "Venue name cannot be empty")
    @Size(max = 150, message = "Venue name must not exceed 150 characters")
    private String venueName;

    @NotBlank(message = "Location cannot be empty")
    private String location;

    private String address;

    @NotBlank(message = "City cannot be empty")
    private String city;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be greater than 0")
    private Integer capacity;

    private String description;
    private String facilities;
    private String imageUrl;

    private VenueStatus status = VenueStatus.ACTIVE;

    // ---- Venue Type: "INDOOR" or "OUTDOOR" ----
    @NotBlank(message = "Venue type is required (INDOOR or OUTDOOR)")
    private String venueType;  // "INDOOR" or "OUTDOOR"

    // ---- IndoorVenue specific fields ----
    private Boolean airConditioned;
    private Integer numberOfHalls;
    private Boolean hasSoundSystem;

    // ---- OutdoorVenue specific fields ----
    private Boolean weatherProtected;
    private Double openGroundArea;
    private Boolean hasParkingArea;

    // ---- Getters and Setters ----

    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFacilities() { return facilities; }
    public void setFacilities(String facilities) { this.facilities = facilities; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public VenueStatus getStatus() { return status; }
    public void setStatus(VenueStatus status) { this.status = status; }

    public String getVenueType() { return venueType; }
    public void setVenueType(String venueType) { this.venueType = venueType; }

    public Boolean getAirConditioned() { return airConditioned; }
    public void setAirConditioned(Boolean airConditioned) { this.airConditioned = airConditioned; }

    public Integer getNumberOfHalls() { return numberOfHalls; }
    public void setNumberOfHalls(Integer numberOfHalls) { this.numberOfHalls = numberOfHalls; }

    public Boolean getHasSoundSystem() { return hasSoundSystem; }
    public void setHasSoundSystem(Boolean hasSoundSystem) { this.hasSoundSystem = hasSoundSystem; }

    public Boolean getWeatherProtected() { return weatherProtected; }
    public void setWeatherProtected(Boolean weatherProtected) { this.weatherProtected = weatherProtected; }

    public Double getOpenGroundArea() { return openGroundArea; }
    public void setOpenGroundArea(Double openGroundArea) { this.openGroundArea = openGroundArea; }

    public Boolean getHasParkingArea() { return hasParkingArea; }
    public void setHasParkingArea(Boolean hasParkingArea) { this.hasParkingArea = hasParkingArea; }

    // ---- Event Association Link ----
    private Long eventId;

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
}
