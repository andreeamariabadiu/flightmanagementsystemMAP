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

    public Ticket save(Ticket t) { return ticketRepository.save(t); }

    public boolean delete(String id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Ticket> findAll() { return ticketRepository.findAll(); }

    public Optional<Ticket> findById(String id) { return ticketRepository.findById(id); }

    public void update(String id, Ticket updated) {
        updated.setId(id);
        ticketRepository.save(updated);
    }
}
