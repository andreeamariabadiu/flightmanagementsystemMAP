package com.example.flightmanagementsystem.model;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Staff implements Identifiable {

    @Id
    @Column(length = 64)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    public Staff() {}
    public Staff(String id, String name) { this.id = id; this.name = name; }

    @Override public String getId() { return id; }
    @Override public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
