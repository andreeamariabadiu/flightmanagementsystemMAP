package com.example.flightmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "luggages")
public class Luggage implements Identifiable {

    @Id
    @Column(length = 64)
    @NotBlank(message = "Id can't be blank")
    private String id;

    @Column(name = "ticket_id", nullable = false, length = 64)
    @NotBlank(message = "Ticket ID can't be blank")
    private String ticketId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Status must be selected")
    private Status status;

    public enum Status {
        CHECKED_IN,
        LOADED,
        DELIVERED
    }

    public Luggage() {}

    public Luggage(String id, String ticketId, Status status) {
        this.id = id;
        this.ticketId = ticketId;
        this.status = status;
    }

    // Getters and Setters

    @Override
    public String getId() { return id; }

    @Override
    public void setId(String id) { this.id = id; }

    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}