package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.Ticket;
import com.example.flightmanagementsystem.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public String listTickets(Model model) {
        model.addAttribute("tickets", ticketService.findAll());
        return "ticket/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "ticket/form";
    }

    @PostMapping
    public String createTicket(
            @RequestParam String id,
            @RequestParam String passengerId,
            @RequestParam String flightId,
            @RequestParam double price,
            @RequestParam String seatNumber,
            @RequestParam(required = false) String luggages
    ) {
        Ticket t = new Ticket(id, passengerId, flightId, price, seatNumber);

        if (luggages != null && !luggages.isBlank()) {
            List<String> list = Arrays.stream(luggages.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            t.setLuggages(list);
        }

        ticketService.save(t);
        return "redirect:/tickets";
    }

    @PostMapping("/{id}/delete")
    public String deleteTicket(@PathVariable String id) {
        ticketService.delete(id);
        return "redirect:/tickets";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Ticket t = ticketService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ticket ID " + id));

        model.addAttribute("ticket", t);
        model.addAttribute("luggagesCsv", String.join(", ", t.getLuggages()));

        return "ticket/form";
    }

    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        Ticket t = ticketService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ticket ID " + id));

        model.addAttribute("ticket", t);
        return "ticket/details";
    }
}
