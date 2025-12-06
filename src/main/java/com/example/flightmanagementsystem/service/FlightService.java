package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Flight;
import com.example.flightmanagementsystem.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public Flight save(Flight f) {
        return flightRepository.save(f);
    }

    public boolean delete(String id) {
        return flightRepository.deleteById(id);
    }

    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    public Optional<Flight> findById(String id) {
        return flightRepository.findById(id);
    }

}
