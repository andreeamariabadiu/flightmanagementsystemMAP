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

    @Column(nullable = false)
    @NotBlank(message = "Passenger ID can't be blank")
    private String passengerId;

    @Column(nullable = false)
    @NotBlank(message = "Flight ID can't be blank")
    private String flightId;

    @Column(nullable = false)
    @NotNull(message = "Price is required") // Folosim NotNull pentru numere
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private Double price; // Wrapper class Double pentru a permite null in formular inainte de validare

    @Column(nullable = false)
    @NotBlank(message = "Seat number can't be blank")
    private String seatNumber;

    @ElementCollection
    @CollectionTable(
            name = "ticket_luggages",
            joinColumns = @JoinColumn(name = "ticket_id")
    )
    @Column(name = "luggage_id")
    private List<String> luggages = new ArrayList<>();

    public Ticket() {}

    public Ticket(String id, String passengerId, String flightId, Double price, String seatNumber) {
        this.id = id;
        this.passengerId = passengerId;
        this.flightId = flightId;
        this.price = price;
        this.seatNumber = seatNumber;
    }

    // Getters and Setters

    @Override
    public String getId() { return id; }
    @Override
    public void setId(String id) { this.id = id; }

    public String getPassengerId() { return passengerId; }
    public void setPassengerId(String passengerId) { this.passengerId = passengerId; }

    public String getFlightId() { return flightId; }
    public void setFlightId(String flightId) { this.flightId = flightId; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public List<String> getLuggages() { return luggages; }
    public void setLuggages(List<String> luggages) { this.luggages = luggages; }
}