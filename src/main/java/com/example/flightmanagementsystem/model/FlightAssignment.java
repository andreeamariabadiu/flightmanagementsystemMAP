package com.example.flightmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "flight_assignments")
public class FlightAssignment implements Identifiable{

    @NotBlank(message = "ID can't be blank")
    @Id
    @Column(length = 64)
    public String id;

    @NotBlank(message = "Flight ID can't be blank")
    @Column(nullable = false)
    public String flightId;

    @NotBlank(message = "Staff ID can't be blank")
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
