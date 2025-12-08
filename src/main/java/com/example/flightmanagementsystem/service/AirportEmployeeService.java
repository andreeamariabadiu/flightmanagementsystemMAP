package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.AirlineEmployee;
import com.example.flightmanagementsystem.model.AirportEmployee;
import com.example.flightmanagementsystem.repository.AirportEmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class AirportEmployeeService {
    private final AirportEmployeeRepository airportEmployeeRepository;

    public AirportEmployeeService(AirportEmployeeRepository airportEmployeeRepository) {
        this.airportEmployeeRepository = airportEmployeeRepository;
    }

    public AirportEmployee save(AirportEmployee a) {
        return airportEmployeeRepository.save(a);
    }

    public boolean delete(String id) {
        if (airportEmployeeRepository.existsById(id)) {
            airportEmployeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<AirportEmployee> findById(String id) {
        return airportEmployeeRepository.findById(id);
    }

    public List<AirportEmployee> findAll() {
        return airportEmployeeRepository.findAll();
    }

    public void updateEmployee(String id, AirportEmployee update) {
        update.setId(id);
        airportEmployeeRepository.save(update);
    }

}
