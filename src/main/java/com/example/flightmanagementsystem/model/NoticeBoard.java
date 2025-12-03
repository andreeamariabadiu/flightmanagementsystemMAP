package com.example.flightmanagementsystem.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NoticeBoard implements Identifiable {
    private String id;
    private LocalDate date;
    private List<String> flightsOfTheDay = new ArrayList<>(); // Flight IDs

    public NoticeBoard() {}

    public NoticeBoard(String id, LocalDate date) {
        this.id = id;
        this.date = date;
    }

    @Override public String getId() { return id; }
    @Override public void setId(String id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public List<String> getFlightsOfTheDay() { return flightsOfTheDay; }
    public void setFlightsOfTheDay(List<String> flightsOfTheDay) { this.flightsOfTheDay = flightsOfTheDay; }
}
