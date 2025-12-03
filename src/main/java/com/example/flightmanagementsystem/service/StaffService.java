package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.Staff;
import com.example.flightmanagementsystem.repository.StaffRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class StaffService {

    private final StaffRepository repo;

    public StaffService(StaffRepository repo) {
        this.repo = repo;
    }

    public Staff create(String name) {
        Staff staff = new Staff() {
            // fiind abstractă, aici ar trebui să creezi subclase concrete (AirlineEmployee / AirportEmployee)
        };
        staff.setId(UUID.randomUUID().toString());
        staff.setName(name);
        return repo.save(staff);
    }

    public List<Staff> findAll() {
        return repo.findAll();
    }

    public Optional<Staff> findById(String id) {
        return repo.findById(id);
    }

    public boolean delete(String id) {
        return repo.deleteById(id);
    }

    public Staff save(Staff staff) {
        if (staff.getId() == null) {
            staff.setId(UUID.randomUUID().toString());
        }
        return repo.save(staff);
    }
}
