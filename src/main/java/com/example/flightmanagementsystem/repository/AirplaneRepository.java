package com.example.flightmanagementsystem.repository;

import com.example.flightmanagementsystem.model.Airplane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirplaneRepository extends JpaRepository<Airplane, String> {
}
