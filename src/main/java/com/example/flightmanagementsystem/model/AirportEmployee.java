package com.example.flightmanagementsystem.model;

public class AirportEmployee extends Staff implements Identifiable{
    private String id;
    private String name;
    private Designation designation;
    private Department department;

    public AirportEmployee(String id, String name, Designation designation, Department department) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.department = department;
    }

    public AirportEmployee() {}


    // getters and setters

    @Override
    public String getId() { return id; }

    @Override
    public void setId(String id) { this.id = id; }


    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Designation getDesignation() { return designation; }

    public void setDesignation(Designation designation) { this.designation = designation; }

    public Department getDepartment() { return department; }

    public void setDepartment(Department department) { this.department = department; }



}
