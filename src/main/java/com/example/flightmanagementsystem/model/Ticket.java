package com.example.flightmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tickets")
public class Ticket implements Identifiable {

    @Id
    @Column(length = 64)
    @NotBlank(message = "Id can't be blank")
    private String id;

    // --- RELAȚIE: ManyToOne cu Passenger ---
    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    // --- RELAȚIE: ManyToOne cu Flight ---
    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Column(nullable = false)
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private Double price;

    @Column(nullable = false)
    @NotBlank(message = "Seat number can't be blank")
    private String seatNumber;

    @ElementCollection
    @CollectionTable(name = "ticket_luggages", joinColumns = @JoinColumn(name = "ticket_id"))
    @Column(name = "luggage_id")
    private List<String> luggages = new ArrayList<>();

    public Ticket() {}

    // Getters and Setters
    @Override public String getId() { return id; }
    @Override public void setId(String id) { this.id = id; }

    public Passenger getPassenger() { return passenger; }
    public void setPassenger(Passenger passenger) { this.passenger = passenger; }

    public Flight getFlight() { return flight; }
    public void setFlight(Flight flight) { this.flight = flight; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public List<String> getLuggages() { return luggages; }
    public void setLuggages(List<String> luggages) { this.luggages = luggages; }
}