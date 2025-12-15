package com.example.flightmanagementsystem.repository;

import com.example.flightmanagementsystem.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // IMPORT
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends
        JpaRepository<Ticket, String>,
        JpaSpecificationExecutor<Ticket> { // EXTINDERE

    boolean existsByFlight_IdAndSeatNumber(String flightId, String seatNumber);
    boolean existsByFlight_IdAndSeatNumberAndIdNot(String flightId, String seatNumber, String id);
}