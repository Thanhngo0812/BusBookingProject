package com.ct08j2e.busbookingproject.service;

import java.util.List;

import com.ct08j2e.busbookingproject.dto.BusDTO;

/**
 * Service interface for Bus operations
 */
public interface BusService {
    
    /**
     * Get all buses
     * @return List of all buses
     */
    List<BusDTO> getAllBuses();
    
    /**
     * Get bus by ID
     * @param id Bus ID
     * @return BusDTO
     */
    BusDTO getBusById(Integer id);
    
    /**
     * Get buses by company ID
     * @param companyId Company ID
     * @return List of buses belonging to the company
     */
    List<BusDTO> getBusesByCompany(Integer companyId);
    
    /**
     * Get buses by company ID
     * @param companyId Company ID
     * @return List of buses belonging to the company
     */
    List<BusDTO> getBusesByCompanyId(Integer companyId);
    
    /**
     * Get bus by license plate (exact match)
     * @param licensePlate License plate
     * @return BusDTO
     */
    BusDTO getBusByLicensePlate(String licensePlate);
    
    /**
     * Get buses by license plate (search)
     * @param licensePlate License plate to search
     * @return List of matching buses
     */
    List<BusDTO> getBusesByLicensePlate(String licensePlate);
    
    /**
     * Create new bus
     * @param busDTO Bus data
     * @return Created bus
     */
    BusDTO createBus(BusDTO busDTO);
    
    /**
     * Update existing bus
     * @param id Bus ID
     * @param busDTO Updated bus data
     * @return Updated bus
     */
    BusDTO updateBus(Integer id, BusDTO busDTO);
    
    /**
     * Delete bus
     * @param id Bus ID
     */
    void deleteBus(Integer id);
    
    /**
     * Get buses by seat type
     * @param seatType Seat type to filter
     * @return List of buses with specified seat type
     */
    List<BusDTO> getBusesBySeatType(String seatType);
    
    /**
     * Search buses with multiple filters
     * @param companyId Company ID (optional)
     * @param seatType Seat type (optional)
     * @param minSeats Minimum seats (optional)
     * @param maxSeats Maximum seats (optional)
     * @return List of matching buses
     */
    List<BusDTO> searchBuses(Integer companyId, String seatType, Integer minSeats, Integer maxSeats);
}
