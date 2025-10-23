package com.ct08j2e.busbookingproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ct08j2e.busbookingproject.entity.Bus;

@Repository
public interface BusRepository extends JpaRepository<Bus, Integer> {
    Optional<Bus> findByLicensePlate(String licensePlate);
    List<Bus> findByCompany_CompanyId(Integer companyId);
    List<Bus> findBySeatType(String seatType);
    List<Bus> findByLicensePlateContaining(String licensePlate);
    List<Bus> findBySeatTypeContaining(String seatType);
    boolean existsByLicensePlate(String licensePlate);
}