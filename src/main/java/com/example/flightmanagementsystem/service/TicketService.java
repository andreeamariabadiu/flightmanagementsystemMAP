package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Flight;
import com.example.flightmanagementsystem.model.Passenger;
import com.example.flightmanagementsystem.model.Ticket;
import com.example.flightmanagementsystem.repository.FlightRepository;
import com.example.flightmanagementsystem.repository.PassengerRepository;
import com.example.flightmanagementsystem.repository.TicketRepository;
import jakarta.persistence.criteria.Predicate; // IMPORT
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification; // IMPORT
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;

    public TicketService(TicketRepository ticketRepository,
                         FlightRepository flightRepository,
                         PassengerRepository passengerRepository) {
        this.ticketRepository = ticketRepository;
        this.flightRepository = flightRepository;
        this.passengerRepository = passengerRepository;
    }

    private void validateBusinessRules(Ticket ticket, String currentId) {
        if (currentId == null && ticketRepository.existsById(ticket.getId())) {
            throw new IllegalArgumentException("Ticket ID " + ticket.getId() + " already exists.");
        }

        boolean seatTaken;
        String flightId = ticket.getFlight().getId();
        String seat = ticket.getSeatNumber();

        if (currentId == null) {
            seatTaken = ticketRepository.existsByFlight_IdAndSeatNumber(flightId, seat);
        } else {
            seatTaken = ticketRepository.existsByFlight_IdAndSeatNumberAndIdNot(flightId, seat, currentId);
        }

        if (seatTaken) {
            throw new IllegalArgumentException("Seat " + seat + " is already booked for Flight " + flightId);
        }
    }

    public Ticket createTicket(Ticket ticket, String flightId, String passengerId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Flight ID: " + flightId));
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Passenger ID: " + passengerId));

        ticket.setFlight(flight);
        ticket.setPassenger(passenger);

        validateBusinessRules(ticket, null);
        return ticketRepository.save(ticket);
    }

    public void updateTicket(String id, Ticket update, String flightId, String passengerId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Flight ID"));
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Passenger ID"));

        update.setId(id);
        update.setFlight(flight);
        update.setPassenger(passenger);

        validateBusinessRules(update, id);
        ticketRepository.save(update);
    }

    public boolean delete(String id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Ticket> findById(String id) { return ticketRepository.findById(id); }

    public List<Ticket> findAll() { return ticketRepository.findAll(); }

    // --- METODA COMPLEXĂ: CĂUTARE + FILTRARE + SORTARE ---
    public List<Ticket> searchTickets(
            String id,
            String flightName,
            String passengerName,
            Sort sort
    ) {
        Specification<Ticket> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filtru ID Bilet (Parțial)
            if (id != null && !id.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("id")), "%" + id.toLowerCase() + "%"));
            }

            // 2. Filtru Nume Zbor (Navigare prin relația 'flight')
            if (flightName != null && !flightName.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("flight").get("flightName")), "%" + flightName.toLowerCase() + "%"));
            }

            // 3. Filtru Nume Pasager (Navigare prin relația 'passenger')
            if (passengerName != null && !passengerName.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("passenger").get("name")), "%" + passengerName.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return ticketRepository.findAll(spec, sort);
    }

    // Compatibilitate
    public List<Ticket> findAll(Sort sort) {
        return searchTickets(null, null, null, sort);
    }
}