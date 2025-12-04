package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.Ticket;
import com.example.flightmanagementsystem.repository.TicketRepository;
import com.example.flightmanagementsystem.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService service;

    public TicketController() {
        this.service = new TicketService(new TicketRepository());
    }

    @PostMapping
    @ResponseBody
    public Ticket create(@RequestParam String passengerId,
                         @RequestParam String flightId,
                         @RequestParam double price,
                         @RequestParam String seatNumber) {
        return service.create(passengerId, flightId, price, seatNumber);
    }

    @GetMapping
    @ResponseBody
    public List<Ticket> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    @ResponseBody
    public Optional<Ticket> findById(@PathVariable String id) { return service.findById(id); }

    @DeleteMapping("/{id}")
    @ResponseBody
    public boolean delete(@PathVariable String id) { return service.delete(id); }
}
