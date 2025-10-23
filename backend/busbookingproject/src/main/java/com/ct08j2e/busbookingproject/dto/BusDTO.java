package com.ct08j2e.busbookingproject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for Bus entity
 */
public class BusDTO {
    
    private Integer busId;
    
    @NotNull(message = "Company ID không được để trống")
    private Integer companyId;
    
    private String companyName;
    
    @NotBlank(message = "Biển số xe không được để trống")
    private String licensePlate;
    
    private String seatType;
    
    @NotNull(message = "Số ghế không được để trống")
    @Min(value = 1, message = "Số ghế phải lớn hơn 0")
    private Integer totalSeats;
    
    public BusDTO() {
    }
    
    public BusDTO(Integer busId, Integer companyId, String companyName, 
                  String licensePlate, String seatType, Integer totalSeats) {
        this.busId = busId;
        this.companyId = companyId;
        this.companyName = companyName;
        this.licensePlate = licensePlate;
        this.seatType = seatType;
        this.totalSeats = totalSeats;
    }
    
    // Getters and Setters
    public Integer getBusId() {
        return busId;
    }
    
    public void setBusId(Integer busId) {
        this.busId = busId;
    }
    
    public Integer getCompanyId() {
        return companyId;
    }
    
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
}
