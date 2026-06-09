package proyecto.intermodular.app.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import proyecto.intermodular.app.model.Incidencia;
import proyecto.intermodular.app.service.IncidenciaService;

public class IncidenciaController {

    @FXML private TextField        TBuscarIncidencia;
    @FXML private ComboBox<String> CIncidencias;

    @FXML private Button BCrear;
    @FXML private Button BEditar;
    @FXML private Button BEliminar;
    @FXML private Button BCambiarEstado;

    @FXML private Label LAsunto;
    @FXML private Label LIdUsuario;
    @FXML private Label LFecha;
    @FXML private Label LEstado;
    @FXML private Label LDescripcion;
    @FXML private Label LContador;

    @FXML private Label LNavIncidencias;

    private static final String[] ESTADOS = {"Abierta", "En proceso", "Resuelta", "Cerrada"};

    private final IncidenciaService service = new IncidenciaService();
    private List<Incidencia> listaActual = new ArrayList<>();
    private int indiceActual = -1;

    @FXML
    public void initialize() {

        CIncidencias.setItems(FXCollections.observableArrayList(
            "Todos", "Abierta", "En proceso", "Resuelta", "Cerrada"
        ));
        CIncidencias.setValue("Todos");

        BEditar.setDisable(true);
        BEliminar.setDisable(true);
        BCambiarEstado.setDisable(true);

        cargarIncidencias("", "Todos");
    }

    @FXML
    private void handleBuscar(KeyEvent event) {
        cargarIncidencias(TBuscarIncidencia.getText().trim(), CIncidencias.getValue());
    }

    @FXML
    private void handleFiltro(ActionEvent event) {
        cargarIncidencias(TBuscarIncidencia.getText().trim(), CIncidencias.getValue());
    }

    @FXML
    private void handleCrear(ActionEvent event) {
        navegarA("/proyecto/intermodular/app/views/crear_incidencia.fxml",
                 "CentroPlus Connect – Nueva incidencia", event);
    }

    @FXML
    private void handleEditar(ActionEvent event) {
        if (indiceActual < 0 || indiceActual >= listaActual.size()) return;
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/proyecto/intermodular/app/views/editar_incidencia.fxml")
            );
            Parent root = loader.load();
            EditarIncidenciaController ctrl = loader.getController();
            Incidencia i = listaActual.get(indiceActual);
            ctrl.cargarDatos(i.getId(), String.valueOf(i.getIdUsuario()), i.getAsunto(),
                 i.getDescripcion(), i.getFecha(), i.getEstado());
            Stage stage = (Stage) BEditar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CentroPlus Connect – Editar incidencia");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo cargar la pantalla de edición.");
        }
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        if (indiceActual < 0 || indiceActual >= listaActual.size()) return;
        Incidencia i = listaActual.get(indiceActual);

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar incidencia");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Seguro que deseas eliminar la incidencia «" + i.getAsunto() + "»?");

        Optional<ButtonType> resultado = confirm.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean ok = service.deleteById(i.getId());
            if (ok) {
                limpiarSeleccion();
                cargarIncidencias(TBuscarIncidencia.getText().trim(), CIncidencias.getValue());
            } else {
                mostrarError("No se pudo eliminar la incidencia.");
            }
        }
    }

    @FXML
    private void handleCambiarEstado(ActionEvent event) {
        if (indiceActual < 0 || indiceActual >= listaActual.size()) return;
        Incidencia i = listaActual.get(indiceActual);
        String estadoSiguiente = siguienteEstado(i.getEstado());

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cambiar estado");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Cambiar el estado de «" + i.getEstado() + "» a «" + estadoSiguiente + "»?");

        Optional<ButtonType> resultado = confirm.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean ok = service.cambiarEstadoIncidencia(i.getId(), estadoSiguiente);
            if (ok) {
                cargarIncidencias(TBuscarIncidencia.getText().trim(), CIncidencias.getValue());
            } else {
                mostrarError("No se pudo cambiar el estado.");
            }
        }
    }

    @FXML
    private void handleAnterior(MouseEvent event) {
        if (listaActual.isEmpty()) return;
        indiceActual = (indiceActual - 1 + listaActual.size()) % listaActual.size();
        mostrarIncidencia(listaActual.get(indiceActual));
    }

    @FXML
    private void handleSiguiente(MouseEvent event) {
        if (listaActual.isEmpty()) return;
        indiceActual = (indiceActual + 1) % listaActual.size();
        mostrarIncidencia(listaActual.get(indiceActual));
    }

    @FXML private void handleNavInicio(MouseEvent e)      { navegarA("/proyecto/intermodular/app/views/dashboard.fxml",    "CentroPlus Connect – Inicio",      e); }
    @FXML private void handleNavUsuarios(MouseEvent e)    { navegarA("/proyecto/intermodular/app/views/usuarios.fxml",     "CentroPlus Connect – Usuarios",    e); }
    @FXML private void handleNavActividades(MouseEvent e) { navegarA("/proyecto/intermodular/app/views/actividades.fxml",  "CentroPlus Connect – Actividades", e); }
    @FXML private void handleNavReservas(MouseEvent e)    { navegarA("/proyecto/intermodular/app/views/reservas.fxml",     "CentroPlus Connect – Reservas",    e); }
    @FXML private void handleNavIncidencias(MouseEvent e) { /* ya estamos aqui */ }

    // Carga de datos
    private void cargarIncidencias(String texto, String estado) {
        List<Incidencia> todas = service.findAll();
        if (todas == null) todas = new ArrayList<>();

        listaActual = new ArrayList<>();
        String textoLow = texto.toLowerCase();

        for (Incidencia i : todas) {
            boolean coincideTexto = texto.isEmpty()
                || (i.getAsunto() != null && i.getAsunto().toLowerCase().contains(textoLow))
                || (i.getDescripcion() != null && i.getDescripcion().toLowerCase().contains(textoLow));
            boolean coincideEstado = "Todos".equals(estado) || estado == null
                || estado.equalsIgnoreCase(i.getEstado());
            if (coincideTexto && coincideEstado) listaActual.add(i);
        }

        if (!listaActual.isEmpty()) {
            indiceActual = 0;
            mostrarIncidencia(listaActual.get(0));
            BEditar.setDisable(false);
            BEliminar.setDisable(false);
            BCambiarEstado.setDisable(false);
        } else {
            indiceActual = -1;
            mostrarPlaceholder();
            limpiarSeleccion();
        }
    }

    private void mostrarIncidencia(Incidencia i) {
        LAsunto.setText(i.getAsunto() != null ? i.getAsunto() : "—");
        LIdUsuario.setText(i.getIdUsuario() != null ? String.valueOf(i.getIdUsuario()) : "—");
        LFecha.setText(i.getFecha() != null ? i.getFecha().toString() : "—");
        LDescripcion.setText(i.getDescripcion() != null ? i.getDescripcion() : "—");
        LEstado.setText(i.getEstado() != null ? i.getEstado() : "—");
        actualizarBadgeEstado(i.getEstado());
        LContador.setText((indiceActual + 1) + " / " + listaActual.size());
        BEditar.setDisable(false);
        BEliminar.setDisable(false);
        BCambiarEstado.setDisable(false);
    }

    private void mostrarPlaceholder() {
        LAsunto.setText("Sin resultados");
        LIdUsuario.setText("—");
        LFecha.setText("—");
        LDescripcion.setText("—");
        LEstado.setText("—");
        LContador.setText("0 / 0");
    }

    private void limpiarSeleccion() {
        BEditar.setDisable(true);
        BEliminar.setDisable(true);
        BCambiarEstado.setDisable(true);
    }

    private String siguienteEstado(String estadoActual) {
        for (int i = 0; i < ESTADOS.length; i++) {
            if (ESTADOS[i].equalsIgnoreCase(estadoActual)) {
                return ESTADOS[(i + 1) % ESTADOS.length];
            }
        }
        return ESTADOS[0];
    }

    private void actualizarBadgeEstado(String estado) {
        if (estado == null) return;
        switch (estado.toLowerCase()) {
            case "abierta"    -> LEstado.setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #842029; -fx-background-radius: 12; -fx-font-size: 12;");
            case "en proceso" -> LEstado.setStyle("-fx-background-color: #fff3cd; -fx-text-fill: #856404; -fx-background-radius: 12; -fx-font-size: 12;");
            case "resuelta"   -> LEstado.setStyle("-fx-background-color: #d1e7dd; -fx-text-fill: #0a3622; -fx-background-radius: 12; -fx-font-size: 12;");
            case "cerrada"    -> LEstado.setStyle("-fx-background-color: #e2e3e5; -fx-text-fill: #41464b; -fx-background-radius: 12; -fx-font-size: 12;");
            default           -> LEstado.setStyle("-fx-background-color: #cfe2ff; -fx-text-fill: #084298; -fx-background-radius: 12; -fx-font-size: 12;");
        }
    }

    // Navegación
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
            mostrarError("No se pudo cargar: " + fxmlPath);
        }
    }

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
            mostrarError("No se pudo cargar: " + fxmlPath);
        }
    }

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