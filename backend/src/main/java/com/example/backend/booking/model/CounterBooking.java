package com.example.backend.booking.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("COUNTER")
public class CounterBooking extends Booking {

    @Column(name = "counter_number")
    private Integer counterNumber;

    @Column(name = "handling_agent")
    private String handlingAgent;

    public CounterBooking() {
        super();
    }

    public CounterBooking(String customerName, String eventCode, Integer numberOfTickets, Double ticketPrice, Integer counterNumber, String handlingAgent) {
        super(customerName, eventCode, numberOfTickets, ticketPrice);
        this.counterNumber = counterNumber;
        this.handlingAgent = handlingAgent;
    }

    @Override
    public Double calculateTotalAmount() {
        return (getNumberOfTickets() != null && getTicketPrice() != null) ? getNumberOfTickets() * getTicketPrice() : 0.0;
    }

    @Override
    public String getBookingSummary() {
        return "Counter Booking at Counter " + counterNumber + " handled by " + handlingAgent + ". Total: $" + calculateTotalAmount();
    }

    public Integer getCounterNumber() {
        return counterNumber;
    }

    public void setCounterNumber(Integer counterNumber) {
        this.counterNumber = counterNumber;
    }

    public String getHandlingAgent() {
        return handlingAgent;
    }

    public void setHandlingAgent(String handlingAgent) {
        this.handlingAgent = handlingAgent;
    }
}
