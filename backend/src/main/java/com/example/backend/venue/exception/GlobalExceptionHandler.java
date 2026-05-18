package com.example.backend.venue.exception;

import com.example.backend.event.exception.EventNotFoundException;
import com.example.backend.ticket.exception.DuplicateTicketTypeException;
import com.example.backend.ticket.exception.InvalidTicketPriceException;
import com.example.backend.ticket.exception.TicketNotFoundException;
import com.example.backend.user.exception.DuplicateEmailException;
import com.example.backend.user.exception.InvalidLoginException;
import com.example.backend.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * =====================================================
 * UNIFIED GlobalExceptionHandler
 * =====================================================
 * Single centralized error handler for ALL modules:
 *   - Event module
 *   - Ticket module
 *   - User module
 *   - Venue module
 *   - Booking module
 *   - Payment module
 *
 * @RestControllerAdvice applies to ALL @RestController classes
 * across the entire application.
 * =====================================================
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---- Helper to build consistent error response ----
    private Map<String, Object> buildErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return body;
    }

    // ===================== EVENT MODULE =====================

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEventNotFound(EventNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(HttpStatus.NOT_FOUND, "Event Not Found", ex.getMessage()));
    }

    // ===================== TICKET MODULE =====================

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTicketNotFound(TicketNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(HttpStatus.NOT_FOUND, "Ticket Not Found", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateTicketTypeException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateTicket(DuplicateTicketTypeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildErrorResponse(HttpStatus.CONFLICT, "Duplicate Ticket Type", ex.getMessage()));
    }

    @ExceptionHandler(InvalidTicketPriceException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidTicketPrice(InvalidTicketPriceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Ticket Price", ex.getMessage()));
    }

    // ===================== USER MODULE =====================

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(HttpStatus.NOT_FOUND, "User Not Found", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEmail(DuplicateEmailException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildErrorResponse(HttpStatus.CONFLICT, "Duplicate Email", ex.getMessage()));
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidLogin(InvalidLoginException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid Credentials", ex.getMessage()));
    }

    // ===================== VENUE MODULE =====================

    @ExceptionHandler(VenueNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleVenueNotFound(VenueNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(HttpStatus.NOT_FOUND, "Venue Not Found", ex.getMessage()));
    }

    @ExceptionHandler(SeatNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleSeatNotFound(SeatNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(HttpStatus.NOT_FOUND, "Seat Not Found", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateSeatException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateSeat(DuplicateSeatException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildErrorResponse(HttpStatus.CONFLICT, "Duplicate Seat", ex.getMessage()));
    }

    // ===================== VALIDATION (ALL MODULES) =====================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Failed");
        body.put("message", "One or more fields have validation errors");
        body.put("fieldErrors", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage()));
    }

    // ===================== CATCH-ALL =====================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                        "An unexpected error occurred: " + ex.getMessage()));
    }
}
