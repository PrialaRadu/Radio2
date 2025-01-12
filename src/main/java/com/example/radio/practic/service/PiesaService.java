package com.example.radio.practic.service;

import com.example.radio.practic.domain.Piesa;
import com.example.radio.practic.repository.Repository;
import com.example.radio.practic.repository.SQLPiesaRepository;
import com.example.radio.practic.repository.SQLPlaylistRepository;

import java.util.*;

public class PiesaService {
    private final Repository<Piesa> piesaRepository;
    private SQLPlaylistRepository playlistRepository;

    public PiesaService(Repository<Piesa> repository, SQLPlaylistRepository playlistRepository) {
        this.piesaRepository = repository;
        this.playlistRepository = playlistRepository;
    }

    public Piesa getPiesaById(int id) {
        return piesaRepository.findById(id);
    }

    public List<Piesa> getAllPiese() {
        return piesaRepository.findAll();
    }

    public List<Piesa> ordonateFormatieTitlu() {
        List<Piesa> lst = piesaRepository.findAll();

        lst.sort(Comparator.comparing(Piesa::getFormatie).thenComparing(Piesa::getTitlu));

        return lst;
    }

    // 5 exemple piese
    public void addSamplePiese() {
        piesaRepository.add(new Piesa(1, "f1", "t1", "g1", "01:00"));
        piesaRepository.add(new Piesa(2, "f5", "t3", "g2", "03:24"));
        piesaRepository.add(new Piesa(3, "f3", "t7", "g3", "04:03"));
        piesaRepository.add(new Piesa(4, "f3", "t5", "g4", "04:25"));
        piesaRepository.add(new Piesa(5, "f3", "t1", "g5", "05:55"));
    }

    // Metoda pentru a salva un playlist
    public void savePlaylist(String playlistName, List<Piesa> playlist) {
        playlistRepository.savePlaylist(playlistName, playlist);
    }

    public List<Piesa> generatePlaylist() {
        List<Piesa> allPiese = getAllPiese();
        List<Piesa> playlist = new ArrayList<>();
        int totalDuration = 0;

        for (Piesa piesa : allPiese) {
            // Convertim durata piesei în secunde (dacă nu este deja în acest format)
            int piesaDuration = parseDurationToSeconds(piesa.getDurata());

            if (totalDuration + piesaDuration <= 900) { // 900 secunde = 15 minute
                playlist.add(piesa);
                totalDuration += piesaDuration;
            } else {
                break; // Ne oprim dacă depășim 15 minute
            }
        }

        if (playlist.isEmpty()) {
            throw new RuntimeException("Nu există suficiente piese pentru a crea un playlist de minim 15 minute.");
        }

        return playlist;
    }


    // Metodă pentru a converti durata de tip "mm:ss" în secunde
    private int parseDurationToSeconds(String durata) {
        String[] parts = durata.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        return minutes * 60 + seconds;
    }

    public List<Piesa> getPieseFromPlaylist(String playlistName) {
        return playlistRepository.getPieseFromPlaylist(playlistName);
    }

}
