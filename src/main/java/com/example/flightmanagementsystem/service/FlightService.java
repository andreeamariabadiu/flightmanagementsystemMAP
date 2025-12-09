package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Airplane;
import com.example.flightmanagementsystem.model.Flight;
import com.example.flightmanagementsystem.model.NoticeBoard;
import com.example.flightmanagementsystem.repository.AirplaneRepository;
import com.example.flightmanagementsystem.repository.FlightRepository;
import com.example.flightmanagementsystem.repository.NoticeBoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private final AirplaneRepository airplaneRepository;
    private final NoticeBoardRepository noticeBoardRepository;

    public FlightService(FlightRepository flightRepository,
                         AirplaneRepository airplaneRepository,
                         NoticeBoardRepository noticeBoardRepository) {
        this.flightRepository = flightRepository;
        this.airplaneRepository = airplaneRepository;
        this.noticeBoardRepository = noticeBoardRepository;
    }

    // --- BUSINESS LOGIC ---
    private void validateBusinessRules(Flight flight, String currentId) {
        // Regula 1: ID Unic (Doar la creare)
        if (currentId == null && flightRepository.existsById(flight.getId())) {
            throw new IllegalArgumentException("Flight ID " + flight.getId() + " is already in use.");
        }

        // Regula 2: Cronologie (Plecarea înainte de Sosire)
        if (flight.getDepartureTime() != null && flight.getArrivalTime() != null) {
            if (flight.getArrivalTime().isBefore(flight.getDepartureTime())) {
                throw new IllegalArgumentException("Arrival time cannot be before Departure time.");
            }
            if (flight.getArrivalTime().isEqual(flight.getDepartureTime())) {
                throw new IllegalArgumentException("Arrival and Departure cannot be exactly the same time.");
            }
        }
    }

    // --- CREATE ---
    public Flight createFlight(Flight flight, String airplaneId, String noticeBoardId) {

        // 1. Căutăm și setăm Avionul
        Airplane realPlane = airplaneRepository.findById(airplaneId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Airplane ID: " + airplaneId));
        flight.setAirplane(realPlane);

        // 2. Căutăm și setăm NoticeBoard-ul
        NoticeBoard realBoard = noticeBoardRepository.findById(noticeBoardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid NoticeBoard ID: " + noticeBoardId));
        flight.setNoticeBoard(realBoard);

        // 3. Validăm regulile de business
        validateBusinessRules(flight, null);

        return flightRepository.save(flight);
    }

    // --- UPDATE ---
    public void updateFlight(String id, Flight update, String airplaneId, String noticeBoardId) {
        // La update, trebuie să re-căutăm relațiile, deoarece userul le putea schimba în formular
        Airplane realPlane = airplaneRepository.findById(airplaneId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Airplane ID: " + airplaneId));

        NoticeBoard realBoard = noticeBoardRepository.findById(noticeBoardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid NoticeBoard ID: " + noticeBoardId));

        update.setId(id); // Ne asigurăm că păstrăm ID-ul original
        update.setAirplane(realPlane);
        update.setNoticeBoard(realBoard);

        validateBusinessRules(update, id);
        flightRepository.save(update);
    }

    // --- STANDARD CRUD ---
    public boolean delete(String id) {
        if (flightRepository.existsById(id)) {
            flightRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    public Optional<Flight> findById(String id) {
        return flightRepository.findById(id);
    }
}