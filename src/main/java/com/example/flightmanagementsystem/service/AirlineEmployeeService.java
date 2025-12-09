package com.example.flightmanagementsystem.service;


import com.example.flightmanagementsystem.model.AirlineEmployee;
import com.example.flightmanagementsystem.repository.AirlineEmployeeRepository;
import com.example.flightmanagementsystem.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirlineEmployeeService {
    private final AirlineEmployeeRepository airlineEmployeeRepository;

    public AirlineEmployeeService(AirlineEmployeeRepository airlineEmployeeRepository) {
        this.airlineEmployeeRepository = airlineEmployeeRepository;
    }

    private void validateBusinessRules(AirlineEmployee employee, String currentId) {

        //  Unique License check
        boolean licenseUsed;
        if (currentId == null) {
            licenseUsed = airlineEmployeeRepository.existsByLicenseNumber(employee.getLicenseNumber());
        } else {
            licenseUsed = airlineEmployeeRepository.existsByLicenseNumberAndIdNot(employee.getLicenseNumber(), currentId);
        }

        if (licenseUsed) {
            throw new IllegalArgumentException("This license number is already used by another employee.");
        }
    }

    public AirlineEmployee save(AirlineEmployee a) {
        validateBusinessRules(a, null);
        return airlineEmployeeRepository.save(a);
    }

    public void updateEmployee(String id, AirlineEmployee update) {
        validateBusinessRules(update, id);
        update.setId(id);
        airlineEmployeeRepository.save(update);
    }

    public boolean delete(String id) {
        if (airlineEmployeeRepository.existsById(id)) {
            airlineEmployeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<AirlineEmployee> findAll() {
        return airlineEmployeeRepository.findAll();
    }

    public Optional<AirlineEmployee> findById(String id) {
        return airlineEmployeeRepository.findById(id);
    }

}
