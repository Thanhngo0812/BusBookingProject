package com.ct08j2e.busbookingproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ct08j2e.busbookingproject.entity.Company;
import com.ct08j2e.busbookingproject.entity.Company.CompanyStatus;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    List<Company> findByStatus(CompanyStatus status);
    List<Company> findByNameContaining(String name);
}