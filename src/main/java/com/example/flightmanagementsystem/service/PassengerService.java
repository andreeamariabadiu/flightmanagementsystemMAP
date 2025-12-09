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
        // Regula: ID Unic (doar la creare)
        if (currentId == null && passengerRepository.existsById(passenger.getId())) {
            throw new IllegalArgumentException("Passenger ID " + passenger.getId() + " is already in use.");
        }
    }

    // CREATE
    public Passenger save(Passenger p) {
        validateBusinessRules(p, null);
        return passengerRepository.save(p);
    }

    // UPDATE
    public void updatePassenger(String id, Passenger updated) {
        validateBusinessRules(updated, id);
        updated.setId(id); // Păstrăm ID-ul original
        // Notă: Lista de bilete nu se pierde, deoarece JPA face merge doar pe câmpurile modificate
        // sau gestionează lista separat. Aici actualizăm datele personale.
        passengerRepository.save(updated);
    }

    // DELETE
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