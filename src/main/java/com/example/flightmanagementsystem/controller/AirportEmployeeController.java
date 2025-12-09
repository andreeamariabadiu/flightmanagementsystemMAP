package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.AirportEmployee;
import com.example.flightmanagementsystem.model.Department;
import com.example.flightmanagementsystem.model.Designation;
import com.example.flightmanagementsystem.service.AirportEmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/airport-employees")
public class AirportEmployeeController {

    private final AirportEmployeeService airportEmployeeService;

    public AirportEmployeeController(AirportEmployeeService airportEmployeeService) {
        this.airportEmployeeService = airportEmployeeService;
    }

    // 1. List all airport employees
    @GetMapping
    public String listAirportEmployees(Model model) {
        model.addAttribute("airportEmployees", airportEmployeeService.findAll());
        return "airportemployees/index";
    }

    // 2. Show create form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("airportEmployee", new AirportEmployee());
        // Pass enums for the dropdowns
        model.addAttribute("designations", Designation.values());
        model.addAttribute("departments", Department.values());
        return "airportemployees/form";
    }

    // 3. Create Airport Employee (POST)
    @PostMapping
    public String createAirportEmployee(
            @Valid @ModelAttribute("airportEmployee") AirportEmployee airportEmployee,
            BindingResult bindingResult,
            Model model
    ) {
        // 1. Check for standard validation errors (empty fields, nulls)
        if (bindingResult.hasErrors()) {
            model.addAttribute("designations", Designation.values());
            model.addAttribute("departments", Department.values());
            return "airportemployees/form";
        }

        // 2. Try to save (Handle Duplicate ID if you have that rule)
        try {
            airportEmployeeService.save(airportEmployee);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("id", "error.airportEmployee", e.getMessage());
            model.addAttribute("designations", Designation.values());
            model.addAttribute("departments", Department.values());
            return "airportemployees/form";
        }

        return "redirect:/airport-employees";
    }

    // 4. Show Edit Form
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        AirportEmployee emp = airportEmployeeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + id));

        model.addAttribute("airportEmployee", emp);
        model.addAttribute("designations", Designation.values());
        model.addAttribute("departments", Department.values());
        return "airportemployees/form";
    }

    // 5. Update Airport Employee (POST)
    @PostMapping("/{id}")
    public String updateAirportEmployee(
            @PathVariable String id,
            @Valid @ModelAttribute("airportEmployee") AirportEmployee airportEmployee,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("designations", Designation.values());
            model.addAttribute("departments", Department.values());
            return "airportemployees/form";
        }

        try {
            airportEmployeeService.updateEmployee(id, airportEmployee);
        } catch (IllegalArgumentException e) {
            // In case of any service errors during update
            bindingResult.reject("global.error", e.getMessage());
            model.addAttribute("designations", Designation.values());
            model.addAttribute("departments", Department.values());
            return "airportemployees/form";
        }

        return "redirect:/airport-employees";
    }

    // 6. Delete
    @PostMapping("/{id}/delete")
    public String deleteAirportEmployee(@PathVariable String id) {
        airportEmployeeService.delete(id);
        return "redirect:/airport-employees";
    }

    // 7. Details
    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        AirportEmployee emp = airportEmployeeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + id));
        model.addAttribute("airportEmployee", emp);
        return "airportemployees/details";
    }
}