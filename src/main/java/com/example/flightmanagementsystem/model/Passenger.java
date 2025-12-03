package com.example.flightmanagementsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Passenger implements Identifiable {
    private String id;
    private String name;
    private String currency;
    private List<String> tickets = new ArrayList<>(); // Ticket IDs

    public Passenger() {}

    public Passenger(String id, String name, String currency) {
        this.id = id;
        this.name = name;
        this.currency = currency;
    }

    @Override public String getId() { return id; }
    @Override public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public List<String> getTickets() { return tickets; }
    public void setTickets(List<String> tickets) { this.tickets = tickets; }
}
