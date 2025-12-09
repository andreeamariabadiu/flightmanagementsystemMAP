package com.example.flightmanagementsystem.repository;

import com.example.flightmanagementsystem.model.NoticeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, String> {

    boolean existsByDate(LocalDate date);
    boolean existsByDateAndIdNot(LocalDate date, String id);
}