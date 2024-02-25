package com.example.tap2024.vistas;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Calculadora extends Stage {
    private Scene escena;
    private VBox vContenedor;
    private GridPane gdpTeclado;
    private TextField txtPantalla;
    private Button[][] arBotones = new Button[4][4];
    private char[] arEtiquetas = {'7', '8','9', '/', '4', '5','6','*',
    '1','2','3','-','0','.','=','+'};

    public Calculadora(){
        CrearUI();
        this.setTitle("Mi primera y ulitma Calculadora");
        this.setScene(escena);
        this.show();
    }

    private void CrearUI(){
        txtPantalla = new TextField("0");
        txtPantalla.setEditable(false);
        gdpTeclado = new GridPane();
        CrearTeclado();
        vContenedor = new VBox(txtPantalla,gdpTeclado);
        vContenedor.setSpacing(5);

        Button btnLimpiar = new Button("Limpiar");
        btnLimpiar.setOnAction(e -> txtPantalla.clear());
        btnLimpiar.setOnAction(e -> txtPantalla.setText("0"));

        /*Button btnEliminar = new Button("Eliminar");
        btnEliminar.setOnAction(e -> { String text = txtPantalla.getText();
            if (!text.isEmpty()) {
                txtPantalla.setText(text.substring(0, text.length() - 1));
            }
        });*/

        HBox buttonBox = new HBox(10, btnLimpiar);
        buttonBox.setPadding(new Insets(10));

        VBox mainContainer = new VBox(vContenedor, buttonBox);
        mainContainer.setSpacing(5);

        escena = new Scene(mainContainer);
        escena.getStylesheets().add(getClass().getResource("/estilos/calculadora.css").toString());
        Button botonIgual = (Button)escena.lookup("#boton-igual");
        botonIgual.setOnAction(actionEvent -> calcular());

    }

    private void CrearTeclado(){
        int pos = 0;
        char simbolo;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

                arBotones[i][j] = new Button(arEtiquetas[pos]+"");
                arBotones[i][j].setPrefSize(50,50);
                int finalPos = pos;
                arBotones[i][j].setOnAction(actionEvent -> setValue(arEtiquetas[finalPos]));
                gdpTeclado.add(arBotones[i][j],j,i);

                if(arEtiquetas[pos]=='+'||arEtiquetas[pos]=='-'||arEtiquetas[pos]=='*'||arEtiquetas[pos]=='/')
                    arBotones[i][j].setId("color-operador");

                if (arEtiquetas[pos] == '=') {
                    arBotones[i][j].setId("boton-igual");
                } else {

                    arBotones[i][j].setOnAction(actionEvent -> setValue(arEtiquetas[finalPos]));
                }
                pos++;
            }

        }
    }

    //private void setValue(char simbolo) {
      //  txtPantalla.appendText(simbolo+"");
    //}

    private void setValue(char simbolo) {
        String pantallaTexto = txtPantalla.getText();

        if (pantallaTexto.equals("0") && esOperador(simbolo)) {
            txtPantalla.setText("syntax error");
            return;
        }

        if (esOperador(simbolo) && esOperador(pantallaTexto.charAt(pantallaTexto.length() - 1))) {
            txtPantalla.setText("syntax error");
            return;
        }

        if ((simbolo == '.' || esOperador(simbolo)) && (pantallaTexto.charAt(pantallaTexto.length() - 1) == '.' || esOperador(pantallaTexto.charAt(pantallaTexto.length() - 1)))) {
            txtPantalla.setText("syntax error");
            return;
        }

        if (pantallaTexto.equals("0")) {
            pantallaTexto = "";
        }

        if (simbolo == '.') {
            if (pantallaTexto.contains(".")) {
                txtPantalla.setText("syntax error");
                return;
            } else {
                txtPantalla.setText(pantallaTexto + simbolo);
                return;
            }
        } else if (esOperador(simbolo)) {
            if (esOperador(pantallaTexto.charAt(pantallaTexto.length() - 1))) {
                txtPantalla.setText("syntax error");
                return;
            }
        }

        txtPantalla.setText(pantallaTexto + simbolo);
    }

    private boolean esOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private double evaluarExpresion(String expresion) {

        String[] partes = expresion.split("(?<=[-+*/])|(?=[-+*/])");

        double resultado = Double.parseDouble(partes[0]);
        char operador = ' ';

        for (int i = 1; i < partes.length; i++) {
            if (partes[i].matches("[-+*/]")) {
                operador = partes[i].charAt(0);
            } else {
                double operando = Double.parseDouble(partes[i]);
                switch (operador) {
                    case '+':
                        resultado += operando;
                        break;
                    case '-':
                        resultado -= operando;
                        break;
                    case '*':
                        resultado *= operando;
                        break;
                    case '/':
                        if (operando == 0) {
                            throw new ArithmeticException();
                        }
                        resultado /= operando;
                        break;
                }
            }
        }

        return resultado;
    }

    private void calcular() {
        String expresion = txtPantalla.getText();
        double resultado = 0.0;

        if (expresion.contains(".+") || expresion.contains(".-") || expresion.contains(".*") || expresion.contains("./")) {
            txtPantalla.setText("syntax error");
            return;
        }

        if (expresion.contains("+.") || expresion.contains("-.") || expresion.contains("*.") || expresion.contains("/.")) {
            txtPantalla.setText("syntax error");
            return;
        }

        if (!esExpresionValida(expresion)) {
            txtPantalla.setText("syntax error");
            return;
        }

        if (expresion.startsWith("/") || expresion.contains("/0") || expresion.startsWith("-") || expresion.startsWith("+") || expresion.startsWith("*") || expresion.startsWith(".")) {
            txtPantalla.setText("syntax error");
            return;
        }

        resultado = evaluarExpresion(expresion);
        txtPantalla.setText(Double.toString(resultado));

        if (expresion.equals("0")) {
            expresion= "";
        }
    }

    private boolean esExpresionValida(String expresion) {
        return !expresion.contains("/0");
    }

}
