package com.example.flightmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "airline_employees")
public class AirlineEmployee extends Staff implements Identifiable {

    @NotEmpty(message = "Employee must have at least one assignment")
    @ElementCollection
    @CollectionTable(
            name = "employee_assignments",
            joinColumns = @JoinColumn(name = "employee_id")
    )
    @Column(name = "assignment")
    private List<String> assignments = new ArrayList<>();;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Role must be selected")
    private Role role;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "License number can't be blank")
    private String licenseNumber;

    @Column(nullable = false)
    @NotNull(message = "Registration date is required")
    @PastOrPresent(message = "Work registration date can't be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
