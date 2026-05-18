package com.example.backend.ticket.exception;

public class DuplicateTicketTypeException extends RuntimeException {
    public DuplicateTicketTypeException(String message) {
        super(message);
    }
}
