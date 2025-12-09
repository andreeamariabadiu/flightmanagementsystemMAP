package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.Passenger;
import com.example.flightmanagementsystem.service.PassengerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    // List all passengers
    @GetMapping
    public String listPassengers(Model model) {
        model.addAttribute("passengers", passengerService.findAll());
        return "passenger/index";
    }

    // Form to create new passenger
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("passenger", new Passenger());
        return "passenger/form";
    }

    // Handle create (POST). tickets param is comma-separated IDs (optional)
    @PostMapping
    public String createPassenger(
            @RequestParam String id,
            @RequestParam String name,
            @RequestParam String currency,
            @RequestParam(required = false) String tickets
    ) {
        Passenger p = new Passenger(id, name, currency);

        if (tickets != null && !tickets.isBlank()) {
            List<String> list = Arrays.stream(tickets.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            p.setTickets(list);
        }

        passengerService.save(p);
        return "redirect:/passengers";
    }

    // Delete
    @PostMapping("/{id}/delete")
    public String deletePassenger(@PathVariable String id) {
        passengerService.delete(id);
        return "redirect:/passengers";
    }

    // Edit form
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Passenger p = passengerService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("passenger", p);
        String ticketsCsv = String.join(", ", p.getTickets());
        model.addAttribute("ticketsCsv", ticketsCsv);
        return "passenger/form";
    }

    // Details
    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        Passenger p = passengerService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("passenger", p);
        return "passenger/details";
    }
}
