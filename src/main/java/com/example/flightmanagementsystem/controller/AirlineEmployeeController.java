package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.AirlineEmployee;
import com.example.flightmanagementsystem.model.Role;
import com.example.flightmanagementsystem.service.AirlineEmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort; // IMPORT NOU
import org.springframework.stereotype.Controller;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
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
            // Parametrii de filtrare (toți opționali, required=false)
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) String licenseNumber,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate minDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate maxDate,
            // Parametrii de sortare
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        // 1. Construim obiectul Sort
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // 2. Apelăm service-ul cu filtre ȘI sortare
        List<AirlineEmployee> employees = service.searchEmployees(name, role, licenseNumber, minDate, maxDate, sort);

        model.addAttribute("airlineEmployees", employees);

        // 3. Trimitem valorile FILTRELOR înapoi în pagină (pentru a rămâne în input-uri)
        model.addAttribute("filterName", name);
        model.addAttribute("filterRole", role);
        model.addAttribute("filterLicense", licenseNumber);
        model.addAttribute("filterMinDate", minDate);
        model.addAttribute("filterMaxDate", maxDate);

        // 4. Trimitem valorile SORTĂRII înapoi (pentru link-urile din tabel)
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");

        // Trimitem lista de roluri pentru dropdown-ul de filtrare
        model.addAttribute("roles", Role.values());

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