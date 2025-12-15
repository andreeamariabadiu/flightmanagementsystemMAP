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
        System.out.println("--- 🧹 Curățare bază de date... ---");
        try {
            luggageRepository.deleteAll();
            ticketRepository.deleteAll();
            flightAssignmentRepository.deleteAll();
            flightRepository.deleteAll();
            airplaneRepository.deleteAll();
            noticeBoardRepository.deleteAll();
            passengerRepository.deleteAll();
            airlineEmployeeRepository.deleteAll();
            airportEmployeeRepository.deleteAll();
        } catch (Exception e) {
            System.out.println("--- Baza de date era deja goală sau a apărut o eroare minoră: " + e.getMessage());
        }

        System.out.println("--- 🚀 Inițializare cu DATE REALISTE ---");

        Random random = new Random();

        // 1. AIRPLANES (Flota Reală)
        // Folosim modele reale de avioane și coduri de înregistrare
        String[] planeModels = {"Boeing 737-800", "Airbus A320neo", "Boeing 787 Dreamliner", "Airbus A350-900", "Embraer E190"};
        List<Airplane> airplanes = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Airplane a = new Airplane();
            // ID: AP-B737, AP-A320 etc. (un prefix + index)
            a.setId(String.format("AP-%03d", i + 1));
            a.setAirplaneNumber(1000 + i * 5); // Număr intern de flotă
            airplanes.add(airplaneRepository.save(a));
        }

        // 2. NOTICE BOARDS (Următoarele 10 zile)
        List<NoticeBoard> noticeBoards = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 10; i++) {
            NoticeBoard nb = new NoticeBoard();
            nb.setId(String.format("NB-%s", today.plusDays(i).toString())); // ID bazat pe dată: NB-2023-12-01
            nb.setDate(today.plusDays(i));
            noticeBoards.add(noticeBoardRepository.save(nb));
        }

        // 3. PASSENGERS (Nume Reale)
        String[] firstNames = {"John", "Emma", "Michael", "Sophia", "William", "Olivia", "James", "Ava", "Alexander", "Isabella", "David", "Mia"};
        String[] lastNames = {"Smith", "Johnson", "Brown", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia"};

        List<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Passenger p = new Passenger();
            p.setId(String.format("PAX-%04d", 2000 + i));

            String fullName = firstNames[i % firstNames.length] + " " + lastNames[i % lastNames.length];
            p.setName(fullName);

            p.setCurrency(i % 3 == 0 ? "EUR" : (i % 3 == 1 ? "USD" : "GBP"));
            passengers.add(passengerRepository.save(p));
        }

        // 4. AIRLINE EMPLOYEES (Crew - Piloti si Insotitori)
        String[] crewNames = {"Capt. Maverick", "Capt. Sully", "Amelia Earhart", "Charles Lindbergh", "Bessie Coleman", "Howard Hughes", "Chuck Yeager", "Buzz Aldrin", "Neil Armstrong", "Yuri Gagarin"};
        List<AirlineEmployee> airlineEmployees = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            AirlineEmployee ae = new AirlineEmployee();
            ae.setId(String.format("CREW-%03d", i + 1));
            ae.setName(crewNames[i]);
            ae.setLicenseNumber(String.format("LIC-%05d", 80000 + i * 123)); // Licențe unice
            ae.setRegistrationDate(LocalDate.of(2018, 1, 1).plusMonths(i * 3));

            if (i < 3) ae.setRole(Role.PILOT);
            else if (i < 6) ae.setRole(Role.CO_PILOT);
            else ae.setRole(Role.FLIGHT_ATTENDANT);

            airlineEmployees.add(airlineEmployeeRepository.save(ae));
        }

        // 5. AIRPORT EMPLOYEES (Staff la sol)
        String[] staffNames = {"Sarah Connor", "Ellen Ripley", "Han Solo", "Leia Organa", "Luke Skywalker", "Marty McFly", "Doc Brown", "Tony Stark", "Bruce Wayne", "Clark Kent"};
        for (int i = 0; i < 10; i++) {
            AirportEmployee ape = new AirportEmployee();
            ape.setId(String.format("STAFF-%03d", i + 1));
            ape.setName(staffNames[i]);
            ape.setDesignation(Designation.STAFF); // Sau MANAGER, depinde ce ai în Enum

            if (i % 3 == 0) ape.setDepartment(Department.OPERATIONS);
            else if (i % 3 == 1) ape.setDepartment(Department.MAINTENANCE);
            else ape.setDepartment(Department.CUSTOMER_SERVICE);

            airportEmployeeRepository.save(ape);
        }

        // 6. FLIGHTS (Zboruri Reale)
        String[] destinations = {"London Heathrow (LHR)", "Paris Charles de Gaulle (CDG)", "New York JFK", "Dubai International (DXB)", "Tokyo Haneda (HND)", "Frankfurt (FRA)", "Amsterdam Schiphol (AMS)", "Singapore Changi (SIN)", "Los Angeles (LAX)", "Sydney Kingsford Smith (SYD)"};
        List<Flight> flights = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Airplane ap = airplanes.get(i % airplanes.size());
            NoticeBoard nb = noticeBoards.get(i % noticeBoards.size()); // Un zbor pe zi

            LocalDateTime depTime = nb.getDate().atTime(8 + i, 30); // 08:30, 09:30...
            LocalDateTime arrTime = depTime.plusHours(2 + random.nextInt(8)); // Zboruri între 2 și 10 ore

            Flight f = new Flight();
            // ID Zbor: RO301, BA550, LH402...
            String flightCode = (i % 2 == 0 ? "RO" : "LH") + String.format("%03d", 300 + i * 15);
            f.setId(flightCode);

            f.setFlightName("Flight to " + destinations[i]);
            f.setDepartureTime(depTime);
            f.setArrivalTime(arrTime);

            // Status variat
            if (i == 0) f.setStatus(Status.SCHEDULED);
            else if (i == 1) f.setStatus(Status.DELAYED);
            else if (i == 2) f.setStatus(Status.CANCELLED);
            else if (i < 5) f.setStatus(Status.ON_TIME);
            else f.setStatus(Status.SCHEDULED);

            f.setAirplane(ap);
            f.setNoticeBoard(nb);

            flights.add(flightRepository.save(f));
        }

        // 7. TICKETS (Bilete)
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < 12; i++) { // 12 bilete pentru 10 zboruri
            Passenger pax = passengers.get(i % passengers.size());
            Flight flight = flights.get(i % flights.size());

            Ticket t = new Ticket();
            t.setId(String.format("TKT-%06d", 900000 + i * 7)); // ID Bilet lung: TKT-900007
            t.setPrice(150.0 + random.nextInt(300)); // Preț între 150 și 450

            // Locuri: 12A, 14C, etc.
            String seat = (10 + i) + String.valueOf((char)('A' + (i % 6)));
            t.setSeatNumber(seat);

            t.setPassenger(pax);
            t.setFlight(flight);

            tickets.add(ticketRepository.save(t));
        }

        // 8. FLIGHT ASSIGNMENTS (Echipaj pe zboruri)
        for (int i = 0; i < 10; i++) {
            Flight flight = flights.get(i);
            // Punem un Pilot (index 0-2) și un Flight Attendant (index 6-9) pe fiecare zbor
            AirlineEmployee pilot = airlineEmployees.get(i % 3);
            AirlineEmployee attendant = airlineEmployees.get(6 + (i % 4));

            // Assignment Pilot
            FlightAssignment fa1 = new FlightAssignment();
            fa1.setId(String.format("ASN-%04d-P", i));
            fa1.setFlight(flight);
            fa1.setEmployee(pilot);
            flightAssignmentRepository.save(fa1);

            // Assignment Attendant
            FlightAssignment fa2 = new FlightAssignment();
            fa2.setId(String.format("ASN-%04d-A", i));
            fa2.setFlight(flight);
            fa2.setEmployee(attendant);
            flightAssignmentRepository.save(fa2);
        }

        // 9. LUGGAGE (Bagaje)
        for (int i = 0; i < 12; i++) {
            Ticket t = tickets.get(i);

            // Doar unii pasageri au bagaj
            if (i % 3 != 0) {
                Luggage l = new Luggage();
                l.setId(String.format("BAG-%05d", 50000 + i * 9));

                if (i % 4 == 0) l.setStatus(Luggage.Status.LOADED);
                else if (i % 4 == 1) l.setStatus(Luggage.Status.DELIVERED);
                else l.setStatus(Luggage.Status.CHECKED_IN);

                l.setTicket(t);
                luggageRepository.save(l);
            }
        }

        System.out.println("--- ✅ Initializare completă cu DATE REALE! ---");
    }
}