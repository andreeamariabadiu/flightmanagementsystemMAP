package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.FlightAssignment;
import com.example.flightmanagementsystem.repository.FlightAssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightAssignmentService {
    private final FlightAssignmentRepository flightAssignmentRepository;

    public FlightAssignmentService(FlightAssignmentRepository flightAssignmentRepository) {
        this.flightAssignmentRepository = flightAssignmentRepository;
    }

    public FlightAssignment save(FlightAssignment fa) {
        return flightAssignmentRepository.save(fa);
    }

    public boolean delete(String id) {
        if (flightAssignmentRepository.existsById(id)) {
            flightAssignmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<FlightAssignment> findAll() {
        return flightAssignmentRepository.findAll();
    }

    public Optional<FlightAssignment> findById(String id) {
        return flightAssignmentRepository.findById(id);
    }

    public void updateEmployee(String id, FlightAssignment update) {
        update.setId(id);
        flightAssignmentRepository.save(update);
    }
}
