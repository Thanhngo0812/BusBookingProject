package com.ct08j2e.busbookingproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ct08j2e.busbookingproject.entity.Payment;
import com.ct08j2e.busbookingproject.entity.Payment.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByBookingBookingId(Integer bookingId);
    List<Payment> findByStatus(PaymentStatus status);
    Optional<Payment> findByTransactionCode(String transactionCode);
    List<Payment> findByPaymentMethod(String paymentMethod);
    
    @Query("SELECT p FROM Payment p WHERE p.paymentTime >= :startTime AND p.paymentTime <= :endTime")
    List<Payment> findByPaymentTimeBetween(
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    @Query("SELECT p FROM Payment p WHERE p.booking.user.userId = :userId")
    List<Payment> findByUserId(@Param("userId") Integer userId);
}
