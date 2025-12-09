package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.Luggage;
import com.example.flightmanagementsystem.model.Luggage.Status;
import com.example.flightmanagementsystem.service.LuggageService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/luggages")
public class LuggageController {

    private final LuggageService luggageService;

    public LuggageController(LuggageService luggageService) {
        this.luggageService = luggageService;
    }

    // 1. Listare
    @GetMapping
    public String listLuggages(Model model) {
        model.addAttribute("luggages", luggageService.findAll());
        return "luggage/index";
    }

    // 2. Formular Creare
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("luggage", new Luggage());
        model.addAttribute("statuses", Status.values());
        return "luggage/form";
    }

    // 3. Procesare Creare (POST)
    @PostMapping
    public String createLuggage(
            @Valid @ModelAttribute("luggage") Luggage luggage,
            BindingResult bindingResult,
            Model model
    ) {
        // Validări standard (@NotBlank, @NotNull)
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", Status.values());
            return "luggage/form";
        }

        // Validare Business (ID Unic)
        try {
            luggageService.save(luggage);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            model.addAttribute("statuses", Status.values());
            return "luggage/form";
        }

        return "redirect:/luggages";
    }

    // 4. Formular Editare
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Luggage l = luggageService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));

        model.addAttribute("luggage", l);
        model.addAttribute("statuses", Status.values());
        return "luggage/form";
    }

    // 5. Procesare Editare (POST)
    @PostMapping("/{id}")
    public String updateLuggage(
            @PathVariable String id,
            @Valid @ModelAttribute("luggage") Luggage luggage,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", Status.values());
            return "luggage/form";
        }

        try {
            luggageService.updateLuggage(id, luggage);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            model.addAttribute("statuses", Status.values());
            return "luggage/form";
        }

        return "redirect:/luggages";
    }

    // 6. Ștergere
    @PostMapping("/{id}/delete")
    public String deleteLuggage(@PathVariable String id) {
        luggageService.delete(id);
        return "redirect:/luggages";
    }

    // 7. Detalii
    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        Luggage l = luggageService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("luggage", l);
        return "luggage/details";
    }
}