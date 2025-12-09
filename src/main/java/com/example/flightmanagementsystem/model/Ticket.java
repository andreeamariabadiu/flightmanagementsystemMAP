package com.example.flightmanagementsystem.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tickets")
public class Ticket implements Identifiable {

    @Id
    @Column(length = 64)
    private String id;

    @Column(nullable = false)
    private String passengerId;

    @Column(nullable = false)
    private String flightId;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String seatNumber;

    @ElementCollection
    @CollectionTable(
            name = "ticket_luggages",
            joinColumns = @JoinColumn(name = "ticket_id")
    )
    @Column(name = "luggage_id")
    private List<String> luggages = new ArrayList<>();

    public Ticket() {}

    public Ticket(String id, String passengerId, String flightId, double price, String seatNumber) {
        this.id = id;
        this.passengerId = passengerId;
        this.flightId = flightId;
        this.price = price;
        this.seatNumber = seatNumber;
    }

    @Override
    public String getId() { return id; }

    @Override
    public void setId(String id) { this.id = id; }

    public String getPassengerId() { return passengerId; }
    public void setPassengerId(String passengerId) { this.passengerId = passengerId; }

    public String getFlightId() { return flightId; }
    public void setFlightId(String flightId) { this.flightId = flightId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public List<String> getLuggages() { return luggages; }
    public void setLuggages(List<String> luggages) { this.luggages = luggages; }
}
