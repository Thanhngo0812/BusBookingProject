package com.ct08j2e.busbookingproject.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ct08j2e.busbookingproject.dto.ApiResponse;
import com.ct08j2e.busbookingproject.dto.TripDTO;
import com.ct08j2e.busbookingproject.dto.TripSearchDTO;
import com.ct08j2e.busbookingproject.entity.Booking;
import com.ct08j2e.busbookingproject.entity.Bus;
import com.ct08j2e.busbookingproject.entity.Route;
import com.ct08j2e.busbookingproject.entity.Seat;
import com.ct08j2e.busbookingproject.entity.Seat.SeatStatus;
import com.ct08j2e.busbookingproject.entity.Trip;
import com.ct08j2e.busbookingproject.entity.Trip.TripStatus;
import com.ct08j2e.busbookingproject.exception.ResourceNotFoundException;
import com.ct08j2e.busbookingproject.exception.TripInUseException;
import com.ct08j2e.busbookingproject.exception.TripStatusException;
import com.ct08j2e.busbookingproject.repository.BookingRepository;
import com.ct08j2e.busbookingproject.repository.BusRepository;
import com.ct08j2e.busbookingproject.repository.RouteRepository;
import com.ct08j2e.busbookingproject.repository.SeatRepository;
import com.ct08j2e.busbookingproject.repository.TripRepository;
import com.ct08j2e.busbookingproject.service.TripService;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingRepository bookingRepository;
    
    @Override
    public ApiResponse<List<TripDTO>> getAllTrips() {
        List<Trip> trips = tripRepository.findAll();
        List<TripDTO> tripDTOs = trips.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "Lấy danh sách chuyến đi thành công! Tìm thấy " + tripDTOs.size() + " chuyến.", tripDTOs);
    }

    @Override
    public ApiResponse<TripDTO> getTripById(Integer tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));
        TripDTO tripDTO = convertToDTO(trip);
        return new ApiResponse<>(true, "Lấy thông tin chuyến đi thành công!", tripDTO);
    }

    @Override
    public ApiResponse<List<TripDTO>> searchTrips(TripSearchDTO searchDTO) {
        List<Trip> trips = tripRepository.searchTrips(
                searchDTO.getDepartureLocationId(),
                searchDTO.getArrivalLocationId(),
                searchDTO.getDepartureDate(),
                TripStatus.valueOf(searchDTO.getStatus() != null ? searchDTO.getStatus() : "SCHEDULED")
        );
        List<TripDTO> tripDTOs = trips.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "Tìm kiếm chuyến đi thành công! Tìm thấy " + tripDTOs.size() + " chuyến phù hợp.", tripDTOs);
    }

    @Override
    @Transactional
    public ApiResponse<TripDTO> createTrip(TripDTO tripDTO) {
        // Validate bus exists
        Bus bus = busRepository.findById(tripDTO.getBusId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + tripDTO.getBusId()));

        // Validate route exists
        Route route = routeRepository.findById(tripDTO.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + tripDTO.getRouteId()));

        // Create Trip
        Trip trip = new Trip();
        trip.setBus(bus);
        trip.setRoute(route);
        trip.setDepartureTime(tripDTO.getDepartureTime());
        trip.setArrivalTime(tripDTO.getArrivalTime());
        trip.setBasePrice(tripDTO.getBasePrice());
        trip.setStatus(TripStatus.SCHEDULED);

        Trip savedTrip = tripRepository.save(trip);

        // Auto-create seats based on bus capacity
        int seatCount = bus.getSeatCapacity();
        for (int i = 1; i <= seatCount; i++) {
            Seat seat = new Seat();
            seat.setTrip(savedTrip);
            seat.setSeatNumber("A" + i); // Format: A1, A2, A3...
            seat.setStatus(SeatStatus.AVAILABLE);
            seatRepository.save(seat);
        }

        TripDTO createdTripDTO = convertToDTO(savedTrip);
        return new ApiResponse<>(true, "Tạo chuyến đi mới thành công! Đã tự động tạo " + seatCount + " ghế.", createdTripDTO);
    }

    @Override
    @Transactional
    public ApiResponse<TripDTO> updateTrip(Integer tripId, TripDTO tripDTO) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));

        // Only allow update if trip is SCHEDULED
        if (trip.getStatus() != TripStatus.SCHEDULED) {
            throw new TripStatusException("Cannot update trip. Only SCHEDULED trips can be updated. Current status: " + trip.getStatus());
        }

        // Update fields
        if (tripDTO.getRouteId() != null) {
            Route route = routeRepository.findById(tripDTO.getRouteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + tripDTO.getRouteId()));
            trip.setRoute(route);
        }
        if (tripDTO.getBusId() != null) {
            Bus bus = busRepository.findById(tripDTO.getBusId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + tripDTO.getBusId()));
            trip.setBus(bus);
        }
        if (tripDTO.getDepartureTime() != null) {
            trip.setDepartureTime(tripDTO.getDepartureTime());
        }
        if (tripDTO.getArrivalTime() != null) {
            trip.setArrivalTime(tripDTO.getArrivalTime());
        }
        if (tripDTO.getBasePrice() != null) {
            trip.setBasePrice(tripDTO.getBasePrice());
        }

        Trip updatedTrip = tripRepository.save(trip);
        TripDTO updatedTripDTO = convertToDTO(updatedTrip);
        return new ApiResponse<>(true, "Cập nhật thông tin chuyến đi thành công!", updatedTripDTO);
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteTrip(Integer tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));

        // Check if trip has any bookings
        List<Booking> bookings = bookingRepository.findByTripTripId(tripId);
        if (!bookings.isEmpty()) {
            throw new TripInUseException("Cannot delete trip. Trip has " + bookings.size() + " booking(s).");
        }

        // Delete all seats associated with this trip first
        List<Seat> seats = seatRepository.findByTripTripId(tripId);
        seatRepository.deleteAll(seats);

        // Delete trip
        tripRepository.delete(trip);
        return new ApiResponse<>(true, "Xóa chuyến đi thành công! ID: " + tripId, null);
    }

    @Override
    @Transactional
    public ApiResponse<TripDTO> patchTripStatus(Integer tripId, TripStatus status) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));

        // Validate status transitions
        TripStatus currentStatus = trip.getStatus();
        if (!isValidStatusTransition(currentStatus, status)) {
            throw new TripStatusException("Invalid status transition from " + currentStatus + " to " + status);
        }

        trip.setStatus(status);
        Trip updatedTrip = tripRepository.save(trip);
        TripDTO updatedTripDTO = convertToDTO(updatedTrip);
        return new ApiResponse<>(true, "Cập nhật trạng thái chuyến đi thành công! Trạng thái mới: " + status.name(), updatedTripDTO);
    }

    @Override
    public ApiResponse<List<TripDTO>> getTripsByBusId(Integer busId) {
        // Validate bus exists
        if (!busRepository.existsById(busId)) {
            throw new ResourceNotFoundException("Bus not found with id: " + busId);
        }

        List<Trip> trips = tripRepository.findByBusBusId(busId);
        List<TripDTO> tripDTOs = trips.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "Lấy danh sách chuyến đi theo xe thành công! Tìm thấy " + tripDTOs.size() + " chuyến.", tripDTOs);
    }

    @Override
    public ApiResponse<List<TripDTO>> getTripsByCompanyId(Integer companyId) {
        // Get all trips and filter by company
        List<Trip> allTrips = tripRepository.findAll();
        List<Trip> trips = allTrips.stream()
                .filter(trip -> trip.getBus().getCompany().getCompanyId().equals(companyId))
                .collect(Collectors.toList());

        List<TripDTO> tripDTOs = trips.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "Lấy danh sách chuyến đi theo công ty thành công! Tìm thấy " + tripDTOs.size() + " chuyến.", tripDTOs);
    }

    @Override
    public ApiResponse<List<TripDTO>> getTripsByRouteId(Integer routeId) {
        // Validate route exists
        if (!routeRepository.existsById(routeId)) {
            throw new ResourceNotFoundException("Route not found with id: " + routeId);
        }

        List<Trip> trips = tripRepository.findByRouteRouteId(routeId);
        List<TripDTO> tripDTOs = trips.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "Lấy danh sách chuyến đi theo tuyến đường thành công! Tìm thấy " + tripDTOs.size() + " chuyến.", tripDTOs);
    }

    @Override
    public ApiResponse<List<TripDTO>> getTripsByStatus(TripStatus status) {
        List<Trip> trips = tripRepository.findByStatus(status);
        List<TripDTO> tripDTOs = trips.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "Lấy danh sách chuyến đi theo trạng thái thành công! Tìm thấy " + tripDTOs.size() + " chuyến " + status.name() + ".", tripDTOs);
    }

    // Helper method to validate status transitions
    private boolean isValidStatusTransition(TripStatus from, TripStatus to) {
        switch (from) {
            case SCHEDULED:
                return to == TripStatus.RUNNING || to == TripStatus.CANCELLED;
            case RUNNING:
                return to == TripStatus.COMPLETED || to == TripStatus.CANCELLED;
            case COMPLETED:
            case CANCELLED:
                return false; // No transitions allowed from terminal states
            default:
                return false;
        }
    }

    // Helper method to convert Entity to DTO
    private TripDTO convertToDTO(Trip trip) {
        TripDTO dto = new TripDTO();
        dto.setTripId(trip.getTripId());
        dto.setRouteId(trip.getRoute().getRouteId());
        dto.setBusId(trip.getBus().getBusId());
        dto.setBusLicensePlate(trip.getBus().getLicensePlate());
        dto.setCompanyId(trip.getBus().getCompany().getCompanyId());
        dto.setCompanyName(trip.getBus().getCompany().getCompanyName());
        dto.setDepartureLocationId(trip.getRoute().getDepartureLocation().getLocationId());
        dto.setDepartureLocationName(trip.getRoute().getDepartureLocation().getLocationName());
        dto.setArrivalLocationId(trip.getRoute().getArrivalLocation().getLocationId());
        dto.setArrivalLocationName(trip.getRoute().getArrivalLocation().getLocationName());
        dto.setDepartureTime(trip.getDepartureTime());
        dto.setArrivalTime(trip.getArrivalTime());
        dto.setBasePrice(trip.getBasePrice());
        dto.setSeatType(trip.getBus().getSeatType());
        dto.setTotalSeats(trip.getBus().getSeatCapacity());
        dto.setStatus(trip.getStatus().name());
        return dto;
    }
}
