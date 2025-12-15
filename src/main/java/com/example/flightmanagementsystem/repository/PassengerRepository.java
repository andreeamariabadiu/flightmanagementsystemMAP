package com.example.flightmanagementsystem.repository;

import com.example.flightmanagementsystem.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // IMPORT
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends
        JpaRepository<Passenger, String>,
        JpaSpecificationExecutor<Passenger> { // EXTINDERE

}