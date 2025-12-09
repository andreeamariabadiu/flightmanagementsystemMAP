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

    private void validateBusinessRules(Flight flight, String currentId) {
        // ID Unic
        if (currentId == null && flightRepository.existsById(flight.getId())) {
            throw new IllegalArgumentException("Flight ID " + flight.getId() + " is already in use.");
        }
        // Cronologie
        if (flight.getDepartureTime() != null && flight.getArrivalTime() != null) {
            if (flight.getArrivalTime().isBefore(flight.getDepartureTime())) {
                throw new IllegalArgumentException("Arrival time cannot be before Departure time.");
            }
        }
    }

    // CREATE
    public Flight createFlight(Flight flight, String airplaneId, String noticeBoardId) {
        // 1. Set Airplane
        Airplane realPlane = airplaneRepository.findById(airplaneId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Airplane ID: " + airplaneId));
        flight.setAirplane(realPlane);

        // 2. Set NoticeBoard
        NoticeBoard realBoard = noticeBoardRepository.findById(noticeBoardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid NoticeBoard ID: " + noticeBoardId));
        flight.setNoticeBoard(realBoard);

        validateBusinessRules(flight, null);
        return flightRepository.save(flight);
    }

    // UPDATE
    public void updateFlight(String id, Flight update, String airplaneId, String noticeBoardId) {
        Airplane realPlane = airplaneRepository.findById(airplaneId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Airplane ID"));

        NoticeBoard realBoard = noticeBoardRepository.findById(noticeBoardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid NoticeBoard ID"));

        update.setAirplane(realPlane);
        update.setNoticeBoard(realBoard);
        update.setId(id);

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

    public List<Flight> findAll() { return flightRepository.findAll(); }
    public Optional<Flight> findById(String id) { return flightRepository.findById(id); }
}