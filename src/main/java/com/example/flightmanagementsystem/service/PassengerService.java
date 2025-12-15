package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Passenger;
import com.example.flightmanagementsystem.repository.PassengerRepository;
import org.springframework.data.domain.Sort; // IMPORT
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PassengerService {
    private final PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    private void validateBusinessRules(Passenger passenger, String currentId) {
        if (currentId == null && passengerRepository.existsById(passenger.getId())) {
            throw new IllegalArgumentException("Passenger ID " + passenger.getId() + " is already in use.");
        }
    }

    public Passenger save(Passenger p) {
        validateBusinessRules(p, null);
        return passengerRepository.save(p);
    }

    public void updatePassenger(String id, Passenger updated) {
        validateBusinessRules(updated, id);
        updated.setId(id);
        passengerRepository.save(updated);
    }

    public boolean delete(String id) {
        if (passengerRepository.existsById(id)) {
            passengerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Passenger> findById(String id) {
        return passengerRepository.findById(id);
    }

    // --- METODE PENTRU SORTARE ---

    // 1. Pentru DataInitializer
    public List<Passenger> findAll() {
        return passengerRepository.findAll();
    }

    // 2. Pentru Controller (cu sortare custom pentru ticketCount)
    public List<Passenger> findAll(Sort sort) {
        // Dacă sortăm după numărul de bilete, o facem în Java
        if (sort.getOrderFor("ticketCount") != null) {
            List<Passenger> passengers = passengerRepository.findAll();

            Sort.Order order = sort.getOrderFor("ticketCount");
            Comparator<Passenger> comparator = Comparator.comparingInt(p -> p.getTickets().size());

            if (order.isDescending()) {
                comparator = comparator.reversed();
            }

            return passengers.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
        }

        // Altfel, lăsăm baza de date
        return passengerRepository.findAll(sort);
    }
}