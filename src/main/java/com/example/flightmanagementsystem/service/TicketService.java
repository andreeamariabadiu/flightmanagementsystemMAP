package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Ticket;
import com.example.flightmanagementsystem.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    // --- BUSINESS VALIDATION ---
    private void validateBusinessRules(Ticket ticket, String currentId) {

        // Regula 1: Verificare ID Unic (doar la creare)
        if (currentId == null && ticketRepository.existsById(ticket.getId())) {
            throw new IllegalArgumentException("Ticket ID " + ticket.getId() + " already exists.");
        }

        // Regula 2: Verificare Loc Unic pe Zbor (Double Booking)
        boolean seatOccupied;
        if (currentId == null) {
            // Caz Creare: Caută orice bilet cu acest zbor și loc
            seatOccupied = ticketRepository.existsByFlightIdAndSeatNumber(
                    ticket.getFlightId(), ticket.getSeatNumber());
        } else {
            // Caz Update: Caută orice ALT bilet (exclude biletul curent) cu acest zbor și loc
            seatOccupied = ticketRepository.existsByFlightIdAndSeatNumberAndIdNot(
                    ticket.getFlightId(), ticket.getSeatNumber(), currentId);
        }

        if (seatOccupied) {
            throw new IllegalArgumentException("Seat " + ticket.getSeatNumber() +
                    " is already booked for Flight " + ticket.getFlightId());
        }
    }

    public Ticket save(Ticket t) {
        validateBusinessRules(t, null); // null indică creare nouă
        return ticketRepository.save(t);
    }

    public void updateTicket(String id, Ticket updated) {
        validateBusinessRules(updated, id); // id indică editare
        updated.setId(id);
        ticketRepository.save(updated);
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