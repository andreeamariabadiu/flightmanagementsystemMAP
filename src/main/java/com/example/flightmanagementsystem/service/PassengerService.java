package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Passenger;
import com.example.flightmanagementsystem.repository.PassengerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PassengerService {
    private final PassengerRepository repo;

    public PassengerService(PassengerRepository repo) { this.repo = repo; }

    public Passenger create(String name, String currency) {
        Passenger p = new Passenger();
        p.setId(UUID.randomUUID().toString());
        p.setName(name);
        p.setCurrency(currency);
        return repo.save(p);
    }

    public List<Passenger> findAll() { return repo.findAll(); }
    public Optional<Passenger> findById(String id) { return repo.findById(id); }
    public boolean delete(String id) { return repo.deleteById(id); }
    public Passenger save(Passenger p) { return repo.save(p); }
}
