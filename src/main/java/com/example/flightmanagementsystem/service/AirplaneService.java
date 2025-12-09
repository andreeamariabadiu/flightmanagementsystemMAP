package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Airplane;
import com.example.flightmanagementsystem.repository.AirplaneRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AirplaneService {
    private final AirplaneRepository airplaneRepository;

    public AirplaneService(AirplaneRepository airplaneRepository) {
        this.airplaneRepository = airplaneRepository;
    }

    private void validateRules(Airplane a, String currentId) {
        // 1. ID Unic
        if (currentId == null && airplaneRepository.existsById(a.getId())) {
            throw new IllegalArgumentException("Airplane ID " + a.getId() + " is already in use.");
        }
        // 2. Număr Unic
        boolean numberTaken;
        if (currentId == null) {
            numberTaken = airplaneRepository.existsByAirplaneNumber(a.getAirplaneNumber());
        } else {
            numberTaken = airplaneRepository.existsByAirplaneNumberAndIdNot(a.getAirplaneNumber(), currentId);
        }
        if (numberTaken) {
            throw new IllegalArgumentException("Airplane Number " + a.getAirplaneNumber() + " is already assigned.");
        }
    }

    public Airplane save(Airplane a) {
        validateRules(a, null);
        return airplaneRepository.save(a);
    }

    public void updateAirplane(String id, Airplane update) {
        validateRules(update, id);
        update.setId(id);
        // Lista de zboruri (flights) este gestionată de JPA, nu o suprascriem aici manual
        airplaneRepository.save(update);
    }

    public boolean delete(String id) {
        if (airplaneRepository.existsById(id)) {
            airplaneRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Airplane> findAll() { return airplaneRepository.findAll(); }
    public Optional<Airplane> findById(String id) { return airplaneRepository.findById(id); }
}