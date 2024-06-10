package com.example.tap2024.vistas;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Impresora extends Stage {

    private Scene escena;
    private TableView<Tarea> tablaTareas;
    private Button btnAgregar;
    private Button btnIniciarSimulador;
    private boolean simuladorActivo = false;
    private Queue<Tarea> colaTareas = new LinkedList<>();

    public Impresora() {
        CrearUI();
        this.setTitle("Impresora");
        this.setScene(escena);
        this.show();
    }

    public void CrearUI() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        tablaTareas = new TableView<>();
        tablaTareas.setPrefWidth(400);

        TableColumn<Tarea, String> columnaNoArchivo = new TableColumn<>("No. Archivo");
        columnaNoArchivo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNoArchivo()));

        TableColumn<Tarea, String> columnaNombreArchivo = new TableColumn<>("Nombre de Archivo");
        columnaNombreArchivo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreArchivo()));

        TableColumn<Tarea, String> columnaNumHojas = new TableColumn<>("Número de Hojas");
        columnaNumHojas.setCellValueFactory(data -> Bindings.createStringBinding(() -> String.valueOf(data.getValue().getNumHojas())));

        tablaTareas.getColumns().addAll(columnaNoArchivo, columnaNombreArchivo, columnaNumHojas);

        Label lblNoArchivo = new Label("No. Archivo:");
        TextField txtNoArchivo = new TextField();

        Label lblNombreArchivo = new Label("Nombre de archivo:");
        TextField txtNombreArchivo = new TextField();

        Label lblNumHojas = new Label("Número de hojas:");
        TextField txtNumHojas = new TextField();

        btnAgregar = new Button("Agregar Nueva Tarea");
        btnAgregar.setOnAction(event -> {
            String nombreArchivo;
            if (txtNombreArchivo.getText().isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String fechaHora = LocalDateTime.now().format(formatter);
                nombreArchivo = "archivo_" + fechaHora + ".txt";
            } else {
                nombreArchivo = txtNombreArchivo.getText();
            }

            Random rand = new Random();
            int numHojasAleatorio = rand.nextInt(20) + 1;
            txtNumHojas.setText(String.valueOf(numHojasAleatorio));

            Tarea nuevaTarea = new Tarea(txtNoArchivo.getText(), nombreArchivo, numHojasAleatorio);

            tablaTareas.getItems().add(nuevaTarea);
            colaTareas.add(nuevaTarea);
        });

        btnIniciarSimulador = new Button("Iniciar Simulador");
        btnIniciarSimulador.setOnAction(event -> {
            if (!simuladorActivo) {
                iniciarSimulador();
            } else {
                detenerSimulador();
            }
        });

        gridPane.add(tablaTareas, 0, 0, 2, 1);
        gridPane.add(lblNoArchivo, 0, 1);
        gridPane.add(txtNoArchivo, 1, 1);
        gridPane.add(lblNombreArchivo, 0, 2);
        gridPane.add(txtNombreArchivo, 1, 2);
        gridPane.add(lblNumHojas, 0, 3);
        gridPane.add(txtNumHojas, 1, 3);
        gridPane.add(btnAgregar, 0, 4);
        gridPane.add(btnIniciarSimulador, 1, 4);

        escena = new Scene(gridPane, 600, 400);
    }

    private void iniciarSimulador() {
        simuladorActivo = true;
        btnIniciarSimulador.setText("Detener Simulador");

        Task<Void> simuladorTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (simuladorActivo) {
                    Tarea tareaActual;
                    synchronized (colaTareas) {
                        tareaActual = colaTareas.poll();
                    }

                    if (tareaActual != null) {
                        final Tarea tareaParaImprimir = tareaActual;
                        Platform.runLater(() -> {
                            simularImpresion(tareaParaImprimir);
                        });
                        Thread.sleep(tareaParaImprimir.getNumHojas() * 100); // Simular el tiempo de impresión en base al número de hojas
                    } else {
                        Thread.sleep(1000); // Esperar un segundo antes de verificar nuevamente la cola de tareas
                    }
                }
                return null;
            }
        };

        Thread simuladorThread = new Thread(simuladorTask);
        simuladorThread.setDaemon(true);
        simuladorThread.start();
    }

    private void detenerSimulador() {
        simuladorActivo = false;
        btnIniciarSimulador.setText("Iniciar Simulador");
    }

    private void simularImpresion(Tarea tarea) {
        VBox vbox = new VBox();
        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(0);

        Label lblNombreArchivo = new Label(tarea.getNombreArchivo());
        Label lblProgreso = new Label("Progreso:");
        HBox hbox = new HBox(lblProgreso, progressBar);

        vbox.getChildren().addAll(lblNombreArchivo, hbox);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20));

        Stage ventanaSimulacion = new Stage();
        ventanaSimulacion.setTitle("Simulación de Impresión");
        ventanaSimulacion.setScene(new Scene(vbox, 300, 150));
        ventanaSimulacion.show();

        Task<Void> simulacionTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int numHojas = tarea.getNumHojas();
                for (int i = 0; i <= numHojas; i++) {
                    final int progreso = i;
                    updateProgress(progreso, numHojas);
                    Thread.sleep(100); // Simular el tiempo de impresión
                }
                return null;
            }
        };

        progressBar.progressProperty().bind(simulacionTask.progressProperty());

        simulacionTask.setOnSucceeded(event -> {
            ventanaSimulacion.close();
            tablaTareas.getItems().remove(tarea); // Eliminar la tarea de la tabla
            System.out.println("Tarea completada: " + tarea.getNombreArchivo());
        });

        Thread simulacionThread = new Thread(simulacionTask);
        simulacionThread.setDaemon(true);
        simulacionThread.start();
    }

    public static class Tarea {
        private final SimpleStringProperty noArchivo;
        private final SimpleStringProperty nombreArchivo;
        private final SimpleIntegerProperty numHojas;

        public Tarea(String noArchivo, String nombreArchivo, int numHojas) {
            this.noArchivo = new SimpleStringProperty(noArchivo);
            this.nombreArchivo = new SimpleStringProperty(nombreArchivo);
            this.numHojas = new SimpleIntegerProperty(numHojas);
        }

        public String getNoArchivo() {
            return noArchivo.get();
        }

        public String getNombreArchivo() {
            return nombreArchivo.get();
        }

        public int getNumHojas() {
            return numHojas.get();
        }
    }
}
