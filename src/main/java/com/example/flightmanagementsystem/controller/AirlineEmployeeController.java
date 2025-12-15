package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.AirlineEmployee;
import com.example.flightmanagementsystem.model.Role;
import com.example.flightmanagementsystem.service.AirlineEmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort; // IMPORT NOU
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List; // Asigură-te că importul este prezent

@Controller
@RequestMapping("/airline-employees")
public class AirlineEmployeeController {

    private final AirlineEmployeeService service;

    public AirlineEmployeeController(AirlineEmployeeService service) {
        this.service = service;
    }

    // METODA MODIFICATĂ: Acceptă parametri de sortare și trimite starea curentă către Model
    @GetMapping
    public String listEmployees(
            Model model,
            @RequestParam(defaultValue = "name") String sortBy, // Câmpul implicit de sortare
            @RequestParam(defaultValue = "asc") String sortDir // Direcția implicită de sortare
    ) {
        // Creăm obiectul Sort pe baza parametrilor
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        List<AirlineEmployee> employees = service.findAll(sort);

        model.addAttribute("airlineEmployees", employees);

        // Trimitem starea curentă a sortării înapoi la View
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");

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