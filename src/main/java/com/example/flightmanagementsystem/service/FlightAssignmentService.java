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

    // --- BUSINESS LOGIC ---
    private void validateBusinessRules(FlightAssignment assignment, String currentId) {

        // Rule 1: Unique Assignment ID (Only on Create)
        if (currentId == null && flightAssignmentRepository.existsById(assignment.getId())) {
            throw new IllegalArgumentException("Assignment ID " + assignment.getId() + " already exists.");
        }

        // Rule 2: Prevent Duplicate Assignment
        // (A staff member cannot be assigned to the same flight twice)
        boolean exists;
        if (currentId == null) {
            exists = flightAssignmentRepository.existsByFlightIdAndStaffId(
                    assignment.getFlightId(), assignment.getStaffId());
        } else {
            exists = flightAssignmentRepository.existsByFlightIdAndStaffIdAndIdNot(
                    assignment.getFlightId(), assignment.getStaffId(), currentId);
        }

        if (exists) {
            throw new IllegalArgumentException("Staff member " + assignment.getStaffId() +
                    " is already assigned to Flight " + assignment.getFlightId());
        }
    }

    public FlightAssignment save(FlightAssignment fa) {
        validateBusinessRules(fa, null);
        return flightAssignmentRepository.save(fa);
    }

    public void updateAssignment(String id, FlightAssignment update) {
        validateBusinessRules(update, id);
        update.setId(id);
        flightAssignmentRepository.save(update);
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
