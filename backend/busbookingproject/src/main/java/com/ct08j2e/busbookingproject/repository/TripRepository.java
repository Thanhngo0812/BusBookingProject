package com.ct08j2e.busbookingproject.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ct08j2e.busbookingproject.entity.Trip;
import com.ct08j2e.busbookingproject.entity.Trip.TripStatus;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
    List<Trip> findByStatus(TripStatus status);
    List<Trip> findByRouteRouteId(Integer routeId);
    List<Trip> findByBusBusId(Integer busId);
    boolean existsByBus_BusId(Integer busId);
    
    @Query("SELECT t FROM Trip t WHERE t.departureTime >= :startTime AND t.departureTime <= :endTime")
    List<Trip> findByDepartureTimeBetween(
        @Param("startTime") LocalDateTime startTime, 
        @Param("endTime") LocalDateTime endTime
    );
    
    @Query("SELECT t FROM Trip t WHERE t.route.departureLocation.locationId = :departureId " +
           "AND t.route.arrivalLocation.locationId = :arrivalId " +
           "AND t.departureTime >= :startTime AND t.status = :status")
    List<Trip> searchTrips(
        @Param("departureId") Integer departureId,
        @Param("arrivalId") Integer arrivalId,
        @Param("startTime") LocalDateTime startTime,
        @Param("status") TripStatus status
    );
}