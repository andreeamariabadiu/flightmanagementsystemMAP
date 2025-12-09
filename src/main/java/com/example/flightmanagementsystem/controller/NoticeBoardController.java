package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.NoticeBoard;
import com.example.flightmanagementsystem.service.NoticeBoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/noticeboards")
public class NoticeBoardController {

    private final NoticeBoardService noticeBoardService;

    public NoticeBoardController(NoticeBoardService noticeBoardService) {
        this.noticeBoardService = noticeBoardService;
    }

    // List all noticeboards
    @GetMapping
    public String listNoticeBoards(Model model) {
        model.addAttribute("noticeboards", noticeBoardService.findAll());
        return "noticeboards/index";
    }

    // Form to create new noticeboard
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("noticeBoard", new NoticeBoard());
        return "noticeboards/form";
    }

    // Handle create (POST). flights parameter is comma-separated IDs (optional)
    @PostMapping
    public String createNoticeBoard(
            @RequestParam String id,
            @RequestParam String date,                // expected format: yyyy-MM-dd from <input type="date">
            @RequestParam(required = false) String flights // optional comma-separated
    ) {
        LocalDate ld = LocalDate.parse(date);
        NoticeBoard nb = new NoticeBoard(id, ld);

        if (flights != null && !flights.isBlank()) {
            List<String> list = Arrays.stream(flights.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            nb.setFlightsOfTheDay(list);
        }

        noticeBoardService.save(nb);
        return "redirect:/noticeboards";
    }

    // Delete
    @PostMapping("/{id}/delete")
    public String deleteNoticeBoard(@PathVariable String id) {
        noticeBoardService.delete(id);
        return "redirect:/noticeboards";
    }

    // Edit form
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        NoticeBoard nb = noticeBoardService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("noticeBoard", nb);
        // also prepare a comma-separated string for the form input
        String flightsCsv = String.join(", ", nb.getFlightsOfTheDay());
        model.addAttribute("flightsCsv", flightsCsv);
        return "noticeboards/form";
    }

    // Details
    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        NoticeBoard nb = noticeBoardService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("noticeBoard", nb);
        return "noticeboards/details";
    }
}
