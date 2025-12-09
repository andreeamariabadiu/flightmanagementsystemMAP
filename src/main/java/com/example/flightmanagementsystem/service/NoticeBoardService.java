package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.NoticeBoard;
import com.example.flightmanagementsystem.repository.NoticeBoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoticeBoardService {
    private final NoticeBoardRepository noticeBoardRepository;

    public NoticeBoardService(NoticeBoardRepository noticeBoardRepository) {
        this.noticeBoardRepository = noticeBoardRepository;
    }

    public NoticeBoard save(NoticeBoard nb) {
        return noticeBoardRepository.save(nb);
    }

    public boolean delete(String id) {
        if (noticeBoardRepository.existsById(id)) {
            noticeBoardRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<NoticeBoard> findAll() {
        return noticeBoardRepository.findAll();
    }

    public Optional<NoticeBoard> findById(String id) {
        return noticeBoardRepository.findById(id);
    }

    public void update(String id, NoticeBoard updated) {
        updated.setId(id);
        noticeBoardRepository.save(updated);
    }
}
