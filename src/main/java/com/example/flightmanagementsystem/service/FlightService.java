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

    // --- BUSINESS LOGIC ---
    private void validateBusinessRules(Flight flight, String currentId) {

        // Rule 1: Unique ID (Only on Create)
        if (currentId == null && flightRepository.existsById(flight.getId())) {
            throw new IllegalArgumentException("Flight ID " + flight.getId() + " is already in use.");
        }

        // Rule 2: Chronology Check
        // The plane cannot arrive before it leaves.
        if (flight.getDepartureTime() != null && flight.getArrivalTime() != null) {
            if (flight.getArrivalTime().isBefore(flight.getDepartureTime())) {
                throw new IllegalArgumentException("Arrival time cannot be before Departure time.");
            }

            if (flight.getArrivalTime().isEqual(flight.getDepartureTime())) {
                throw new IllegalArgumentException("Arrival and Departure cannot be the exact same time.");
            }
        }
    }

    public Flight save(Flight f) {
        validateBusinessRules(f, null);
        return flightRepository.save(f);
    }

    public void updateFlight(String id, Flight update) {
        validateBusinessRules(update, id);
        update.setId(id);
        flightRepository.save(update);
    }

    public boolean delete(String id) {
        if (flightRepository.existsById(id)) {
            flightRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    public Optional<Flight> findById(String id) {
        return flightRepository.findById(id);
    }

}
