package com.ct08j2e.busbookingproject.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "Bookings")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;
    
    @Column(name = "booking_time")
    private LocalDateTime bookingTime = LocalDateTime.now();
    
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Convert(converter = Booking.BookingStatusConverter.class)
    @Column(name = "status")
    private BookingStatus status = BookingStatus.PENDING_PAYMENT;
    
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private Set<Ticket> tickets;
    
    @OneToMany(mappedBy = "booking")
    private Set<Payment> payments;
    
    public Booking() {
    }
    
    public Booking(Integer bookingId, User user, Trip trip, LocalDateTime bookingTime,
                   BigDecimal totalAmount, BookingStatus status, User createdBy) {
        this.bookingId = bookingId;
        this.user = user;
        this.trip = trip;
        this.bookingTime = bookingTime;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdBy = createdBy;
    }
    
    public Integer getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Trip getTrip() {
        return trip;
    }
    
    public void setTrip(Trip trip) {
        this.trip = trip;
    }
    
    public LocalDateTime getBookingTime() {
        return bookingTime;
    }
    
    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public BookingStatus getStatus() {
        return status;
    }
    
    public void setStatus(BookingStatus status) {
        this.status = status;
    }
    
    public User getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
    
    public Set<Ticket> getTickets() {
        return tickets;
    }
    
    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }
    
    public Set<Payment> getPayments() {
        return payments;
    }
    
    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    // Nested types to consolidate files
    public static enum BookingStatus {
        PENDING_PAYMENT,
        CONFIRMED,
        CANCELLED
    }

    @Converter(autoApply = false)
    public static class BookingStatusConverter implements AttributeConverter<BookingStatus, String> {
        @Override
        public String convertToDatabaseColumn(BookingStatus attribute) {
            if (attribute == null) return null;
            // Store as UPPERCASE
            return attribute.name();
        }

        @Override
        public BookingStatus convertToEntityAttribute(String dbData) {
            if (dbData == null) return null;
            // Accept lowercase/uppercase from DB
            return BookingStatus.valueOf(dbData.trim().toUpperCase());
        }
    }
}