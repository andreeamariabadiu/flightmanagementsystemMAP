package com.example.flightmanagementsystem.controller;

import org.springframework.ui.Model;
import com.example.flightmanagementsystem.model.Airplane;
import com.example.flightmanagementsystem.service.AirplaneService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/airplanes")
public class AirplaneController {
    public final AirplaneService airplaneService;

    public AirplaneController(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    //List all airplanes
    @GetMapping
    public String listAirplanes(Model model) {
        model.addAttribute("airplanes", airplaneService.findAll());
        return "airplane/index";
    }

    // Form to create new airplane
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("airplane", new Airplane());
        return "airplane/form";
    }

    // Handle form submission and create an airplane
    @PostMapping
    public String createAirplane(
            @RequestParam String id,
            @RequestParam int airplaneNumber,
            @RequestParam List<String> flights
            ) {
        Airplane a = new Airplane(id, airplaneNumber, flights);
        airplaneService.save(a);
        return "redirect:/airplanes";
    }

    //Delete airplane
    @PostMapping("/{id}/delete")
    public String deleteAirplane(@PathVariable String id) {
        airplaneService.delete(id);
        return "redirect:/airplanes";
    }


}
