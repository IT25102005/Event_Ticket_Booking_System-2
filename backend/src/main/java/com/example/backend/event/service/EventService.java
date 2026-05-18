package com.example.backend.event.service;

import com.example.backend.event.dto.EventRequestDTO;
import com.example.backend.event.dto.EventResponseDTO;
import com.example.backend.event.model.EventCategory;
import com.example.backend.event.model.EventStatus;

import java.util.List;
/**
 * =====================================================
 * OOP CONCEPT: ABSTRACTION
 *
 * This interface HIDES implementation details.
 * The EventController only knows WHAT these methods do,
 * not HOW they do it.
 *
 * EventServiceImpl provides the actual implementation.
 * This makes the system loosely coupled and easy to test.
 * =====================================================
 */
public interface EventService {

    /** Create a new event and return the saved event as a DTO */
    EventResponseDTO createEvent(EventRequestDTO requestDTO);

    /** Get all events from the database */
    List<EventResponseDTO> getAllEvents();

    /** Get a single event by its ID — throws EventNotFoundException if not found */
    EventResponseDTO getEventById(Long id);

    /** Search events by keyword, category, and/or status (any can be null = no filter) */
    List<EventResponseDTO> searchEvents(String keyword, EventCategory category, EventStatus status);

    /** Update an existing event by ID */
    EventResponseDTO updateEvent(Long id, EventRequestDTO requestDTO);

    /** Permanently delete an event by ID */
    void deleteEvent(Long id);

    /** Change event status to CANCELLED (soft cancel) */
    EventResponseDTO cancelEvent(Long id);
}

