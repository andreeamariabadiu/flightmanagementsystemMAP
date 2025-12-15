package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.Airplane;
import com.example.flightmanagementsystem.service.AirplaneService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/airplanes")
public class AirplaneController {

    private final AirplaneService airplaneService;

    public AirplaneController(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    @GetMapping
    public String listAirplanes(
            Model model,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        List<Airplane> airplanes = airplaneService.findAll(sort);

        model.addAttribute("airplanes", airplanes);

        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");

        return "airplane/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("airplane", new Airplane());
        return "airplane/form";
    }

    @PostMapping
    public String createAirplane(@Valid @ModelAttribute("airplane") Airplane airplane, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "airplane/form";
        try {
            airplaneService.save(airplane);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            return "airplane/form";
        }
        return "redirect:/airplanes";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Airplane a = airplaneService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("airplane", a);
        return "airplane/form";
    }

    @PostMapping("/{id}")
    public String updateAirplane(@PathVariable String id, @Valid @ModelAttribute("airplane") Airplane airplane, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "airplane/form";
        try {
            airplaneService.updateAirplane(id, airplane);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            return "airplane/form";
        }
        return "redirect:/airplanes";
    }

    @PostMapping("/{id}/delete")
    public String deleteAirplane(@PathVariable String id) {
        airplaneService.delete(id);
        return "redirect:/airplanes";
    }

    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        Airplane a = airplaneService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("airplane", a);
        return "airplane/details";
    }
}