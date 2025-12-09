package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.AirlineEmployee;
import com.example.flightmanagementsystem.model.Role;
import com.example.flightmanagementsystem.service.AirlineEmployeeService;
import jakarta.validation.Valid; // Ensure this import matches your validation dependency
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/airline-employees")
public class AirlineEmployeeController {

    private final AirlineEmployeeService airlineEmployeeService;

    public AirlineEmployeeController(AirlineEmployeeService airlineEmployeeService) {
        this.airlineEmployeeService = airlineEmployeeService;
    }

    // 1. List - No changes needed
    @GetMapping
    public String listAirlineEmployees(Model model) {
        model.addAttribute("airlineEmployees", airlineEmployeeService.findAll());
        return "airlineemployees/index";
    }

    // 2. New Form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("airlineEmployee", new AirlineEmployee());
        model.addAttribute("roles", Role.values());
        return "airlineemployees/form";
    }

    // 3. Create - UPDATED
    @PostMapping
    public String createAirlineEmployee(
            @Valid @ModelAttribute("airlineEmployee") AirlineEmployee airlineEmployee,
            BindingResult bindingResult, // Must come immediately after the @Valid object
            Model model
    ) {
        // 1. Check for standard annotation errors (NotBlank, NotNull, etc.)
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", Role.values()); // Re-send roles so dropdown works
            return "airlineemployees/form"; // Return to form to show errors
        }

        // 2. Check for custom business rules (Unique License)
        try {
            airlineEmployeeService.save(airlineEmployee);
        } catch (IllegalArgumentException e) {
            // Add the service error to the binding result manually
            bindingResult.rejectValue("licenseNumber", "error.user", e.getMessage());
            model.addAttribute("roles", Role.values());
            return "airlineemployees/form";
        }

        return "redirect:/airline-employees";
    }

    // 4. Edit Form - No changes needed
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        AirlineEmployee emp = airlineEmployeeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + id));
        model.addAttribute("airlineEmployee", emp);
        model.addAttribute("roles", Role.values());
        return "airlineemployees/form";
    }

    // 5. Update - UPDATED
    @PostMapping("/{id}")
    public String updateAirlineEmployee(
            @PathVariable String id,
            @Valid @ModelAttribute("airlineEmployee") AirlineEmployee airlineEmployee,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", Role.values());
            return "airlineemployees/form";
        }

        try {
            airlineEmployeeService.updateEmployee(id, airlineEmployee);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("licenseNumber", "error.user", e.getMessage());
            model.addAttribute("roles", Role.values());
            return "airlineemployees/form";
        }

        return "redirect:/airline-employees";
    }

    // 6) Delete airline employee
    @PostMapping("/{id}/delete")
    public String deleteAirlineEmployee(@PathVariable String id) {
        airlineEmployeeService.delete(id);
        return "redirect:/airline-employees";
    }

    // 7) Show details page
    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        AirlineEmployee emp = airlineEmployeeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + id));

        model.addAttribute("airlineEmployee", emp);
        return "airlineemployees/details";
    }
}
