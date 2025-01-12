package com.example.radio.practic.repository;

import com.example.radio.practic.domain.Piesa;
import com.example.radio.practic.repository.exceptions.RepositoryExceptions;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SQLPiesaRepository extends Repository<Piesa> implements AutoCloseable{
    private static final String JDBC_URL = "jdbc:sqlite:src/main/java/com/example/radio/piese.db";

    private Connection conn = null;

    public SQLPiesaRepository() {
        openConnection();
        createSchema();
        loadData();
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

    private void createSchema() {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Piese (
                    ID INTEGER PRIMARY KEY,
                    formatie TEXT NOT NULL,
                    titlu TEXT NOT NULL,
                    gen TEXT NOT NULL,
                    durata TEXT NOT NULL
                );
            """);
        } catch (SQLException e) {
            System.err.println("[ERROR] createSchema: " + e.getMessage());
        }
    }

    private void loadData() {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM Piese");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("ID");
                String formatie = rs.getString("formatie");
                String titlu = rs.getString("titlu");
                String gen = rs.getString("gen");
                String durata = rs.getString("durata");

                Piesa piesa = new Piesa(id, formatie, titlu, gen, durata);
                elements.add(piesa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Piesa piesa) throws RepositoryExceptions.DuplicateIDException {
        super.add(piesa);
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Piese (ID, formatie, titlu, gen, durata) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setInt(1, piesa.getId());
            stmt.setString(2, piesa.getFormatie());
            stmt.setString(3, piesa.getTitlu());
            stmt.setString(4, piesa.getGen());
            stmt.setString(5, piesa.getDurata());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryExceptions.DuplicateIDException("Error saving into database");
        }
    }

    @Override
    public void close() throws Exception {
        if (conn != null) {
            conn.close();
        }
    }

    public void createPlaylistTable(String playlistName) {
        try (Statement stmt = conn.createStatement()) {
            // Creăm un tabel pentru lista de redare
            String createTableQuery = String.format("""
            CREATE TABLE IF NOT EXISTS %s (
                ID INTEGER PRIMARY KEY,
                formatie TEXT NOT NULL,
                titlu TEXT NOT NULL,
                gen TEXT NOT NULL,
                durata TEXT NOT NULL
            );
            """, playlistName);
            stmt.executeUpdate(createTableQuery);
        } catch (SQLException e) {
            System.err.println("[ERROR] createPlaylistTable: " + e.getMessage());
        }
    }

    public void savePlaylist(String playlistName, List<Piesa> playlist) {
        try {
            // Creăm un tabel pentru playlist cu numele dat de utilizator
            String createTableSQL = "CREATE TABLE IF NOT EXISTS " + playlistName + " (ID INTEGER PRIMARY KEY, formatie TEXT, titlu TEXT, gen TEXT, durata TEXT)";
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createTableSQL);
            }

            // Inserăm piesele în tabelul respectiv
            String insertSQL = "INSERT INTO " + playlistName + " (ID, formatie, titlu, gen, durata) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                for (Piesa piesa : playlist) {
                    stmt.setInt(1, piesa.getId());
                    stmt.setString(2, piesa.getFormatie());
                    stmt.setString(3, piesa.getTitlu());
                    stmt.setString(4, piesa.getGen());
                    stmt.setString(5, piesa.getDurata());
                    stmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Eroare la salvarea playlist-ului în baza de date.");
        }
    }
}
