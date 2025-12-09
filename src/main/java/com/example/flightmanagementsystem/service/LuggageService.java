package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Luggage;
import com.example.flightmanagementsystem.model.Ticket;
import com.example.flightmanagementsystem.repository.LuggageRepository;
import com.example.flightmanagementsystem.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LuggageService {

    private final LuggageRepository luggageRepository;
    private final TicketRepository ticketRepository;

    public LuggageService(LuggageRepository luggageRepository, TicketRepository ticketRepository) {
        this.luggageRepository = luggageRepository;
        this.ticketRepository = ticketRepository;
    }

    private void validateRules(Luggage luggage, String currentId) {
        // Regula: ID Unic
        if (currentId == null && luggageRepository.existsById(luggage.getId())) {
            throw new IllegalArgumentException("Luggage ID " + luggage.getId() + " already exists.");
        }
    }

    // CREATE
    public Luggage createLuggage(Luggage luggage, String ticketId) {
        // 1. Găsim biletul părintelui
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Ticket ID: " + ticketId));

        // 2. Atașăm biletul la bagaj
        luggage.setTicket(ticket);

        validateRules(luggage, null);
        return luggageRepository.save(luggage);
    }

    // UPDATE
    public void updateLuggage(String id, Luggage updated, String ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Ticket ID"));

        updated.setId(id);
        updated.setTicket(ticket);

        validateRules(updated, id);
        luggageRepository.save(updated);
    }

    // STANDARD
    public boolean delete(String id) {
        if (luggageRepository.existsById(id)) {
            luggageRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Luggage> findAll() { return luggageRepository.findAll(); }
    public Optional<Luggage> findById(String id) { return luggageRepository.findById(id); }
}