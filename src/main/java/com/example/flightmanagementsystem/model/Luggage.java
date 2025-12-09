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

    // RELAȚIE: ManyToOne cu Ticket
    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

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

    @Override public String getId() { return id; }
    @Override public void setId(String id) { this.id = id; }

    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}