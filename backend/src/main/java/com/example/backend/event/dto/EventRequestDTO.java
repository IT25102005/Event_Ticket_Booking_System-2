package com.example.backend.event.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data Transfer Object (DTO) for receiving event data from the client
 * (frontend).
 *
 * WHY DTO? We use a DTO instead of directly receiving the Entity because:
 * 1. We can accept String for category/status/eventType and convert them in the
 * service
 * 2. We can include subclass-specific fields (artistName, teamOne, etc.) in one
 * flat object
 * 3. Protects our database entity from being directly exposed
 *
 * OOP CONCEPT: ENCAPSULATION — All fields private, accessed via getters/setters
 */
public class EventRequestDTO {

    // ---- Common Event Fields ----
    private String title;
    private String description;
    private String category; // e.g., "CONCERT", "SPORT" — converted to enum in service
    private Long venueId;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private String imageUrl;
    private String status; // e.g., "UPCOMING" — converted to enum in service
    private String eventType; // e.g., "CONCERT", "SPORT", "DRAMA", "MOVIE", "FESTIVAL"

    // ---- ConcertEvent specific fields ----
    private String artistName;
    private String musicGenre;

    // ---- SportEvent specific fields ----
    private String teamOne;
    private String teamTwo;
    private String sportType;

    // ---- DramaEvent specific fields ----
    private String director;
    private String cast;
    private String language;

    // ---- MovieEvent specific fields (shares director, cast from DramaEvent DTO)
    // ----
    private String genre;
    private String rating;

    // ---- FestivalEvent specific fields ----
    private String theme;
    private String organizerName;
    private String highlights;

    // ==================== GETTERS AND SETTERS ====================

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getMusicGenre() {
        return musicGenre;
    }

    public void setMusicGenre(String musicGenre) {
        this.musicGenre = musicGenre;
    }

    public String getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(String teamOne) {
        this.teamOne = teamOne;
    }

    public String getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(String teamTwo) {
        this.teamTwo = teamTwo;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getHighlights() {
        return highlights;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }
}
