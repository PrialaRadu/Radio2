package com.example.radio.practic.repository;

import com.example.radio.practic.domain.Piesa;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SQLPlaylistRepository {

    private static final String JDBC_URL = "jdbc:sqlite:src/main/java/com/example/radio/playlist.db";
    private Connection conn = null;
    private final Repository<Piesa> piesaRepository;

    public SQLPlaylistRepository(Repository<Piesa> piesaRepository) {
        this.piesaRepository = piesaRepository;
        openConnection();
        createPlaylistTable();
    }

    private void openConnection() {
        try {
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(JDBC_URL);
            if (conn == null || conn.isClosed())
                conn = ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to database", e);
        }
    }

    private void createPlaylistTable() {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Playlists (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    piesa_ids TEXT
                );
            """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePlaylist(String playlistName, List<Piesa> playlist) {
        String insertPlaylistSQL = "INSERT INTO Playlists (name, piesa_ids) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(insertPlaylistSQL)) {
            // Transformă lista de ID-uri într-un șir de tip "1,2,3"
            String piesaIds = playlist.stream()
                    .map(p -> String.valueOf(p.getId()))
                    .collect(Collectors.joining(","));

            preparedStatement.setString(1, playlistName);
            preparedStatement.setString(2, piesaIds);

            preparedStatement.executeUpdate();
            System.out.println("Playlist salvat cu succes: " + playlistName);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Eroare la salvarea playlistului: " + e.getMessage());
        }
    }

    public List<Piesa> getPieseFromPlaylist(String playlistName) {
        String query = "SELECT piesa_ids FROM Playlists WHERE name = ?";
        List<Piesa> piesaList = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, playlistName);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String piesaIds = resultSet.getString("piesa_ids");

                if (piesaIds != null && !piesaIds.isEmpty()) {
                    String[] idsArray = piesaIds.split(",");
                    for (String id : idsArray) {
                        // Obține fiecare piesă pe baza ID-ului
                        Piesa piesa = getPiesaById(Integer.parseInt(id.trim()));
                        if (piesa != null) {
                            piesaList.add(piesa);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la obținerea playlistului: " + e.getMessage(), e);
        }

        return piesaList;
    }

    private Piesa getPiesaById(int id) {
        String query = "SELECT * FROM Piese WHERE id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Piesa(
                        resultSet.getInt("id"),
                        resultSet.getString("formatie"),
                        resultSet.getString("titlu"),
                        resultSet.getString("durata"),
                        resultSet.getString("gen")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
