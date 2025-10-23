package com.ct08j2e.busbookingproject.dto;

import java.util.List;

public class UpdateSeatsStatusDTO {
    private List<Integer> seatIds;
    private String status; // AVAILABLE, BOOKED

    public UpdateSeatsStatusDTO() {
    }

    public UpdateSeatsStatusDTO(List<Integer> seatIds, String status) {
        this.seatIds = seatIds;
        this.status = status;
    }

    // Getters and Setters
    public List<Integer> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Integer> seatIds) {
        this.seatIds = seatIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
