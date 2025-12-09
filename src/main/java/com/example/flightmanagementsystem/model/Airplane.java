package com.example.flightmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "airplanes")
public class Airplane implements Identifiable {

    @Id
    @Column(length = 64)
    @NotBlank(message = "ID can't be blank")
    private String id;

    @Column(nullable = false, unique = true)
    @NotNull(message = "Airplane number is required")
    @Positive(message = "Airplane number must be > 0")
    private Integer airplaneNumber;

    // RELAȚIE: Un avion efectuează mai multe zboruri în timp
    @OneToMany(mappedBy = "airplane", cascade = CascadeType.ALL)
    private List<Flight> flights = new ArrayList<>();

    public Airplane() {}

    public Airplane(String id, Integer airplaneNumber) {
        this.id = id;
        this.airplaneNumber = airplaneNumber;
    }

    @Override public String getId() { return id; }
    @Override public void setId(String id) { this.id = id; }

    public Integer getAirplaneNumber() { return airplaneNumber; }
    public void setAirplaneNumber(Integer airplaneNumber) { this.airplaneNumber = airplaneNumber; }

    public List<Flight> getFlights() { return flights; }
    public void setFlights(List<Flight> flights) { this.flights = flights; }
}