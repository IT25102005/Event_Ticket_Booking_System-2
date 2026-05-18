package com.example.backend.ticket.exception;

public class InvalidTicketPriceException extends RuntimeException {
    public InvalidTicketPriceException(String message) {
        super(message);
    }
}
