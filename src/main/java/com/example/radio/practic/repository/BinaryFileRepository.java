package com.example.radio.practic.repository;

import com.example.radio.practic.domain.Entity;
import com.example.radio.practic.repository.exceptions.RepositoryExceptions;

import java.io.*;
import java.util.List;

public class BinaryFileRepository<T extends Entity> extends AbstractFileRepository<T> {
    public BinaryFileRepository(String fileName) {
        super(fileName);
        try {
            loadFromFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void writeToFile() {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(elements);

        } catch (IOException exc) {
            throw new RepositoryExceptions.BinaryFileException("Eroare la salvarea fisierului");
        } finally {
            try {
                assert oos != null;
                oos.close();
            } catch (IOException e) {
                throw new RepositoryExceptions.BinaryFileException(e.getMessage());
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void loadFromFile() {
        File fileObj = new File(file);
        if (!fileObj.exists() || fileObj.length() == 0) {
            elements.clear();
            return;
        }

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileObj));
            elements.clear();
            elements.addAll((List<T>) ois.readObject());
        } catch (FileNotFoundException exc){
            throw new RuntimeException("The file not found", exc);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error loading from binary file", e);
        }
    }
}

