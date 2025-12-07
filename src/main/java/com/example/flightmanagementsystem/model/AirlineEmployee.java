package com.example.flightmanagementsystem.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AirlineEmployee extends Staff implements Identifiable {
    private String id;
    private String name;
    private List<String> assignments = new ArrayList<>();;
    private Role role;
    private String licenseNumber;
    private LocalDate registrationDate;

    public AirlineEmployee() {}

    public AirlineEmployee(String id, String name, List assignments, Role role, String licenseNumber, LocalDate registrationDate) {
        this.id = id;
        this.name = name;
        this.assignments = assignments;
        this.role = role;
        this.licenseNumber = licenseNumber;
        this.registrationDate = registrationDate;
    }


    // getters and setters
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String airlineEmployeeId) {
        this.id = airlineEmployeeId;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public List<String> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<String> assignments) {
        this.assignments = assignments;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

}
