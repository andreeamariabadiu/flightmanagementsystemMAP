package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.Luggage;
import com.example.flightmanagementsystem.model.Luggage.Status;
import com.example.flightmanagementsystem.service.LuggageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/luggages")
public class LuggageController {

    private final LuggageService luggageService;

    public LuggageController(LuggageService luggageService) {
        this.luggageService = luggageService;
    }

    // List all luggages
    @GetMapping
    public String listLuggages(Model model) {
        model.addAttribute("luggages", luggageService.findAll());
        return "luggage/index";
    }

    // Form to create a new luggage
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("luggage", new Luggage());
        model.addAttribute("statuses", Status.values());
        return "luggage/form";
    }

    // Handle create (and also used for update because save is upsert)
    @PostMapping
    public String createLuggage(
            @RequestParam String id,
            @RequestParam String ticketId,
            @RequestParam String status
    ) {
        Status s = Status.valueOf(status);
        Luggage l = new Luggage(id, ticketId, s);
        luggageService.save(l);
        return "redirect:/luggages";
    }

    // Delete
    @PostMapping("/{id}/delete")
    public String deleteLuggage(@PathVariable String id) {
        luggageService.delete(id);
        return "redirect:/luggages";
    }

    // Edit form (reuses form.html)
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Luggage l = luggageService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("luggage", l);
        model.addAttribute("statuses", Status.values());
        return "luggage/form";
    }

    // Details
    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        Luggage l = luggageService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("luggage", l);
        return "luggage/details";
    }
}
