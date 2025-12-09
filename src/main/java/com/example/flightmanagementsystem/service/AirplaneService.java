package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Airplane;
import com.example.flightmanagementsystem.repository.AirplaneRepository;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class AirplaneService {
    private final AirplaneRepository airplaneRepository;

    public AirplaneService(AirplaneRepository airplaneRepository) {
        this.airplaneRepository = airplaneRepository;
    }

    // --- BUSINESS LOGIC ---
    private void validateBusinessRules(Airplane airplane, String currentId) {

        // Rule 1: Unique ID (Only check on Create)
        if (currentId == null && airplaneRepository.existsById(airplane.getId())) {
            throw new IllegalArgumentException("Airplane ID " + airplane.getId() + " is already taken.");
        }

        // Rule 2: Unique Airplane Number
        boolean numberTaken;
        if (currentId == null) {
            // New airplane
            numberTaken = airplaneRepository.existsByAirplaneNumber(airplane.getAirplaneNumber());
        } else {
            // Editing existing airplane (ignore self)
            numberTaken = airplaneRepository.existsByAirplaneNumberAndIdNot(airplane.getAirplaneNumber(), currentId);
        }

        if (numberTaken) {
            throw new IllegalArgumentException("Airplane Number " + airplane.getAirplaneNumber() + " is already in use.");
        }
    }


    public Airplane save(Airplane a) {
        validateBusinessRules(a, null);
        return airplaneRepository.save(a);
    }

    public boolean delete(String id) {
        if (airplaneRepository.existsById(id)) {
            airplaneRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Airplane> findAll() {
        return airplaneRepository.findAll();
    }

    public Optional<Airplane> findById(String id) {
        return airplaneRepository.findById(id);
    }

    public void updateAirplane(String id, Airplane update) {
        validateBusinessRules(update, id);
        update.setId(id);
        airplaneRepository.save(update);
    }
}
