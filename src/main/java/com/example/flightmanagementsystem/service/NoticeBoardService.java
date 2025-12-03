package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.NoticeBoard;
import com.example.flightmanagementsystem.repository.NoticeBoardRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NoticeBoardService {
    private final NoticeBoardRepository repo;

    public NoticeBoardService(NoticeBoardRepository repo) { this.repo = repo; }

    public NoticeBoard create(LocalDate date) {
        NoticeBoard n = new NoticeBoard();
        n.setId(UUID.randomUUID().toString());
        n.setDate(date);
        return repo.save(n);
    }

    public List<NoticeBoard> findAll() { return repo.findAll(); }
    public Optional<NoticeBoard> findById(String id) { return repo.findById(id); }
    public boolean delete(String id) { return repo.deleteById(id); }
    public NoticeBoard save(NoticeBoard n) { return repo.save(n); }
}

