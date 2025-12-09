package com.example.flightmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@MappedSuperclass
public abstract class Staff implements Identifiable {

    @Id
    @Column(length = 64)
    @NotBlank(message = "ID can't be blank")
    private String id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Name can't be blank")
    private String name;

    public Staff() {}
    public Staff(String id, String name) { this.id = id; this.name = name; }

    @Override public String getId() { return id; }
    @Override public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
