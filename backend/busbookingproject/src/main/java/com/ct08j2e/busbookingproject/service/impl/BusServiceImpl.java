package com.ct08j2e.busbookingproject.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ct08j2e.busbookingproject.dto.BusDTO;
import com.ct08j2e.busbookingproject.entity.Bus;
import com.ct08j2e.busbookingproject.entity.Company;
import com.ct08j2e.busbookingproject.exception.DuplicateResourceException;
import com.ct08j2e.busbookingproject.exception.InUseResourceException;
import com.ct08j2e.busbookingproject.exception.ResourceNotFoundException;
import com.ct08j2e.busbookingproject.repository.BusRepository;
import com.ct08j2e.busbookingproject.repository.CompanyRepository;
import com.ct08j2e.busbookingproject.repository.TripRepository;
import com.ct08j2e.busbookingproject.service.BusService;

/**
 * Implementation of BusService
 */
@Service
@Transactional
public class BusServiceImpl implements BusService {
    
    private final BusRepository busRepository;
    private final CompanyRepository companyRepository;
    private final TripRepository tripRepository;
    
    @Autowired
    public BusServiceImpl(BusRepository busRepository, CompanyRepository companyRepository, TripRepository tripRepository) {
        this.busRepository = busRepository;
        this.companyRepository = companyRepository;
        this.tripRepository = tripRepository;
    }
    
    @Override
    public List<BusDTO> getAllBuses() {
        return busRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public BusDTO getBusById(Integer id) {
    Bus bus = busRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xe với ID: " + id));
        return convertToDTO(bus);
    }
    
    @Override
    public List<BusDTO> getBusesByCompanyId(Integer companyId) {
        return busRepository.findByCompany_CompanyId(companyId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BusDTO> getBusesByCompany(Integer companyId) {
        return getBusesByCompanyId(companyId);
    }
    
    @Override
    public BusDTO getBusByLicensePlate(String licensePlate) {
    Bus bus = busRepository.findByLicensePlate(licensePlate)
        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xe với biển số: " + licensePlate));
        return convertToDTO(bus);
    }
    
    @Override
    public List<BusDTO> getBusesByLicensePlate(String licensePlate) {
        return busRepository.findByLicensePlateContaining(licensePlate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public BusDTO createBus(BusDTO busDTO) {
    Company company = companyRepository.findById(busDTO.getCompanyId())
        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy công ty với ID: " + busDTO.getCompanyId()));

    // Unique license plate check
    if (busRepository.existsByLicensePlate(busDTO.getLicensePlate())) {
        throw new DuplicateResourceException("Biển số xe đã tồn tại: " + busDTO.getLicensePlate());
    }
        
        Bus bus = new Bus();
        bus.setCompany(company);
        bus.setLicensePlate(busDTO.getLicensePlate());
        bus.setSeatType(busDTO.getSeatType());
        bus.setTotalSeats(busDTO.getTotalSeats());
        
        Bus savedBus = busRepository.save(bus);
        return convertToDTO(savedBus);
    }
    
    @Override
    public BusDTO updateBus(Integer id, BusDTO busDTO) {
    Bus bus = busRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xe với ID: " + id));
        
        if (busDTO.getCompanyId() != null) {
            Company company = companyRepository.findById(busDTO.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy công ty với ID: " + busDTO.getCompanyId()));
            bus.setCompany(company);
        }
        
        if (busDTO.getLicensePlate() != null) {
            if (!busDTO.getLicensePlate().equals(bus.getLicensePlate()) &&
                busRepository.existsByLicensePlate(busDTO.getLicensePlate())) {
                throw new DuplicateResourceException("Biển số xe đã tồn tại: " + busDTO.getLicensePlate());
            }
            bus.setLicensePlate(busDTO.getLicensePlate());
        }
        
        if (busDTO.getSeatType() != null) {
            bus.setSeatType(busDTO.getSeatType());
        }
        
        if (busDTO.getTotalSeats() != null) {
            bus.setTotalSeats(busDTO.getTotalSeats());
        }
        
        Bus updatedBus = busRepository.save(bus);
        return convertToDTO(updatedBus);
    }
    
    @Override
    public void deleteBus(Integer id) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xe với ID: " + id));
        // Business rule: prevent deletion if any trip uses this bus
        boolean inUse = tripRepository.existsByBus_BusId(id);
        if (inUse) {
            throw new InUseResourceException("Không thể xóa xe vì đang được sử dụng trong các chuyến đi (trip)");
        }
        busRepository.delete(bus);
    }
    
    @Override
    public List<BusDTO> getBusesBySeatType(String seatType) {
        return busRepository.findBySeatTypeContaining(seatType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BusDTO> searchBuses(Integer companyId, String seatType, Integer minSeats, Integer maxSeats) {
        List<Bus> allBuses = busRepository.findAll();
        
        return allBuses.stream()
                .filter(bus -> companyId == null || 
                        (bus.getCompany() != null && bus.getCompany().getCompanyId().equals(companyId)))
                .filter(bus -> seatType == null || 
                        (bus.getSeatType() != null && bus.getSeatType().toLowerCase().contains(seatType.toLowerCase())))
                .filter(bus -> minSeats == null || bus.getTotalSeats() >= minSeats)
                .filter(bus -> maxSeats == null || bus.getTotalSeats() <= maxSeats)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert Bus entity to BusDTO
     */
    private BusDTO convertToDTO(Bus bus) {
        BusDTO dto = new BusDTO();
        dto.setBusId(bus.getBusId());
        dto.setCompanyId(bus.getCompany() != null ? bus.getCompany().getCompanyId() : null);
        dto.setCompanyName(bus.getCompany() != null ? bus.getCompany().getName() : null);
        dto.setLicensePlate(bus.getLicensePlate());
        dto.setSeatType(bus.getSeatType());
        dto.setTotalSeats(bus.getTotalSeats());
        return dto;
    }
}
