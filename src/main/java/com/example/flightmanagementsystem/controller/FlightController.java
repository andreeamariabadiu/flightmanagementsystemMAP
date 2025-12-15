package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.Flight;
import com.example.flightmanagementsystem.model.Status;
import com.example.flightmanagementsystem.service.AirplaneService;
import com.example.flightmanagementsystem.service.FlightService;
import com.example.flightmanagementsystem.service.NoticeBoardService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort; // IMPORT NOU
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;
    private final AirplaneService airplaneService;
    private final NoticeBoardService noticeBoardService;

    public FlightController(FlightService flightService,
                            AirplaneService airplaneService,
                            NoticeBoardService noticeBoardService) {
        this.flightService = flightService;
        this.airplaneService = airplaneService;
        this.noticeBoardService = noticeBoardService;
    }

    // --- METODA MODIFICATĂ PENTRU SORTARE ---
    @GetMapping
    public String listFlights(
            Model model,
            @RequestParam(defaultValue = "departureTime") String sortBy, // Implicit: cronologic
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        List<Flight> flights = flightService.findAll(sort);

        model.addAttribute("flights", flights);

        // Parametrii pentru View
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");

        return "flight/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("flight", new Flight());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("airplanes", airplaneService.findAll());
        model.addAttribute("noticeBoards", noticeBoardService.findAll());
        return "flight/form";
    }

    @PostMapping
    public String createFlight(
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

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Flight f = flightService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("flight", f);
        model.addAttribute("statuses", Status.values());
        model.addAttribute("airplanes", airplaneService.findAll());
        model.addAttribute("noticeBoards", noticeBoardService.findAll());
        return "flight/form";
    }

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

    @PostMapping("/{id}/delete")
    public String deleteFlight(@PathVariable String id) {
        flightService.delete(id);
        return "redirect:/flights";
    }

    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        Flight f = flightService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("flight", f);
        return "flight/details";
    }
}