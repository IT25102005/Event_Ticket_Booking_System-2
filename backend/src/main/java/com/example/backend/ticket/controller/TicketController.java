package com.example.backend.ticket.controller;

import com.example.backend.ticket.dto.*;
import com.example.backend.ticket.model.TicketCategory;
import com.example.backend.ticket.model.TicketStatus;
import com.example.backend.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*") // For local development with frontend
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponseDTO> createTicket(@RequestBody TicketRequestDTO request) {
        return new ResponseEntity<>(ticketService.createTicket(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<TicketResponseDTO>> getTicketsByEventId(@PathVariable Long eventId) {
        return ResponseEntity.ok(ticketService.getTicketsByEventId(eventId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TicketResponseDTO>> searchTickets(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) TicketCategory category,
            @RequestParam(required = false) TicketStatus status) {
        return ResponseEntity.ok(ticketService.searchTickets(keyword, eventId, category, status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> updateTicket(@PathVariable Long id, @RequestBody TicketRequestDTO request) {
        return ResponseEntity.ok(ticketService.updateTicket(id, request));
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<TicketResponseDTO> updatePrice(@PathVariable Long id, @RequestParam Double price) {
        return ResponseEntity.ok(ticketService.updatePrice(id, price));
    }

    @PatchMapping("/{id}/quantity")
    public ResponseEntity<TicketResponseDTO> updateQuantity(@PathVariable Long id, @RequestParam Integer additionalQuantity) {
        return ResponseEntity.ok(ticketService.updateQuantity(id, additionalQuantity));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<TicketResponseDTO> activateTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.activateTicket(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<TicketResponseDTO> deactivateTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.deactivateTicket(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<TicketAvailabilityDTO> getAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketAvailability(id));
    }
}
