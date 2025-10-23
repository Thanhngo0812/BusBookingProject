package com.ct08j2e.busbookingproject.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ct08j2e.busbookingproject.dto.ApiResponse;
import com.ct08j2e.busbookingproject.dto.TicketDTO;
import com.ct08j2e.busbookingproject.entity.Seat;
import com.ct08j2e.busbookingproject.entity.Ticket;
import com.ct08j2e.busbookingproject.entity.Ticket.TicketStatus;
import com.ct08j2e.busbookingproject.entity.Trip;
import com.ct08j2e.busbookingproject.exception.ResourceNotFoundException;
import com.ct08j2e.busbookingproject.repository.TicketRepository;
import com.ct08j2e.busbookingproject.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public ApiResponse<TicketDTO> getTicketById(Integer ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));
        
        TicketDTO ticketDTO = convertToDTO(ticket);
        return new ApiResponse<>(true, "✅ Lấy thông tin vé thành công! Vé #" + ticketId, ticketDTO);
    }

    @Override
    public ApiResponse<List<TicketDTO>> getTicketsByBookingId(Integer bookingId) {
        List<Ticket> tickets = ticketRepository.findByBookingBookingId(bookingId);
        
        if (tickets.isEmpty()) {
            throw new ResourceNotFoundException("No tickets found for booking id: " + bookingId);
        }
        
        List<TicketDTO> ticketDTOs = tickets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new ApiResponse<>(true, "✅ Lấy danh sách vé thành công! Tìm thấy " + ticketDTOs.size() + " vé.", ticketDTOs);
    }

    @Override
    public ApiResponse<List<TicketDTO>> getTicketsByTripId(Integer tripId) {
        List<Ticket> tickets = ticketRepository.findByTripId(tripId);
        
        List<TicketDTO> ticketDTOs = tickets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new ApiResponse<>(true, "✅ Lấy danh sách vé của chuyến đi thành công! Tìm thấy " + ticketDTOs.size() + " vé.", ticketDTOs);
    }

    @Override
    public ApiResponse<List<TicketDTO>> getTicketsByUserId(Integer userId) {
        List<Ticket> tickets = ticketRepository.findByUserId(userId);
        
        List<TicketDTO> ticketDTOs = tickets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new ApiResponse<>(true, "✅ Lấy lịch sử vé thành công! Tìm thấy " + ticketDTOs.size() + " vé.", ticketDTOs);
    }

    @Override
    public ApiResponse<List<TicketDTO>> getTicketsByStatus(TicketStatus status) {
        List<Ticket> tickets = ticketRepository.findByStatus(status);
        
        List<TicketDTO> ticketDTOs = tickets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new ApiResponse<>(true, "✅ Lấy danh sách vé theo trạng thái thành công! Tìm thấy " + ticketDTOs.size() + " vé " + status.name() + ".", ticketDTOs);
    }

    @Override
    @Transactional
    public ApiResponse<TicketDTO> checkInTicket(Integer ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));
        
        // Kiểm tra vé phải có trạng thái VALID
        if (ticket.getStatus() != TicketStatus.VALID) {
            throw new IllegalArgumentException("Không thể check-in vé. Trạng thái hiện tại: " + ticket.getStatus());
        }
        
        ticket.setStatus(TicketStatus.CHECKED_IN);
        Ticket updatedTicket = ticketRepository.save(ticket);
        
        TicketDTO ticketDTO = convertToDTO(updatedTicket);
        return new ApiResponse<>(true, "✅ Check-in vé thành công! Vé #" + ticketId + " - " + ticket.getPassengerName(), ticketDTO);
    }

    @Override
    @Transactional
    public ApiResponse<TicketDTO> cancelTicket(Integer ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));
        
        // Kiểm tra vé phải có trạng thái VALID
        if (ticket.getStatus() != TicketStatus.VALID) {
            throw new IllegalArgumentException("Không thể hủy vé. Trạng thái hiện tại: " + ticket.getStatus());
        }
        
        ticket.setStatus(TicketStatus.CANCELLED);
        Ticket updatedTicket = ticketRepository.save(ticket);
        
        // Khi hủy vé, cập nhật lại trạng thái ghế thành AVAILABLE
        Seat seat = ticket.getSeat();
        if (seat != null) {
            seat.setStatus(Seat.SeatStatus.AVAILABLE);
            // Note: Cần inject SeatRepository nếu muốn save seat ở đây
            // Hoặc để logic này trong BookingService khi hủy cả booking
        }
        
        TicketDTO ticketDTO = convertToDTO(updatedTicket);
        return new ApiResponse<>(true, "✅ Hủy vé thành công! Vé #" + ticketId + " - " + ticket.getPassengerName(), ticketDTO);
    }

    // Helper method to convert Entity to DTO
    private TicketDTO convertToDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setTicketId(ticket.getTicketId());
        dto.setBookingId(ticket.getBooking().getBookingId());
        dto.setSeatId(ticket.getSeat().getSeatId());
        dto.setSeatNumber(ticket.getSeat().getSeatNumber());
        dto.setPassengerName(ticket.getPassengerName());
        dto.setPassengerPhone(ticket.getPassengerPhone());
        dto.setPrice(ticket.getPrice());
        dto.setQrCodeUrl(ticket.getQrCodeUrl());
        dto.setStatus(ticket.getStatus().name());
        
        // Thông tin từ Trip
        Trip trip = ticket.getSeat().getTrip();
        if (trip != null) {
            dto.setTripId(trip.getTripId());
            dto.setDepartureTime(trip.getDepartureTime());
            dto.setArrivalTime(trip.getArrivalTime());
            dto.setBusLicensePlate(trip.getBus().getLicensePlate());
            
            if (trip.getRoute() != null) {
                dto.setDepartureLocationName(trip.getRoute().getDepartureLocation().getLocationName());
                dto.setArrivalLocationName(trip.getRoute().getArrivalLocation().getLocationName());
            }
        }
        
        return dto;
    }
}
