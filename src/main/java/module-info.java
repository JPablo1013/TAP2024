module com.example.tap2024 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tap2024 to javafx.fxml;
    exports com.example.tap2024;

    requires java.sql;
    requires org.kordamp.bootstrapfx.core;
    requires mysql.connector.j;
    opens  com.example.tap2024.modelos;
}