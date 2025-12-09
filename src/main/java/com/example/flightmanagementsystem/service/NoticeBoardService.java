package com.example.flightmanagementsystem.service;

import com.example.flightmanagementsystem.model.NoticeBoard;
import com.example.flightmanagementsystem.repository.NoticeBoardRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeBoardService {
    private final NoticeBoardRepository repository;

    public NoticeBoardService(NoticeBoardRepository repository) {
        this.repository = repository;
    }

    private void validateRules(NoticeBoard nb, String currentId) {
        if (currentId == null && repository.existsById(nb.getId())) {
            throw new IllegalArgumentException("ID exists");
        }
        boolean dateTaken;
        if (currentId == null) dateTaken = repository.existsByDate(nb.getDate());
        else dateTaken = repository.existsByDateAndIdNot(nb.getDate(), currentId);

        if (dateTaken) throw new IllegalArgumentException("A schedule for this date already exists.");
    }

    public NoticeBoard save(NoticeBoard nb) {
        validateRules(nb, null);
        return repository.save(nb);
    }

    public void update(String id, NoticeBoard nb) {
        validateRules(nb, id);
        nb.setId(id);
        repository.save(nb);
    }

    public boolean delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
    public List<NoticeBoard> findAll() { return repository.findAll(); }
    public Optional<NoticeBoard> findById(String id) { return repository.findById(id); }
}