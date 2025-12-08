package com.example.flightmanagementsystem.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "airline_employees")
public class AirlineEmployee extends Staff implements Identifiable {

    @ElementCollection
    @CollectionTable(
            name = "employee_assignments",
            joinColumns = @JoinColumn(name = "employee_id")
    )
    @Column(name = "assignment")
    private List<String> assignments = new ArrayList<>();;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String licenseNumber;

    @Column(nullable = false)
    private LocalDate registrationDate;

    public AirlineEmployee() {}

    public AirlineEmployee(String id, String name, List assignments, Role role, String licenseNumber, LocalDate registrationDate) {
        super(id, name);
        this.assignments = assignments;
        this.role = role;
        this.licenseNumber = licenseNumber;
        this.registrationDate = registrationDate;
    }


    // getters and setters

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
