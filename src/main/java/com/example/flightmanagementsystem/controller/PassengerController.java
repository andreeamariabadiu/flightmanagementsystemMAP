package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.Passenger;
import com.example.flightmanagementsystem.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort; // IMPORT
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    // --- METODA MODIFICATĂ PENTRU SORTARE ---
    @GetMapping
    public String listPassengers(
            Model model,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        List<Passenger> passengers = passengerService.findAll(sort);

        model.addAttribute("passengers", passengers);

        // Parametrii pentru View
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");

        return "passenger/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("passenger", new Passenger());
        return "passenger/form";
    }

    @PostMapping
    public String createPassenger(
            @Valid @ModelAttribute("passenger") Passenger passenger,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "passenger/form";
        }
        try {
            passengerService.save(passenger);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            return "passenger/form";
        }
        return "redirect:/passengers";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Passenger p = passengerService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid passenger id: " + id));
        model.addAttribute("passenger", p);
        return "passenger/form";
    }

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

    @PostMapping("/{id}/delete")
    public String deletePassenger(@PathVariable String id) {
        passengerService.delete(id);
        return "redirect:/passengers";
    }

    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        Passenger p = passengerService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid passenger id: " + id));
        model.addAttribute("passenger", p);
        return "passenger/details";
    }
}