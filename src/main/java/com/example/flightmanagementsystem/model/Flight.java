package com.example.flightmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "flights")
public class Flight implements Identifiable {

    @Id
    @Column(length = 64)
    @NotBlank(message = "ID can't be blank")
    private String id;

    @NotBlank(message = "Flight name can't be blank")
    @Column(nullable = false)
    private String flightName;

    // RELAȚIE PĂRINTE: ManyToOne cu NoticeBoard
    @ManyToOne
    @JoinColumn(name = "notice_board_id", nullable = false)
    private NoticeBoard noticeBoard;

    // RELAȚIE PĂRINTE: ManyToOne cu Airplane
    @ManyToOne
    @JoinColumn(name = "airplane_id", nullable = false)
    private Airplane airplane;

    // RELAȚIE COPIL: OneToMany cu Tickets
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();

    // RELAȚIE COPIL: OneToMany cu FlightAssignments
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private List<FlightAssignment> flightAssignments = new ArrayList<>();

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

    public Flight() {}

    // Getters and Setters
    @Override public String getId() { return id; }
    @Override public void setId(String id) { this.id = id; }

    public String getFlightName() { return flightName; }
    public void setFlightName(String flightName) { this.flightName = flightName; }

    public NoticeBoard getNoticeBoard() { return noticeBoard; }
    public void setNoticeBoard(NoticeBoard noticeBoard) { this.noticeBoard = noticeBoard; }

    public Airplane getAirplane() { return airplane; }
    public void setAirplane(Airplane airplane) { this.airplane = airplane; }

    public List<Ticket> getTickets() { return tickets; }
    public void setTickets(List<Ticket> tickets) { this.tickets = tickets; }

    public List<FlightAssignment> getFlightAssignments() { return flightAssignments; }
    public void setFlightAssignments(List<FlightAssignment> flightAssignments) { this.flightAssignments = flightAssignments; }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}