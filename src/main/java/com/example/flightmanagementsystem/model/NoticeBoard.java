package com.example.flightmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notice_boards")
public class NoticeBoard implements Identifiable {

    @Id
    @Column(length = 64)
    @NotBlank(message = "ID can't be blank")
    private String id;

    @Column(nullable = false, unique = true)
    @NotNull(message = "Date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ElementCollection
    @CollectionTable(
            name = "noticeboard_flights",
            joinColumns = @JoinColumn(name = "noticeboard_id")
    )
    @Column(name = "flight_id")
    private List<String> flightsOfTheDay = new ArrayList<>(); // Flight IDs

    public NoticeBoard() {}

    public NoticeBoard(String id, LocalDate date) {
        this.id = id;
        this.date = date;
    }

    // Getters and Setters

    @Override
    public String getId() { return id; }

    @Override
    public void setId(String id) { this.id = id; }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }

    public List<String> getFlightsOfTheDay() { return flightsOfTheDay; }

    public void setFlightsOfTheDay(List<String> flightsOfTheDay) { this.flightsOfTheDay = flightsOfTheDay; }
}