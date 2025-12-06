package com.example.flightmanagementsystem.service;

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
        return airportEmployeeRepository.deleteById(id);
    }

    public Optional<AirportEmployee> findById(String id) {
        return airportEmployeeRepository.findById(id);
    }

    public List<AirportEmployee> findAll() {
        return airportEmployeeRepository.findAll();
    }

}
