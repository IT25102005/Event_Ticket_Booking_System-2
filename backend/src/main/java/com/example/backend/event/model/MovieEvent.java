package com.example.backend.event.model;

import jakarta.persistence.*;

/**
 * OOP CONCEPT: INHERITANCE — MovieEvent EXTENDS Event
 * OOP CONCEPT: POLYMORPHISM — Overrides getEventSummary() with movie-specific behavior
 * OOP CONCEPT: ENCAPSULATION — director, cast, genre, rating are private
 */
@Entity
@DiscriminatorValue("MOVIE")
public class MovieEvent extends Event {

    // Unique fields for Movie events only
    private String director;
    @Column(name = "event_cast")
    private String cast;
    private String genre;
    private String rating; // e.g., U, U/A, A, PG-13

    public MovieEvent() {
        super();
    }

    /**
     * POLYMORPHISM: Different behavior from DramaEvent even though both have a director field.
     */
    @Override
    public String getEventSummary() {
        return "🎬 Movie: " + getTitle() + " | Dir: " + director + " | Genre: " + genre + " | Rating: " + rating;
    }

    @Override
    public String getDisplayCategory() {
        return "MOVIE - " + (genre != null ? genre : "Cinema");
    }

    // Getters and Setters (Encapsulation)
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getCast() { return cast; }
    public void setCast(String cast) { this.cast = cast; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }
}
