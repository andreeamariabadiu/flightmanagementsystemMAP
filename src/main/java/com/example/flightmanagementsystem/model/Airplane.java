package com.example.flightmanagementsystem.model;
import java.util.ArrayList;
import java.util.List;

public class Airplane implements Identifiable{
    private String id;
    private int airplaneNumber;
    private List<String> flights = new ArrayList<>();// Flight IDs

    public Airplane(String id, int airplaneNumber, List flights) {
        this.id = id;
        this.airplaneNumber = airplaneNumber;
        this.flights = flights;
    }

    // getterss and setters

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public int getAirplaneNumber() {
        return airplaneNumber;
    }

    public void setAirplaneNumber(int airplaneNumber) {
        this.airplaneNumber = airplaneNumber;
    }

    public List<String> getFlights() {
        return flights;
    }

    public void setFlights(List<String> flights) {
        this.flights = flights;
    }
}
