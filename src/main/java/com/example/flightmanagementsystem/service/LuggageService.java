package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Luggage;
import com.example.flightmanagementsystem.model.Ticket;
import com.example.flightmanagementsystem.repository.LuggageRepository;
import com.example.flightmanagementsystem.repository.TicketRepository;
import org.springframework.data.domain.Sort; // IMPORT
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
        if (currentId == null && luggageRepository.existsById(luggage.getId())) {
            throw new IllegalArgumentException("Luggage ID " + luggage.getId() + " already exists.");
        }
    }

    public Luggage createLuggage(Luggage luggage, String ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Ticket ID: " + ticketId));

        luggage.setTicket(ticket);

        validateRules(luggage, null);
        return luggageRepository.save(luggage);
    }

    public void updateLuggage(String id, Luggage updated, String ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Ticket ID"));

        updated.setId(id);
        updated.setTicket(ticket);

        validateRules(updated, id);
        luggageRepository.save(updated);
    }

    public boolean delete(String id) {
        if (luggageRepository.existsById(id)) {
            luggageRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Luggage> findById(String id) { return luggageRepository.findById(id); }

    // --- METODE PENTRU SORTARE ---

    // 1. Folosită de DataInitializer
    public List<Luggage> findAll() { return luggageRepository.findAll(); }

    // 2. Folosită de Controller pentru Sortare
    public List<Luggage> findAll(Sort sort) {
        return luggageRepository.findAll(sort);
    }
}