package com.ct08j2e.busbookingproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ct08j2e.busbookingproject.dto.ApiResponse;
import com.ct08j2e.busbookingproject.dto.TicketDTO;
import com.ct08j2e.busbookingproject.entity.Ticket.TicketStatus;
import com.ct08j2e.busbookingproject.service.TicketService;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    /**
     * 1. GET /api/v1/tickets/{id} - Lấy thông tin một vé cụ thể
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketDTO>> getTicketById(@PathVariable Integer id) {
        ApiResponse<TicketDTO> response = ticketService.getTicketById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 2. GET /api/v1/tickets/booking/{bookingId} - Lấy tất cả vé của một booking
     * Dùng cho khách hàng xem vé đã đặt
     */
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<List<TicketDTO>>> getTicketsByBookingId(@PathVariable Integer bookingId) {
        ApiResponse<List<TicketDTO>> response = ticketService.getTicketsByBookingId(bookingId);
        return ResponseEntity.ok(response);
    }

    /**
     * 3. GET /api/v1/tickets/trip/{tripId} - Lấy tất cả vé của một chuyến đi
     * Dùng cho nhân viên/admin kiểm tra danh sách vé đã bán
     */
    @GetMapping("/trip/{tripId}")
    public ResponseEntity<ApiResponse<List<TicketDTO>>> getTicketsByTripId(@PathVariable Integer tripId) {
        ApiResponse<List<TicketDTO>> response = ticketService.getTicketsByTripId(tripId);
        return ResponseEntity.ok(response);
    }

    /**
     * 4. GET /api/v1/tickets/user/{userId} - Lấy lịch sử vé của một user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TicketDTO>>> getTicketsByUserId(@PathVariable Integer userId) {
        ApiResponse<List<TicketDTO>> response = ticketService.getTicketsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 5. GET /api/v1/tickets/status/{status} - Lấy vé theo trạng thái
     * status: VALID, CANCELLED, CHECKED_IN
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<TicketDTO>>> getTicketsByStatus(@PathVariable TicketStatus status) {
        ApiResponse<List<TicketDTO>> response = ticketService.getTicketsByStatus(status);
        return ResponseEntity.ok(response);
    }

    /**
     * 6. PATCH /api/v1/tickets/{id}/check-in - Check-in vé (quét QR hoặc nhập ID)
     * Dành cho nhân viên tại bến xe
     */
    @PatchMapping("/{id}/check-in")
    public ResponseEntity<ApiResponse<TicketDTO>> checkInTicket(@PathVariable Integer id) {
        ApiResponse<TicketDTO> response = ticketService.checkInTicket(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 7. PATCH /api/v1/tickets/{id}/cancel - Hủy vé
     * Dành cho khách hàng hoặc nhân viên
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<TicketDTO>> cancelTicket(@PathVariable Integer id) {
        ApiResponse<TicketDTO> response = ticketService.cancelTicket(id);
        return ResponseEntity.ok(response);
    }
}
