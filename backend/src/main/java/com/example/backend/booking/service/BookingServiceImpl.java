package com.example.backend.booking.service;

import com.example.backend.booking.model.Booking;
import com.example.backend.booking.model.BookingStatus;
import com.example.backend.booking.model.CounterBooking;
import com.example.backend.booking.model.OnlineBooking;
import com.example.backend.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final com.example.backend.venue.repository.SeatRepository seatRepository;
    private final com.example.backend.ticket.repository.TicketRepository ticketRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              com.example.backend.venue.repository.SeatRepository seatRepository,
                              com.example.backend.ticket.repository.TicketRepository ticketRepository,
                              org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
        try {
            jdbcTemplate.execute("ALTER TABLE bookings MODIFY COLUMN status VARCHAR(50)");
        } catch (Exception e) {
            System.err.println("Could not alter bookings table: " + e.getMessage());
        }
    }

    @org.springframework.transaction.annotation.Transactional
    @Override
    public Booking createBooking(Booking booking) {
        if (booking.getSeatIds() == null || booking.getSeatIds().size() != booking.getNumberOfTickets()) {
            throw new RuntimeException("Number of selected seats must exactly match the number of tickets");
        }

        com.example.backend.ticket.model.TicketType ticket = ticketRepository.findById(booking.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
                
        booking.setTicketPrice(ticket.getFinalPrice());
        
        java.util.List<com.example.backend.venue.model.Seat> selectedSeats = seatRepository.findAllById(booking.getSeatIds());
        if (selectedSeats.size() != booking.getSeatIds().size()) {
            throw new RuntimeException("Some seats were not found");
        }
        
        for (com.example.backend.venue.model.Seat seat : selectedSeats) {
            if (seat.getSeatStatus() != com.example.backend.venue.model.SeatStatus.AVAILABLE) {
                throw new RuntimeException("Seat " + seat.getSeatNumber() + " is no longer available");
            }
            seat.setSeatStatus(com.example.backend.venue.model.SeatStatus.RESERVED);
        }
        
        seatRepository.saveAll(selectedSeats);
        booking.setSelectedSeats(selectedSeats);
        booking.setStatus(BookingStatus.PENDING_PAYMENT);
        
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public Booking updateBooking(Long id, Booking bookingDetails) {
        return bookingRepository.findById(id).map(existingBooking -> {
            existingBooking.setCustomerName(bookingDetails.getCustomerName());
            existingBooking.setEventCode(bookingDetails.getEventCode());
            existingBooking.setNumberOfTickets(bookingDetails.getNumberOfTickets());
            existingBooking.setTicketPrice(bookingDetails.getTicketPrice());
            
            if (bookingDetails.getStatus() != null) {
                existingBooking.setStatus(bookingDetails.getStatus());
            }
            if (bookingDetails.getPaymentStatus() != null) {
                existingBooking.setPaymentStatus(bookingDetails.getPaymentStatus());
            }
            
            if (existingBooking instanceof OnlineBooking && bookingDetails instanceof OnlineBooking) {
                ((OnlineBooking) existingBooking).setPaymentGateway(((OnlineBooking) bookingDetails).getPaymentGateway());
                ((OnlineBooking) existingBooking).setConvenienceFee(((OnlineBooking) bookingDetails).getConvenienceFee());
            } else if (existingBooking instanceof CounterBooking && bookingDetails instanceof CounterBooking) {
                ((CounterBooking) existingBooking).setCounterNumber(((CounterBooking) bookingDetails).getCounterNumber());
                ((CounterBooking) existingBooking).setHandlingAgent(((CounterBooking) bookingDetails).getHandlingAgent());
            }

            return bookingRepository.save(existingBooking);
        }).orElseThrow(() -> new RuntimeException("Booking not found with id " + id));
    }

    @org.springframework.transaction.annotation.Transactional
    @Override
    public Booking updateBookingPaymentStatus(Long bookingId, String bookingStatus, String paymentStatus) {
        return bookingRepository.findById(bookingId).map(booking -> {
            if (bookingStatus != null && !bookingStatus.isBlank()) {
                booking.setStatus(BookingStatus.valueOf(bookingStatus));
            }
            if (paymentStatus != null && !paymentStatus.isBlank()) {
                booking.setPaymentStatus(paymentStatus);
            }
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));
    }

    @org.springframework.transaction.annotation.Transactional
    @Override
    public Booking cancelBooking(Long id) {
        return bookingRepository.findById(id).map(booking -> {
            booking.setStatus(BookingStatus.CANCELLED);
            for (com.example.backend.venue.model.Seat seat : booking.getSelectedSeats()) {
                seat.setSeatStatus(com.example.backend.venue.model.SeatStatus.AVAILABLE);
            }
            seatRepository.saveAll(booking.getSelectedSeats());
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Booking not found with id " + id));
    }

    @org.springframework.transaction.annotation.Transactional
    @Override
    public void deleteBooking(Long id) {
        bookingRepository.findById(id).ifPresent(booking -> {
            for (com.example.backend.venue.model.Seat seat : booking.getSelectedSeats()) {
                seat.setSeatStatus(com.example.backend.venue.model.SeatStatus.AVAILABLE);
            }
            seatRepository.saveAll(booking.getSelectedSeats());
            bookingRepository.delete(booking);
        });
    }
}
