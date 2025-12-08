package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.AirportEmployee;
import com.example.flightmanagementsystem.model.Department;
import com.example.flightmanagementsystem.model.Designation;
import org.springframework.ui.Model;
import com.example.flightmanagementsystem.service.AirportEmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/airport-employees")
public class AirportEmployeeController {
    public final AirportEmployeeService airportEmployeeService;

    public AirportEmployeeController(AirportEmployeeService airportEmployeeService) {
        this.airportEmployeeService = airportEmployeeService;
    }

    //list all airport employees
    @GetMapping
    public String listAirportEmployees(Model model) {
        model.addAttribute("airportEmployees", airportEmployeeService.findAll());
        return "airportemployees/index";
    }

    //show create airport employee form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("airportEmployee", new AirportEmployee());
        return "airportemployees/form";
    }

    // Handle form submission and create an airport employee
    @PostMapping
    public String createAirportEmployee(
            @RequestParam String id,
            @RequestParam String name,
            @RequestParam Designation designation,
            @RequestParam Department department
            ) {
        AirportEmployee are = new AirportEmployee(id, name, designation, department);
        airportEmployeeService.save(are);
        return "redirect:/airport-employees";
    }

    // delete airport employee
    @PostMapping("/{id}/delete")
    public String deleteAirportEmployee(@PathVariable String id) {
        airportEmployeeService.delete(id);
        return "redirect:/airport-employees";
    }

    //update airport employee form
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        model.addAttribute("airportEmployee", airportEmployeeService.findById(id));
        return "airportemployees/form";
    }

    //show details page
    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        model.addAttribute("airportEmployee", airportEmployeeService.findById(id));
        return "airportemployees/details";
    }



}
