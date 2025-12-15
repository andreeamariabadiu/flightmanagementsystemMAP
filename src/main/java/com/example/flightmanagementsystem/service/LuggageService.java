package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Luggage;
import com.example.flightmanagementsystem.model.Ticket;
import com.example.flightmanagementsystem.repository.LuggageRepository;
import com.example.flightmanagementsystem.repository.TicketRepository;
import jakarta.persistence.criteria.Predicate; // IMPORT
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification; // IMPORT
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Luggage> findAll() { return luggageRepository.findAll(); }

    // --- METODA COMPLEXĂ: CĂUTARE + FILTRARE + SORTARE ---
    public List<Luggage> searchLuggages(
            String id,
            String ticketId,
            Luggage.Status status,
            Sort sort
    ) {
        Specification<Luggage> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filtru ID Bagaj (Parțial)
            if (id != null && !id.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("id")), "%" + id.toLowerCase() + "%"));
            }

            // 2. Filtru ID Ticket (Navigare prin relația 'ticket')
            if (ticketId != null && !ticketId.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("ticket").get("id")), "%" + ticketId.toLowerCase() + "%"));
            }

            // 3. Filtru Status (Exact)
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return luggageRepository.findAll(spec, sort);
    }

    // Compatibilitate
    public List<Luggage> findAll(Sort sort) {
        return searchLuggages(null, null, null, sort);
    }
}