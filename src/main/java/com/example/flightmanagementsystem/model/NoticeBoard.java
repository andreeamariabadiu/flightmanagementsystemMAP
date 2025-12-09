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

    // --- RELAȚIE OneToMany: Un panou are mai multe zboruri ---
    @OneToMany(mappedBy = "noticeBoard", cascade = CascadeType.ALL)
    private List<Flight> flightsOfTheDay = new ArrayList<>();

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

    public List<Flight> getFlightsOfTheDay() { return flightsOfTheDay; }
    public void setFlightsOfTheDay(List<Flight> flightsOfTheDay) { this.flightsOfTheDay = flightsOfTheDay; }
}