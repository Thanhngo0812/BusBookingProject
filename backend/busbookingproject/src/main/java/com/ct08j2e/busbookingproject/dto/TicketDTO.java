package com.ct08j2e.busbookingproject.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TicketDTO {
    private Integer ticketId;
    private Integer bookingId;
    private Integer seatId;
    private String seatNumber;
    private Integer tripId;
    private String passengerName;
    private String passengerPhone;
    private BigDecimal price;
    private String qrCodeUrl;
    private String status; // VALID, CANCELLED, CHECKED_IN
    
    // Thông tin bổ sung từ Trip và Route
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String departureLocationName;
    private String arrivalLocationName;
    private String busLicensePlate;
    
    public TicketDTO() {
    }
    
    public TicketDTO(Integer ticketId, Integer bookingId, Integer seatId, String seatNumber,
                     Integer tripId, String passengerName, String passengerPhone, BigDecimal price,
                     String qrCodeUrl, String status) {
        this.ticketId = ticketId;
        this.bookingId = bookingId;
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.tripId = tripId;
        this.passengerName = passengerName;
        this.passengerPhone = passengerPhone;
        this.price = price;
        this.qrCodeUrl = qrCodeUrl;
        this.status = status;
    }
    
    // Getters and Setters
    public Integer getTicketId() {
        return ticketId;
    }
    
    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }
    
    public Integer getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }
    
    public Integer getSeatId() {
        return seatId;
    }
    
    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }
    
    public String getSeatNumber() {
        return seatNumber;
    }
    
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    
    public Integer getTripId() {
        return tripId;
    }
    
    public void setTripId(Integer tripId) {
        this.tripId = tripId;
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
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
    
    public String getBusLicensePlate() {
        return busLicensePlate;
    }
    
    public void setBusLicensePlate(String busLicensePlate) {
        this.busLicensePlate = busLicensePlate;
    }
}
