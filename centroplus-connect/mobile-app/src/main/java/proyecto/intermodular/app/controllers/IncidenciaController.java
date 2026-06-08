package proyecto.intermodular.app.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class IncidenciaController {

    // ── Cabecera y búsqueda ──────────────────────────────────────────────────
    @FXML private TextField        TBuscarIncidencia;
    @FXML private ComboBox<String> CIncidencias;
    @FXML private ScrollBar        SIncidencias;

    // ── Botones CRUD ─────────────────────────────────────────────────────────
    @FXML private Button BCrear;
    @FXML private Button BEditar;
    @FXML private Button BEliminar;

    // ── Tarjeta de incidencia (plantilla visible en el FXML) ─────────────────
    @FXML private AnchorPane AIncidencias4;
    @FXML private Label      LIdUsuario;
    @FXML private Label      LFecha;
    @FXML private Label      LEstado;
    @FXML private Label      LAsunto;
    @FXML private Label      LDescripcion;
    @FXML private Button     BCambiarEstado;

    // ── Navegación inferior ──────────────────────────────────────────────────
    @FXML private Label LInicio;
    @FXML private Label LUsuarios;
    @FXML private Label LActividades;
    @FXML private Label LReservas;
    @FXML private Label LNavIncidencias;

    // Estados posibles de una incidencia (ciclo de cambio)
    private static final String[] ESTADOS = {"Abierta", "En proceso", "Resuelta", "Cerrada"};

    // ── Inicialización ───────────────────────────────────────────────────────
    @FXML
    public void initialize() {
        // Filtro por estado
        CIncidencias.setItems(FXCollections.observableArrayList(
            "Todos", "Abierta", "En proceso", "Resuelta", "Cerrada"
        ));
        CIncidencias.setValue("Todos");

        // Búsqueda en tiempo real
        TBuscarIncidencia.textProperty().addListener((obs, oldVal, newVal) -> filtrarIncidencias());
    }

    // ── Filtro por estado ────────────────────────────────────────────────────
    @FXML
    private void handleFiltro(ActionEvent event) {
        filtrarIncidencias();
    }

    private void filtrarIncidencias() {
        String textoBusqueda = TBuscarIncidencia.getText().trim().toLowerCase();
        String filtroEstado  = CIncidencias.getValue();

        // TODO: aplicar filtros sobre la lista de incidencias cargada desde el servicio
        System.out.printf("Filtrando incidencias → texto='%s', estado='%s'%n",
                          textoBusqueda, filtroEstado);
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────
    @FXML
    private void handleCrear(ActionEvent event) {
        navegarA("/proyecto/intermodular/app/views/crear_incidencia.fxml",
                 "CentroPlus Connect – Nueva incidencia", event);
    }

    @FXML
    private void handleEditar(ActionEvent event) {
        // TODO: comprobar que hay una incidencia seleccionada antes de navegar
        navegarA("/proyecto/intermodular/app/views/editar_incidencia.fxml",
                 "CentroPlus Connect – Editar incidencia", event);
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        // TODO: obtener la incidencia seleccionada y pedir confirmación
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar incidencia");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Seguro que deseas eliminar esta incidencia? Esta acción no se puede deshacer.");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // TODO: llamar al servicio para eliminar la incidencia
            System.out.println("Incidencia eliminada.");
            mostrarExito("Incidencia eliminada correctamente.");
        }
    }

    // ── Cambiar estado (desde la tarjeta) ────────────────────────────────────
    @FXML
    private void handleCambiarEstado(ActionEvent event) {
        // Cicla al siguiente estado en el array ESTADOS
        String estadoActual = LEstado.getText();
        String estadoSiguiente = siguienteEstado(estadoActual);

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cambiar estado");
        confirm.setHeaderText(null);
        confirm.setContentText(
            "¿Cambiar el estado de «" + estadoActual + "» a «" + estadoSiguiente + "»?"
        );
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // TODO: llamar al servicio para actualizar el estado en la BD
            LEstado.setText(estadoSiguiente);
            actualizarColorEstado(estadoSiguiente);
            System.out.printf("Estado cambiado: %s → %s%n", estadoActual, estadoSiguiente);
        }
    }

    /** Devuelve el siguiente estado en el ciclo; si es el último, vuelve al primero. */
    private String siguienteEstado(String estadoActual) {
        for (int i = 0; i < ESTADOS.length; i++) {
            if (ESTADOS[i].equalsIgnoreCase(estadoActual)) {
                return ESTADOS[(i + 1) % ESTADOS.length];
            }
        }
        return ESTADOS[0]; // fallback
    }

    /** Actualiza el color del label de estado según su valor. */
    private void actualizarColorEstado(String estado) {
        switch (estado) {
            case "Abierta"    -> LEstado.setStyle("-fx-text-fill: #a42525;"); // rojo
            case "En proceso" -> LEstado.setStyle("-fx-text-fill: #c98c22;"); // naranja
            case "Resuelta"   -> LEstado.setStyle("-fx-text-fill: #56b248;"); // verde
            case "Cerrada"    -> LEstado.setStyle("-fx-text-fill: #888888;"); // gris
        }
    }

    // ── Navegación inferior ──────────────────────────────────────────────────
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
    private void handleNavReservas(MouseEvent event) {
        navegarA("/proyecto/intermodular/app/views/reservas.fxml",
                 "CentroPlus Connect – Reservas", event);
    }

    // ── Utilidades de navegación ─────────────────────────────────────────────

    /** Navegación desde botones (ActionEvent). */
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

    /** Navegación desde labels de la barra inferior (MouseEvent). */
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

    // ── Alertas ──────────────────────────────────────────────────────────────
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
