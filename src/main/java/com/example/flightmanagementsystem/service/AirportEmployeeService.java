package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.AirportEmployee;
import com.example.flightmanagementsystem.repository.AirportEmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirportEmployeeService {

    private final AirportEmployeeRepository repository;

    public AirportEmployeeService(AirportEmployeeRepository repository) {
        this.repository = repository;
    }

    private void validateRules(AirportEmployee emp, String currentId) {
        // Regula: ID Unic
        if (currentId == null && repository.existsById(emp.getId())) {
            throw new IllegalArgumentException("Employee ID " + emp.getId() + " is already in use.");
        }
    }

    public AirportEmployee save(AirportEmployee emp) {
        validateRules(emp, null);
        return repository.save(emp);
    }

    public void update(String id, AirportEmployee emp) {
        validateRules(emp, id);
        emp.setId(id);
        repository.save(emp);
    }

    public boolean delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<AirportEmployee> findAll() { return repository.findAll(); }
    public Optional<AirportEmployee> findById(String id) { return repository.findById(id); }
}