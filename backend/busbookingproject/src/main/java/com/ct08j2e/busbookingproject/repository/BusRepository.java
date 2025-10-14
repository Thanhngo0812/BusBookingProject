package com.ct08j2e.busbookingproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ct08j2e.busbookingproject.entity.Bus;

import java.util.Optional;
import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Integer> {
    Optional<Bus> findByLicensePlate(String licensePlate);
    List<Bus> findByCompanyCompanyId(Integer companyId);
    List<Bus> findBySeatType(String seatType);
}