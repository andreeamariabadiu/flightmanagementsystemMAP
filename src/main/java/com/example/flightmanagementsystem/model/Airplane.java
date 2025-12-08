package com.example.flightmanagementsystem.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "airplanes")
public class Airplane implements Identifiable{

    @Id
    @Column(length = 64)
    private String id;

    @Column(nullable = false)
    private int airplaneNumber;

    @ElementCollection
    @CollectionTable(
            name = "airplane_flights",
            joinColumns = @JoinColumn(name = "airplane_id")
    )
    @Column(name = "flight_id")
    private List<String> flights = new ArrayList<>();// Flight IDs

    public Airplane(String id, int airplaneNumber, List flights) {
        this.id = id;
        this.airplaneNumber = airplaneNumber;
        this.flights = flights;
    }

    public Airplane() {}

    // getterss and setters

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

    public List<String> getFlights() {
        return flights;
    }

    public void setFlights(List<String> flights) {
        this.flights = flights;
    }
}
