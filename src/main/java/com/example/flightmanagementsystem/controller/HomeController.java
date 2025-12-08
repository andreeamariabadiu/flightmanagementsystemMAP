package com.example.flightmanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Homepage – root URL
    @GetMapping("/")
    public String home() {
        // looks for templates/home.html
        return "home";
    }

    // Optional: /dashboard also goes to home
    @GetMapping("/dashboard")
    public String dashboard() {
        return "home";
    }
}
