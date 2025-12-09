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

    public TicketService(TicketRepository ticketRepository, FlightRepository flightRepository, PassengerRepository passengerRepository) {
        this.ticketRepository = ticketRepository;
        this.flightRepository = flightRepository;
        this.passengerRepository = passengerRepository;
    }

    private void validateBusinessRules(Ticket ticket, String currentId) {
        if (currentId == null && ticketRepository.existsById(ticket.getId())) {
            throw new IllegalArgumentException("Ticket ID already exists.");
        }
        // Verificare loc ocupat
        // Notă: Aici folosim getFlight().getId() pentru că avem obiectul
        boolean seatTaken;
        if (currentId == null) {
            seatTaken = ticketRepository.existsByFlightIdAndSeatNumber(ticket.getFlight().getId(), ticket.getSeatNumber());
        } else {
            seatTaken = ticketRepository.existsByFlightIdAndSeatNumberAndIdNot(ticket.getFlight().getId(), ticket.getSeatNumber(), currentId);
        }
        if (seatTaken) {
            throw new IllegalArgumentException("Seat " + ticket.getSeatNumber() + " is already booked.");
        }
    }

    // CREATE
    public Ticket createTicket(Ticket ticket, String flightId, String passengerId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Flight ID"));
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Passenger ID"));

        ticket.setFlight(flight);
        ticket.setPassenger(passenger);

        validateBusinessRules(ticket, null);
        return ticketRepository.save(ticket);
    }

    // UPDATE
    public void updateTicket(String id, Ticket ticket, String flightId, String passengerId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Flight ID"));
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Passenger ID"));

        ticket.setFlight(flight);
        ticket.setPassenger(passenger);
        ticket.setId(id);

        validateBusinessRules(ticket, id);
        ticketRepository.save(ticket);
    }

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