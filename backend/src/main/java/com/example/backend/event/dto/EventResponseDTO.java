package com.example.backend.event.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Data Transfer Object for sending event data BACK to the client (frontend).
 *
 * Includes the result of Polymorphism:
 *   - eventSummary: different string returned by each subclass
 *   - displayCategory: different formatted string by each subclass
 *
 * OOP CONCEPT: ENCAPSULATION — All fields private with getters/setters
 */
public class EventResponseDTO {

    // ---- Common Fields ----
    private Long eventId;
    private String title;
    private String description;
    private String category;
    private Long venueId;
    private String venueName;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private String imageUrl;
    private int totalSeats;
    private int availableSeats;
    private BigDecimal basePrice;
    private String status;
    private String eventType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ---- Polymorphism Results ----
    private String eventSummary;      // Returns different value for each event subclass
    private String displayCategory;   // Returns different value for each event subclass

    // ---- ConcertEvent fields ----
    private String artistName;
    private String musicGenre;

    // ---- SportEvent fields ----
    private String teamOne;
    private String teamTwo;
    private String sportType;

    // ---- DramaEvent fields ----
    private String director;
    private String cast;
    private String language;

    // ---- MovieEvent fields ----
    private String genre;
    private String rating;

    // ---- FestivalEvent fields ----
    private String theme;
    private String organizerName;
    private String highlights;

    // ==================== GETTERS AND SETTERS ====================

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }

    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

    public LocalTime getEventTime() { return eventTime; }
    public void setEventTime(LocalTime eventTime) { this.eventTime = eventTime; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getEventSummary() { return eventSummary; }
    public void setEventSummary(String eventSummary) { this.eventSummary = eventSummary; }

    public String getDisplayCategory() { return displayCategory; }
    public void setDisplayCategory(String displayCategory) { this.displayCategory = displayCategory; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    public String getMusicGenre() { return musicGenre; }
    public void setMusicGenre(String musicGenre) { this.musicGenre = musicGenre; }

    public String getTeamOne() { return teamOne; }
    public void setTeamOne(String teamOne) { this.teamOne = teamOne; }

    public String getTeamTwo() { return teamTwo; }
    public void setTeamTwo(String teamTwo) { this.teamTwo = teamTwo; }

    public String getSportType() { return sportType; }
    public void setSportType(String sportType) { this.sportType = sportType; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getCast() { return cast; }
    public void setCast(String cast) { this.cast = cast; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public String getOrganizerName() { return organizerName; }
    public void setOrganizerName(String organizerName) { this.organizerName = organizerName; }

    public String getHighlights() { return highlights; }
    public void setHighlights(String highlights) { this.highlights = highlights; }
}

