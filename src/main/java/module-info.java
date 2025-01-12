module com.example.radio {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.xerial.sqlitejdbc;

    opens com.example.radio to javafx.fxml;
    exports com.example.radio;
}