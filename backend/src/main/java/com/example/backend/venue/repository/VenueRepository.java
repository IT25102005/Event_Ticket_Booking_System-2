package com.example.backend.venue.repository;

import com.example.backend.venue.model.Venue;
import com.example.backend.venue.model.VenueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * =====================================================
 * VenueRepository.java
 * =====================================================
 * OOP Concept: ABSTRACTION — Spring Data JPA provides
 *   CRUD methods automatically. We only define the interface.
 *
 * Extends JpaRepository which gives us:
 *   save(), findById(), findAll(), deleteById(), etc.
 * =====================================================
 */
@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

    // Find all venues by status (e.g., ACTIVE, INACTIVE)
    List<Venue> findByStatus(VenueStatus status);

    // Find venues by city (case-insensitive)
    List<Venue> findByCityIgnoreCase(String city);

    // Check if a venue name already exists (to prevent duplicates)
    boolean existsByVenueNameIgnoreCase(String venueName);

    /**
     * Search venues by keyword across name, location, city, description.
     * Also supports filtering by status and venueType (discriminator column).
     */
    @Query("SELECT v FROM Venue v WHERE " +
           "(:keyword IS NULL OR LOWER(v.venueName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "   OR LOWER(v.location) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "   OR LOWER(v.city) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "   OR LOWER(v.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:city IS NULL OR LOWER(v.city) = LOWER(:city)) " +
           "AND (:status IS NULL OR v.status = :status) " +
           "ORDER BY v.createdAt DESC")
    List<Venue> searchVenues(
            @Param("keyword") String keyword,
            @Param("city") String city,
            @Param("status") VenueStatus status
    );

    // Get all venues ordered by newest first
    List<Venue> findAllByOrderByCreatedAtDesc();
}
