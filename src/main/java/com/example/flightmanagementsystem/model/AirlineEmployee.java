package com.example.flightmanagementsystem.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class AirlineEmployee extends Staff implements Identifiable {
    private String airlineEmployeeId;
    private List<String> assignments = new ArrayList<>();;
    private Role role;
    private String licenseNumber;
    private LocalDate registrationDate;

    public AirlineEmployee() {}

    public AirlineEmployee(String id, String name, String employeeId, List assignments, Role role) {
        super(id, name);
        this.airlineEmployeeId = employeeId;
        this.assignments = assignments;
        this.role = role;
    }


    // getters and setters

    public String getAirlineEmployeeId() {
        return airlineEmployeeId;
    }

    public void setAirlineEmployeeId(String airlineEmployeeId) {
        this.airlineEmployeeId = airlineEmployeeId;
    }

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
