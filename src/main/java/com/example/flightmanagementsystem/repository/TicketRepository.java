package com.example.flightmanagementsystem.repository;

import com.example.flightmanagementsystem.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {

    // Verifică dacă locul este ocupat la un anumit zbor (pentru creare)
    boolean existsByFlightIdAndSeatNumber(String flightId, String seatNumber);

    // Verifică dacă locul este ocupat de ALTCINEVA la un anumit zbor (pentru editare)
    boolean existsByFlightIdAndSeatNumberAndIdNot(String flightId, String seatNumber, String id);
}