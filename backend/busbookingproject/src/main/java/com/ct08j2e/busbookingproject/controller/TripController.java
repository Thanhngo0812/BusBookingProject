package com.ct08j2e.busbookingproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ct08j2e.busbookingproject.dto.ApiResponse;
import com.ct08j2e.busbookingproject.dto.TripDTO;
import com.ct08j2e.busbookingproject.dto.TripSearchDTO;
import com.ct08j2e.busbookingproject.entity.Trip.TripStatus;
import com.ct08j2e.busbookingproject.service.TripService;

@RestController
@RequestMapping("/api/v1/trips")
public class TripController {
    @Autowired
    private TripService tripService;

    @GetMapping
    public ApiResponse<List<TripDTO>> getAllTrips() {
        return tripService.getAllTrips();
    }

    @GetMapping("/{tripId}")
    public ApiResponse<TripDTO> getTripById(@PathVariable Integer tripId) {
        return tripService.getTripById(tripId);
    }

    @PostMapping("/search")
    public ApiResponse<List<TripDTO>> searchTrips(@RequestBody TripSearchDTO searchDTO) {
        return tripService.searchTrips(searchDTO);
    }

    @PostMapping
    public ApiResponse<TripDTO> createTrip(@RequestBody TripDTO tripDTO) {
        return tripService.createTrip(tripDTO);
    }

    @PutMapping("/{tripId}")
    public ApiResponse<TripDTO> updateTrip(@PathVariable Integer tripId, @RequestBody TripDTO tripDTO) {
        return tripService.updateTrip(tripId, tripDTO);
    }

    @DeleteMapping("/{tripId}")
    public ApiResponse<Void> deleteTrip(@PathVariable Integer tripId) {
        return tripService.deleteTrip(tripId);
    }

    @PatchMapping("/{tripId}/status")
    public ApiResponse<TripDTO> patchTripStatus(@PathVariable Integer tripId, @RequestParam TripStatus status) {
        return tripService.patchTripStatus(tripId, status);
    }

    @GetMapping("/bus/{busId}")
    public ApiResponse<List<TripDTO>> getTripsByBusId(@PathVariable Integer busId) {
        return tripService.getTripsByBusId(busId);
    }

    @GetMapping("/company/{companyId}")
    public ApiResponse<List<TripDTO>> getTripsByCompanyId(@PathVariable Integer companyId) {
        return tripService.getTripsByCompanyId(companyId);
    }

    @GetMapping("/route/{routeId}")
    public ApiResponse<List<TripDTO>> getTripsByRouteId(@PathVariable Integer routeId) {
        return tripService.getTripsByRouteId(routeId);
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<TripDTO>> getTripsByStatus(@PathVariable TripStatus status) {
        return tripService.getTripsByStatus(status);
    }
}
