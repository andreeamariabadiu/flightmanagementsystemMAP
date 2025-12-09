package com.example.flightmanagementsystem.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "passengers")
public class Passenger implements Identifiable {

    @Id
    @Column(length = 64)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String currency;

    @ElementCollection
    @CollectionTable(
            name = "passenger_tickets",
            joinColumns = @JoinColumn(name = "passenger_id")
    )
    @Column(name = "ticket_id")
    private List<String> tickets = new ArrayList<>(); // Ticket IDs

    public Passenger() {}

    public Passenger(String id, String name, String currency) {
        this.id = id;
        this.name = name;
        this.currency = currency;
    }

    @Override
    public String getId() { return id; }

    @Override
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public List<String> getTickets() { return tickets; }
    public void setTickets(List<String> tickets) { this.tickets = tickets; }
}
