package com.example.radio;

import com.example.radio.practic.service.PiesaService;
import com.example.radio.practic.domain.Piesa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class HelloController {
    private PiesaService service;

    // listview pentru cerinta1
    @FXML
    private ListView<Piesa> listViewPieseOrdonate;

    // listview pentru cerinta2
    @FXML
    private ListView<Piesa> listViewPieseFiltrate;

    // pt cerinta3
    @FXML
    private TextField playlistNameField;

    @FXML
    private TextField searchField;

    private ObservableList<Piesa> observablePieseOrdonate = FXCollections.observableArrayList();
    private ObservableList<Piesa> observablePieseFiltrate = FXCollections.observableArrayList();

    public void setService(PiesaService service) {
        this.service = service;
        initialize();
    }

    @FXML
    private void initialize() {
        if (service != null) {
            // cerinta 1
            List<Piesa> sortedList = ordonateFormatieTitlu();
            observablePieseOrdonate.setAll(sortedList);
            listViewPieseOrdonate.setItems(observablePieseOrdonate);

            // cerinta 2
            listViewPieseFiltrate.setItems(observablePieseFiltrate);
        }
    }

    public List<Piesa> ordonateFormatieTitlu() {
        return service.ordonateFormatieTitlu();
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            showAlert("Cautare invalida", "Textul de cautare este gol!", "Introduceti un text valid pentru a cauta.", AlertType.WARNING);
            return;
        }

        // filtrare
        List<Piesa> filteredList = service.getAllPiese().stream()
                .filter(p -> p.getFormatie().toLowerCase().contains(searchText) ||
                        p.getTitlu().toLowerCase().contains(searchText) ||
                        p.getDurata().toLowerCase().contains(searchText) ||
                        p.getGen().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        if (filteredList.isEmpty()) {
            showAlert("Niciun rezultat", "Cautarea nu a returnat rezultate.", "Nicio piesa nu corespunde cautarii.", AlertType.INFORMATION);
        } else {
            observablePieseFiltrate.setAll(filteredList);
        }
    }


    @FXML
    private void handleReset() {
        searchField.clear();
        observablePieseFiltrate.clear();
    }

    @FXML
    private void handleCreatePlaylist() {
        try {
            String playlistName = playlistNameField.getText();  // Numele listei de redare din căsuța editabilă
            if (playlistName.isEmpty()) {
                showAlert("Eroare", "Numele listei de redare", "Vă rugăm să introduceți un nume pentru playlist.", Alert.AlertType.ERROR);
                return;
            }

            List<Piesa> playlist = service.generatePlaylist();
            service.savePlaylist(playlistName, playlist);

            showAlert("Succes", "Playlist salvat", "Lista de redare a fost salvată cu succes!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Eroare", "Creare playlist", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private List<Piesa> generatePlaylist(List<Piesa> allPiese) {
        List<Piesa> playlist = new ArrayList<>();
        Set<String> usedFormatii = new HashSet<>();
        Set<String> usedGenuri = new HashSet<>();
        double totalDuration = 0;

        Random random = new Random();
        while (totalDuration < 15 * 60) {  // 15 minute = 900 secunde
            Piesa randomPiesa = allPiese.get(random.nextInt(allPiese.size()));

            // Verificăm dacă putem adăuga piesa la lista de redare
            if (!usedFormatii.contains(randomPiesa.getFormatie()) &&
                    !usedGenuri.contains(randomPiesa.getGen())) {
                playlist.add(randomPiesa);
                usedFormatii.add(randomPiesa.getFormatie());
                usedGenuri.add(randomPiesa.getGen());
                totalDuration += parseDuration(randomPiesa.getDurata());
            }

            // Dacă durata totală depășește 15 minute, terminăm selecția
            if (totalDuration > 15 * 60) {
                break;
            }
        }

        // Dacă lista este goală (nu am putut selecta piesele), returnăm null
        if (playlist.isEmpty()) {
            return null;
        }
        return playlist;
    }

    private double parseDuration(String durata) {
        String[] parts = durata.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    private void showAlert(String title, String header, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
