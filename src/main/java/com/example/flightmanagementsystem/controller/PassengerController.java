package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.Passenger;
import com.example.flightmanagementsystem.repository.PassengerRepository;
import com.example.flightmanagementsystem.service.PassengerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/passengers")
public class PassengerController {
    private final PassengerService service;

    public PassengerController() {
        this.service = new PassengerService(new PassengerRepository());
    }

    @PostMapping
    @ResponseBody
    public Passenger create(@RequestParam String name, @RequestParam String currency) {
        return service.create(name, currency);
    }

    @GetMapping
    @ResponseBody
    public List<Passenger> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    @ResponseBody
    public Optional<Passenger> findById(@PathVariable String id) { return service.findById(id); }

    @DeleteMapping("/{id}")
    @ResponseBody
    public boolean delete(@PathVariable String id) { return service.delete(id); }
}
