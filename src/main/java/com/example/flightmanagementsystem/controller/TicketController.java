package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.Ticket;
import com.example.flightmanagementsystem.service.FlightService;
import com.example.flightmanagementsystem.service.PassengerService;
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
    private final FlightService flightService;
    private final PassengerService passengerService;

    public TicketController(TicketService ticketService, FlightService flightService, PassengerService passengerService) {
        this.ticketService = ticketService;
        this.flightService = flightService;
        this.passengerService = passengerService;
    }

    @GetMapping
    public String listTickets(Model model) {
        model.addAttribute("tickets", ticketService.findAll());
        return "ticket/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("flights", flightService.findAll());
        model.addAttribute("passengers", passengerService.findAll());
        return "ticket/form";
    }

    @PostMapping
    public String createTicket(
            @Valid @ModelAttribute("ticket") Ticket ticket,
            BindingResult bindingResult,
            @RequestParam("flightId") String flightId,
            @RequestParam("passengerId") String passengerId,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("flights", flightService.findAll());
            model.addAttribute("passengers", passengerService.findAll());
            return "ticket/form";
        }
        try {
            ticketService.createTicket(ticket, flightId, passengerId);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            model.addAttribute("flights", flightService.findAll());
            model.addAttribute("passengers", passengerService.findAll());
            return "ticket/form";
        }
        return "redirect:/tickets";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Ticket t = ticketService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("ticket", t);
        model.addAttribute("flights", flightService.findAll());
        model.addAttribute("passengers", passengerService.findAll());
        return "ticket/form";
    }

    @PostMapping("/{id}")
    public String updateTicket(
            @PathVariable String id,
            @Valid @ModelAttribute("ticket") Ticket ticket,
            BindingResult bindingResult,
            @RequestParam("flightId") String flightId,
            @RequestParam("passengerId") String passengerId,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("flights", flightService.findAll());
            model.addAttribute("passengers", passengerService.findAll());
            return "ticket/form";
        }
        try {
            ticketService.updateTicket(id, ticket, flightId, passengerId);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            model.addAttribute("flights", flightService.findAll());
            model.addAttribute("passengers", passengerService.findAll());
            return "ticket/form";
        }
        return "redirect:/tickets";
    }

    // ... delete si details raman la fel ...
    @PostMapping("/{id}/delete")
    public String deleteTicket(@PathVariable String id) {
        ticketService.delete(id);
        return "redirect:/tickets";
    }

    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        Ticket t = ticketService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("ticket", t);
        return "ticket/details";
    }
}