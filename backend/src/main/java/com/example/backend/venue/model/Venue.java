package com.example.backend.venue.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * =====================================================
 * Venue.java — Base Entity Class
 * =====================================================
 * OOP Concept: INHERITANCE — This is the parent class.
 *   IndoorVenue and OutdoorVenue both extend this class.
 *
 * OOP Concept: ENCAPSULATION — All fields are private.
 *   Data is accessed only via getters and setters.
 *
 * OOP Concept: ABSTRACTION — The method getVenueTypeDescription()
 *   is meant to be overridden by subclasses (Polymorphism).
 *
 * JPA: SINGLE_TABLE inheritance — all venue types share one DB table.
 *   A discriminator column "venue_type" stores the class type.
 * =====================================================
 */
@Entity
@Table(name = "venues")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "venue_type", discriminatorType = DiscriminatorType.STRING)
public class Venue {

    // ---- Primary Key ----
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long venueId;

    // ---- Core Fields (Encapsulated with private access) ----
    @Column(nullable = false, length = 150)
    private String venueName;

    @Column(nullable = false, length = 200)
    private String location;

    @Column(length = 300)
    private String address;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false)
    private Integer capacity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String facilities;

    @Column(length = 500)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VenueStatus status = VenueStatus.ACTIVE;

    // ---- Timestamps ----
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ---- Relationship: One Venue has Many Seats ----
    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> seats = new ArrayList<>();

    // ---- Lifecycle Hooks ----
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // =====================================================
    // OOP Concept: POLYMORPHISM
    // This method is overridden in IndoorVenue and OutdoorVenue
    // to return different descriptions based on venue type.
    // =====================================================
    public String getVenueTypeDescription() {
        return "General Venue";
    }

    // =====================================================
    // ENCAPSULATION: Getters and Setters
    // Private fields are accessed only via these methods
    // =====================================================

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<Seat> getSeats() { return seats; }
    public void setSeats(List<Seat> seats) { this.seats = seats; }
}
