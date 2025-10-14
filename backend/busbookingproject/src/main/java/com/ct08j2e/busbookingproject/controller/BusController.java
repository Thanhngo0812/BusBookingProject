package com.ct08j2e.busbookingproject.controller;


import com.ct08j2e.busbookingproject.dto.BusDTO;
import com.ct08j2e.busbookingproject.dto.ApiResponse;
import com.ct08j2e.busbookingproject.service.BusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buses")

public class BusController {

    private final BusService busService;

    @Autowired
    public BusController(BusService busService) {
        this.busService = busService;
    }

    /**
     * GET /api/v1/buses - Lấy danh sách tất cả xe
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BusDTO>>> getAllBuses() {
        List<BusDTO> buses = busService.getAllBuses();
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Lấy danh sách xe thành công", buses)
        );
    }

    /**
     * GET /api/v1/buses/{id} - Lấy thông tin xe theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BusDTO>> getBusById(@PathVariable Integer id) {
        BusDTO bus = busService.getBusById(id);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Lấy thông tin xe thành công", bus)
        );
    }

    /**
     * GET /api/v1/buses/company/{companyId} - Lấy danh sách xe theo công ty
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<List<BusDTO>>> getBusesByCompany(
            @PathVariable Integer companyId) {
        List<BusDTO> buses = busService.getBusesByCompany(companyId);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Lấy danh sách xe theo công ty thành công", buses)
        );
    }

    /**
     * GET /api/v1/buses/license/{licensePlate} - Tìm xe theo biển số
     */
    @GetMapping("/license/{licensePlate}")
    public ResponseEntity<ApiResponse<BusDTO>> getBusByLicensePlate(
            @PathVariable String licensePlate) {
        BusDTO bus = busService.getBusByLicensePlate(licensePlate);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Tìm xe theo biển số thành công", bus)
        );
    }

    /**
     * GET /api/v1/buses/seat-type/{seatType} - Lấy danh sách xe theo loại ghế
     */
    @GetMapping("/seat-type/{seatType}")
    public ResponseEntity<ApiResponse<List<BusDTO>>> getBusesBySeatType(
            @PathVariable String seatType) {
        List<BusDTO> buses = busService.getBusesBySeatType(seatType);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Lấy danh sách xe theo loại ghế thành công", buses)
        );
    }

    /**
     * POST /api/v1/buses - Tạo xe mới
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BusDTO>> createBus(
            @Valid @RequestBody BusDTO busDTO) {
        BusDTO createdBus = busService.createBus(busDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(true, "Tạo xe thành công", createdBus)
        );
    }

    /**
     * PUT /api/v1/buses/{id} - Cập nhật thông tin xe
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BusDTO>> updateBus(
            @PathVariable Integer id,
            @Valid @RequestBody BusDTO busDTO) {
        BusDTO updatedBus = busService.updateBus(id, busDTO);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Cập nhật xe thành công", updatedBus)
        );
    }

    /**
     * DELETE /api/v1/buses/{id} - Xóa xe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBus(@PathVariable Integer id) {
        busService.deleteBus(id);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Xóa xe thành công", null)
        );
    }

    /**
     * GET /api/v1/buses/search - Tìm kiếm xe
     * Query params: companyId, seatType, minSeats, maxSeats
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BusDTO>>> searchBuses(
            @RequestParam(required = false) Integer companyId,
            @RequestParam(required = false) String seatType,
            @RequestParam(required = false) Integer minSeats,
            @RequestParam(required = false) Integer maxSeats) {
        List<BusDTO> buses = busService.searchBuses(companyId, seatType, minSeats, maxSeats);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Tìm kiếm xe thành công", buses)
        );
    }
}