package com.example.radio.practic.repository;

import com.example.radio.practic.domain.Entity;

public abstract class AbstractFileRepository<T extends Entity> extends Repository<T> {
    protected String file;

    public AbstractFileRepository(String fileName) {
        this.file = fileName;
    }

    @Override
    public void add(T entity) {
        super.add(entity);
        writeToFile();
    }

    @Override
    public void remove(int id) {
        super.remove(id);
        writeToFile();
    }

    @Override
    public void update(T updatedItem) {
        super.update(updatedItem);
        writeToFile();
    }

    protected abstract void writeToFile();

    protected abstract void loadFromFile();
}

