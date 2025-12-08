package com.example.flightmanagementsystem.repository;

import com.example.flightmanagementsystem.model.FlightAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightAssignmentRepository extends JpaRepository<FlightAssignment, String> {
}
