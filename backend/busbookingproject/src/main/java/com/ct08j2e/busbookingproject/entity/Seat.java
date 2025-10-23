package com.ct08j2e.busbookingproject.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "Seats", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"trip_id", "seat_number"})
})
public class Seat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Integer seatId;
    
    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;
    
    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;
    
    @Convert(converter = Seat.SeatStatusConverter.class)
    @Column(name = "status")
    private SeatStatus status = SeatStatus.AVAILABLE;
    
    @OneToOne(mappedBy = "seat")
    private Ticket ticket;
    
    public Seat() {
    }
    
    public Seat(Integer seatId, Trip trip, String seatNumber, SeatStatus status) {
        this.seatId = seatId;
        this.trip = trip;
        this.seatNumber = seatNumber;
        this.status = status;
    }
    
    public Integer getSeatId() {
        return seatId;
    }
    
    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }
    
    public Trip getTrip() {
        return trip;
    }
    
    public void setTrip(Trip trip) {
        this.trip = trip;
    }
    
    public String getSeatNumber() {
        return seatNumber;
    }
    
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    
    public SeatStatus getStatus() {
        return status;
    }
    
    public void setStatus(SeatStatus status) {
        this.status = status;
    }
    
    public Ticket getTicket() {
        return ticket;
    }
    
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
    
    public enum SeatStatus {
        AVAILABLE, PENDING, BOOKED
    }

    @Converter(autoApply = false)
    public static class SeatStatusConverter implements AttributeConverter<SeatStatus, String> {
        @Override
        public String convertToDatabaseColumn(SeatStatus attribute) {
            if (attribute == null) return null;
            // Store as UPPERCASE to match enum convention
            return attribute.name();
        }

        @Override
        public SeatStatus convertToEntityAttribute(String dbData) {
            if (dbData == null) return null;
            // Accept lowercase/uppercase/mixed case from DB
            return SeatStatus.valueOf(dbData.trim().toUpperCase());
        }
    }
}
