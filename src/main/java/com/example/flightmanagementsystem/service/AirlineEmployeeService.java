package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.AirlineEmployee;
import com.example.flightmanagementsystem.repository.AirlineEmployeeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification; // IMPORT
import jakarta.persistence.criteria.Predicate; // IMPORT
import com.example.flightmanagementsystem.model.Role; // IMPORT
import java.time.LocalDate; // IMPORT
import java.util.ArrayList;
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

    public List<AirlineEmployee> searchEmployees(
            String name,
            Role role,
            String licenseNumber,
            LocalDate minDate,
            LocalDate maxDate,
            Sort sort) {

        Specification<AirlineEmployee> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filtru după Nume (Case Insensitive, Parțial)
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            // 2. Filtru după Rol (Exact)
            if (role != null) {
                predicates.add(cb.equal(root.get("role"), role));
            }

            // 3. Filtru după Licență (Case Insensitive, Parțial)
            if (licenseNumber != null && !licenseNumber.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("licenseNumber")), "%" + licenseNumber.toLowerCase() + "%"));
            }

            // 4. Filtru Interval Dată (De la... Până la...)
            if (minDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("registrationDate"), minDate));
            }
            if (maxDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("registrationDate"), maxDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // Apelăm repository-ul cu Specificația ȘI Sortarea
        return repository.findAll(spec, sort);
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