package com.example.radio.practic.repository;

import com.example.radio.practic.domain.Entity;

import java.util.List;

public interface GenericRepository<T extends Entity> {
    void add(T element);
    T findById(int id);
    List<T> findAll();
    void update(T element);
    void remove(int id);
}

