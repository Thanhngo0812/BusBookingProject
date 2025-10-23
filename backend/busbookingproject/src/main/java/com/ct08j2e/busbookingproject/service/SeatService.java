package com.ct08j2e.busbookingproject.service;

import java.util.List;

import com.ct08j2e.busbookingproject.dto.ApiResponse;
import com.ct08j2e.busbookingproject.dto.SeatDTO;
import com.ct08j2e.busbookingproject.dto.UpdateSeatsStatusDTO;
import com.ct08j2e.busbookingproject.entity.Seat.SeatStatus;

public interface SeatService {
    // GET /api/trips/{tripId}/seats - Lấy sơ đồ ghế của một chuyến đi
    ApiResponse<List<SeatDTO>> getSeatsByTripId(Integer tripId, SeatStatus status);
    
    // GET /api/seats/{id} - Lấy thông tin một ghế cụ thể
    ApiResponse<SeatDTO> getSeatById(Integer seatId);
    
    // PATCH /api/seats/{id}/status - Cập nhật trạng thái một ghế (Admin)
    ApiResponse<SeatDTO> updateSeatStatus(Integer seatId, SeatStatus status);
    
    // PUT /api/trips/{tripId}/seats/status - Cập nhật trạng thái nhiều ghế (System)
    ApiResponse<List<SeatDTO>> updateMultipleSeatsStatus(Integer tripId, UpdateSeatsStatusDTO updateDTO);
}
