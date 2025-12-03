package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Ticket;
import com.example.flightmanagementsystem.repository.TicketRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TicketService {
    private final TicketRepository repo;

    public TicketService(TicketRepository repo) { this.repo = repo; }

    public Ticket create(String passengerId, String flightId, double price, String seatNumber) {
        Ticket t = new Ticket();
        t.setId(UUID.randomUUID().toString());
        t.setPassengerId(passengerId);
        t.setFlightId(flightId);
        t.setPrice(price);
        t.setSeatNumber(seatNumber);
        return repo.save(t);
    }

    public List<Ticket> findAll() { return repo.findAll(); }
    public Optional<Ticket> findById(String id) { return repo.findById(id); }
    public boolean delete(String id) { return repo.deleteById(id); }
    public Ticket save(Ticket t) { return repo.save(t); }
}
