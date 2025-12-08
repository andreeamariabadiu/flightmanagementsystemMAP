package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.AirlineEmployee;
import com.example.flightmanagementsystem.model.Flight;
import com.example.flightmanagementsystem.model.Status;
import com.example.flightmanagementsystem.service.FlightService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;

    private static final DateTimeFormatter FORM_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    // List all flights
    @GetMapping
    public String listFlights(Model model) {
        model.addAttribute("flights", flightService.findAll());
        return "flight/index";
    }

    // Show empty form to create a new flight
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("flight", new Flight());
        return "flight/form";
    }

    // Handle form submission and create a flight
    @PostMapping
    public String createFlight(
            @RequestParam String id,
            @RequestParam String name,
            @RequestParam(required = false) String noticeBoardId,
            @RequestParam(required = false) String airplaneId,
            @RequestParam(required = false) List tickets,
            @RequestParam(required = false) List flightAssignments,
            @RequestParam String departureTime,
            @RequestParam String arrivalTime,
            @RequestParam Status status
    ) {
        LocalDateTime dep = LocalDateTime.parse(departureTime, FORM_FMT);
        LocalDateTime arr = LocalDateTime.parse(arrivalTime, FORM_FMT);

        Flight f = new Flight(id, name, noticeBoardId, airplaneId, tickets, flightAssignments, dep, arr, status);
        flightService.save(f);

        return "redirect:/flights";
    }

    // Delete a flight
    @PostMapping("/{id}/delete")
    public String deleteFlight(@PathVariable String id) {
        flightService.delete(id);
        return "redirect:/flights";
    }

    //update airline employee form
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Flight f = flightService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("flight", f);
        return "flight/form";
    }

    //show details page
    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        Flight f = flightService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("flight", f);
        return "flight/details";
    }
}
