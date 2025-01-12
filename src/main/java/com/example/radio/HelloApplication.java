package com.example.radio;

import com.example.radio.practic.config.Settings;
import com.example.radio.practic.domain.Piesa;
import com.example.radio.practic.domain.PiesaConverter;
import com.example.radio.practic.repository.Repository;
import com.example.radio.practic.repository.RepositoryFactory;
import com.example.radio.practic.repository.SQLPiesaRepository;
import com.example.radio.practic.repository.SQLPlaylistRepository;
import com.example.radio.practic.service.PiesaService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load settings
        Settings settings = new Settings("src/main/java/com/example/radio/practic/config/settings.properties");

        // Initialize repositories
        Repository<Piesa> repositoryPiesa;
        if (settings.getProperty("Repository").equalsIgnoreCase("SQL")) {
            repositoryPiesa = new SQLPiesaRepository();
        } else {
            RepositoryFactory factory = new RepositoryFactory(settings);
            repositoryPiesa = factory.createRepository("Piese", new PiesaConverter());
        }

        SQLPlaylistRepository repositoryPlaylist = new SQLPlaylistRepository(repositoryPiesa);

        // Initialize service
        PiesaService service = new PiesaService(repositoryPiesa, repositoryPlaylist);

        // Add sample data if necessary
        if (service.getAllPiese().isEmpty()) {
            service.addSamplePiese();
            System.out.println("Sample data added.");
        } else {
            System.out.println("Repository already contains data.");
        }

        // Load FXML and controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();

        HelloController controller = loader.getController();
        controller.setService(service);

        // Setup and display the main stage
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Radio App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
