package com.example.flightmanagementsystem.config;

import com.example.flightmanagementsystem.model.*;
import com.example.flightmanagementsystem.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AirplaneRepository airplaneRepository;
    private final NoticeBoardRepository noticeBoardRepository;
    private final PassengerRepository passengerRepository;
    private final AirlineEmployeeRepository airlineEmployeeRepository;
    private final AirportEmployeeRepository airportEmployeeRepository;
    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;
    private final FlightAssignmentRepository flightAssignmentRepository;
    private final LuggageRepository luggageRepository;

    public DataInitializer(AirplaneRepository airplaneRepository, NoticeBoardRepository noticeBoardRepository, PassengerRepository passengerRepository, AirlineEmployeeRepository airlineEmployeeRepository, AirportEmployeeRepository airportEmployeeRepository, FlightRepository flightRepository, TicketRepository ticketRepository, FlightAssignmentRepository flightAssignmentRepository, LuggageRepository luggageRepository) {
        this.airplaneRepository = airplaneRepository;
        this.noticeBoardRepository = noticeBoardRepository;
        this.passengerRepository = passengerRepository;
        this.airlineEmployeeRepository = airlineEmployeeRepository;
        this.airportEmployeeRepository = airportEmployeeRepository;
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
        this.flightAssignmentRepository = flightAssignmentRepository;
        this.luggageRepository = luggageRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. CURĂȚENIE (Ștergem copilul înainte de părinte pentru a evita erori de Foreign Key)
        System.out.println("--- Curățare bază de date... ---");
        luggageRepository.deleteAll();
        ticketRepository.deleteAll();
        flightAssignmentRepository.deleteAll();
        flightRepository.deleteAll();

        airplaneRepository.deleteAll();
        noticeBoardRepository.deleteAll();
        passengerRepository.deleteAll();
        airlineEmployeeRepository.deleteAll();
        airportEmployeeRepository.deleteAll();

        System.out.println("--- Inițializare date (10+ înregistrări per tabel)... ---");

        // 2. CREARE PĂRINȚI

        // --- Airplanes ---
        List<Airplane> airplanes = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Airplane a = new Airplane();
            a.setId("AP-" + i);
            a.setAirplaneNumber(100 + i);
            airplanes.add(airplaneRepository.save(a));
        }

        // --- NoticeBoards ---
        List<NoticeBoard> noticeBoards = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 10; i++) {
            NoticeBoard nb = new NoticeBoard();
            nb.setId("NB-" + i);
            nb.setDate(today.plusDays(i));
            noticeBoards.add(noticeBoardRepository.save(nb));
        }

        // --- Passengers ---
        List<Passenger> passengers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Passenger p = new Passenger();
            p.setId("PAX-" + i);
            p.setName("Passenger " + i);
            p.setCurrency(i % 2 == 0 ? "USD" : "EUR");
            passengers.add(passengerRepository.save(p));
        }

        // --- Airline Employees (Crew) ---
        List<AirlineEmployee> airlineEmployees = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            AirlineEmployee ae = new AirlineEmployee();
            ae.setId("CREW-" + i);
            ae.setName("Flight Crew " + i);
            ae.setLicenseNumber("LIC-" + (5000 + i));
            ae.setRegistrationDate(LocalDate.of(2022, 1, 1).plusMonths(i));

            // Logica corectă folosind Enum-ul tău ROLE
            if (i <= 3) {
                ae.setRole(Role.PILOT);
            } else if (i <= 6) {
                ae.setRole(Role.CO_PILOT);
            } else {
                ae.setRole(Role.FLIGHT_ATTENDANT); // Corectat din CREW
            }

            airlineEmployees.add(airlineEmployeeRepository.save(ae));
        }

        // --- Airport Employees (Staff Sol) ---
        for (int i = 1; i <= 10; i++) {
            AirportEmployee ape = new AirportEmployee();
            ape.setId("GND-" + i);
            ape.setName("Ground Staff " + i);

            // Folosim Enum-urile Designation și Department corecte
            ape.setDesignation(Designation.STAFF);

            // Rotim departamentele
            if (i % 3 == 0) ape.setDepartment(Department.OPERATIONS); // Corectat din LOGISTICS
            else if (i % 3 == 1) ape.setDepartment(Department.MAINTENANCE);
            else ape.setDepartment(Department.CUSTOMER_SERVICE);

            airportEmployeeRepository.save(ape);
        }

        // 3. CREARE COPII (Legături)

        // --- Flights ---
        List<Flight> flights = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Airplane ap = airplanes.get(i);
            NoticeBoard nb = noticeBoards.get(i);

            LocalDateTime depTime = nb.getDate().atTime(10 + (i % 5), 0);
            LocalDateTime arrTime = depTime.plusHours(2);

            Flight f = new Flight();
            f.setId("FL-" + (100 + i));
            f.setFlightName("Flight to City " + i);
            f.setDepartureTime(depTime);
            f.setArrivalTime(arrTime);

            // Folosim Enum-ul STATUS corect
            f.setStatus(Status.SCHEDULED);

            // RELAȚIILE IMPORTANTE (Setăm obiectele)
            f.setAirplane(ap);
            f.setNoticeBoard(nb);

            flights.add(flightRepository.save(f));
        }

        // --- Tickets ---
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Passenger pax = passengers.get(i);
            Flight flight = flights.get(i);

            Ticket t = new Ticket();
            t.setId("TKT-" + (500 + i));
            t.setPrice(150.0 + (i * 10));
            t.setSeatNumber("12" + (char)('A' + (i % 6)));

            // RELAȚII
            t.setPassenger(pax);
            t.setFlight(flight);

            tickets.add(ticketRepository.save(t));
        }

        // --- Flight Assignments ---
        for (int i = 0; i < 10; i++) {
            Flight flight = flights.get(i);
            AirlineEmployee emp = airlineEmployees.get(i);

            FlightAssignment fa = new FlightAssignment();
            fa.setId("ASN-" + (900 + i));

            // RELAȚII
            fa.setFlight(flight);
            fa.setEmployee(emp);

            flightAssignmentRepository.save(fa);
        }

        // --- Luggage ---
        for (int i = 0; i < 10; i++) {
            Ticket t = tickets.get(i);

            Luggage l = new Luggage();
            l.setId("LUG-" + (8000 + i));

            // Folosim Status din Luggage (CHECKED_IN este standard în modelul tău)
            l.setStatus(Luggage.Status.CHECKED_IN);

            // RELAȚII
            l.setTicket(t);

            luggageRepository.save(l);
        }

        System.out.println("--- Initializare completă: Datele au fost generate cu succes! ---");
    }
}