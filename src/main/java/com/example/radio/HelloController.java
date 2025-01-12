package com.example.radio;

import com.example.radio.practic.domain.Piesa;
import com.example.radio.practic.service.PiesaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.util.List;
import java.util.stream.Collectors;

public class HelloController {
    private PiesaService service;

    @FXML
    private ListView<Piesa> listViewPieseOrdonate;

    @FXML
    private ListView<Piesa> listViewPieseFiltrate;

    @FXML
    private TextField playlistNameField;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Piesa> listViewLoadedPlaylist;

    @FXML
    private TextField loadPlaylistNameField; // Pentru încărcarea playlistului

    @FXML
    private TextField createPlaylistNameField; // Pentru crearea playlistului

    private ObservableList<Piesa> observablePieseOrdonate = FXCollections.observableArrayList();
    private ObservableList<Piesa> observablePieseFiltrate = FXCollections.observableArrayList();

    public void setService(PiesaService service) {
        this.service = service;
        initialize();
    }

    @FXML
    private void initialize() {
        if (service != null) {
            List<Piesa> sortedList = ordonateFormatieTitlu();
            observablePieseOrdonate.setAll(sortedList);
            listViewPieseOrdonate.setItems(observablePieseOrdonate);
            listViewPieseFiltrate.setItems(observablePieseFiltrate);
        }
    }

    private List<Piesa> ordonateFormatieTitlu() {
        return service.ordonateFormatieTitlu();
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            showAlert("Căutare invalidă", "Textul de căutare este gol!", "Introduceți un text valid.", AlertType.WARNING);
            return;
        }

        List<Piesa> filteredList = service.getAllPiese().stream()
                .filter(p -> p.getFormatie().toLowerCase().contains(searchText) ||
                        p.getTitlu().toLowerCase().contains(searchText) ||
                        p.getDurata().toLowerCase().contains(searchText) ||
                        p.getGen().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        if (filteredList.isEmpty()) {
            showAlert("Niciun rezultat", "Nu s-au găsit piese.", "Nici o piesă nu corespunde căutării.", AlertType.INFORMATION);
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
        String playlistName = playlistNameField.getText().trim();
        if (playlistName.isEmpty()) {
            showAlert("Eroare", "Numele playlistului este gol", "Introduceți un nume pentru playlist.", AlertType.ERROR);
            return;
        }

        try {
            List<Piesa> playlist = service.generatePlaylist();

            if (playlist == null || playlist.isEmpty()) {
                showAlert("Eroare", "Playlist gol", "Nu există suficiente piese.", AlertType.ERROR);
                return;
            }

            service.savePlaylist(playlistName, playlist);
            showAlert("Succes", "Playlist salvat", "Playlistul a fost salvat cu succes.", AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Eroare", "Salvare eșuată", e.getMessage(), AlertType.ERROR);
        }
    }


    @FXML
    private void handleSavePlaylist() {
        String playlistName = createPlaylistNameField.getText();

        if (playlistName.isEmpty()) {
            showAlert("Eroare", "Numele playlistului este gol", "Vă rugăm să introduceți un nume pentru playlist.", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Generăm un playlist
            List<Piesa> playlist = service.generatePlaylist();

            if (playlist == null || playlist.isEmpty()) {
                showAlert("Eroare", "Playlist gol", "Nu există piese suficiente pentru a crea un playlist.", Alert.AlertType.ERROR);
                return;
            }

            // Salvăm playlistul
            service.savePlaylist(playlistName, playlist);
            showAlert("Succes", "Playlist salvat", "Playlistul a fost salvat cu succes!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Eroare", "Salvarea playlistului a eșuat", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleLoadPlaylist() {
        String playlistName = loadPlaylistNameField.getText().trim();

        if (playlistName.isEmpty()) {
            showAlert("Eroare", "Numele playlistului este gol", "Introduceți un nume de playlist pentru a-l încărca.", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Obținem piesele din playlist
            List<Piesa> playlist = service.getPieseFromPlaylist(playlistName);

            if (playlist == null || playlist.isEmpty()) {
                showAlert("Eroare", "Playlist gol", "Playlistul nu conține piese sau nu există.", Alert.AlertType.ERROR);
            } else {
                listViewLoadedPlaylist.setItems(FXCollections.observableArrayList(playlist));
                showAlert("Succes", "Playlist încărcat", "Playlistul a fost încărcat cu succes!", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showAlert("Eroare", "Încărcare eșuată", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String header, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
