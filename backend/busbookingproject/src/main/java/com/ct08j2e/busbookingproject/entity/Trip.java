package com.ct08j2e.busbookingproject.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Converter;

@Entity
@Table(name = "Trips")
public class Trip {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Integer tripId;
    
    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;
    
    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;
    
    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;
    
    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;
    
    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;
    
    @Convert(converter = Trip.TripStatusConverter.class)
    @Column(name = "status")
    private TripStatus status = TripStatus.SCHEDULED;
    
    @OneToMany(mappedBy = "trip")
    private Set<Seat> seats;
    
    @OneToMany(mappedBy = "trip")
    private Set<Booking> bookings;
    
    public Trip() {
    }
    
    public Trip(Integer tripId, Route route, Bus bus, LocalDateTime departureTime, 
                LocalDateTime arrivalTime, BigDecimal basePrice, TripStatus status) {
        this.tripId = tripId;
        this.route = route;
        this.bus = bus;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.basePrice = basePrice;
        this.status = status;
    }
    
    public Integer getTripId() {
        return tripId;
    }
    
    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }
    
    public Route getRoute() {
        return route;
    }
    
    public void setRoute(Route route) {
        this.route = route;
    }
    
    public Bus getBus() {
        return bus;
    }
    
    public void setBus(Bus bus) {
        this.bus = bus;
    }
    
    public LocalDateTime getDepartureTime() {
        return departureTime;
    }
    
    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }
    
    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }
    
    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    
    public BigDecimal getBasePrice() {
        return basePrice;
    }
    
    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }
    
    public TripStatus getStatus() {
        return status;
    }
    
    public void setStatus(TripStatus status) {
        this.status = status;
    }
    
    public Set<Seat> getSeats() {
        return seats;
    }
    
    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }
    
    public Set<Booking> getBookings() {
        return bookings;
    }
    
    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    // Nested types to consolidate files
    public static enum TripStatus {
        SCHEDULED,
        RUNNING,
        COMPLETED,
        CANCELLED
    }

    @Converter(autoApply = false)
    public static class TripStatusConverter implements AttributeConverter<TripStatus, String> {
        @Override
        public String convertToDatabaseColumn(TripStatus attribute) {
            if (attribute == null) return null;
            // Store as lowercase to be tolerant with existing data
            return attribute.name().toLowerCase();
        }

        @Override
        public TripStatus convertToEntityAttribute(String dbData) {
            if (dbData == null) return null;
            // Accept lowercase/uppercase/mixed case
            return TripStatus.valueOf(dbData.trim().toUpperCase());
        }
    }
}
