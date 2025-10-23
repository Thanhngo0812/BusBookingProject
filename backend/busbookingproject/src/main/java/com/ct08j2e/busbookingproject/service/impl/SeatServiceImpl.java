package com.ct08j2e.busbookingproject.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ct08j2e.busbookingproject.dto.ApiResponse;
import com.ct08j2e.busbookingproject.dto.SeatDTO;
import com.ct08j2e.busbookingproject.dto.UpdateSeatsStatusDTO;
import com.ct08j2e.busbookingproject.entity.Seat;
import com.ct08j2e.busbookingproject.entity.Seat.SeatStatus;
import com.ct08j2e.busbookingproject.exception.ResourceNotFoundException;
import com.ct08j2e.busbookingproject.repository.SeatRepository;
import com.ct08j2e.busbookingproject.repository.TripRepository;
import com.ct08j2e.busbookingproject.service.SeatService;

@Service
public class SeatServiceImpl implements SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TripRepository tripRepository;

    @Override
    public ApiResponse<List<SeatDTO>> getSeatsByTripId(Integer tripId, SeatStatus status) {
        // Kiểm tra trip có tồn tại không
        if (!tripRepository.existsById(tripId)) {
            throw new ResourceNotFoundException("Trip not found with id: " + tripId);
        }

        List<Seat> seats;
        if (status != null) {
            seats = seatRepository.findByTripTripIdAndStatus(tripId, status);
        } else {
            seats = seatRepository.findByTripTripId(tripId);
        }

        List<SeatDTO> seatDTOs = seats.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        String message = status != null 
            ? "Lấy sơ đồ ghế thành công! Tìm thấy " + seatDTOs.size() + " ghế " + status.name().toLowerCase() + "."
            : "Lấy sơ đồ ghế thành công! Tìm thấy " + seatDTOs.size() + " ghế.";
        return new ApiResponse<>(true, message, seatDTOs);
    }

    @Override
    public ApiResponse<SeatDTO> getSeatById(Integer seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with id: " + seatId));

        SeatDTO seatDTO = convertToDTO(seat);
        return new ApiResponse<>(true, "Lấy thông tin ghế thành công! Số ghế: " + seat.getSeatNumber(), seatDTO);
    }

    @Override
    @Transactional
    public ApiResponse<SeatDTO> updateSeatStatus(Integer seatId, SeatStatus status) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with id: " + seatId));

        seat.setStatus(status);
        Seat updatedSeat = seatRepository.save(seat);

        SeatDTO seatDTO = convertToDTO(updatedSeat);
        return new ApiResponse<>(true, "Cập nhật trạng thái ghế thành công! Ghế " + seat.getSeatNumber() + " → " + status.name(), seatDTO);
    }

    @Override
    @Transactional
    public ApiResponse<List<SeatDTO>> updateMultipleSeatsStatus(Integer tripId, UpdateSeatsStatusDTO updateDTO) {
        // Kiểm tra trip có tồn tại không
        if (!tripRepository.existsById(tripId)) {
            throw new ResourceNotFoundException("Trip not found with id: " + tripId);
        }

        // Validate status
        SeatStatus newStatus;
        try {
            newStatus = SeatStatus.valueOf(updateDTO.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid seat status: " + updateDTO.getStatus());
        }

        // Lấy tất cả các ghế theo seatIds
        List<Seat> seats = seatRepository.findAllById(updateDTO.getSeatIds());

        if (seats.isEmpty()) {
            throw new ResourceNotFoundException("No seats found with provided IDs");
        }

        // Kiểm tra tất cả các ghế có thuộc về trip này không
        for (Seat seat : seats) {
            if (!seat.getTrip().getTripId().equals(tripId)) {
                throw new IllegalArgumentException("Seat " + seat.getSeatId() + " does not belong to trip " + tripId);
            }
        }

        // Cập nhật trạng thái cho tất cả các ghế
        seats.forEach(seat -> seat.setStatus(newStatus));
        List<Seat> updatedSeats = seatRepository.saveAll(seats);

        List<SeatDTO> seatDTOs = updatedSeats.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ApiResponse<>(true, "Cập nhật trạng thái thành công cho " + seatDTOs.size() + " ghế! Trạng thái mới: " + newStatus.name(), seatDTOs);
    }

    // Helper method to convert Entity to DTO
    private SeatDTO convertToDTO(Seat seat) {
        return new SeatDTO(
                seat.getSeatId(),
                seat.getTrip().getTripId(),
                seat.getSeatNumber(),
                seat.getStatus().name()
        );
    }
}
