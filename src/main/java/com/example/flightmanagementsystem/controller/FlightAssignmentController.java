package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.AirlineEmployee;
import com.example.flightmanagementsystem.model.FlightAssignment;
import com.example.flightmanagementsystem.service.FlightAssignmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/flight-assignments")
public class FlightAssignmentController {
    public final FlightAssignmentService flightAssignmentService;

    public FlightAssignmentController(FlightAssignmentService flightAssignmentService) {
        this.flightAssignmentService = flightAssignmentService;
    }

    // list all flight assignments
    @GetMapping
    public String listFlightAssignments(Model model) {
        model.addAttribute("flightAssignments", flightAssignmentService.findAll());
        return "flightassignment/index";
    }

    // show form to create new flight assignment
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("flightAssignment", new FlightAssignment());
        return "flightassignment/form";
    }

    // handle form submission and create a flight assignment
    @PostMapping
    public String createFlightAssignment(
            @RequestParam String id,
            @RequestParam String flightId,
            @RequestParam String staffId
    ) {
        FlightAssignment fa = new FlightAssignment(id, flightId, staffId);
        flightAssignmentService.save(fa);
        return "redirect:/flight-assignments";
    }

    //delete flight assignment
    @PostMapping("/{id}/delete")
    public String deleteFlightAssignment(@PathVariable String id) {
        flightAssignmentService.delete(id);
        return "redirect:/flight-assignments";
    }

    //update airline employee form
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        FlightAssignment fa = flightAssignmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("flightAssignment", fa);
        return "flightassignment/form";
    }

    //show details page
    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        FlightAssignment fa = flightAssignmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("flightAssignment", fa);
        return "flightassignment/details";
    }
}
