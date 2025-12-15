package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.AirportEmployee;
import com.example.flightmanagementsystem.model.Department;
import com.example.flightmanagementsystem.model.Designation;
import com.example.flightmanagementsystem.repository.AirportEmployeeRepository;
import jakarta.persistence.criteria.Predicate; // IMPORT
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification; // IMPORT
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AirportEmployeeService {

    private final AirportEmployeeRepository repository;

    public AirportEmployeeService(AirportEmployeeRepository repository) {
        this.repository = repository;
    }

    private void validateRules(AirportEmployee emp, String currentId) {
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

    public Optional<AirportEmployee> findById(String id) { return repository.findById(id); }

    public List<AirportEmployee> findAll() { return repository.findAll(); }

    // --- METODA NOUĂ: CĂUTARE + FILTRARE + SORTARE ---
    public List<AirportEmployee> searchEmployees(
            String name,
            Designation designation,
            Department department,
            Sort sort
    ) {
        Specification<AirportEmployee> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filtru Nume (Case Insensitive, Parțial)
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            // 2. Filtru Designation (Exact)
            if (designation != null) {
                predicates.add(cb.equal(root.get("designation"), designation));
            }

            // 3. Filtru Department (Exact)
            if (department != null) {
                predicates.add(cb.equal(root.get("department"), department));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return repository.findAll(spec, sort);
    }

    // Metoda veche findAll(sort) o putem păstra delegând către search cu null
    public List<AirportEmployee> findAll(Sort sort) {
        return searchEmployees(null, null, null, sort);
    }
}