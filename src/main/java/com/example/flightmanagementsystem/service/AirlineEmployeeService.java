package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.AirlineEmployee;
import com.example.flightmanagementsystem.repository.AirlineEmployeeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AirlineEmployeeService {
    private final AirlineEmployeeRepository repository;

    public AirlineEmployeeService(AirlineEmployeeRepository repository) {
        this.repository = repository;
    }

    private void validateRules(AirlineEmployee emp, String currentId) {
        if (currentId == null && repository.existsById(emp.getId())) {
            throw new IllegalArgumentException("Employee ID " + emp.getId() + " is already in use.");
        }
        boolean licenseExists;
        if (currentId == null) {
            licenseExists = repository.existsByLicenseNumber(emp.getLicenseNumber());
        } else {
            licenseExists = repository.existsByLicenseNumberAndIdNot(emp.getLicenseNumber(), currentId);
        }
        if (licenseExists) {
            throw new IllegalArgumentException("License Number " + emp.getLicenseNumber() + " is already registered.");
        }
    }

    public AirlineEmployee save(AirlineEmployee emp) {
        validateRules(emp, null);
        return repository.save(emp);
    }

    public void update(String id, AirlineEmployee update) {
        update.setId(id);
        validateRules(update, id);
        repository.save(update);
    }

    public boolean delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<AirlineEmployee> findById(String id) { return repository.findById(id); }

    // --- FIX AICI: Avem nevoie de ambele metode ---

    // 1. Metoda folosită de Controller pentru sortare
    public List<AirlineEmployee> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    // 2. Metoda folosită de DataInitializer (fără parametri) - REPARĂ EROAREA
    public List<AirlineEmployee> findAll() {
        return repository.findAll();
    }
}