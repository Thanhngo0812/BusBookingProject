package com.ct08j2e.busbookingproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ct08j2e.busbookingproject.entity.Seat;
import com.ct08j2e.busbookingproject.entity.Seat.SeatStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
    List<Seat> findByTripTripId(Integer tripId);
    List<Seat> findByTripTripIdAndStatus(Integer tripId, SeatStatus status);
    Optional<Seat> findByTripTripIdAndSeatNumber(Integer tripId, String seatNumber);
}