package com.example.tap2024.vistas;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CuadroMagico extends Stage {
    private RandomAccessFile file; //creamos el acceso aleatoriio
    private Scene escena;

    private GridPane gridPane;

    public CuadroMagico() {
        CrearUI();
        this.setTitle("Cuadro Mágico");
        //this.setScene(escena);
        this.show();
    }

    private void CrearUI() {
        MenuBar menuBar = new MenuBar();
        Menu primerParcialMenu = new Menu("Primer Parcial");
        MenuItem nuevoCuadro = new MenuItem("Nuevo Cuadro");
        primerParcialMenu.getItems().add(nuevoCuadro);
        menuBar.getMenus().add(primerParcialMenu);

        nuevoCuadro.setOnAction(actionEvent -> {
            this.close();
            new CuadroMagico();
        });

        Label sizeLabel = new Label("Tamaño del cuadro:");
        TextField sizeTextField = new TextField();
        Button calcularButton = new Button("Calcular");
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        calcularButton.setOnAction(event -> {
            try {
                int size = Integer.parseInt(sizeTextField.getText());
                if (size >= 42) {
                    showAlert(Alert.AlertType.WARNING, "Advertencia", "Advertencia: el número debe ser igual o menor a 41.");
                } else {
                    if (size % 2 == 0 || size < 3) {
                        showAlert(Alert.AlertType.ERROR, "Error", "El tamaño debe ser impar y mayor o igual a 3.");
                    } else {
                        generarCuadroMagico(size);
                    }
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Por favor, ingrese un número entero válido.");
            }
        });

        gridPane.add(sizeLabel, 0, 0);
        gridPane.add(sizeTextField, 1, 0);
        gridPane.add(calcularButton, 0, 1, 2, 1);

        VBox vbox = new VBox(menuBar, gridPane);
        vbox.setAlignment(Pos.CENTER);

        escena = new Scene(vbox, 400, 400);
        escena.getStylesheets().add(getClass().getResource("/estilos/cuadroMagico.css").toString());
        this.setScene(escena);
    }

    private void generarCuadroMagico(int size) {
        try {
            // Abrir el archivo para escritura
            file = new RandomAccessFile("cuadro_magico.dat", "rw");

            // Limpiar el archivo si ya existe
            file.setLength(0);
            /*
        if (!gridPane.getChildren().isEmpty()) {
            gridPane.getChildren().clear();


        StringBuilder cuadroMagicoTexto = new StringBuilder();
        int[][] cuadro = new int[size][size];
        }*/

        int[][] cuadro = new int[size][size];

        int num = 1;
        int row = 0;
        int col = size / 2;

        while (num <= size * size) {
            cuadro[row][col] = num;
            num++;
            row--;
            col++;

            if (row == -1 && col == size) {
                row = 1;
                col = size - 1;
            } else {
                if (row == -1) {
                    row = size - 1;
                }
                if (col == size) {
                    col = 0;
                }
            }
            if (cuadro[row][col] != 0) {
                row += 2;
                col--;
            }
        }
        /*
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cuadroMagicoTexto.append(cuadro[i][j]).append("\t");
                Label label = new Label(String.valueOf(cuadro[i][j]));
                label.setFont(Font.font(16));
                gridPane.add(label, j, i + 2);
            }
            cuadroMagicoTexto.append("\n");
        }

         */
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    // Escribir cada número en el archivo
                    file.writeInt(cuadro[i][j]);
                }
            }

            // Cerrar el archivo después de escribir los datos
            file.close();

            // Leer los datos del archivo y mostrarlos en la interfaz gráfica
            leerYMostrarCuadroMagico(size);

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al acceder al archivo.");
        }
    }

    private void leerYMostrarCuadroMagico(int size) {
        try {
            // Abrir el archivo para lectura
            file = new RandomAccessFile("cuadro_magico.dat", "r");

            gridPane.getChildren().clear();

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    // Leer cada número del archivo y mostrarlo en la interfaz gráfica
                    int num = file.readInt();
                    Label label = new Label(String.valueOf(num));
                    label.setFont(Font.font(16));
                    gridPane.add(label, j, i + 2);
                }
            }

            // Cerrar el archivo después de leer los datos
            file.close();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al acceder al archivo.");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
