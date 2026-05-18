package com.example.backend.event.model;

import jakarta.persistence.*;

/**
 * OOP CONCEPT: INHERITANCE — ConcertEvent EXTENDS Event (inherits all base
 * fields)
 * OOP CONCEPT: POLYMORPHISM — Overrides getEventSummary() and
 * getDisplayCategory()
 * OOP CONCEPT: ENCAPSULATION — artistName, musicGenre are private with
 * getters/setters
 */
@Entity
@DiscriminatorValue("CONCERT")
public class ConcertEvent extends Event {

    // Unique fields for Concert events only
    private String artistName;
    private String musicGenre;

    public ConcertEvent() {
        super();
    }

    /**
     * POLYMORPHISM: This method behaves DIFFERENTLY from SportEvent, DramaEvent,
     * etc.
     * Same method name — different behavior per subclass.
     */
    @Override
    public String getEventSummary() {
        return "🎵 Concert: " + getTitle() + " | Artist: " + artistName + " | Genre: " + musicGenre;
    }

    @Override
    public String getDisplayCategory() {
        return "CONCERT - " + (musicGenre != null ? musicGenre : "Music");
    }

    // Getters and Setters (Encapsulation)
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
}
