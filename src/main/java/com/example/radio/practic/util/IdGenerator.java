package com.example.radio.practic.util;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private static final String FILE_NAME = "id_generator.txt";
    private final AtomicInteger currentId; // gestioneaza incrementarea id-urilor

    public IdGenerator() {
        currentId = new AtomicInteger(readLastId());
    }

    private int readLastId() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) { // pt citirea fisierului
            String line = br.readLine();
            return line != null ? Integer.parseInt(line) : 99; // daca fisierul este gol, incepe de la 99
        } catch (IOException e) {
            return 99; // in cazul unei erori, incepe de la 99
        }
    }

    public int generateId() {
        int id = currentId.incrementAndGet();
        saveCurrentId(id);
        return id;
    }

    // scrie in fisier noul ID generat pt a-l stoca
    private void saveCurrentId(int id) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            bw.write(String.valueOf(id));
        } catch (IOException e) {
            e.printStackTrace();  // pt traseul exceptiei
        }
    }
}