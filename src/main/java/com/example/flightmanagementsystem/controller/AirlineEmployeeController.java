package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.AirlineEmployee;
import com.example.flightmanagementsystem.model.Role;
import com.example.flightmanagementsystem.service.AirlineEmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/airline-employees")
public class AirlineEmployeeController {

    private final AirlineEmployeeService service;

    public AirlineEmployeeController(AirlineEmployeeService service) {
        this.service = service;
    }

    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("airlineEmployees", service.findAll());
        return "airlineemployee/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("airlineEmployee", new AirlineEmployee());
        model.addAttribute("roles", Role.values());
        return "airlineemployee/form";
    }

    @PostMapping
    public String createEmployee(
            @Valid @ModelAttribute("airlineEmployee") AirlineEmployee employee,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", Role.values());
            return "airlineemployee/form";
        }
        try {
            service.save(employee);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            model.addAttribute("roles", Role.values());
            return "airlineemployee/form";
        }
        return "redirect:/airline-employees";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        AirlineEmployee e = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("airlineEmployee", e);
        model.addAttribute("roles", Role.values());
        return "airlineemployee/form";
    }

    @PostMapping("/{id}")
    public String updateEmployee(
            @PathVariable String id,
            @Valid @ModelAttribute("airlineEmployee") AirlineEmployee employee,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", Role.values());
            return "airlineemployee/form";
        }
        try {
            service.update(id, employee);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            model.addAttribute("roles", Role.values());
            return "airlineemployee/form";
        }
        return "redirect:/airline-employees";
    }

    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable String id) {
        service.delete(id);
        return "redirect:/airline-employees";
    }

    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        AirlineEmployee e = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("airlineEmployee", e);
        return "airlineemployee/details";
    }
}