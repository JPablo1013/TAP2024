package com.example.tap2024;

import com.example.tap2024.vistas.Calculadora;
import com.example.tap2024.vistas.CuadroMagico;
import com.example.tap2024.vistas.Memorama;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private MenuBar mnbPrincipal;
    private Menu mnParcial1, mnParcial2, menSalir;
    private MenuItem mitCalculadora, mitSalir,  mitCuadromagico, mitMemorama;
    private BorderPane bdpPanel;

    @Override
    public void start(Stage stage) throws IOException {
        crearMenu();
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        bdpPanel = new BorderPane();
        bdpPanel.setTop(mnbPrincipal);
        Scene scene = new Scene(bdpPanel);
        scene.getStylesheets().add(getClass().getResource("/estilos/main.css").toString());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        //new Calculadora();
    }

    private void crearMenu() {
        //primer parcial menu
        mitCalculadora = new MenuItem("Calculadora");
        mitCalculadora.setOnAction(actionEvent -> new Calculadora());
        mitCuadromagico = new MenuItem("Cuadromagico");
        mitCuadromagico.setOnAction(actionEvent -> new CuadroMagico());
        mitMemorama = new MenuItem("Memorama");
        mitMemorama.setOnAction(actionEvent -> new Memorama());
        mnParcial1 = new Menu("Primer parcial");
        mnParcial1.getItems().addAll(mitCalculadora);
        mnParcial1.getItems().addAll(mitCuadromagico);
        mnParcial1.getItems().addAll(mitMemorama);

        //menu segundo parcial
        mnParcial2 = new Menu("Segundo parcial");

        //menu salir
        mitSalir = new MenuItem("salir");
        menSalir = new Menu("Salir");
        menSalir.getItems().add(mitSalir);
        mitSalir.setOnAction(actionEvent -> System.exit(0));

        mnbPrincipal = new MenuBar();
        mnbPrincipal.getMenus().addAll(mnParcial1, mnParcial2, menSalir);
    }

    public static void main(String[] args) {
        launch();
    }
}