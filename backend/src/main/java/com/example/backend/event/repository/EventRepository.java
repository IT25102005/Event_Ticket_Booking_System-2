package com.example.backend.event.repository;

import com.example.backend.event.model.Event;
import com.example.backend.event.model.EventCategory;
import com.example.backend.event.model.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    java.util.Optional<Event> findFirstByVenueId(Long venueId);

    List<Event> findByTitleContainingIgnoreCase(String title);

    List<Event> findByCategory(EventCategory category);

    List<Event> findByStatus(EventStatus status);

    @Query("SELECT e FROM Event e WHERE " +
            "(:keyword IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:category IS NULL OR e.category = :category) AND " +
            "(:status IS NULL OR e.status = :status)")
    List<Event> searchEvents(
            @Param("keyword") String keyword,
            @Param("category") EventCategory category,
            @Param("status") EventStatus status
    );
}