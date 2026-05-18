package com.example.backend.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * =====================================================
 * OOP CONCEPT 1: ENCAPSULATION
 * - All fields are PRIVATE
 * - Access is only through PUBLIC getters and setters
 * - Validation annotations protect data integrity
 *
 * OOP CONCEPT 2: INHERITANCE (Base/Parent Class)
 * - This abstract class is the PARENT of all event types
 * - ConcertEvent, SportEvent, DramaEvent, MovieEvent, FestivalEvent all EXTEND
 * this class
 * - Child classes inherit ALL fields and methods defined here
 *
 * OOP CONCEPT 4: ABSTRACTION
 * - getEventSummary() is declared abstract → forces subclasses to implement it
 * - Hides implementation details from the caller
 * =====================================================
 */
@Entity
@Table(name = "events")
// SINGLE_TABLE: All event types stored in one table — simple and easy to
// explain in viva
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "event_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Event {

    // --- Primary Key ---
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    // --- ENCAPSULATION: All fields are private ---
    @NotBlank(message = "Title cannot be empty")
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventCategory category;

    @Column(name = "venue_id", nullable = true)
    private Long venueId;

    @Column(nullable = true)
    private LocalDate eventDate;

    @Column(nullable = true)
    private LocalTime eventTime;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // --- Auto-set timestamps ---
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = EventStatus.UPCOMING; // Default status
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // =====================================================
    // OOP CONCEPT 3: POLYMORPHISM (Abstract Method)
    // Each subclass MUST override this method with its own behavior
    // =====================================================
    public abstract String getEventSummary();

    // This method CAN be overridden by subclasses (Polymorphism)
    public String getDisplayCategory() {
        return category != null ? category.name() : "GENERAL";
    }

    // =====================================================
    // GETTERS AND SETTERS — Encapsulation in action
    // =====================================================
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventCategory getCategory() {
        return category;
    }

    public void setCategory(EventCategory category) {
        this.category = category;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalTime eventTime) {
        this.eventTime = eventTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
