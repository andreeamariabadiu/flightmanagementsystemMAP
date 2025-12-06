package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Airplane;
import com.example.flightmanagementsystem.repository.AirplaneRepository;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class AirplaneService {
    private final AirplaneRepository airplaneRepository;

    public AirplaneService(AirplaneRepository airplaneRepository) {
        this.airplaneRepository = airplaneRepository;
    }

    public Airplane save(Airplane a) {
        return airplaneRepository.save(a);
    }

    public boolean delete(String id) {
        return airplaneRepository.deleteById(id);
    }

    public List<Airplane> findAll() {
        return airplaneRepository.findAll();
    }

    public Optional<Airplane> findById(String id) {
        return airplaneRepository.findById(id);
    }
}
