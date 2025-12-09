package com.example.flightmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "flights")
public class Flight implements Identifiable{

    @NotBlank(message = "ID can't be blank")
    @Id
    @Column(length = 64)
    private String id;

    @NotBlank(message = "Flight name can't be blank")
    @Column(nullable = false)
    private String flightName;

    @NotBlank(message = "NoticeBoard ID can't be blank")
    @Column(nullable = false)
    private String NoticeBoardId;

    @NotBlank(message = "Airplane ID can't be blank")
    @Column(nullable = false)
    private String AirplaneId;

    @ElementCollection
    @CollectionTable(
            name = "flight_tickets",
            joinColumns = @JoinColumn(name = "flight_id")
    )
    @Column(name = "ticket_id")
    private List<String> tickets = new ArrayList<>(); // Ticket IDs

    @ElementCollection
    @CollectionTable(
            name = "flight_assignments_linksflight",
            joinColumns = @JoinColumn(name = "flight_id")
    )
    @Column(name = "assignment_id")
    private List<String> flightAssignments = new ArrayList<>(); // flight assignment IDs

    @NotNull(message = "Departure time is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(nullable = false)
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @NotNull(message = "Status must be selected")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public Flight(String id, String flightName, String NoticeBoardId, String AirplaneId, List tickets, List flightAssignments, LocalDateTime ArrivalTime, LocalDateTime departureTime, Status status) {
        this.id = id;
        this.flightName = flightName;
        this.NoticeBoardId = NoticeBoardId;
        this.AirplaneId = AirplaneId;
        this.tickets = tickets;
        this.flightAssignments = flightAssignments;
        this.arrivalTime = ArrivalTime;
        this.departureTime = departureTime;
        this.status = status;
    }

    public Flight() {}

    // getters and setters

    @Override
    public String getId() {
        return id;
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

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
