package com.example.flightmanagementsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "luggages")
public class Luggage implements Identifiable {

    @Id
    @Column(length = 64)
    private String id;

    @Column(name = "ticket_id", nullable = false, length = 64)
    private String ticketId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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

    @Override
    public String getId() { return id; }

    @Override
    public void setId(String id) { this.id = id; }

    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
