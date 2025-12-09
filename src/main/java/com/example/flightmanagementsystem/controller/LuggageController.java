package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.Luggage;
import com.example.flightmanagementsystem.model.Luggage.Status;
import com.example.flightmanagementsystem.service.LuggageService;
import com.example.flightmanagementsystem.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/luggages")
public class LuggageController {

    private final LuggageService luggageService;
    private final TicketService ticketService; // Avem nevoie de serviciul de bilete

    public LuggageController(LuggageService luggageService, TicketService ticketService) {
        this.luggageService = luggageService;
        this.ticketService = ticketService;
    }

    @GetMapping
    public String listLuggages(Model model) {
        model.addAttribute("luggages", luggageService.findAll());
        return "luggage/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("luggage", new Luggage());
        model.addAttribute("statuses", Status.values());
        // Trimitem lista de bilete pentru Dropdown
        model.addAttribute("tickets", ticketService.findAll());
        return "luggage/form";
    }

    @PostMapping
    public String createLuggage(
            @Valid @ModelAttribute("luggage") Luggage luggage,
            BindingResult bindingResult,
            @RequestParam("ticketId") String ticketId, // ID-ul selectat din Dropdown
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", Status.values());
            model.addAttribute("tickets", ticketService.findAll());
            return "luggage/form";
        }

        try {
            luggageService.createLuggage(luggage, ticketId);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            model.addAttribute("statuses", Status.values());
            model.addAttribute("tickets", ticketService.findAll());
            return "luggage/form";
        }

        return "redirect:/luggages";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Luggage l = luggageService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("luggage", l);
        model.addAttribute("statuses", Status.values());
        model.addAttribute("tickets", ticketService.findAll());
        return "luggage/form";
    }

    @PostMapping("/{id}")
    public String updateLuggage(
            @PathVariable String id,
            @Valid @ModelAttribute("luggage") Luggage luggage,
            BindingResult bindingResult,
            @RequestParam("ticketId") String ticketId,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", Status.values());
            model.addAttribute("tickets", ticketService.findAll());
            return "luggage/form";
        }

        try {
            luggageService.updateLuggage(id, luggage, ticketId);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            model.addAttribute("statuses", Status.values());
            model.addAttribute("tickets", ticketService.findAll());
            return "luggage/form";
        }

        return "redirect:/luggages";
    }

    @PostMapping("/{id}/delete")
    public String deleteLuggage(@PathVariable String id) {
        luggageService.delete(id);
        return "redirect:/luggages";
    }

    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        Luggage l = luggageService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("luggage", l);
        return "luggage/details";
    }
}