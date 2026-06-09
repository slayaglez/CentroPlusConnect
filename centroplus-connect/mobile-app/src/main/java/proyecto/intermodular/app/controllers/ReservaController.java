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
import proyecto.intermodular.app.model.ReservaDetalle;
import proyecto.intermodular.app.service.ReservaService;

public class ReservaController {

    @FXML
    private TextField TBuscarReserva;
    @FXML
    private ComboBox<String> CReservas;

    @FXML
    private Button BCrear;
    @FXML
    private Button BEditar;
    @FXML
    private Button BEliminar;
    @FXML
    private Button BCancelarReserva;

    @FXML
    private Label LId;
    @FXML
    private Label LNombreUsuario;
    @FXML
    private Label LNombreActividad;
    @FXML
    private Label LFecha;
    @FXML
    private Label LEstado;
    @FXML
    private Label LContador;

    @FXML
    private Label LNavReservas;


    private final ReservaService service = new ReservaService();
    private List<ReservaDetalle> listaActual = new ArrayList<>();
    private int indiceActual = -1;

    @FXML
    public void initialize() {

        CReservas.setItems(FXCollections.observableArrayList(
                "Todos", "Confirmada", "Pendiente", "Cancelada"));
        CReservas.setValue("Todos");

        BEditar.setDisable(true);
        BEliminar.setDisable(true);
        BCancelarReserva.setDisable(true);

        cargarReservas("", "Todos");
    }

    @FXML
    private void handleBuscar(KeyEvent event) {
        cargarReservas(TBuscarReserva.getText().trim(), CReservas.getValue());
    }

    @FXML
    private void handleFiltro(ActionEvent event) {
        cargarReservas(TBuscarReserva.getText().trim(), CReservas.getValue());
    }

    @FXML
    private void handleCrear(ActionEvent event) {
        navegarA("/proyecto/intermodular/app/views/crear_reserva.fxml",
                "CentroPlus Connect - Nueva reserva", event);
    }

    @FXML
    private void handleEditar(ActionEvent event) {
        if (indiceActual < 0 || indiceActual >= listaActual.size())
            return;
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/proyecto/intermodular/app/views/editar_reserva.fxml"));
            Parent root = loader.load();
            EditarReservaController ctrl = loader.getController();
            ReservaDetalle r = listaActual.get(indiceActual);
            ctrl.setReserva(r.id, r.idUsuario, r.idActividad, r.fecha, r.estado);
            Stage stage = (Stage) BEditar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CentroPlus Connect - Editar reserva");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo cargar la pantalla de edición.");
        }
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        if (indiceActual < 0 || indiceActual >= listaActual.size())
            return;
        ReservaDetalle r = listaActual.get(indiceActual);

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar reserva");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Seguro que deseas eliminar la reserva nº " + r.id + "?");

        Optional<ButtonType> resultado = confirm.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean ok = service.deleteById(r.id);
            if (ok) {
                limpiarSeleccion();
                cargarReservas(TBuscarReserva.getText().trim(), CReservas.getValue());
            } else {
                mostrarError("No se pudo eliminar la reserva.");
            }
        }
    }

    @FXML
    private void handleCancelarReserva(ActionEvent event) {
        if (indiceActual < 0 || indiceActual >= listaActual.size())
            return;
        ReservaDetalle r = listaActual.get(indiceActual);

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cancelar reserva");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Deseas cancelar la reserva nº " + r.id + "?");

        Optional<ButtonType> resultado = confirm.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean ok = service.cancelarReserva(r.id);
            if (ok) {
                cargarReservas(TBuscarReserva.getText().trim(), CReservas.getValue());
            } else {
                mostrarError("No se pudo cancelar la reserva.");
            }
        }
    }

    @FXML
    private void handleAnterior(MouseEvent event) {
        if (listaActual.isEmpty())
            return;
        indiceActual = (indiceActual - 1 + listaActual.size()) % listaActual.size();
        mostrarReserva(listaActual.get(indiceActual));
    }

    @FXML
    private void handleSiguiente(MouseEvent event) {
        if (listaActual.isEmpty())
            return;
        indiceActual = (indiceActual + 1) % listaActual.size();
        mostrarReserva(listaActual.get(indiceActual));
    }

    @FXML
    private void handleNavInicio(MouseEvent e) {
        navegarA("/proyecto/intermodular/app/views/dashboard.fxml", "CentroPlus Connect - Inicio", e);
    }

    @FXML
    private void handleNavUsuarios(MouseEvent e) {
        navegarA("/proyecto/intermodular/app/views/usuarios.fxml", "CentroPlus Connect - Usuarios", e);
    }

    @FXML
    private void handleNavActividades(MouseEvent e) {
        navegarA("/proyecto/intermodular/app/views/actividades.fxml", "CentroPlus Connect - Actividades", e);
    }

    @FXML
    private void handleNavReservas(MouseEvent e) {
        /* ya estamos aquí */ }

    @FXML
    private void handleNavIncidencias(MouseEvent e) {
        navegarA("/proyecto/intermodular/app/views/incidencias.fxml", "CentroPlus Connect - Incidencias", e);
    }

    // Carga de datos
    private void cargarReservas(String texto, String estado) {
        List<ReservaDetalle> todas = service.findAllConDetalle();
        if (todas == null)
            todas = new ArrayList<>();

        listaActual = new ArrayList<>();
        String textoLow = texto.toLowerCase();

        for (ReservaDetalle r : todas) {
            boolean coincideTexto = texto.isEmpty()
                    || r.nombreUsuario.toLowerCase().contains(textoLow)
                    || r.nombreActividad.toLowerCase().contains(textoLow);
            boolean coincideEstado = "Todos".equals(estado) || estado == null
                    || estado.equalsIgnoreCase(r.estado);
            if (coincideTexto && coincideEstado)
                listaActual.add(r);
        }

        if (!listaActual.isEmpty()) {
            indiceActual = 0;
            mostrarReserva(listaActual.get(0));
            BEditar.setDisable(false);
            BEliminar.setDisable(false);
            BCancelarReserva.setDisable(false);
        } else {
            indiceActual = -1;
            mostrarPlaceholder();
            limpiarSeleccion();
        }
    }

    private void mostrarReserva(ReservaDetalle r) {
        LId.setText("Reserva nº " + r.id);
        LNombreUsuario.setText(r.nombreUsuario);
        LNombreActividad.setText(r.nombreActividad);
        LFecha.setText(r.fecha.toString());
        LEstado.setText(r.estado);
        actualizarBadgeEstado(r.estado);
        LContador.setText((indiceActual + 1) + " / " + listaActual.size());
        BEditar.setDisable(false);
        BEliminar.setDisable(false);
        BCancelarReserva.setDisable("Cancelada".equalsIgnoreCase(r.estado));
    }

    private void actualizarBadgeEstado(String estado) {
        switch (estado.toLowerCase()) {
            case "confirmada" -> LEstado.setStyle(
                    "-fx-background-color: #d1e7dd; -fx-text-fill: #0a3622; -fx-background-radius: 12; -fx-font-size: 12;");
            case "cancelada" -> LEstado.setStyle(
                    "-fx-background-color: #f8d7da; -fx-text-fill: #842029; -fx-background-radius: 12; -fx-font-size: 12;");
            default -> LEstado.setStyle(
                    "-fx-background-color: #cfe2ff; -fx-text-fill: #084298; -fx-background-radius: 12; -fx-font-size: 12;");
        }
    }

    private void mostrarPlaceholder() {
        LId.setText("Sin resultados");
        LNombreUsuario.setText("—");
        LNombreActividad.setText("—");
        LFecha.setText("—");
        LEstado.setText("—");
        LContador.setText("0 / 0");
    }

    private void limpiarSeleccion() {
        BEditar.setDisable(true);
        BEliminar.setDisable(true);
        BCancelarReserva.setDisable(true);
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
}