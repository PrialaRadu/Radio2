package com.example.radio.practic.repository;

import com.example.radio.practic.domain.Entity;
import com.example.radio.practic.domain.EntityConverter;
import com.example.radio.practic.repository.exceptions.RepositoryExceptions;

import java.io.*;

public class TextFileRepository<T extends Entity> extends AbstractFileRepository<T> {

    protected EntityConverter<T> converter;

    public TextFileRepository(String fileName, EntityConverter<T> converter) {
        super(fileName);
        this.converter = converter;
        try {
            loadFromFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (var item : elements) {
                String asString = converter.toString(item);
                writer.write(asString);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RepositoryExceptions.TextFileException("Error writing to text file");
        }
    }

    @Override
    protected void loadFromFile() {
        File fileObj = new File(file);
        if (!fileObj.exists() || fileObj.length() == 0) {
            elements.clear();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                elements.add(converter.fromString(line));
            }
        } catch (FileNotFoundException e) {
            throw new RepositoryExceptions.TextFileException("The file not found");
        } catch (IOException e) {
            throw new RepositoryExceptions.TextFileException("Error loading from text file");
        }
    }
}