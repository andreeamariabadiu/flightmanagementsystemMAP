package com.example.flightmanagementsystem.repository;

import com.example.flightmanagementsystem.model.AirlineEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // IMPORT NOU
import org.springframework.stereotype.Repository;

@Repository
public interface AirlineEmployeeRepository extends
        JpaRepository<AirlineEmployee, String>,
        JpaSpecificationExecutor<AirlineEmployee> { // Pentru filtrare

    boolean existsByLicenseNumber(String licenseNumber);
    boolean existsByLicenseNumberAndIdNot(String licenseNumber, String id);
}