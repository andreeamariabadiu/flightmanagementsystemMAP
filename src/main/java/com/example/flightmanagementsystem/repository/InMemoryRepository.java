package com.example.flightmanagementsystem.repository;

import com.example.flightmanagementsystem.model.Identifiable;

import java.util.*;

public class InMemoryRepository<T extends Identifiable> implements BaseRepository<T> {
    protected final Map<String, T> storage = new HashMap<>();

    @Override
    public T save(T entity) {
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<T> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public boolean deleteById(String id) {
        return storage.remove(id) != null;
    }
}
