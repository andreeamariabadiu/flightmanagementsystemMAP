package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Airplane;
import com.example.flightmanagementsystem.model.Flight;
import com.example.flightmanagementsystem.model.NoticeBoard;
import com.example.flightmanagementsystem.repository.AirplaneRepository;
import com.example.flightmanagementsystem.repository.FlightRepository;
import com.example.flightmanagementsystem.repository.NoticeBoardRepository;
import org.springframework.data.domain.Sort; // IMPORT NOU
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

    private void validateBusinessRules(Flight flight, String currentId) {
        if (currentId == null && flightRepository.existsById(flight.getId())) {
            throw new IllegalArgumentException("Flight ID " + flight.getId() + " is already in use.");
        }
        if (flight.getDepartureTime() != null && flight.getArrivalTime() != null) {
            if (flight.getArrivalTime().isBefore(flight.getDepartureTime())) {
                throw new IllegalArgumentException("Arrival time cannot be before Departure time.");
            }
            if (flight.getArrivalTime().isEqual(flight.getDepartureTime())) {
                throw new IllegalArgumentException("Arrival and Departure cannot be exactly the same time.");
            }
        }
    }

    public Flight createFlight(Flight flight, String airplaneId, String noticeBoardId) {
        Airplane realPlane = airplaneRepository.findById(airplaneId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Airplane ID: " + airplaneId));
        flight.setAirplane(realPlane);

        NoticeBoard realBoard = noticeBoardRepository.findById(noticeBoardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid NoticeBoard ID: " + noticeBoardId));
        flight.setNoticeBoard(realBoard);

        validateBusinessRules(flight, null);
        return flightRepository.save(flight);
    }

    public void updateFlight(String id, Flight update, String airplaneId, String noticeBoardId) {
        Airplane realPlane = airplaneRepository.findById(airplaneId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Airplane ID: " + airplaneId));

        NoticeBoard realBoard = noticeBoardRepository.findById(noticeBoardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid NoticeBoard ID: " + noticeBoardId));

        update.setId(id);
        update.setAirplane(realPlane);
        update.setNoticeBoard(realBoard);

        validateBusinessRules(update, id);
        flightRepository.save(update);
    }

    public boolean delete(String id) {
        if (flightRepository.existsById(id)) {
            flightRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Flight> findById(String id) { return flightRepository.findById(id); }

    // --- METODE PENTRU SORTARE ---

    // 1. Folosită de DataInitializer
    public List<Flight> findAll() { return flightRepository.findAll(); }

    // 2. Folosită de Controller
    public List<Flight> findAll(Sort sort) {
        return flightRepository.findAll(sort);
    }
}