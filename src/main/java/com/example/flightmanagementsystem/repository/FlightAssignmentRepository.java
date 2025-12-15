package com.example.flightmanagementsystem.repository;

import com.example.flightmanagementsystem.model.FlightAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // IMPORT NOU
import org.springframework.stereotype.Repository;

@Repository
public interface FlightAssignmentRepository extends
        JpaRepository<FlightAssignment, String>,
        JpaSpecificationExecutor<FlightAssignment> { // EXTINDERE

    boolean existsByFlight_IdAndEmployee_Id(String flightId, String employeeId);
    boolean existsByFlight_IdAndEmployee_IdAndIdNot(String flightId, String employeeId, String id);
}