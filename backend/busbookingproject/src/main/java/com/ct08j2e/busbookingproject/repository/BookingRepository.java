package com.ct08j2e.busbookingproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ct08j2e.busbookingproject.entity.Booking;
import com.ct08j2e.busbookingproject.entity.Booking.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByUserUserId(Integer userId);
    List<Booking> findByTripTripId(Integer tripId);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByUserUserIdAndStatus(Integer userId, BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.bookingTime >= :startTime AND b.bookingTime <= :endTime")
    List<Booking> findByBookingTimeBetween(
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
}