package com.example.flightmanagementsystem.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "airplanes")
public class Airplane implements Identifiable{

    @Id
    @Column(length = 64)
    @NotBlank(message = "ID can't be blank")
    private String id;

    @Column(nullable = false, unique = true)
    @Positive(message = "Airplane number must be > 0")
    private int airplaneNumber;

    @OneToMany(mappedBy = "airplane", cascade = CascadeType.ALL)
    private List<Flight> flights = new ArrayList<>();// Flight IDs

    public Airplane(String id, int airplaneNumber, List flights) {
        this.id = id;
        this.airplaneNumber = airplaneNumber;
        this.flights = flights;
    }

    public Airplane() {}

    // getters and setters

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public int getAirplaneNumber() {
        return airplaneNumber;
    }

    public void setAirplaneNumber(int airplaneNumber) {
        this.airplaneNumber = airplaneNumber;
    }

    public List<Flight> getFlights() { return flights; }

    public void setFlights(List<Flight> flights) { this.flights = flights; }
}
