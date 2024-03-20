package com.example.tap2024.vistas;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Timer;

public class Memorama2 extends Stage {

    private Scene escena;
    private VBox vContenedor1, vContenedor2;
    private HBox hContenedor;
    private GridPane gdpTarjetas;
    private TextField pntJugador1, pntJugador2, noTarjetas;
    private Timer tiemJuego;
    private Button[][] arTarjetas = new Button[2][5];
    private Button btnResolver;
    private char[] arEtiquetas = {'A', 'A', 'B', 'B', 'C', 'C', 'D', 'D', 'E', 'E'};

    public Memorama2(){
        CrearUI();
        this.setTitle("Memorama");
        this.setScene(escena);
        this.show();
    }

    private void CrearUI(){
        btnResolver = new Button("Resolver");
        pntJugador1= new TextField("0");
        pntJugador2= new TextField("0");
        noTarjetas = new TextField("0");
        gdpTarjetas = new GridPane();
        CrearTeclado();
        vContenedor1 = new VBox(gdpTarjetas);
        vContenedor2 = new VBox(new Label("Puntuación Jugador 1: "), pntJugador1, new Label("Puntuación Jugador 2: "), pntJugador2);
        hContenedor = new HBox(btnResolver, new Label("Pares"), noTarjetas, new Label("Tiempo"));
        vContenedor1.setSpacing(10);
        escena = new Scene(new HBox(vContenedor1, vContenedor2, new VBox(hContenedor)), 400, 400);
    }

    private void CrearTeclado(){
        int pos = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                arTarjetas[i][j] = new Button(arEtiquetas[pos]+"");
                arTarjetas[i][j].setPrefSize(50,50);
                int finalPos = pos;
                arTarjetas[i][j].setOnAction(actionEvent -> setValue(arEtiquetas[finalPos]));
                gdpTarjetas.add(arTarjetas[i][j],j,i);
                pos++;
            }
        }
    }

    private void setValue(char simbolo) {
        pntJugador1.setEditable(false);
        pntJugador2.setEditable(false);
        noTarjetas.setEditable(false);
    }
}


//en memorama en la parte de crear tabla
//agregar vbxPrincipal=new
