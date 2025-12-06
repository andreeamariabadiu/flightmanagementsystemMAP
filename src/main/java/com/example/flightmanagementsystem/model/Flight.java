package com.example.flightmanagementsystem.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Flight implements Identifiable{
    private String id;
    private String flightName;
    private String NoticeBoardId;
    private String AirplaneId;
    private List<String> tickets = new ArrayList<>(); // Ticket IDs
    private List<String> flightAssignments = new ArrayList<>(); // flight assignment IDs
    private LocalDate departureTime;
    private LocalDate arrivalTime;
    private Status status;

    public Flight(String id, String flightName, String NoticeBoardId, String AirplaneId, List tickets, List flightAssignments) {
        this.id = id;
        this.flightName = flightName;
        this.NoticeBoardId = NoticeBoardId;
        this.AirplaneId = AirplaneId;
        this.tickets = tickets;
        this.flightAssignments = flightAssignments;
    }

    // getters and setters

    @Override
    public String getId() {
        return "";
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getFlightName() {
        return flightName;
    }
    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }

    public String getNoticeBoardId() {
        return NoticeBoardId;
    }
    public void setNoticeBoardId(String NoticeBoardId) {
        this.NoticeBoardId = NoticeBoardId; }

    public String getAirplaneId() {
        return AirplaneId;
    }

    public void setAirplaneId(String AirplaneId) {
        this.AirplaneId = AirplaneId;
    }

    public List<String> getTickets() {
        return tickets;
    }
    public void setTickets(List<String> tickets) {
        this.tickets = tickets;
    }

    public List<String> getFlightAssignments() {
        return flightAssignments;
    }
    public void setFlightAssignments(List<String> flightAssignments) {
        this.flightAssignments = flightAssignments;
    }

    public LocalDate getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDate departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDate getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDate arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
