package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.Luggage;
import com.example.flightmanagementsystem.model.Luggage.Status;
import com.example.flightmanagementsystem.service.LuggageService;
import com.example.flightmanagementsystem.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort; // IMPORT
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/luggages")
public class LuggageController {

    private final LuggageService luggageService;
    private final TicketService ticketService;

    public LuggageController(LuggageService luggageService, TicketService ticketService) {
        this.luggageService = luggageService;
        this.ticketService = ticketService;
    }

    // --- METODA MODIFICATĂ PENTRU SORTARE ---
    @GetMapping
    public String listLuggages(
            Model model,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        List<Luggage> luggages = luggageService.findAll(sort);

        model.addAttribute("luggages", luggages);

        // Parametrii pentru View
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");

        return "luggage/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("luggage", new Luggage());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("tickets", ticketService.findAll());
        return "luggage/form";
    }

    @PostMapping
    public String createLuggage(
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