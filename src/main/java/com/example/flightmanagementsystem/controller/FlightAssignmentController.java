package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.FlightAssignment;
import com.example.flightmanagementsystem.service.FlightAssignmentService;
import jakarta.validation.Valid; // Ensure you have this import
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/flight-assignments")
public class FlightAssignmentController {

    private final FlightAssignmentService flightAssignmentService;

    public FlightAssignmentController(FlightAssignmentService flightAssignmentService) {
        this.flightAssignmentService = flightAssignmentService;
    }

    // List all
    @GetMapping
    public String listFlightAssignments(Model model) {
        model.addAttribute("flightAssignments", flightAssignmentService.findAll());
        return "flightassignment/index";
    }

    // Create Form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("flightAssignment", new FlightAssignment());
        return "flightassignment/form";
    }

    // Handle Create (POST)
    @PostMapping
    public String createFlightAssignment(
            @Valid @ModelAttribute("flightAssignment") FlightAssignment flightAssignment,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "flightassignment/form";
        }

        try {
            flightAssignmentService.save(flightAssignment);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            return "flightassignment/form";
        }

        return "redirect:/flight-assignments";
    }

    // Edit Form
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        FlightAssignment fa = flightAssignmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("flightAssignment", fa);
        return "flightassignment/form";
    }

    // Handle Update (POST)
    @PostMapping("/{id}")
    public String updateFlightAssignment(
            @PathVariable String id,
            @Valid @ModelAttribute("flightAssignment") FlightAssignment flightAssignment,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "flightassignment/form";
        }

        try {
            flightAssignmentService.updateAssignment(id, flightAssignment);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            return "flightassignment/form";
        }

        return "redirect:/flight-assignments";
    }

    // Delete
    @PostMapping("/{id}/delete")
    public String deleteFlightAssignment(@PathVariable String id) {
        flightAssignmentService.delete(id);
        return "redirect:/flight-assignments";
    }

    // Details
    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        FlightAssignment fa = flightAssignmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("flightAssignment", fa);
        return "flightassignment/details";
    }
}