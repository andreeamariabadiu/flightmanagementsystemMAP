package com.example.flightmanagementsystem.model;

import jakarta.validation.constraints.*;

import jakarta.persistence.*;

@Entity
@Table(name = "airport_employees")
public class AirportEmployee extends Staff implements Identifiable{

    @NotNull(message = "Designation must be selected")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Designation designation;

    @NotNull(message = "Department must be selected")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department department;

    public AirportEmployee(String id, String name, Designation designation, Department department) {
        super(id, name);
        this.designation = designation;
        this.department = department;
    }

    public AirportEmployee() {}


    // getters and setters

    public Designation getDesignation() { return designation; }

    public void setDesignation(Designation designation) { this.designation = designation; }

    public Department getDepartment() { return department; }

    public void setDepartment(Department department) { this.department = department; }



}
