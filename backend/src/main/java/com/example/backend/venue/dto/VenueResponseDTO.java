package com.example.backend.venue.dto;

import com.example.backend.venue.model.VenueStatus;
import java.time.LocalDateTime;

/**
 * =====================================================
 * VenueResponseDTO.java
 * =====================================================
 * OOP Concept: ENCAPSULATION — This DTO is what the API
 *   returns to the client. It hides the internal JPA entity
 *   and the lazy-loaded seats list, preventing serialization issues.
 *
 * It includes a venueTypeDescription from the polymorphic
 *   getVenueTypeDescription() method call in the service layer.
 * =====================================================
 */
public class VenueResponseDTO {

    private Long venueId;
    private String venueName;
    private String location;
    private String address;
    private String city;
    private Integer capacity;
    private String description;
    private String facilities;
    private String imageUrl;
    private VenueStatus status;

    // Discriminator value: "INDOOR" or "OUTDOOR"
    private String venueType;

    // Result of polymorphic getVenueTypeDescription() call
    private String venueTypeDescription;

    // ---- IndoorVenue fields (null if outdoor) ----
    private Boolean airConditioned;
    private Integer numberOfHalls;
    private Boolean hasSoundSystem;

    // ---- OutdoorVenue fields (null if indoor) ----
    private Boolean weatherProtected;
    private Double openGroundArea;
    private Boolean hasParkingArea;

    // ---- Seat Summary (computed in service layer) ----
    private long totalSeats;
    private long availableSeats;
    private long bookedSeats;
    private long reservedSeats;
    private long vipSeats;
    private long regularSeats;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ---- Getters and Setters ----

    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }

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

    public String getVenueTypeDescription() { return venueTypeDescription; }
    public void setVenueTypeDescription(String venueTypeDescription) { this.venueTypeDescription = venueTypeDescription; }

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

    public long getTotalSeats() { return totalSeats; }
    public void setTotalSeats(long totalSeats) { this.totalSeats = totalSeats; }

    public long getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(long availableSeats) { this.availableSeats = availableSeats; }

    public long getBookedSeats() { return bookedSeats; }
    public void setBookedSeats(long bookedSeats) { this.bookedSeats = bookedSeats; }

    public long getReservedSeats() { return reservedSeats; }
    public void setReservedSeats(long reservedSeats) { this.reservedSeats = reservedSeats; }

    public long getVipSeats() { return vipSeats; }
    public void setVipSeats(long vipSeats) { this.vipSeats = vipSeats; }

    public long getRegularSeats() { return regularSeats; }
    public void setRegularSeats(long regularSeats) { this.regularSeats = regularSeats; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // ---- Event Association Link ----
    private Long eventId;

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
}
