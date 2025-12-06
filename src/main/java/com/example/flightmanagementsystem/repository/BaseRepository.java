package com.example.flightmanagementsystem.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaseRepository<T> {
    T save(T entity); // upsert
    List<T> findAll();
    Optional<T> findById(String id);
    boolean deleteById(String id);
}
