package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Airplane;
import com.example.flightmanagementsystem.repository.AirplaneRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AirplaneService {
    private final AirplaneRepository airplaneRepository;

    public AirplaneService(AirplaneRepository airplaneRepository) {
        this.airplaneRepository = airplaneRepository;
    }

    private void validateRules(Airplane a, String currentId) {
        // 1. ID Unic
        if (currentId == null && airplaneRepository.existsById(a.getId())) {
            throw new IllegalArgumentException("Airplane ID " + a.getId() + " is already in use.");
        }
        // 2. Număr Unic
        boolean numberTaken;
        if (currentId == null) {
            numberTaken = airplaneRepository.existsByAirplaneNumber(a.getAirplaneNumber());
        } else {
            numberTaken = airplaneRepository.existsByAirplaneNumberAndIdNot(a.getAirplaneNumber(), currentId);
        }
        if (numberTaken) {
            throw new IllegalArgumentException("Airplane Number " + a.getAirplaneNumber() + " is already assigned.");
        }
    }

    public Airplane save(Airplane a) {
        validateRules(a, null);
        return airplaneRepository.save(a);
    }

    public void updateAirplane(String id, Airplane update) {
        validateRules(update, id);
        update.setId(id);
        airplaneRepository.save(update);
    }

    public boolean delete(String id) {
        if (airplaneRepository.existsById(id)) {
            airplaneRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Airplane> findById(String id) { return airplaneRepository.findById(id); }

    // --- METODE PENTRU SORTARE ---

    public List<Airplane> findAll() { return airplaneRepository.findAll(); }

    // METODA SPECIALĂ PENTRU SORTARE
    public List<Airplane> findAll(Sort sort) {
        // Verificăm dacă utilizatorul a cerut sortarea după "flightCount" (numărul de zboruri)
        // JPA nu poate sorta direct după mărimea unei liste, așa că o facem în Java
        if (sort.getOrderFor("flightCount") != null) {
            List<Airplane> airplanes = airplaneRepository.findAll();

            Sort.Order order = sort.getOrderFor("flightCount");
            Comparator<Airplane> comparator = Comparator.comparingInt(a -> a.getFlights().size());

            // Inversăm dacă e DESC
            if (order.isDescending()) {
                comparator = comparator.reversed();
            }

            return airplanes.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
        }

        // Pentru restul câmpurilor (id, airplaneNumber), lăsăm baza de date să sorteze
        return airplaneRepository.findAll(sort);
    }
}