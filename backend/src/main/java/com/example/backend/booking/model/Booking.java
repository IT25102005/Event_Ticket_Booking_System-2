package com.example.backend.booking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Entity
@Table(name = "bookings")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "booking_type", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = OnlineBooking.class, name = "ONLINE"),
    @JsonSubTypes.Type(value = CounterBooking.class, name = "COUNTER")
})
public abstract class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "event_id")
    private Long eventId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "booking_seats",
        joinColumns = @JoinColumn(name = "booking_id"),
        inverseJoinColumns = @JoinColumn(name = "seat_id")
    )
    private java.util.List<com.example.backend.venue.model.Seat> selectedSeats = new java.util.ArrayList<>();

    @Transient
    private java.util.List<Long> seatIds = new java.util.ArrayList<>();

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String eventCode;

    @Column(nullable = false)
    private Integer numberOfTickets;

    @Column(nullable = false)
    private Double ticketPrice;

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    @Column(nullable = true)
    private String phoneNumber;

    @Column(name = "preferred_payment_method", nullable = true)
    private String preferredPaymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private BookingStatus status;
    
    @Column(name = "payment_status")
    private String paymentStatus = "PENDING";

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_type", insertable = false, updatable = false)
    private BookingType type;

    // Constructors
    public Booking() {
        this.bookingDate = LocalDateTime.now();
        this.status = BookingStatus.PENDING_PAYMENT;
    }

    public Booking(String customerName, String eventCode, Integer numberOfTickets, Double ticketPrice) {
        this();
        this.customerName = customerName;
        this.eventCode = eventCode;
        this.numberOfTickets = numberOfTickets;
        this.ticketPrice = ticketPrice;
    }

    // Abstract methods to enforce polymorphism
    public abstract Double calculateTotalAmount();
    public abstract String getBookingSummary();

    public Double getTotalAmount() {
        return calculateTotalAmount();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public Integer getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(Integer numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public BookingType getType() {
        return type;
    }

    public void setType(BookingType type) {
        this.type = type;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public java.util.List<com.example.backend.venue.model.Seat> getSelectedSeats() { return selectedSeats; }
    public void setSelectedSeats(java.util.List<com.example.backend.venue.model.Seat> selectedSeats) { this.selectedSeats = selectedSeats; }

    public java.util.List<Long> getSeatIds() { return seatIds; }
    public void setSeatIds(java.util.List<Long> seatIds) { this.seatIds = seatIds; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPreferredPaymentMethod() { return preferredPaymentMethod; }
    public void setPreferredPaymentMethod(String preferredPaymentMethod) { this.preferredPaymentMethod = preferredPaymentMethod; }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}
