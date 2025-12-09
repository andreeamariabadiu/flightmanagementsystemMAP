package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Flight;
import com.example.flightmanagementsystem.model.Passenger;
import com.example.flightmanagementsystem.model.Ticket;
import com.example.flightmanagementsystem.repository.FlightRepository;
import com.example.flightmanagementsystem.repository.PassengerRepository;
import com.example.flightmanagementsystem.repository.TicketRepository;
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

    // --- BUSINESS LOGIC ---
    private void validateBusinessRules(Ticket ticket, String currentId) {
        // 1. ID Unic
        if (currentId == null && ticketRepository.existsById(ticket.getId())) {
            throw new IllegalArgumentException("Ticket ID " + ticket.getId() + " already exists.");
        }

        // 2. Verificare Scaun Ocupat (Double Booking)
        // Accesăm ID-ul zborului prin obiect: ticket.getFlight().getId()
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

    // CREATE
    public Ticket createTicket(Ticket ticket, String flightId, String passengerId) {
        // 1. Găsim Zborul
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Flight ID: " + flightId));

        // 2. Găsim Pasagerul
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Passenger ID: " + passengerId));

        // 3. Setăm relațiile
        ticket.setFlight(flight);
        ticket.setPassenger(passenger);

        validateBusinessRules(ticket, null);
        return ticketRepository.save(ticket);
    }

    // UPDATE
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

    // CRUD
    public boolean delete(String id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Ticket> findAll() { return ticketRepository.findAll(); }
    public Optional<Ticket> findById(String id) { return ticketRepository.findById(id); }
}