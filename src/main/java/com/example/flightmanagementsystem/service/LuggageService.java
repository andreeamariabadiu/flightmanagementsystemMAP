package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Luggage;
import com.example.flightmanagementsystem.repository.LuggageRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LuggageService {
    private final LuggageRepository repo;

    public LuggageService(LuggageRepository repo) { this.repo = repo; }

    public Luggage create(String ticketId, Luggage.Status status) {
        Luggage l = new Luggage();
        l.setId(UUID.randomUUID().toString());
        l.setTicketId(ticketId);
        l.setStatus(status);
        return repo.save(l);
    }

    public List<Luggage> findAll() { return repo.findAll(); }
    public Optional<Luggage> findById(String id) { return repo.findById(id); }
    public boolean delete(String id) { return repo.deleteById(id); }
    public Luggage save(Luggage l) { return repo.save(l); }
}
