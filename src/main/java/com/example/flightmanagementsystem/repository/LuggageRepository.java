package com.example.flightmanagementsystem.repository;

import com.example.flightmanagementsystem.model.Luggage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LuggageRepository extends JpaRepository<Luggage, String> {
    // JpaRepository oferă deja existsById, save, findAll etc.
}