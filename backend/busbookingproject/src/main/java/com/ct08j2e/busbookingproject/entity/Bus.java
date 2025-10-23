package com.ct08j2e.busbookingproject.entity;


import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Buses")
public class Bus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bus_id")
    private Integer busId;
    
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    
    @Column(name = "license_plate", nullable = false, unique = true, length = 20)
    private String licensePlate;
    
    @Column(name = "seat_type", length = 50)
    private String seatType;
    
    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;
    
    @OneToMany(mappedBy = "bus")
    private Set<Trip> trips;
    
    public Bus() {
    }
    
    public Bus(Integer busId, Company company, String licensePlate, 
               String seatType, Integer totalSeats) {
        this.busId = busId;
        this.company = company;
        this.licensePlate = licensePlate;
        this.seatType = seatType;
        this.totalSeats = totalSeats;
    }
    
    public Integer getBusId() {
        return busId;
    }
    
    public void setBusId(Integer busId) {
        this.busId = busId;
    }
    
    public Company getCompany() {
        return company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    
    public String getLicensePlate() {
        return licensePlate;
    }
    
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
    
    public String getSeatType() {
        return seatType;
    }
    
    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }
    
    public Integer getTotalSeats() {
        return totalSeats;
    }
    
    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }
    
    // Alias method for seat capacity
    public Integer getSeatCapacity() {
        return totalSeats;
    }
    
    public Set<Trip> getTrips() {
        return trips;
    }
    
    public void setTrips(Set<Trip> trips) {
        this.trips = trips;
    }
}