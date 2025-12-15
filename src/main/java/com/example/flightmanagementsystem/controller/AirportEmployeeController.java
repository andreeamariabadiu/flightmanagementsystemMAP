package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.AirportEmployee;
import com.example.flightmanagementsystem.model.Department;
import com.example.flightmanagementsystem.model.Designation;
import com.example.flightmanagementsystem.service.AirportEmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort; // IMPORT
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/airport-employees")
public class AirportEmployeeController {

    private final AirportEmployeeService service;

    public AirportEmployeeController(AirportEmployeeService service) {
        this.service = service;
    }

    // --- METODA MODIFICATĂ PENTRU SORTARE ---
    @GetMapping
    public String list(
            Model model,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        List<AirportEmployee> employees = service.findAll(sort);

        model.addAttribute("employees", employees);

        // Parametrii pentru View
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");

        return "airportemployee/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("airportEmployee", new AirportEmployee());
        model.addAttribute("designations", Designation.values());
        model.addAttribute("departments", Department.values());
        return "airportemployee/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("airportEmployee") AirportEmployee emp,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("designations", Designation.values());
            model.addAttribute("departments", Department.values());
            return "airportemployee/form";
        }
        try {
            service.save(emp);
        } catch (Exception e) {
            result.reject("global.error", e.getMessage());
            model.addAttribute("designations", Designation.values());
            model.addAttribute("departments", Department.values());
            return "airportemployee/form";
        }
        return "redirect:/airport-employees";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        AirportEmployee e = service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("airportEmployee", e);
        model.addAttribute("designations", Designation.values());
        model.addAttribute("departments", Department.values());
        return "airportemployee/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable String id,
            @Valid @ModelAttribute("airportEmployee") AirportEmployee emp,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("designations", Designation.values());
            model.addAttribute("departments", Department.values());
            return "airportemployee/form";
        }
        try {
            service.update(id, emp);
        } catch (Exception e) {
            result.reject("global.error", e.getMessage());
            model.addAttribute("designations", Designation.values());
            model.addAttribute("departments", Department.values());
            return "airportemployee/form";
        }
        return "redirect:/airport-employees";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        service.delete(id);
        return "redirect:/airport-employees";
    }

    @GetMapping("/{id}/details")
    public String details(@PathVariable String id, Model model) {
        AirportEmployee e = service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("airportEmployee", e);
        return "airportemployee/details";
    }
}