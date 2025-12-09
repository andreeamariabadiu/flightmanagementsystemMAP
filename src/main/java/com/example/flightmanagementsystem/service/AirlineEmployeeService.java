package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.AirlineEmployee;
import com.example.flightmanagementsystem.repository.AirlineEmployeeRepository;
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
        // 1. ID Unic
        if (currentId == null && repository.existsById(emp.getId())) {
            throw new IllegalArgumentException("Employee ID " + emp.getId() + " is already in use.");
        }
        // 2. Licență Unică
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
        // Păstrăm referința la ID
        update.setId(id);
        // ATENȚIE: Nu suprascriem lista de 'assignments' aici, JPA se ocupă de ea.
        // Validăm doar datele scalare (nume, rol, licență)
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

    public List<AirlineEmployee> findAll() { return repository.findAll(); }
    public Optional<AirlineEmployee> findById(String id) { return repository.findById(id); }
}