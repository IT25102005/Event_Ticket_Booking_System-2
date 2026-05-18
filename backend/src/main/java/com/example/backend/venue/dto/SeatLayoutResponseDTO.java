package com.example.backend.venue.dto;

import java.util.List;
import java.util.Map;

/**
 * =====================================================
 * SeatLayoutResponseDTO.java
 * =====================================================
 * Returns the full seat layout for a venue.
 * Groups seats by section and row for the frontend grid.
 *
 * OOP Concept: ENCAPSULATION — Bundles layout data into
 *   a clean response object for the frontend.
 * =====================================================
 */
public class SeatLayoutResponseDTO {

    private Long venueId;
    private String venueName;
    private Integer venueCapacity;

    // Availability summary counts
    private long totalSeats;
    private long availableSeats;
    private long reservedSeats;
    private long bookedSeats;
    private long blockedSeats;
    private long maintenanceSeats;
    private long vipSeats;
    private long regularSeats;

    // List of distinct section names
    private List<String> sections;

    // All seats — grouped by section then row: { "A": { "Row1": [seat, seat...] } }
    private Map<String, Map<String, List<SeatResponseDTO>>> seatGrid;

    // Flat list of all seats (useful for table view)
    private List<SeatResponseDTO> allSeats;

    // ---- Getters and Setters ----

    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }

    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }

    public Integer getVenueCapacity() { return venueCapacity; }
    public void setVenueCapacity(Integer venueCapacity) { this.venueCapacity = venueCapacity; }

    public long getTotalSeats() { return totalSeats; }
    public void setTotalSeats(long totalSeats) { this.totalSeats = totalSeats; }

    public long getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(long availableSeats) { this.availableSeats = availableSeats; }

    public long getReservedSeats() { return reservedSeats; }
    public void setReservedSeats(long reservedSeats) { this.reservedSeats = reservedSeats; }

    public long getBookedSeats() { return bookedSeats; }
    public void setBookedSeats(long bookedSeats) { this.bookedSeats = bookedSeats; }

    public long getBlockedSeats() { return blockedSeats; }
    public void setBlockedSeats(long blockedSeats) { this.blockedSeats = blockedSeats; }

    public long getMaintenanceSeats() { return maintenanceSeats; }
    public void setMaintenanceSeats(long maintenanceSeats) { this.maintenanceSeats = maintenanceSeats; }

    public long getVipSeats() { return vipSeats; }
    public void setVipSeats(long vipSeats) { this.vipSeats = vipSeats; }

    public long getRegularSeats() { return regularSeats; }
    public void setRegularSeats(long regularSeats) { this.regularSeats = regularSeats; }

    public List<String> getSections() { return sections; }
    public void setSections(List<String> sections) { this.sections = sections; }

    public Map<String, Map<String, List<SeatResponseDTO>>> getSeatGrid() { return seatGrid; }
    public void setSeatGrid(Map<String, Map<String, List<SeatResponseDTO>>> seatGrid) { this.seatGrid = seatGrid; }

    public List<SeatResponseDTO> getAllSeats() { return allSeats; }
    public void setAllSeats(List<SeatResponseDTO> allSeats) { this.allSeats = allSeats; }
}
