package com.ct08j2e.busbookingproject.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TripDTO {
    private Integer tripId;
    private Integer routeId;
    private Integer busId;
    private String busLicensePlate;
    private String companyName;
    private Integer companyId;
    private Integer departureLocationId;
    private Integer arrivalLocationId;
    private String departureLocationName;
    private String arrivalLocationName;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private BigDecimal basePrice;
    private String seatType;
    private Integer totalSeats;
    private String status;

    // Constructors
    public TripDTO() {
    }

    // Getters and Setters
    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public Integer getBusId() {
        return busId;
    }

    public void setBusId(Integer busId) {
        this.busId = busId;
    }

    public String getBusLicensePlate() {
        return busLicensePlate;
    }

    public void setBusLicensePlate(String busLicensePlate) {
        this.busLicensePlate = busLicensePlate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getDepartureLocationId() {
        return departureLocationId;
    }

    public void setDepartureLocationId(Integer departureLocationId) {
        this.departureLocationId = departureLocationId;
    }

    public Integer getArrivalLocationId() {
        return arrivalLocationId;
    }

    public void setArrivalLocationId(Integer arrivalLocationId) {
        this.arrivalLocationId = arrivalLocationId;
    }

    public String getDepartureLocationName() {
        return departureLocationName;
    }

    public void setDepartureLocationName(String departureLocationName) {
        this.departureLocationName = departureLocationName;
    }

    public String getArrivalLocationName() {
        return arrivalLocationName;
    }

    public void setArrivalLocationName(String arrivalLocationName) {
        this.arrivalLocationName = arrivalLocationName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
