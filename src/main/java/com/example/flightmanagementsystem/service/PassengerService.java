package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Passenger;
import com.example.flightmanagementsystem.model.Ticket;
import com.example.flightmanagementsystem.repository.PassengerRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PassengerService {
    private final PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    private void validateBusinessRules(Passenger passenger, String currentId) {
        if (currentId == null && passengerRepository.existsById(passenger.getId())) {
            throw new IllegalArgumentException("Passenger ID " + passenger.getId() + " is already in use.");
        }
    }

    public Passenger save(Passenger p) {
        validateBusinessRules(p, null);
        return passengerRepository.save(p);
    }

    public void updatePassenger(String id, Passenger updated) {
        validateBusinessRules(updated, id);
        updated.setId(id);
        passengerRepository.save(updated);
    }

    // --- LOGICA DE ȘTERGERE CU REGULĂ BUSINESS ---
    public void delete(String id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found"));

        // Verificăm dacă are bilete pentru zboruri viitoare
        boolean hasFutureFlights = passenger.getTickets().stream()
                .map(Ticket::getFlight) // Luăm zborul de pe bilet
                .anyMatch(flight -> flight.getDepartureTime().isAfter(LocalDateTime.now())); // Verificăm data

        if (hasFutureFlights) {
            throw new IllegalStateException("Cannot delete passenger. They have active tickets for future flights.");
        }

        passengerRepository.delete(passenger);
    }

    public Optional<Passenger> findById(String id) {
        return passengerRepository.findById(id);
    }

    public List<Passenger> findAll() { return passengerRepository.findAll(); }

    // --- METODA COMPLEXĂ: CĂUTARE + FILTRARE + SORTARE ---
    public List<Passenger> searchPassengers(
            String id,
            String name,
            String currency,
            Sort sort
    ) {
        Specification<Passenger> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filtru ID (Parțial)
            if (id != null && !id.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("id")), "%" + id.toLowerCase() + "%"));
            }

            // 2. Filtru Nume (Parțial)
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            // 3. Filtru Currency (Exact)
            if (currency != null && !currency.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("currency"), currency));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // Sortare specială pentru Ticket Count (în memorie)
        if (sort.getOrderFor("ticketCount") != null) {
            List<Passenger> filteredList = passengerRepository.findAll(spec);
            Sort.Order order = sort.getOrderFor("ticketCount");
            Comparator<Passenger> comparator = Comparator.comparingInt(p -> p.getTickets().size());

            if (order.isDescending()) comparator = comparator.reversed();

            return filteredList.stream().sorted(comparator).collect(Collectors.toList());
        }

        return passengerRepository.findAll(spec, sort);
    }

    // Compatibilitate
    public List<Passenger> findAll(Sort sort) {
        return searchPassengers(null, null, null, sort);
    }
}