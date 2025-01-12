package com.example.radio;

import com.example.radio.practic.config.Settings;
import com.example.radio.practic.domain.Piesa;
import com.example.radio.practic.domain.PiesaConverter;
import com.example.radio.practic.repository.Repository;
import com.example.radio.practic.repository.RepositoryFactory;
import com.example.radio.practic.repository.SQLPiesaRepository;
import com.example.radio.practic.service.PiesaService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Settings settings = new Settings("src/main/java/com/example/radio/practic/config/settings.properties");

        Repository<Piesa> repositoryPiesa;

        if(settings.getProperty("Repository").equalsIgnoreCase("SQL")) {
            repositoryPiesa = new SQLPiesaRepository();
        } else {
            RepositoryFactory factory = new RepositoryFactory(settings);
            repositoryPiesa = factory.createRepository("Piese", new PiesaConverter());
        }

        PiesaService service = new PiesaService(repositoryPiesa);

        if (service.getAllPiese().isEmpty()) {
            service.addSamplePiese();
            System.out.println("sample data added");
        } else {
            System.out.println("repo already contains data");
        }

        FXMLLoader loader = new FXMLLoader((getClass().getResource( "hello-view.fxml")));
        Parent root = loader.load();

        HelloController controller = loader.getController();
        controller.setService(service);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("RADIO");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}