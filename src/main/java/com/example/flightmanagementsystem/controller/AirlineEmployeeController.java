package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.AirlineEmployee;
import com.example.flightmanagementsystem.model.Role;
import com.example.flightmanagementsystem.service.AirlineEmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/airline-employees")
public class AirlineEmployeeController {

    public final AirlineEmployeeService airlineEmployeeService;

    private static final DateTimeFormatter FORM_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AirlineEmployeeController(AirlineEmployeeService airlineEmployeeService) {
        this.airlineEmployeeService = airlineEmployeeService;
    }

    //list all airline employees
    @GetMapping
    public String listAirlineEmployees(Model model) {
        model.addAttribute("airlineEmployees", airlineEmployeeService.findAll());
        return "airlineemployees/index";
    }

    // create new airline employee form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("airlineEmployee", new AirlineEmployee());
        return "airlineemployees/form";
    }

    // Handle form submission and create an airline employee
    @PostMapping
    public String createAirlineEmployee(
            @RequestParam String id,
            @RequestParam String name,
            @RequestParam Role role,
            @RequestParam List<String> assignments,
            @RequestParam String licenseNumber,
            @RequestParam String registrationDate

    ) {
        LocalDate RegDate = LocalDate.parse(registrationDate, FORM_FMT);

        AirlineEmployee ae = new AirlineEmployee(id, name, assignments, role, licenseNumber, RegDate);
        airlineEmployeeService.save(ae);

        return "redirect:/airline-employees";
    }

    //delete airline employee
    @PostMapping("/{id}/delete")
    public String deleteAirlineEmployee(@PathVariable String id) {
        airlineEmployeeService.delete(id);
        return "redirect:/airline-employees";
    }

    //update airline employee form
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        model.addAttribute("airlineEmployee", airlineEmployeeService.findById(id));
        return "airlineemployees/form";
    }

    //show details page
    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        model.addAttribute("airlineEmployee", airlineEmployeeService.findById(id));
        return "airlineemployees/details";
    }

}
