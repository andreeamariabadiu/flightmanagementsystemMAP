package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.NoticeBoard;
import com.example.flightmanagementsystem.repository.NoticeBoardRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoticeBoardService {
    private final NoticeBoardRepository repository;

    public NoticeBoardService(NoticeBoardRepository repository) {
        this.repository = repository;
    }

    private void validateRules(NoticeBoard nb, String currentId) {
        if (currentId == null && repository.existsById(nb.getId())) {
            throw new IllegalArgumentException("ID exists");
        }
        boolean dateTaken;
        if (currentId == null) dateTaken = repository.existsByDate(nb.getDate());
        else dateTaken = repository.existsByDateAndIdNot(nb.getDate(), currentId);

        if (dateTaken) throw new IllegalArgumentException("A schedule for this date already exists.");
    }

    public NoticeBoard save(NoticeBoard nb) {
        validateRules(nb, null);
        return repository.save(nb);
    }

    public void update(String id, NoticeBoard nb) {
        validateRules(nb, id);
        nb.setId(id);
        repository.save(nb);
    }

    public boolean delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<NoticeBoard> findById(String id) { return repository.findById(id); }

    public List<NoticeBoard> findAll() { return repository.findAll(); }

    // --- METODA COMPLEXĂ: CĂUTARE + FILTRARE + SORTARE ---
    public List<NoticeBoard> searchNoticeBoards(
            String id,
            LocalDate minDate,
            LocalDate maxDate,
            Integer minFlights, // Parametru NOU
            Integer maxFlights, // Parametru NOU
            Sort sort
    ) {
        // 1. Construim Specificația (Filtrele SQL)
        Specification<NoticeBoard> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtru ID (Parțial)
            if (id != null && !id.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("id")), "%" + id.toLowerCase() + "%"));
            }

            // Filtru Dată (Min)
            if (minDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), minDate));
            }

            // Filtru Dată (Max)
            if (maxDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), maxDate));
            }

            // Filtru Număr Zboruri (Min) - folosind mărimea listei flightsOfTheDay
            if (minFlights != null) {
                predicates.add(cb.greaterThanOrEqualTo(cb.size(root.get("flightsOfTheDay")), minFlights));
            }

            // Filtru Număr Zboruri (Max)
            if (maxFlights != null) {
                predicates.add(cb.lessThanOrEqualTo(cb.size(root.get("flightsOfTheDay")), maxFlights));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // 2. Gestionăm Sortarea
        // Caz special: Sortare după numărul de zboruri (flightCount) - în memorie
        if (sort.getOrderFor("flightCount") != null) {
            // Luăm datele filtrate din DB
            List<NoticeBoard> filteredList = repository.findAll(spec);

            Sort.Order order = sort.getOrderFor("flightCount");
            Comparator<NoticeBoard> comparator = Comparator.comparingInt(nb -> nb.getFlightsOfTheDay().size());

            if (order.isDescending()) {
                comparator = comparator.reversed();
            }

            return filteredList.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
        }

        // Caz standard: Sortare DB
        return repository.findAll(spec, sort);
    }

    // Compatibilitate
    public List<NoticeBoard> findAll(Sort sort) {
        return searchNoticeBoards(null, null, null, null, null, sort);
    }
}