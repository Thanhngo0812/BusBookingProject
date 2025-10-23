package com.ct08j2e.busbookingproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ct08j2e.busbookingproject.dto.ApiResponse;
import com.ct08j2e.busbookingproject.dto.SeatDTO;
import com.ct08j2e.busbookingproject.dto.UpdateSeatsStatusDTO;
import com.ct08j2e.busbookingproject.entity.Seat.SeatStatus;
import com.ct08j2e.busbookingproject.service.SeatService;

@RestController
@RequestMapping("/api/v1")
public class SeatController {

    @Autowired
    private SeatService seatService;

    /**
     * 1. GET /api/v1/trips/{tripId}/seats - LẤY SƠ ĐỒ GHẾ CỦA MỘT CHUYẾN ĐI (QUAN TRỌNG NHẤT)
     * Query param: ?status=available hoặc ?status=booked để lọc ghế theo trạng thái
     */
    @GetMapping("/trips/{tripId}/seats")
    public ResponseEntity<ApiResponse<List<SeatDTO>>> getSeatsByTripId(
            @PathVariable Integer tripId,
            @RequestParam(required = false) String status) {
        
        SeatStatus seatStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                seatStatus = SeatStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Trạng thái ghế không hợp lệ: " + status + ". Chỉ chấp nhận: AVAILABLE, BOOKED, PENDING");
            }
        }
        
        ApiResponse<List<SeatDTO>> response = seatService.getSeatsByTripId(tripId, seatStatus);
        return ResponseEntity.ok(response);
    }

    /**
     * 2. GET /api/v1/seats/{id} - Lấy thông tin một ghế cụ thể theo ID
     * Ít được sử dụng, hữu ích cho quản trị hoặc debug
     */
    @GetMapping("/seats/{id}")
    public ResponseEntity<ApiResponse<SeatDTO>> getSeatById(@PathVariable Integer id) {
        ApiResponse<SeatDTO> response = seatService.getSeatById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 3. PATCH /api/v1/seats/{id}/status - Cập nhật trạng thái của MỘT ghế (Dành cho Admin)
     * Query param: ?status=AVAILABLE hoặc ?status=BOOKED
     * Chỉ dành cho CompanyAdmin hoặc SuperAdmin
     */
    @PatchMapping("/seats/{id}/status")
    public ResponseEntity<ApiResponse<SeatDTO>> updateSeatStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        
        SeatStatus seatStatus;
        try {
            seatStatus = SeatStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trạng thái ghế không hợp lệ: " + status + ". Chỉ chấp nhận: AVAILABLE, BOOKED, PENDING");
        }
        
        ApiResponse<SeatDTO> response = seatService.updateSeatStatus(id, seatStatus);
        return ResponseEntity.ok(response);
    }

    /**
     * 4. PUT /api/v1/trips/{tripId}/seats/status - Cập nhật trạng thái cho NHIỀU ghế (Dành cho hệ thống)
     * Body: {"seatIds": [1, 2, 3], "status": "BOOKED"}
     * Dùng cho việc giữ chỗ (pending) hoặc xác nhận booking
     */
    @PutMapping("/trips/{tripId}/seats/status")
    public ResponseEntity<ApiResponse<List<SeatDTO>>> updateMultipleSeatsStatus(
            @PathVariable Integer tripId,
            @RequestBody UpdateSeatsStatusDTO updateDTO) {
        
        ApiResponse<List<SeatDTO>> response = seatService.updateMultipleSeatsStatus(tripId, updateDTO);
        return ResponseEntity.ok(response);
    }
}
