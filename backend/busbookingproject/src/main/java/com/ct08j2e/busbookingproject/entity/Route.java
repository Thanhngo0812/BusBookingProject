package com.ct08j2e.busbookingproject.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Routes")
public class Route {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Integer routeId;
    
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    
    @ManyToOne
    @JoinColumn(name = "departure_location_id")
    private Location departureLocation;
    
    @ManyToOne
    @JoinColumn(name = "arrival_location_id")
    private Location arrivalLocation;
    
    @Column(name = "duration_minutes")
    private Integer durationMinutes;
    
    @OneToMany(mappedBy = "route")
    private Set<Trip> trips;
    
    public Route() {
    }
    
    public Route(Integer routeId, Company company, Location departureLocation, 
                 Location arrivalLocation, Integer durationMinutes) {
        this.routeId = routeId;
        this.company = company;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.durationMinutes = durationMinutes;
    }
    
    public Integer getRouteId() {
        return routeId;
    }
    
    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }
    
    public Company getCompany() {
        return company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    
    public Location getDepartureLocation() {
        return departureLocation;
    }
    
    public void setDepartureLocation(Location departureLocation) {
        this.departureLocation = departureLocation;
    }
    
    public Location getArrivalLocation() {
        return arrivalLocation;
    }
    
    public void setArrivalLocation(Location arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }
    
    public Integer getDurationMinutes() {
        return durationMinutes;
    }
    
    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
    public Set<Trip> getTrips() {
        return trips;
    }
    
    public void setTrips(Set<Trip> trips) {
        this.trips = trips;
    }
}