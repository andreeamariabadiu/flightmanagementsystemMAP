package com.example.flightmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "flight_assignments")
public class FlightAssignment implements Identifiable {

    @Id
    @Column(length = 64)
    @NotBlank(message = "ID can't be blank")
    private String id;

    // --- RELAȚIE: ManyToOne cu Flight ---
    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    // --- RELAȚIE: ManyToOne cu AirlineEmployee ---
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private AirlineEmployee employee;

    public FlightAssignment() {}

    // Getters and Setters
    @Override public String getId() { return id; }
    @Override public void setId(String id) { this.id = id; }

    public Flight getFlight() { return flight; }
    public void setFlight(Flight flight) { this.flight = flight; }

    public AirlineEmployee getEmployee() { return employee; }
    public void setEmployee(AirlineEmployee employee) { this.employee = employee; }
}