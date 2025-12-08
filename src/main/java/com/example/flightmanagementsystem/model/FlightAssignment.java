package com.example.flightmanagementsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "flight_assignments")
public class FlightAssignment implements Identifiable{

    @Id
    @Column(length = 64)
    public String id;

    @Column(nullable = false)
    public String flightId;

    @Column(nullable = false)
    public String staffId;

    public FlightAssignment(String id, String flightId, String staffId) {
        this.id = id;
        this.flightId = flightId;
        this.staffId = staffId;
    }

    public FlightAssignment() {}

    //getters and setters

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getFlightId() {
        return flightId;
    }
    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getStaffId() {
        return staffId;
    }
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

}
