package com.example.flightmanagementsystem.controller;

import com.example.flightmanagementsystem.model.NoticeBoard;
import com.example.flightmanagementsystem.service.NoticeBoardService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/noticeboards")
public class NoticeBoardController {

    private final NoticeBoardService noticeBoardService;

    public NoticeBoardController(NoticeBoardService noticeBoardService) {
        this.noticeBoardService = noticeBoardService;
    }

    // 1. Listare
    @GetMapping
    public String listNoticeBoards(Model model) {
        model.addAttribute("noticeboards", noticeBoardService.findAll());
        return "noticeboards/index";
    }

    // 2. Formular Creare
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("noticeBoard", new NoticeBoard());
        return "noticeboards/form";
    }

    // 3. Procesare Creare (POST)
    @PostMapping
    public String createNoticeBoard(
            @Valid @ModelAttribute("noticeBoard") NoticeBoard noticeBoard,
            BindingResult bindingResult
    ) {
        // Validări standard (@NotBlank, @NotNull)
        if (bindingResult.hasErrors()) {
            return "noticeboards/form";
        }

        // Validări Business (ID unic, Dată unică)
        try {
            noticeBoardService.save(noticeBoard);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            return "noticeboards/form";
        }

        return "redirect:/noticeboards";
    }

    // 4. Formular Editare
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        NoticeBoard nb = noticeBoardService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("noticeBoard", nb);
        return "noticeboards/form";
    }

    // 5. Procesare Editare (POST)
    @PostMapping("/{id}")
    public String updateNoticeBoard(
            @PathVariable String id,
            @Valid @ModelAttribute("noticeBoard") NoticeBoard noticeBoard,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "noticeboards/form";
        }

        try {
            noticeBoardService.updateNoticeBoard(id, noticeBoard);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("global.error", e.getMessage());
            return "noticeboards/form";
        }

        return "redirect:/noticeboards";
    }

    // 6. Ștergere
    @PostMapping("/{id}/delete")
    public String deleteNoticeBoard(@PathVariable String id) {
        noticeBoardService.delete(id);
        return "redirect:/noticeboards";
    }

    // 7. Detalii
    @GetMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        NoticeBoard nb = noticeBoardService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("noticeBoard", nb);
        return "noticeboards/details";
    }
}