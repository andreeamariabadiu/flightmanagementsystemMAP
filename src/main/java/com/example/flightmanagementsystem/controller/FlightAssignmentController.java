package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.FlightAssignment;
import com.example.flightmanagementsystem.service.AirlineEmployeeService;
import com.example.flightmanagementsystem.service.FlightAssignmentService;
import com.example.flightmanagementsystem.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort; // IMPORT NOU
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/flight-assignments")
public class FlightAssignmentController {

    private final FlightAssignmentService flightAssignmentService;
    private final FlightService flightService;
    private final AirlineEmployeeService employeeService;

    public FlightAssignmentController(FlightAssignmentService flightAssignmentService,
                                      FlightService flightService,
                                      AirlineEmployeeService employeeService) {
        this.flightAssignmentService = flightAssignmentService;
        this.flightService = flightService;
        this.employeeService = employeeService;
    }

    // METODĂ ACTUALIZATĂ PENTRU SORTARE
    @GetMapping
    public String listAssignments(
            Model model,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        List<FlightAssignment> assignments = flightAssignmentService.findAll(sort);

        model.addAttribute("flightAssignments", assignments);

        // Parametrii pentru View
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");

        return "flightassignment/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("flightAssignment", new FlightAssignment());
        model.addAttribute("flights", flightService.findAll());
        model.addAttribute("employees", employeeService.findAll());
        return "flightassignment/form";
    }

    @PostMapping
    public String createAssignment(
            @Valid @ModelAttribute("flightAssignment") FlightAssignment fa,
            BindingResult bindingResult,
            @RequestParam("flightId") String flightId,
            @RequestParam("employeeId") String employeeId,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("flights", flightService.findAll());
            model.addAttribute("employees", employeeService.findAll());
            return "flightassignment/form";
        }

        try {
            flightAssignmentService.createAssignment(fa, flightId, employeeId);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            model.addAttribute("flights", flightService.findAll());
            model.addAttribute("employees", employeeService.findAll());
            return "flightassignment/form";
        }

        return "redirect:/flight-assignments";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        FlightAssignment fa = flightAssignmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID"));

        model.addAttribute("flightAssignment", fa);
        model.addAttribute("flights", flightService.findAll());
        model.addAttribute("employees", employeeService.findAll());
        return "flightassignment/form";
    }

    @PostMapping("/{id}")
    public String updateAssignment(
            @PathVariable String id,
            @Valid @ModelAttribute("flightAssignment") FlightAssignment fa,
            BindingResult bindingResult,
            @RequestParam("flightId") String flightId,
            @RequestParam("employeeId") String employeeId,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("flights", flightService.findAll());
            model.addAttribute("employees", employeeService.findAll());
            return "flightassignment/form";
        }

        try {
            flightAssignmentService.updateAssignment(id, fa, flightId, employeeId);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            model.addAttribute("flights", flightService.findAll());
            model.addAttribute("employees", employeeService.findAll());
            return "flightassignment/form";
        }

        return "redirect:/flight-assignments";
    }

    @PostMapping("/{id}/delete")
    public String deleteAssignment(@PathVariable String id) {
        flightAssignmentService.delete(id);
        return "redirect:/flight-assignments";
    }

    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        FlightAssignment fa = flightAssignmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("flightAssignment", fa);
        return "flightassignment/details";
    }
}