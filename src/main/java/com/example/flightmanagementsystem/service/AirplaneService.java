package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Airplane;
import com.example.flightmanagementsystem.repository.AirplaneRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        // 1. Validare ID Unic
        if (currentId == null && airplaneRepository.existsById(a.getId())) {
            throw new IllegalArgumentException("Airplane ID " + a.getId() + " is already in use.");
        }
        // 2. Validare Număr Unic
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

    public List<Airplane> findAll() { return airplaneRepository.findAll(); }

    // --- METODA COMPLEXĂ: FILTRARE + SORTARE ---
    public List<Airplane> searchAirplanes(
            String id,
            Integer airplaneNumber,
            Integer minFlights,
            Integer maxFlights,
            Sort sort
    ) {
        // 1. Construim Specificația (Filtrele SQL)
        Specification<Airplane> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtru ID (Text - Like)
            if (id != null && !id.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("id")), "%" + id.toLowerCase() + "%"));
            }

            // Filtru Număr Avion (Exact)
            if (airplaneNumber != null) {
                predicates.add(cb.equal(root.get("airplaneNumber"), airplaneNumber));
            }

            // Filtru Număr Zboruri (Min) - Folosim cb.size() pe lista 'flights'
            if (minFlights != null) {
                predicates.add(cb.greaterThanOrEqualTo(cb.size(root.get("flights")), minFlights));
            }

            // Filtru Număr Zboruri (Max)
            if (maxFlights != null) {
                predicates.add(cb.lessThanOrEqualTo(cb.size(root.get("flights")), maxFlights));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // 2. Gestionăm Sortarea
        // Caz special: Sortare după mărimea listei (flightCount) - se face în Java
        if (sort.getOrderFor("flightCount") != null) {
            // Luăm datele filtrate din DB
            List<Airplane> filteredList = airplaneRepository.findAll(spec);

            Sort.Order order = sort.getOrderFor("flightCount");
            Comparator<Airplane> comparator = Comparator.comparingInt(a -> a.getFlights().size());

            if (order.isDescending()) {
                comparator = comparator.reversed();
            }

            return filteredList.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
        }

        // Caz standard: Sortare direct în baza de date
        return airplaneRepository.findAll(spec, sort);
    }

    // Metoda findAll simplă delegatează cu null
    public List<Airplane> findAll(Sort sort) {
        return searchAirplanes(null, null, null, null, sort);
    }
}