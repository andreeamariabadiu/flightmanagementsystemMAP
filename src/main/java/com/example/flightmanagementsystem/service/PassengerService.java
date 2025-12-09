package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Passenger;
import com.example.flightmanagementsystem.repository.PassengerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerService {
    private final PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    // --- BUSINESS VALIDATION ---
    private void validateBusinessRules(Passenger passenger, String currentId) {
        // Regula 1: ID Unic (doar la creare)
        if (currentId == null && passengerRepository.existsById(passenger.getId())) {
            throw new IllegalArgumentException("Passenger ID " + passenger.getId() + " is already in use.");
        }
    }

    public Passenger save(Passenger p) {
        validateBusinessRules(p, null);
        return passengerRepository.save(p);
    }

    public void updatePassenger(String id, Passenger updated) {
        validateBusinessRules(updated, id);
        updated.setId(id);
        passengerRepository.save(updated);
    }

    public boolean delete(String id) {
        if (passengerRepository.existsById(id)) {
            passengerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Passenger> findAll() {
        return passengerRepository.findAll();
    }

    public Optional<Passenger> findById(String id) {
        return passengerRepository.findById(id);
    }
}