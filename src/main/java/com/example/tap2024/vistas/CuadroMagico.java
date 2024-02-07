package com.example.tap2024.vistas;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CuadroMagico extends Stage {

    private Scene scene;
    public CuadroMagico(){
        this.setTitle("cuadro Magico");
        this.setScene(new Scene(new Button("da click")));
        this.show();
    }

    private void CrearUI(){
        scene = new Scene(new Button("da click aqui"));
    }
}
