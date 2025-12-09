package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.Airplane;
import com.example.flightmanagementsystem.service.AirplaneService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/airplanes")
public class AirplaneController {

    private final AirplaneService airplaneService;

    public AirplaneController(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    // List all
    @GetMapping
    public String listAirplanes(Model model) {
        model.addAttribute("airplanes", airplaneService.findAll());
        return "airplane/index";
    }

    // New Form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("airplane", new Airplane());
        return "airplane/form";
    }

    // Create (POST)
    @PostMapping
    public String createAirplane(
            @Valid @ModelAttribute("airplane") Airplane airplane,
            BindingResult bindingResult
    ) {
        // 1. Validation Annotations (@NotNull, @Positive, @NotEmpty)
        if (bindingResult.hasErrors()) {
            return "airplane/form";
        }

        // 2. Business Logic (Unique ID, Unique Number)
        try {
            airplaneService.save(airplane);
        } catch (IllegalArgumentException e) {
            // Determine if error is about ID or Number based on message,
            // or just add global error. Here we add a global error for simplicity.
            bindingResult.reject("global.error", e.getMessage());
            return "airplane/form";
        }

        return "redirect:/airplanes";
    }

    // Edit Form
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Airplane a = airplaneService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("airplane", a);
        return "airplane/form";
    }

    // Update (POST) - This was missing in your original code
    @PostMapping("/{id}")
    public String updateAirplane(
            @PathVariable String id,
            @Valid @ModelAttribute("airplane") Airplane airplane,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "airplane/form";
        }

        try {
            airplaneService.updateAirplane(id, airplane);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            return "airplane/form";
        }

        return "redirect:/airplanes";
    }

    // Delete
    @PostMapping("/{id}/delete")
    public String deleteAirplane(@PathVariable String id) {
        airplaneService.delete(id);
        return "redirect:/airplanes";
    }

    // Details
    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        Airplane a = airplaneService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("airplane", a);
        return "airplane/details";
    }
}