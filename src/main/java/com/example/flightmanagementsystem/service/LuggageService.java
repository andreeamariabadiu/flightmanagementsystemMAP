package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Luggage;
import com.example.flightmanagementsystem.repository.LuggageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LuggageService {
    private final LuggageRepository luggageRepository;

    public LuggageService(LuggageRepository luggageRepository) {
        this.luggageRepository = luggageRepository;
    }

    // --- BUSINESS VALIDATION ---
    private void validateBusinessRules(Luggage luggage, String currentId) {
        // Regula 1: ID Unic (doar la creare)
        if (currentId == null && luggageRepository.existsById(luggage.getId())) {
            throw new IllegalArgumentException("Luggage ID " + luggage.getId() + " already exists.");
        }
    }

    public Luggage save(Luggage l) {
        validateBusinessRules(l, null);
        return luggageRepository.save(l);
    }

    public void updateLuggage(String id, Luggage updated) {
        validateBusinessRules(updated, id);
        updated.setId(id);
        luggageRepository.save(updated);
    }

    public boolean delete(String id) {
        if (luggageRepository.existsById(id)) {
            luggageRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Luggage> findAll() {
        return luggageRepository.findAll();
    }

    public Optional<Luggage> findById(String id) {
        return luggageRepository.findById(id);
    }
}