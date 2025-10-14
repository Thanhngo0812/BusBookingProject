package com.ct08j2e.busbookingproject.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Tickets")
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Integer ticketId;
    
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
    
    @OneToOne
    @JoinColumn(name = "seat_id", unique = true)
    private Seat seat;
    
    @Column(name = "passenger_name", nullable = false, length = 100)
    private String passengerName;
    
    @Column(name = "passenger_phone", length = 15)
    private String passengerPhone;
    
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "qr_code_url", length = 255)
    private String qrCodeUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TicketStatus status = TicketStatus.VALID;
    
    public Ticket() {
    }
    
    public Ticket(Integer ticketId, Booking booking, Seat seat, String passengerName,
                  String passengerPhone, BigDecimal price, String qrCodeUrl, TicketStatus status) {
        this.ticketId = ticketId;
        this.booking = booking;
        this.seat = seat;
        this.passengerName = passengerName;
        this.passengerPhone = passengerPhone;
        this.price = price;
        this.qrCodeUrl = qrCodeUrl;
        this.status = status;
    }
    
    public Integer getTicketId() {
        return ticketId;
    }
    
    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }
    
    public Booking getBooking() {
        return booking;
    }
    
    public void setBooking(Booking booking) {
        this.booking = booking;
    }
    
    public Seat getSeat() {
        return seat;
    }
    
    public void setSeat(Seat seat) {
        this.seat = seat;
    }
    
    public String getPassengerName() {
        return passengerName;
    }
    
    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }
    
    public String getPassengerPhone() {
        return passengerPhone;
    }
    
    public void setPassengerPhone(String passengerPhone) {
        this.passengerPhone = passengerPhone;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String getQrCodeUrl() {
        return qrCodeUrl;
    }
    
    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }
    
    public TicketStatus getStatus() {
        return status;
    }
    
    public void setStatus(TicketStatus status) {
        this.status = status;
    }
    
    public enum TicketStatus {
        VALID, CANCELLED, CHECKED_IN
    }
}