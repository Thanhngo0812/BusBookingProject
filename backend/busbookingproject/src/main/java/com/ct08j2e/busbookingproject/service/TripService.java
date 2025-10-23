package com.ct08j2e.busbookingproject.service;

import java.util.List;

import com.ct08j2e.busbookingproject.dto.ApiResponse;
import com.ct08j2e.busbookingproject.dto.TripDTO;
import com.ct08j2e.busbookingproject.dto.TripSearchDTO;
import com.ct08j2e.busbookingproject.entity.Trip.TripStatus;

public interface TripService {
    ApiResponse<List<TripDTO>> getAllTrips();
    ApiResponse<TripDTO> getTripById(Integer tripId);
    ApiResponse<List<TripDTO>> searchTrips(TripSearchDTO searchDTO);
    ApiResponse<TripDTO> createTrip(TripDTO tripDTO);
    ApiResponse<TripDTO> updateTrip(Integer tripId, TripDTO tripDTO);
    ApiResponse<Void> deleteTrip(Integer tripId);
    ApiResponse<TripDTO> patchTripStatus(Integer tripId, TripStatus status);
    ApiResponse<List<TripDTO>> getTripsByBusId(Integer busId);
    ApiResponse<List<TripDTO>> getTripsByCompanyId(Integer companyId);
    ApiResponse<List<TripDTO>> getTripsByRouteId(Integer routeId);
    ApiResponse<List<TripDTO>> getTripsByStatus(TripStatus status);
}
