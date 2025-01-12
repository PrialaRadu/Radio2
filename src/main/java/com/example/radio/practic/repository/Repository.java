package com.example.radio.practic.repository;

import com.example.radio.practic.domain.Entity;
import com.example.radio.practic.repository.exceptions.RepositoryExceptions;

import java.util.ArrayList;
import java.util.List;

public class Repository<T extends Entity> implements GenericRepository<T> {
    protected final List<T> elements = new ArrayList<>();

    @Override
    public void add(T element) {
        for (T e : elements) {
            if (e.getId() == element.getId()) {
                throw new RepositoryExceptions.DuplicateIDException("ID-ul exista deja in repository.");
            }
        }
        elements.add(element);
    }

    @Override
    public T findById(int id) {
        for (T entity : elements) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(elements);
    }

    @Override
    public void update(T element) {
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getId() == element.getId()) {
                elements.set(i, element);
                return;
            }
        }
        throw new RepositoryExceptions.EntityNotFoundException("Elementul cu ID-ul specificat nu a fost gasit.");
    }

    @Override
    public void remove(int id) {
        T element = findById(id);
        if (element != null) {
            elements.remove(element);
        } else {
            throw new RepositoryExceptions.EntityNotFoundException("Elementul cu ID-ul specificat nu a fost gasit.");
        }
    }
}
