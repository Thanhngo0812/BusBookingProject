package com.ct08j2e.busbookingproject.service;

import java.util.List;

import com.ct08j2e.busbookingproject.dto.ApiResponse;
import com.ct08j2e.busbookingproject.dto.TicketDTO;
import com.ct08j2e.busbookingproject.entity.Ticket.TicketStatus;

public interface TicketService {
    // Lấy vé theo ID
    ApiResponse<TicketDTO> getTicketById(Integer ticketId);
    
    // Lấy tất cả vé của một booking
    ApiResponse<List<TicketDTO>> getTicketsByBookingId(Integer bookingId);
    
    // Lấy tất cả vé của một trip (dành cho nhân viên/admin)
    ApiResponse<List<TicketDTO>> getTicketsByTripId(Integer tripId);
    
    // Lấy tất cả vé của một user
    ApiResponse<List<TicketDTO>> getTicketsByUserId(Integer userId);
    
    // Lấy vé theo trạng thái
    ApiResponse<List<TicketDTO>> getTicketsByStatus(TicketStatus status);
    
    // Check-in vé (cập nhật status thành CHECKED_IN)
    ApiResponse<TicketDTO> checkInTicket(Integer ticketId);
    
    // Hủy vé (cập nhật status thành CANCELLED)
    ApiResponse<TicketDTO> cancelTicket(Integer ticketId);
}
