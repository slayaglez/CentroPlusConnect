package proyecto.intermodular.app.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ReservaController {

    @FXML private TextField  TReservas;
    @FXML private ComboBox<String> CReservas;
    @FXML private ScrollBar  SReservas;

    @FXML private Button BCrear;
    @FXML private Button BEditar;
    @FXML private Button BEliminar;

    @FXML private AnchorPane AReservas4;
    @FXML private Label      LIdUsuario;
    @FXML private Label      LIdActividad;
    @FXML private Label      LFecha;
    @FXML private Label      LEstado;
    @FXML private Button     BCancelarReserva;

    @FXML private Label LInicio;
    @FXML private Label LUsuarios;
    @FXML private Label LActividades;
    @FXML private Label LNavReservas;
    @FXML private Label LIncidencias;

    // Inicialización
    @FXML
    public void initialize() {
        // Filtro de estado
        CReservas.setItems(FXCollections.observableArrayList(
            "Todos", "Confirmada", "Pendiente", "Cancelada"
        ));
        CReservas.setValue("Todos");

        // Búsqueda en tiempo real
        TReservas.textProperty().addListener((obs, oldVal, newVal) -> filtrarReservas());
    }

    // Filtro por estado
    @FXML
    private void handleFiltro(ActionEvent event) {
        filtrarReservas();
    }

    private void filtrarReservas() {
        String textoBusqueda = TReservas.getText().trim().toLowerCase();
        String filtroEstado  = CReservas.getValue();

        System.out.printf("Filtrando reservas → texto='%s', estado='%s'%n",
                          textoBusqueda, filtroEstado);
    }

    // CRUD
    @FXML
    private void handleCrear(ActionEvent event) {
        navegarA("/proyecto/intermodular/app/views/crear_reserva.fxml",
                 "CentroPlus Connect – Nueva reserva", event);
    }

    @FXML
    private void handleEditar(ActionEvent event) {
        navegarA("/proyecto/intermodular/app/views/editar_reserva.fxml",
                 "CentroPlus Connect – Editar reserva", event);
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar reserva");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Seguro que deseas eliminar esta reserva? Esta acción no se puede deshacer.");
        confirm.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                // TODO: llamar al servicio para eliminar la reserva
                System.out.println("Reserva eliminada.");
                mostrarExito("Reserva eliminada correctamente.");
            }
        });
    }

    // Cancelar reserva
    @FXML
    private void handleCancelarReserva(ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cancelar reserva");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Deseas cancelar esta reserva?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                LEstado.setText("Cancelada");
                LEstado.setStyle("-fx-text-fill: #a42525;");
                System.out.println("Reserva cancelada.");
            }
        });
    }

    // Navegación inferior
    @FXML
    private void handleNavInicio(MouseEvent event) {
        navegarA("/proyecto/intermodular/app/views/dashboard.fxml",
                 "CentroPlus Connect – Inicio", event);
    }

    @FXML
    private void handleNavUsuarios(MouseEvent event) {
        navegarA("/proyecto/intermodular/app/views/usuarios.fxml",
                 "CentroPlus Connect – Usuarios", event);
    }

    @FXML
    private void handleNavActividades(MouseEvent event) {
        navegarA("/proyecto/intermodular/app/views/actividades.fxml",
                 "CentroPlus Connect – Actividades", event);
    }

    @FXML
    private void handleNavIncidencias(MouseEvent event) {
        navegarA("/proyecto/intermodular/app/views/incidencias.fxml",
                 "CentroPlus Connect – Incidencias", event);
    }

    // Utilidades de navegación
    // Navegación desde botones
    private void navegarA(String fxmlPath, String titulo, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo navegar a: " + titulo);
        }
    }

    // Navegación desde labels de la barra inferior
    private void navegarA(String fxmlPath, String titulo, MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo navegar a: " + titulo);
        }
    }

    // Alertas
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Operación completada");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
