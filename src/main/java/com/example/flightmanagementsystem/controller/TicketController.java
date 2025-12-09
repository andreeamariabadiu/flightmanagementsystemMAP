package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.Ticket;
import com.example.flightmanagementsystem.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Listare
    @GetMapping
    public String listTickets(Model model) {
        model.addAttribute("tickets", ticketService.findAll());
        return "ticket/index";
    }

    // Formular Creare
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "ticket/form";
    }

    // Procesare Creare
    @PostMapping
    public String createTicket(
            @Valid @ModelAttribute("ticket") Ticket ticket,
            BindingResult bindingResult,
            Model model
    ) {
        // 1. Validări standard (@NotBlank, @NotNull, @DecimalMin)
        if (bindingResult.hasErrors()) {
            return "ticket/form";
        }

        // 2. Validări de Business (ID unic, Loc ocupat)
        try {
            ticketService.save(ticket);
        } catch (IllegalArgumentException e) {
            // Adăugăm eroarea globală pentru a fi afișată în formular
            bindingResult.reject("global.error", e.getMessage());
            return "ticket/form";
        }

        return "redirect:/tickets";
    }

    // Formular Editare
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Ticket t = ticketService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ticket ID " + id));

        model.addAttribute("ticket", t);
        return "ticket/form";
    }

    // Procesare Editare
    @PostMapping("/{id}")
    public String updateTicket(
            @PathVariable String id,
            @Valid @ModelAttribute("ticket") Ticket ticket,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "ticket/form";
        }

        try {
            ticketService.updateTicket(id, ticket);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            return "ticket/form";
        }

        return "redirect:/tickets";
    }

    // Ștergere
    @PostMapping("/{id}/delete")
    public String deleteTicket(@PathVariable String id) {
        ticketService.delete(id);
        return "redirect:/tickets";
    }

    // Detalii
    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        Ticket t = ticketService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ticket ID " + id));
        model.addAttribute("ticket", t);
        return "ticket/details";
    }
}