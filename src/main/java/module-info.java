module com.example.radio {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.xerial.sqlitejdbc;

    opens com.example.radio to javafx.fxml;
    exports com.example.radio;
    requires org.junit.jupiter.api;
    requires org.junit.platform.commons;
    requires org.junit.jupiter.engine;

}