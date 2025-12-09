package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.Flight;
import com.example.flightmanagementsystem.model.Status;
import com.example.flightmanagementsystem.service.AirplaneService;
import com.example.flightmanagementsystem.service.FlightService;
import com.example.flightmanagementsystem.service.NoticeBoardService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;
    private final AirplaneService airplaneService;
    private final NoticeBoardService noticeBoardService;

    // Injectăm toate service-urile necesare pentru a popula Dropdown-urile
    public FlightController(FlightService flightService,
                            AirplaneService airplaneService,
                            NoticeBoardService noticeBoardService) {
        this.flightService = flightService;
        this.airplaneService = airplaneService;
        this.noticeBoardService = noticeBoardService;
    }

    // Listare
    @GetMapping
    public String listFlights(Model model) {
        model.addAttribute("flights", flightService.findAll());
        return "flight/index";
    }

    // Formular Creare
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("flight", new Flight());
        model.addAttribute("statuses", Status.values());
        // Trimitem listele pentru <select>
        model.addAttribute("airplanes", airplaneService.findAll());
        model.addAttribute("noticeBoards", noticeBoardService.findAll());
        return "flight/form";
    }

    // Procesare Creare
    @PostMapping
    public String createFlight(
            @Valid @ModelAttribute("flight") Flight flight,
            BindingResult bindingResult,
            @RequestParam("airplaneId") String airplaneId,     // ID din Dropdown
            @RequestParam("noticeBoardId") String noticeBoardId, // ID din Dropdown
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            // Dacă sunt erori, retrimitem listele ca să nu dispară dropdown-urile
            model.addAttribute("statuses", Status.values());
            model.addAttribute("airplanes", airplaneService.findAll());
            model.addAttribute("noticeBoards", noticeBoardService.findAll());
            return "flight/form";
        }

        try {
            flightService.createFlight(flight, airplaneId, noticeBoardId);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            model.addAttribute("statuses", Status.values());
            model.addAttribute("airplanes", airplaneService.findAll());
            model.addAttribute("noticeBoards", noticeBoardService.findAll());
            return "flight/form";
        }

        return "redirect:/flights";
    }

    // Formular Editare
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Flight f = flightService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));

        model.addAttribute("flight", f);
        model.addAttribute("statuses", Status.values());
        // Listele sunt necesare și la editare
        model.addAttribute("airplanes", airplaneService.findAll());
        model.addAttribute("noticeBoards", noticeBoardService.findAll());

        return "flight/form";
    }

    // Procesare Editare
    @PostMapping("/{id}")
    public String updateFlight(
            @PathVariable String id,
            @Valid @ModelAttribute("flight") Flight flight,
            BindingResult bindingResult,
            @RequestParam("airplaneId") String airplaneId,
            @RequestParam("noticeBoardId") String noticeBoardId,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", Status.values());
            model.addAttribute("airplanes", airplaneService.findAll());
            model.addAttribute("noticeBoards", noticeBoardService.findAll());
            return "flight/form";
        }

        try {
            flightService.updateFlight(id, flight, airplaneId, noticeBoardId);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            model.addAttribute("statuses", Status.values());
            model.addAttribute("airplanes", airplaneService.findAll());
            model.addAttribute("noticeBoards", noticeBoardService.findAll());
            return "flight/form";
        }

        return "redirect:/flights";
    }

    // Ștergere
    @PostMapping("/{id}/delete")
    public String deleteFlight(@PathVariable String id) {
        flightService.delete(id);
        return "redirect:/flights";
    }

    // Detalii
    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        Flight f = flightService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("flight", f);
        return "flight/details";
    }
}