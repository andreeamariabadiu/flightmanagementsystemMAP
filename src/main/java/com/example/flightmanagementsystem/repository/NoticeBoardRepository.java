package com.example.flightmanagementsystem.repository;

import com.example.flightmanagementsystem.model.NoticeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // IMPORT
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface NoticeBoardRepository extends
        JpaRepository<NoticeBoard, String>,
        JpaSpecificationExecutor<NoticeBoard> { // EXTINDERE

    boolean existsByDate(LocalDate date);
    boolean existsByDateAndIdNot(LocalDate date, String id);
}