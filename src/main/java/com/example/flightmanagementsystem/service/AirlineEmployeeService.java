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

    public AirlineEmployee save(AirlineEmployee a) {
        return airlineEmployeeRepository.save(a);
    }

    public boolean delete(String id) {
        return airlineEmployeeRepository.deleteById(id);
    }

    public List<AirlineEmployee> findAll() {
        return airlineEmployeeRepository.findAll();
    }

    public Optional<AirlineEmployee> findById(String id) {
        return airlineEmployeeRepository.findById(id);
    }



}
