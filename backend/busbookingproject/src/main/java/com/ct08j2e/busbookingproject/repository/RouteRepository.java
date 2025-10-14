package com.ct08j2e.busbookingproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ct08j2e.busbookingproject.entity.Route;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
    List<Route> findByCompanyCompanyId(Integer companyId);
    
    @Query("SELECT r FROM Route r WHERE r.departureLocation.locationId = :departureId " +
           "AND r.arrivalLocation.locationId = :arrivalId")
    List<Route> findByDepartureAndArrival(
        @Param("departureId") Integer departureId, 
        @Param("arrivalId") Integer arrivalId
    );
}
