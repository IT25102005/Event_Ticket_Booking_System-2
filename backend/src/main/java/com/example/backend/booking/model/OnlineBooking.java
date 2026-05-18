package com.example.backend.booking.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ONLINE")
public class OnlineBooking extends Booking {

    @Column(name = "payment_gateway")
    private String paymentGateway;

    @Column(name = "convenience_fee")
    private Double convenienceFee;

    public OnlineBooking() {
        super();
    }

    public OnlineBooking(String customerName, String eventCode, Integer numberOfTickets, Double ticketPrice, String paymentGateway, Double convenienceFee) {
        super(customerName, eventCode, numberOfTickets, ticketPrice);
        this.paymentGateway = paymentGateway;
        this.convenienceFee = convenienceFee;
    }

    @Override
    public Double calculateTotalAmount() {
        double basePrice = (getNumberOfTickets() != null && getTicketPrice() != null) ? getNumberOfTickets() * getTicketPrice() : 0.0;
        return basePrice + (convenienceFee != null ? convenienceFee : 0.0);
    }

    @Override
    public String getBookingSummary() {
        return "Online Booking via " + paymentGateway + ". Total: $" + calculateTotalAmount();
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public Double getConvenienceFee() {
        return convenienceFee;
    }

    public void setConvenienceFee(Double convenienceFee) {
        this.convenienceFee = convenienceFee;
    }
}
