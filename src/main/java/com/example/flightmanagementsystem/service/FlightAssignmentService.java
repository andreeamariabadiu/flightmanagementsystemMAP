package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.AirlineEmployee;
import com.example.flightmanagementsystem.model.Flight;
import com.example.flightmanagementsystem.model.FlightAssignment;
import com.example.flightmanagementsystem.repository.AirlineEmployeeRepository;
import com.example.flightmanagementsystem.repository.FlightAssignmentRepository;
import com.example.flightmanagementsystem.repository.FlightRepository;
import org.springframework.data.domain.Sort; // IMPORT NOU
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightAssignmentService {

    private final FlightAssignmentRepository repo;
    private final FlightRepository flightRepo;
    private final AirlineEmployeeRepository employeeRepo;

    public FlightAssignmentService(FlightAssignmentRepository repo,
                                   FlightRepository flightRepo,
                                   AirlineEmployeeRepository employeeRepo) {
        this.repo = repo;
        this.flightRepo = flightRepo;
        this.employeeRepo = employeeRepo;
    }

    private void validateRules(FlightAssignment fa, String currentId) {
        if (currentId == null && repo.existsById(fa.getId())) {
            throw new IllegalArgumentException("Assignment ID " + fa.getId() + " already exists.");
        }
        boolean exists;
        String fId = fa.getFlight().getId();
        String eId = fa.getEmployee().getId();

        if (currentId == null) {
            exists = repo.existsByFlight_IdAndEmployee_Id(fId, eId);
        } else {
            exists = repo.existsByFlight_IdAndEmployee_IdAndIdNot(fId, eId, currentId);
        }

        if (exists) {
            throw new IllegalArgumentException("Employee is already assigned to this flight.");
        }
    }

    public FlightAssignment createAssignment(FlightAssignment fa, String flightId, String employeeId) {
        Flight flight = flightRepo.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Flight ID"));
        AirlineEmployee emp = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Employee ID"));

        fa.setFlight(flight);
        fa.setEmployee(emp);

        validateRules(fa, null);
        return repo.save(fa);
    }

    public void updateAssignment(String id, FlightAssignment fa, String flightId, String employeeId) {
        Flight flight = flightRepo.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Flight ID"));
        AirlineEmployee emp = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Employee ID"));

        fa.setId(id);
        fa.setFlight(flight);
        fa.setEmployee(emp);

        validateRules(fa, id);
        repo.save(fa);
    }

    // CRUD
    public List<FlightAssignment> findAll() { return repo.findAll(); }
    public Optional<FlightAssignment> findById(String id) { return repo.findById(id); }
    public void delete(String id) {
        if(repo.existsById(id)) repo.deleteById(id);
    }

    // METODA NOUĂ PENTRU SORTARE
    public List<FlightAssignment> findAll(Sort sort) {
        return repo.findAll(sort);
    }
}