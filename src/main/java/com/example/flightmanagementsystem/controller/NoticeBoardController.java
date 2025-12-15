package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.NoticeBoard;
import com.example.flightmanagementsystem.service.NoticeBoardService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort; // IMPORT
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/noticeboards")
public class NoticeBoardController {

    private final NoticeBoardService service;

    public NoticeBoardController(NoticeBoardService service) {
        this.service = service;
    }

    // --- METODA MODIFICATĂ PENTRU SORTARE ---
    @GetMapping
    public String list(
            Model model,
            @RequestParam(defaultValue = "date") String sortBy, // Implicit cronologic
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        List<NoticeBoard> noticeboards = service.findAll(sort);

        model.addAttribute("noticeboards", noticeboards);

        // Parametrii pentru View
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");

        return "noticeboards/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("noticeBoard", new NoticeBoard());
        return "noticeboards/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("noticeBoard") NoticeBoard nb, BindingResult res) {
        if (res.hasErrors()) return "noticeboards/form";
        try {
            service.save(nb);
        } catch (Exception e) {
            res.reject("global.error", e.getMessage());
            return "noticeboards/form";
        }
        return "redirect:/noticeboards";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        NoticeBoard nb = service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("noticeBoard", nb);
        return "noticeboards/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable String id, @Valid @ModelAttribute("noticeBoard") NoticeBoard nb, BindingResult res) {
        if (res.hasErrors()) return "noticeboards/form";
        try {
            service.update(id, nb);
        } catch (Exception e) {
            res.reject("global.error", e.getMessage());
            return "noticeboards/form";
        }
        return "redirect:/noticeboards";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        service.delete(id);
        return "redirect:/noticeboards";
    }

    @GetMapping("/{id}/details")
    public String details(@PathVariable String id, Model model) {
        NoticeBoard nb = service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("noticeBoard", nb);
        return "noticeboards/details";
    }
}