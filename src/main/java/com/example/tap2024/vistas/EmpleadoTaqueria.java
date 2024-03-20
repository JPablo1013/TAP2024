package com.example.tap2024.vistas;

import com.example.tap2024.components.ButtonCell;
import com.example.tap2024.modelos.EmpleadosDAO;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

public class EmpleadoTaqueria extends Stage {
    private Panel pnlPrincipal;
    private BorderPane bpnPrincipal;
    private VBox vbxPrincipal;
    private ToolBar tblMenu;
    private Scene escena;
    private TableView<EmpleadosDAO> tblEmpleados;
    private Button btnAddEmpleado;

    public EmpleadoTaqueria(){
        CrearUI();
        this.setTitle("Taqueria tacos de suaperro");
        this.setScene(escena);
        this.show();
    }

    private void CrearUI(){
        ImageView imvempleado = new ImageView(getClass().getResource("/images/employee.png").toString());
        imvempleado.setFitHeight(50);
        imvempleado.setFitWidth(50);
        //tblMenu = new ToolBar();
        btnAddEmpleado = new Button();
        btnAddEmpleado.setOnAction(event -> new EmpleadosForm(tblEmpleados, null));
        btnAddEmpleado.setPrefSize(50,50);
        btnAddEmpleado.setGraphic(imvempleado);
        tblMenu = new ToolBar(btnAddEmpleado);

        CreateTable();
        //pnlPrincipal= new VBox(tblMenu,tblEmpleados);
        //vbxPrincipal = new VBox(tblMenu, tblEmpleados);
        bpnPrincipal = new BorderPane();
        bpnPrincipal.setTop(tblMenu);
        bpnPrincipal.setCenter(tblEmpleados);
        pnlPrincipal= new Panel("Taqueria");
        pnlPrincipal.getStylesheets().add("panel-info");
        pnlPrincipal.setBody(bpnPrincipal);
        escena = new Scene(pnlPrincipal, 600, 400);
        escena.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
    }

    private void CreateTable(){

        EmpleadosDAO objEmp = new EmpleadosDAO();
        tblEmpleados = new TableView<EmpleadosDAO>();
        TableColumn<EmpleadosDAO, String> tbcNomEmp = new TableColumn<>("Empleado");
        tbcNomEmp.setCellValueFactory(new PropertyValueFactory<>("nomEmpleado"));

        TableColumn<EmpleadosDAO, String> tbcRFC = new TableColumn<>("RFC");
        tbcRFC.setCellValueFactory(new PropertyValueFactory<>("rfcEmpleado"));

        TableColumn<EmpleadosDAO, Float> tbsSueldo = new TableColumn<>("SALARIO");
        tbsSueldo.setCellValueFactory(new PropertyValueFactory<>("salario"));

        TableColumn<EmpleadosDAO, String> tbcTelefono = new TableColumn<>("TELEFONO");
        tbcTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        TableColumn<EmpleadosDAO, String> tbcDireccion = new TableColumn<>("DIRECCION");
        tbcDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        TableColumn<EmpleadosDAO, String> tbcEditar = new TableColumn<>("Editar");
        tbcEditar.setCellFactory(
                new Callback<TableColumn<EmpleadosDAO, String>, TableCell<EmpleadosDAO, String>>() {
                    @Override
                    public TableCell<EmpleadosDAO, String> call(TableColumn<EmpleadosDAO, String> empleadosDAOStringTableColumn) {
                        return new ButtonCell(1);
                    }
                }
        );

        TableColumn<EmpleadosDAO, String> tbcEliminar = new TableColumn<>("ELIMINAR");
        tbcEliminar.setCellFactory(
                new Callback<TableColumn<EmpleadosDAO, String>, TableCell<EmpleadosDAO, String>>() {
                    @Override
                    public TableCell<EmpleadosDAO, String> call(TableColumn<EmpleadosDAO, String> empleadosDAOStringTableColumn) {
                        return new ButtonCell(2);
                    }
                }
        );

        tblEmpleados.getColumns().addAll(tbcNomEmp,tbcRFC,tbsSueldo,tbcTelefono,tbcDireccion, tbcEditar, tbcEliminar);
        tblEmpleados.setItems(objEmp.CONSULTAR());
    }
}
