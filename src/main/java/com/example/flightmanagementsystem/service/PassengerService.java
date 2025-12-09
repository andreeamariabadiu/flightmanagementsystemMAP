package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Passenger;
import com.example.flightmanagementsystem.repository.PassengerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerService {
    private final PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    public Passenger save(Passenger p) {
        return passengerRepository.save(p);
    }

    public boolean delete(String id) {
        if (passengerRepository.existsById(id)) {
            passengerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Passenger> findAll() {
        return passengerRepository.findAll();
    }

    public Optional<Passenger> findById(String id) {
        return passengerRepository.findById(id);
    }

    public void update(String id, Passenger updated) {
        updated.setId(id);
        passengerRepository.save(updated);
    }
}
