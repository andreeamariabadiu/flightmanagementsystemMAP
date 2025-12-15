package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Flight;
import com.example.flightmanagementsystem.model.Passenger;
import com.example.flightmanagementsystem.model.Ticket;
import com.example.flightmanagementsystem.repository.FlightRepository;
import com.example.flightmanagementsystem.repository.PassengerRepository;
import com.example.flightmanagementsystem.repository.TicketRepository;
import org.springframework.data.domain.Sort; // IMPORT
import org.springframework.stereotype.Service;

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

    // --- METODE PENTRU SORTARE ---

    // 1. Folosită de DataInitializer
    public List<Ticket> findAll() { return ticketRepository.findAll(); }

    // 2. Folosită de Controller pentru Sortare
    public List<Ticket> findAll(Sort sort) {
        return ticketRepository.findAll(sort);
    }
}