package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.Luggage;
import com.example.flightmanagementsystem.repository.LuggageRepository;
import com.example.flightmanagementsystem.service.LuggageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/luggages")
public class LuggageController {
    private final LuggageService service;

    public LuggageController() {
        this.service = new LuggageService(new LuggageRepository());
    }

    @PostMapping
    @ResponseBody
    public Luggage create(@RequestParam String ticketId, @RequestParam Luggage.Status status) {
        return service.create(ticketId, status);
    }

    @GetMapping
    @ResponseBody
    public List<Luggage> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    @ResponseBody
    public Optional<Luggage> findById(@PathVariable String id) { return service.findById(id); }

    @DeleteMapping("/{id}")
    @ResponseBody
    public boolean delete(@PathVariable String id) { return service.delete(id); }
}
