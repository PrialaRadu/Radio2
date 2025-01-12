package com.example.radio.practic.service;

import com.example.radio.practic.domain.Piesa;
import com.example.radio.practic.repository.Repository;
import com.example.radio.practic.repository.SQLPiesaRepository;

import java.util.*;

public class PiesaService {
    private final Repository<Piesa> piesaRepository;

    public PiesaService(Repository<Piesa> piesaRepository) {
        this.piesaRepository = piesaRepository;
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

    public void savePlaylist(String playlistName, List<Piesa> playlist) {
        if (piesaRepository instanceof SQLPiesaRepository) {
            SQLPiesaRepository sqlRepository = (SQLPiesaRepository) piesaRepository;
            sqlRepository.savePlaylist(playlistName, playlist);
        }
    }

    public List<Piesa> generatePlaylist() {
        List<Piesa> allPiese = getAllPiese();
        List<Piesa> playlist = new ArrayList<>();
        Set<String> lastFormatieUsed = new HashSet<>();
        Set<String> lastGenUsed = new HashSet<>();
        int totalDurationInMinutes = 0;

        Random rand = new Random();

        while (totalDurationInMinutes < 15 && !allPiese.isEmpty()) {
            Piesa selectedPiesa = allPiese.remove(rand.nextInt(allPiese.size()));

            if (!lastFormatieUsed.contains(selectedPiesa.getFormatie()) &&
                    !lastGenUsed.contains(selectedPiesa.getGen())) {

                playlist.add(selectedPiesa);
                lastFormatieUsed.add(selectedPiesa.getFormatie());
                lastGenUsed.add(selectedPiesa.getGen());

                String[] durataSplit = selectedPiesa.getDurata().split(":");
                int durataMinute = Integer.parseInt(durataSplit[0]) * 60 + Integer.parseInt(durataSplit[1]);
                totalDurationInMinutes += durataMinute;
            }
        }

        return playlist;
    }
}
