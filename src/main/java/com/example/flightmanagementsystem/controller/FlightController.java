package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.Flight;
import com.example.flightmanagementsystem.model.Status;
import com.example.flightmanagementsystem.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    // List all
    @GetMapping
    public String listFlights(Model model) {
        model.addAttribute("flights", flightService.findAll());
        return "flight/index";
    }

    // New Form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("flight", new Flight());
        model.addAttribute("statuses", Status.values()); // Pass Enums
        return "flight/form";
    }

    // Create (POST)
    @PostMapping
    public String createFlight(
            @Valid @ModelAttribute("flight") Flight flight,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", Status.values());
            return "flight/form";
        }

        try {
            flightService.save(flight);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            model.addAttribute("statuses", Status.values());
            return "flight/form";
        }

        return "redirect:/flights";
    }

    // Edit Form
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Flight f = flightService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));

        model.addAttribute("flight", f);
        model.addAttribute("statuses", Status.values());
        return "flight/form";
    }

    // Update (POST)
    @PostMapping("/{id}")
    public String updateFlight(
            @PathVariable String id,
            @Valid @ModelAttribute("flight") Flight flight,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", Status.values());
            return "flight/form";
        }

        try {
            flightService.updateFlight(id, flight);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            model.addAttribute("statuses", Status.values());
            return "flight/form";
        }

        return "redirect:/flights";
    }

    // Delete
    @PostMapping("/{id}/delete")
    public String deleteFlight(@PathVariable String id) {
        flightService.delete(id);
        return "redirect:/flights";
    }

    // Details
    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        Flight f = flightService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("flight", f);
        return "flight/details";
    }
}