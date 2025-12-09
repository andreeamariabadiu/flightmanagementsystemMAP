package com.example.flightmanagementsystem.repository;

import com.example.flightmanagementsystem.model.NoticeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, String> {
}
