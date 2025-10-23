package com.ct08j2e.busbookingproject.dto;

public class SeatDTO {
    private Integer seatId;
    private Integer tripId;
    private String seatNumber;
    private String status; // AVAILABLE, BOOKED

    public SeatDTO() {
    }

    public SeatDTO(Integer seatId, Integer tripId, String seatNumber, String status) {
        this.seatId = seatId;
        this.tripId = tripId;
        this.seatNumber = seatNumber;
        this.status = status;
    }

    // Getters and Setters
    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
