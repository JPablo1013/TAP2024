package com.example.tap2024.vistas;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.util.*;

public class Memorama extends Stage {

    private Scene escena;
    private Label lblPares, lblTiempo, lblJugador1, lblJugador2;
    private TextField txtPares, txtScore1, txtScore2;
    private GridPane gdpJuego;
    private VBox vbxPrincipal, vbxJugadores;
    private List<String> cartas;
    private List<Button> cartasButtons;
    private int paresRestantes;
    private int cartaSeleccionada = -1;
    private int turno;
    private int scoreJugador1 = 0;
    private int scoreJugador2 = 0;
    private AnimationTimer timer;
    private boolean juegoTerminado = false;

    public Memorama() {
        CrearUI();
        this.setTitle("Memorama :)");
        this.setScene(escena);
        this.show();
    }

    private void CrearUI() {
        lblPares = new Label("Número de Pares:");
        txtPares = new TextField();
        Button btnJugar = new Button("Iniciar Juego");
        lblTiempo = new Label("00:00");
        HBox hbxTiempo = new HBox(lblPares, txtPares, btnJugar, lblTiempo);

        lblJugador1 = new Label("Jugador 1");
        txtScore1 = new TextField("0");
        lblJugador1.setTextFill(Color.GREEN);
        HBox hbxJugador1 = new HBox(lblJugador1, txtScore1);

        lblJugador2 = new Label("Jugador 2");
        txtScore2 = new TextField("0");
        lblJugador2.setTextFill(Color.RED);
        HBox hbxJugador2 = new HBox(lblJugador2, txtScore2);

        vbxJugadores = new VBox(hbxJugador1, hbxJugador2);
        gdpJuego = new GridPane();
        HBox hbxJuego = new HBox(gdpJuego, vbxJugadores);

        vbxPrincipal = new VBox(hbxTiempo, hbxJuego);
        vbxPrincipal.setPadding(new Insets(10));
        vbxPrincipal.setSpacing(10);

        escena = new Scene(vbxPrincipal, 800, 600);
        escena.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        btnJugar.setOnAction(event -> {
            int numPares = Integer.parseInt(txtPares.getText());
            if (numPares >= 3 && numPares <= 15) {
                IniciarJuego(numPares);
            } else {
                MostrarAlerta(Alert.AlertType.ERROR, "Error", "Número de pares no válido");
            }
        });
    }

    private void IniciarJuego(int numPares) {
        if (timer != null) {
            timer.stop();
        }
        if (juegoTerminado) {
            gdpJuego.getChildren().clear();
        }

        List<String> rutas = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            rutas.add("/images/" + i + ".jpg");
        }

        cartas = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numPares; i++) {
            int indice = random.nextInt(rutas.size());
            String ruta = rutas.remove(indice);
            cartas.add(ruta);
            cartas.add(ruta);
        }

        Collections.shuffle(cartas);

        cartasButtons = new ArrayList<>();
        for (int i = 0; i < cartas.size(); i++) {
            Button button = new Button();
            button.setId(String.valueOf(i));
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/default.jpg")));
            imageView.setFitWidth(100);
            imageView.setFitHeight(150);
            button.setGraphic(imageView);
            button.setOnAction(event -> SeleccionarCarta(button));
            cartasButtons.add(button);
        }

        int columnas = 6;
        for (int i = 0; i < cartasButtons.size(); i++) {
            Button button = cartasButtons.get(i);
            gdpJuego.add(button, i % columnas, i / columnas);
        }

        paresRestantes = numPares;
        turno = 1;
        scoreJugador1 = 0;
        scoreJugador2 = 0;
        txtScore1.setText("0");
        txtScore2.setText("0");

        // Iniciar timer para el tiempo de turno (30 segundos)
        timer = new AnimationTimer() {
            private long startTime = System.nanoTime();

            @Override
            public void handle(long now) {
                long elapsedTime = now - startTime;
                int seconds = (int) (elapsedTime / 1_000_000_000);
                lblTiempo.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
                if (seconds >= 30) {
                    CambiarTurno();
                }
            }
        };
        timer.start();
    }

    private void SeleccionarCarta(Button cartaButton) {
        if (juegoTerminado) return; // No permitir selección de cartas si el juego ha terminado

        int indiceCarta = Integer.parseInt(cartaButton.getId());
        cartaButton.setDisable(true);
        ImageView imageView = (ImageView) cartaButton.getGraphic();
        imageView.setImage(new Image(getClass().getResourceAsStream(cartas.get(indiceCarta))));

        if (cartaSeleccionada == -1) {
            cartaSeleccionada = indiceCarta;
        } else {
            if (!Objects.equals(cartas.get(cartaSeleccionada), cartas.get(indiceCarta))) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            VoltearCartas(cartaSeleccionada, indiceCarta);
                            CambiarTurno();
                            cartaSeleccionada = -1;
                        });
                    }
                }, 1000);
            } else {
                if (turno == 1) {
                    scoreJugador1++;
                    txtScore1.setText(String.valueOf(scoreJugador1));
                } else {
                    scoreJugador2++;
                    txtScore2.setText(String.valueOf(scoreJugador2));
                }
                paresRestantes--;
                if (paresRestantes == 0) {
                    timer.stop();
                    juegoTerminado = true;
                    MostrarAlerta(Alert.AlertType.INFORMATION, "Fin del juego", "¡El juego ha terminado!");
                }
                if (timer != null) {
                    timer.stop();
                }
                cartaSeleccionada = -1;
            }
        }
    }

    private void VoltearCartas(int indiceCarta1, int indiceCarta2) {
        if (indiceCarta1 >= 0 && indiceCarta1 < cartasButtons.size() &&
                indiceCarta2 >= 0 && indiceCarta2 < cartasButtons.size()) {
            cartasButtons.get(indiceCarta1).setDisable(false);
            cartasButtons.get(indiceCarta2).setDisable(false);

            ((ImageView) cartasButtons.get(indiceCarta1).getGraphic()).setImage(new Image(getClass().getResourceAsStream("/images/default.jpg")));
            ((ImageView) cartasButtons.get(indiceCarta2).getGraphic()).setImage(new Image(getClass().getResourceAsStream("/images/default.jpg")));

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        ((ImageView) cartasButtons.get(indiceCarta1).getGraphic()).setImage(new Image(getClass().getResourceAsStream("/images/default.jpg")));
                        ((ImageView) cartasButtons.get(indiceCarta2).getGraphic()).setImage(new Image(getClass().getResourceAsStream("/images/default.jpg")));
                        cartasButtons.get(indiceCarta1).setDisable(false);
                        cartasButtons.get(indiceCarta2).setDisable(false);
                    });
                }
            };

            Timer timer = new Timer();
            timer.schedule(task, 1000);
        } else {
            System.out.println("Índice de carta incorrecto: " + indiceCarta1 + ", " + indiceCarta2);
        }
    }

    private void CambiarTurno() {
        if (paresRestantes > 0) {
            turno = turno == 1 ? 2 : 1;
            if (turno == 1) {
                lblJugador1.setTextFill(Color.GREEN);
                lblJugador2.setTextFill(Color.RED);
            } else {
                lblJugador1.setTextFill(Color.RED);
                lblJugador2.setTextFill(Color.GREEN);
            }
        } else {
            MostrarAlerta(Alert.AlertType.INFORMATION, "Fin del juego", "¡El juego ha terminado!");
            timer.stop();
        }
    }

    private void MostrarAlerta(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
