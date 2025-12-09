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

    // --- BUSINESS VALIDATION ---
    private void validateBusinessRules(NoticeBoard nb, String currentId) {

        // Regula 1: ID Unic (doar la creare)
        if (currentId == null && noticeBoardRepository.existsById(nb.getId())) {
            throw new IllegalArgumentException("NoticeBoard ID " + nb.getId() + " already exists.");
        }

        // Regula 2: Unicitatea Datei (Un singur panou pe zi)
        boolean dateTaken;
        if (currentId == null) {
            dateTaken = noticeBoardRepository.existsByDate(nb.getDate());
        } else {
            dateTaken = noticeBoardRepository.existsByDateAndIdNot(nb.getDate(), currentId);
        }

        if (dateTaken) {
            throw new IllegalArgumentException("A NoticeBoard for date " + nb.getDate() + " already exists.");
        }
    }

    public NoticeBoard save(NoticeBoard nb) {
        validateBusinessRules(nb, null);
        return noticeBoardRepository.save(nb);
    }

    public void updateNoticeBoard(String id, NoticeBoard updated) {
        validateBusinessRules(updated, id);
        updated.setId(id);
        noticeBoardRepository.save(updated);
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
}