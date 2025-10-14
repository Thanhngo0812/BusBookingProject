package com.ct08j2e.busbookingproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ct08j2e.busbookingproject.entity.Ticket;
import com.ct08j2e.busbookingproject.entity.Ticket.TicketStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByBookingBookingId(Integer bookingId);
    Optional<Ticket> findBySeatSeatId(Integer seatId);
    List<Ticket> findByStatus(TicketStatus status);
    
    @Query("SELECT t FROM Ticket t WHERE t.booking.user.userId = :userId")
    List<Ticket> findByUserId(@Param("userId") Integer userId);
    
    @Query("SELECT t FROM Ticket t WHERE t.seat.trip.tripId = :tripId")
    List<Ticket> findByTripId(@Param("tripId") Integer tripId);
}
