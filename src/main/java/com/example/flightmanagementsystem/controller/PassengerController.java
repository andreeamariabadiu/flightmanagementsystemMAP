package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.Passenger;
import com.example.flightmanagementsystem.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    // 1. Listare
    @GetMapping
    public String listPassengers(Model model) {
        model.addAttribute("passengers", passengerService.findAll());
        return "passenger/index";
    }

    // 2. Formular Creare
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("passenger", new Passenger());
        return "passenger/form";
    }

    // 3. Procesare Creare (POST)
    @PostMapping
    public String createPassenger(
            @Valid @ModelAttribute("passenger") Passenger passenger,
            BindingResult bindingResult
    ) {
        // Validări standard (@NotBlank, @Pattern)
        if (bindingResult.hasErrors()) {
            return "passenger/form";
        }

        // Validare Business (ID unic)
        try {
            passengerService.save(passenger);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            return "passenger/form";
        }

        return "redirect:/passengers";
    }

    // 4. Formular Editare
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Passenger p = passengerService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid passenger id: " + id));

        model.addAttribute("passenger", p);
        return "passenger/form";
    }

    // 5. Procesare Editare (POST)
    @PostMapping("/{id}")
    public String updatePassenger(
            @PathVariable String id,
            @Valid @ModelAttribute("passenger") Passenger passenger,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "passenger/form";
        }

        try {
            passengerService.updatePassenger(id, passenger);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            return "passenger/form";
        }

        return "redirect:/passengers";
    }

    // 6. Ștergere
    @PostMapping("/{id}/delete")
    public String deletePassenger(@PathVariable String id) {
        passengerService.delete(id);
        return "redirect:/passengers";
    }

    // 7. Detalii
    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        Passenger p = passengerService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid passenger id: " + id));
        model.addAttribute("passenger", p);
        return "passenger/details";
    }
}