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

    public Luggage save(Luggage l) {
        return luggageRepository.save(l);
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

    public void update(String id, Luggage updated) {
        updated.setId(id);
        luggageRepository.save(updated);
    }
}
