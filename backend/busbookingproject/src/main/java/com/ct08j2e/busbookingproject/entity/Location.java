package com.ct08j2e.busbookingproject.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Locations")
public class Location {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Integer locationId;
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "city", nullable = false, length = 100)
    private String city;
    
    @Column(name = "address", length = 255)
    private String address;
    
    @OneToMany(mappedBy = "departureLocation")
    private Set<Route> departureRoutes;
    
    @OneToMany(mappedBy = "arrivalLocation")
    private Set<Route> arrivalRoutes;
    
    public Location() {
    }
    
    public Location(Integer locationId, String name, String city, String address) {
        this.locationId = locationId;
        this.name = name;
        this.city = city;
        this.address = address;
    }
    
    public Integer getLocationId() {
        return locationId;
    }
    
    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    // Alias method for location name
    public String getLocationName() {
        return name;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public Set<Route> getDepartureRoutes() {
        return departureRoutes;
    }
    
    public void setDepartureRoutes(Set<Route> departureRoutes) {
        this.departureRoutes = departureRoutes;
    }
    
    public Set<Route> getArrivalRoutes() {
        return arrivalRoutes;
    }
    
    public void setArrivalRoutes(Set<Route> arrivalRoutes) {
        this.arrivalRoutes = arrivalRoutes;
    }
}