package com.example.backend.venue.service;

import com.example.backend.venue.dto.SeatLayoutResponseDTO;
import com.example.backend.venue.dto.SeatBulkRequestDTO;
import com.example.backend.venue.dto.SeatRequestDTO;
import com.example.backend.venue.dto.SeatResponseDTO;
import com.example.backend.venue.exception.DuplicateSeatException;
import com.example.backend.venue.exception.SeatNotFoundException;
import com.example.backend.venue.exception.VenueNotFoundException;
import com.example.backend.venue.model.*;
import com.example.backend.venue.repository.SeatRepository;
import com.example.backend.venue.repository.VenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * =====================================================
 * SeatServiceImpl.java — Service Implementation
 * =====================================================
 * OOP Concept: ABSTRACTION — Implements SeatService interface.
 *
 * OOP Concept: POLYMORPHISM — When we call seat.calculateFinalPrice()
 *   and seat.getSeatBenefits(), Java automatically uses the correct
 *   override from RegularSeat or VIPSeat.
 *
 * OOP Concept: INHERITANCE — We handle both RegularSeat and VIPSeat
 *   through their common Seat parent type using instanceof checks.
 *
 * OOP Concept: ENCAPSULATION — All business logic, validation,
 *   and rules are inside this class, not in controllers.
 * =====================================================
 */
@Service
@Transactional
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final VenueRepository venueRepository;

    public SeatServiceImpl(SeatRepository seatRepository, VenueRepository venueRepository) {
        this.seatRepository = seatRepository;
        this.venueRepository = venueRepository;
    }

    // =====================================================
    // CREATE: Add one seat to a venue
    // =====================================================
    @Override
    public SeatResponseDTO addSeat(Long venueId, SeatRequestDTO request) {
        // 1. Check venue exists
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException(venueId));

        // 2. Check capacity constraint
        long currentSeatCount = seatRepository.countByVenue_VenueId(venueId);
        if (currentSeatCount >= venue.getCapacity()) {
            throw new IllegalArgumentException(
                "Cannot add more seats. Venue capacity is " + venue.getCapacity() +
                " and it already has " + currentSeatCount + " seats."
            );
        }

        // 3. Check for duplicate seat
        checkDuplicateSeat(venueId, request.getSectionName(), request.getRowName(), request.getSeatNumber());

        // 4. Create the correct seat subclass based on type
        Seat seat = createSeatFromRequest(request, venue);

        // 5. Save and return DTO
        Seat saved = seatRepository.save(seat);
        return mapSeatToResponse(saved);
    }

    // =====================================================
    // CREATE: Add a block of seats (bulk)
    // e.g. Section A, Row B, seats 1-20
    // =====================================================
    @Override
    public List<SeatResponseDTO> addSeatBlock(Long venueId, SeatBulkRequestDTO request) {
        // 1. Check venue exists
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException(venueId));

        // 2. Check total capacity will not be exceeded
        long currentCount = seatRepository.countByVenue_VenueId(venueId);
        long remaining = venue.getCapacity() - currentCount;

        if (request.getNumberOfSeats() > remaining) {
            throw new IllegalArgumentException(
                "Cannot add " + request.getNumberOfSeats() + " seats. " +
                "Only " + remaining + " slots remaining in venue capacity of " + venue.getCapacity() + "."
            );
        }

        // 3. Generate seats: e.g. start=1, count=5 → seatNumbers = "1","2","3","4","5"
        List<Seat> seatsToSave = new ArrayList<>();
        int start = request.getStartingSeatNumber();
        int end   = start + request.getNumberOfSeats();

        for (int i = start; i < end; i++) {
            String seatNumber = String.valueOf(i);

            // Skip duplicates instead of throwing an error for bulk operations
            boolean exists = seatRepository
                .existsByVenue_VenueIdAndSectionNameIgnoreCaseAndRowNameIgnoreCaseAndSeatNumber(
                    venueId,
                    request.getSectionName(),
                    request.getRowName(),
                    seatNumber
                );

            if (!exists) {
                // Build a temporary SeatRequestDTO for each seat
                SeatRequestDTO singleRequest = buildSingleSeatRequest(request, seatNumber);
                Seat seat = createSeatFromRequest(singleRequest, venue);
                seatsToSave.add(seat);
            }
        }

        if (seatsToSave.isEmpty()) {
            throw new DuplicateSeatException("All seats in the specified range already exist in this venue.");
        }

        // 4. Save all seats in one batch
        List<Seat> saved = seatRepository.saveAll(seatsToSave);

        return saved.stream()
                .map(this::mapSeatToResponse)
                .collect(Collectors.toList());
    }

    // =====================================================
    // READ: Get all seats for a venue
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public List<SeatResponseDTO> getSeatsByVenue(Long venueId) {
        if (!venueRepository.existsById(venueId)) {
            throw new VenueNotFoundException(venueId);
        }
        return seatRepository.findByVenue_VenueId(venueId)
                .stream()
                .map(this::mapSeatToResponse)
                .collect(Collectors.toList());
    }

    // =====================================================
    // READ: Get full seat layout (grouped by section and row)
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public SeatLayoutResponseDTO getSeatLayout(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException(venueId));

        List<Seat> allSeats = seatRepository.findByVenue_VenueId(venueId);
        List<SeatResponseDTO> seatDTOs = allSeats.stream()
                .map(this::mapSeatToResponse)
                .collect(Collectors.toList());

        // Build the seat grid: section → row → list of seats
        // Example: { "A": { "Row1": [seat1, seat2...] }, "B": {...} }
        Map<String, Map<String, List<SeatResponseDTO>>> seatGrid = new TreeMap<>();
        for (SeatResponseDTO seat : seatDTOs) {
            seatGrid
                .computeIfAbsent(seat.getSectionName(), k -> new TreeMap<>())
                .computeIfAbsent(seat.getRowName(), k -> new ArrayList<>())
                .add(seat);
        }

        // Build response DTO
        SeatLayoutResponseDTO layout = new SeatLayoutResponseDTO();
        layout.setVenueId(venueId);
        layout.setVenueName(venue.getVenueName());
        layout.setVenueCapacity(venue.getCapacity());
        layout.setAllSeats(seatDTOs);
        layout.setSeatGrid(seatGrid);
        layout.setSections(new ArrayList<>(seatGrid.keySet()));

        // Availability counts
        layout.setTotalSeats(allSeats.size());
        layout.setAvailableSeats(countByStatus(allSeats, SeatStatus.AVAILABLE));
        layout.setReservedSeats(countByStatus(allSeats, SeatStatus.RESERVED));
        layout.setBookedSeats(countByStatus(allSeats, SeatStatus.BOOKED));
        layout.setBlockedSeats(countByStatus(allSeats, SeatStatus.BLOCKED));
        layout.setMaintenanceSeats(countByStatus(allSeats, SeatStatus.MAINTENANCE));
        layout.setVipSeats(countByType(allSeats, SeatType.VIP));
        layout.setRegularSeats(countByType(allSeats, SeatType.REGULAR));

        return layout;
    }

    // =====================================================
    // READ: Get one seat by ID
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public SeatResponseDTO getSeatById(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new SeatNotFoundException(seatId));
        return mapSeatToResponse(seat);
    }

    // =====================================================
    // READ: Search/filter seats
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public List<SeatResponseDTO> searchSeats(Long venueId, String sectionName, SeatType seatType, SeatStatus seatStatus) {
        String section = (sectionName != null && !sectionName.isBlank()) ? sectionName.trim() : null;
        return seatRepository.searchSeats(venueId, section, seatType, seatStatus)
                .stream()
                .map(this::mapSeatToResponse)
                .collect(Collectors.toList());
    }

    // =====================================================
    // UPDATE: Update seat details
    // =====================================================
    @Override
    public SeatResponseDTO updateSeat(Long seatId, SeatRequestDTO request) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new SeatNotFoundException(seatId));

        // Check for duplicate if section/row/number changed
        boolean locationChanged = !seat.getSectionName().equalsIgnoreCase(request.getSectionName())
                || !seat.getRowName().equalsIgnoreCase(request.getRowName())
                || !seat.getSeatNumber().equals(request.getSeatNumber());

        if (locationChanged) {
            checkDuplicateSeat(seat.getVenue().getVenueId(),
                    request.getSectionName(), request.getRowName(), request.getSeatNumber());
        }

        // Update common Seat fields
        seat.setSectionName(request.getSectionName());
        seat.setRowName(request.getRowName());
        seat.setSeatNumber(request.getSeatNumber());
        seat.setSeatType(request.getSeatType());
        if (request.getSeatStatus() != null) seat.setSeatStatus(request.getSeatStatus());
        seat.setBasePrice(request.getBasePrice() != null ? request.getBasePrice() : BigDecimal.ZERO);

        // Update subclass-specific fields
        if (seat instanceof RegularSeat regular) {
            if (request.getStandardView() != null) regular.setStandardView(request.getStandardView());
            if (request.getRegularServiceFee() != null) regular.setRegularServiceFee(request.getRegularServiceFee());

        } else if (seat instanceof VIPSeat vip) {
            if (request.getLoungeAccess() != null) vip.setLoungeAccess(request.getLoungeAccess());
            if (request.getComplimentaryDrink() != null) vip.setComplimentaryDrink(request.getComplimentaryDrink());
            if (request.getVipServiceFee() != null) vip.setVipServiceFee(request.getVipServiceFee());
        }

        // Recalculate final price (polymorphic)
        seat.setFinalPrice(seat.calculateFinalPrice());

        Seat updated = seatRepository.save(seat);
        return mapSeatToResponse(updated);
    }

    // =====================================================
    // UPDATE: Change seat status only (quick update)
    // =====================================================
    @Override
    public SeatResponseDTO updateSeatStatus(Long seatId, SeatStatus newStatus) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new SeatNotFoundException(seatId));
        seat.setSeatStatus(newStatus);
        return mapSeatToResponse(seatRepository.save(seat));
    }

    // =====================================================
    // DELETE: Remove a single seat
    // =====================================================
    @Override
    public void deleteSeat(Long seatId) {
        if (!seatRepository.existsById(seatId)) {
            throw new SeatNotFoundException(seatId);
        }
        seatRepository.deleteById(seatId);
    }

    // =====================================================
    // PRIVATE HELPER: Duplicate check
    // =====================================================
    private void checkDuplicateSeat(Long venueId, String sectionName, String rowName, String seatNumber) {
        boolean exists = seatRepository
            .existsByVenue_VenueIdAndSectionNameIgnoreCaseAndRowNameIgnoreCaseAndSeatNumber(
                venueId, sectionName, rowName, seatNumber);
        if (exists) {
            throw new DuplicateSeatException(sectionName, rowName, seatNumber);
        }
    }

    // =====================================================
    // PRIVATE HELPER: Create seat object from DTO
    // OOP INHERITANCE in action: creates RegularSeat or VIPSeat
    // =====================================================
    private Seat createSeatFromRequest(SeatRequestDTO request, Venue venue) {
        Seat seat;

        if (request.getSeatType() == SeatType.VIP) {
            // OOP: Creating a VIPSeat instance (subclass of Seat)
            VIPSeat vip = new VIPSeat();
            vip.setLoungeAccess(request.getLoungeAccess() != null ? request.getLoungeAccess() : false);
            vip.setComplimentaryDrink(request.getComplimentaryDrink() != null ? request.getComplimentaryDrink() : false);
            vip.setVipServiceFee(request.getVipServiceFee() != null ? request.getVipServiceFee() : BigDecimal.ZERO);
            seat = vip;

        } else {
            // OOP: Creating a RegularSeat instance (subclass of Seat)
            RegularSeat regular = new RegularSeat();
            regular.setStandardView(request.getStandardView() != null ? request.getStandardView() : true);
            regular.setRegularServiceFee(request.getRegularServiceFee() != null ? request.getRegularServiceFee() : BigDecimal.ZERO);
            seat = regular;
        }

        // Set common Seat fields (inherited by both subclasses)
        seat.setVenue(venue);
        seat.setSectionName(request.getSectionName());
        seat.setRowName(request.getRowName());
        seat.setSeatNumber(request.getSeatNumber());
        seat.setSeatType(request.getSeatType());
        seat.setSeatStatus(request.getSeatStatus() != null ? request.getSeatStatus() : SeatStatus.AVAILABLE);
        seat.setBasePrice(request.getBasePrice() != null ? request.getBasePrice() : BigDecimal.ZERO);

        return seat;
    }

    // =====================================================
    // PRIVATE HELPER: Build SeatRequestDTO from SeatBulkRequestDTO
    // =====================================================
    private SeatRequestDTO buildSingleSeatRequest(SeatBulkRequestDTO bulk, String seatNumber) {
        SeatRequestDTO single = new SeatRequestDTO();
        single.setSectionName(bulk.getSectionName());
        single.setRowName(bulk.getRowName());
        single.setSeatNumber(seatNumber);
        single.setSeatType(bulk.getSeatType());
        single.setSeatStatus(bulk.getSeatStatus());
        single.setBasePrice(bulk.getBasePrice());
        single.setStandardView(bulk.getStandardView());
        single.setRegularServiceFee(bulk.getRegularServiceFee());
        single.setLoungeAccess(bulk.getLoungeAccess());
        single.setComplimentaryDrink(bulk.getComplimentaryDrink());
        single.setVipServiceFee(bulk.getVipServiceFee());
        return single;
    }

    // =====================================================
    // PRIVATE HELPER: Map Seat entity → SeatResponseDTO
    // OOP POLYMORPHISM in action:
    //   seat.getSeatBenefits()     → calls RegularSeat or VIPSeat override
    //   seat.calculateFinalPrice() → calls RegularSeat or VIPSeat override
    // =====================================================
    private SeatResponseDTO mapSeatToResponse(Seat seat) {
        SeatResponseDTO dto = new SeatResponseDTO();

        dto.setSeatId(seat.getSeatId());
        dto.setVenueId(seat.getVenue().getVenueId());
        dto.setVenueName(seat.getVenue().getVenueName());
        dto.setSectionName(seat.getSectionName());
        dto.setRowName(seat.getRowName());
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setSeatType(seat.getSeatType());
        dto.setSeatStatus(seat.getSeatStatus());
        dto.setBasePrice(seat.getBasePrice());
        dto.setCreatedAt(seat.getCreatedAt());
        dto.setUpdatedAt(seat.getUpdatedAt());

        // OOP POLYMORPHISM: Java decides at runtime which override to call
        dto.setFinalPrice(seat.calculateFinalPrice());
        dto.setSeatBenefits(seat.getSeatBenefits());

        // Map subclass-specific fields
        if (seat instanceof RegularSeat regular) {
            dto.setSeatCategory("REGULAR");
            dto.setStandardView(regular.getStandardView());
            dto.setRegularServiceFee(regular.getRegularServiceFee());

        } else if (seat instanceof VIPSeat vip) {
            dto.setSeatCategory("VIP");
            dto.setLoungeAccess(vip.getLoungeAccess());
            dto.setComplimentaryDrink(vip.getComplimentaryDrink());
            dto.setVipServiceFee(vip.getVipServiceFee());
        }

        return dto;
    }

    // =====================================================
    // PRIVATE HELPER: Count seats by status
    // =====================================================
    private long countByStatus(List<Seat> seats, SeatStatus status) {
        return seats.stream().filter(s -> s.getSeatStatus() == status).count();
    }

    // =====================================================
    // PRIVATE HELPER: Count seats by type
    // =====================================================
    private long countByType(List<Seat> seats, SeatType type) {
        return seats.stream().filter(s -> s.getSeatType() == type).count();
    }
}
