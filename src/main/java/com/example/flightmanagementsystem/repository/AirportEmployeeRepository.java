package com.example.flightmanagementsystem.repository;

import com.example.flightmanagementsystem.model.AirportEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportEmployeeRepository extends JpaRepository<AirportEmployee, String> {
}
