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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import proyecto.intermodular.app.model.Actividad;
import proyecto.intermodular.app.service.ActividadService;

public class ActividadController {

    @FXML
    private TextField TActividad;
    @FXML
    private ComboBox<String> CActividades;

    @FXML
    private Button BCrear;
    @FXML
    private Button BEditar;
    @FXML
    private Button BEliminar;

    @FXML
    private Label LNombre;
    @FXML
    private Label LDuracion;
    @FXML
    private Label LPrecio;
    @FXML
    private Label LTipoActividad;
    @FXML
    private ProgressBar PBActividades;
    @FXML
    private Label LPlazasOcupadas;
    @FXML
    private Label LTotalPlazas;
    @FXML
    private Button BReservarPlazas;

    @FXML
    private Label LNavActividades;

    @FXML
    private Label LContador;

    private final ActividadService service = new ActividadService();
    private List<Actividad> listaActual = new ArrayList<>();
    private int indiceActual = -1;

    @FXML
    public void initialize() {

        CActividades.setItems(FXCollections.observableArrayList(
                "Todos", "Deportiva", "Academica"));
        CActividades.setValue("Todos");

        BEditar.setDisable(true);
        BEliminar.setDisable(true);
        BReservarPlazas.setDisable(true);

        cargarActividades("", "Todos");
    }

    @FXML
    private void handleBuscar(KeyEvent event) {
        cargarActividades(TActividad.getText().trim(), CActividades.getValue());
    }

    @FXML
    private void handleFiltro(ActionEvent event) {
        cargarActividades(TActividad.getText().trim(), CActividades.getValue());
    }

    @FXML
    private void handleCrear(ActionEvent event) {
        navegarA("/proyecto/intermodular/app/views/crear_actividad.fxml",
                "CentroPlus Connect - Crear actividad", event);
    }

    @FXML
    private void handleEditar(ActionEvent event) {
        if (indiceActual < 0 || indiceActual >= listaActual.size())
            return;
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/proyecto/intermodular/app/views/editar_actividad.fxml"));
            Parent root = loader.load();
            EditarActividadController ctrl = loader.getController();
            Actividad a = listaActual.get(indiceActual);
            ctrl.setActividad(a.getId(), a.getNombre(), a.getTipoActividad(),
                    a.getDuracion(), a.getPrecio(), a.getPlazasMaximas());
            Stage stage = (Stage) BEditar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CentroPlus Connect - Editar actividad");
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
        Actividad a = listaActual.get(indiceActual);

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar actividad");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Seguro que quieres eliminar «" + a.getNombre() + "»?");

        Optional<ButtonType> resultado = confirm.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean ok = service.deleteById(a.getId());
            if (ok) {
                limpiarSeleccion();
                cargarActividades(TActividad.getText().trim(), CActividades.getValue());
            } else {
                mostrarError("No se pudo eliminar la actividad.");
            }
        }
    }

    @FXML
    private void handleReservarPlazas(ActionEvent event) {
        if (indiceActual < 0 || indiceActual >= listaActual.size())
            return;
        navegarA("/proyecto/intermodular/app/views/crear_reservas.fxml",
                "CentroPlus Connect - Nueva Reserva", event);
    }

    // Navegacion tarjeta anterior / siguiente
    @FXML
    private void handleAnterior(MouseEvent event) {
        if (listaActual.isEmpty())
            return;
        indiceActual = (indiceActual - 1 + listaActual.size()) % listaActual.size();
        mostrarActividad(listaActual.get(indiceActual));
    }

    @FXML
    private void handleSiguiente(MouseEvent event) {
        if (listaActual.isEmpty())
            return;
        indiceActual = (indiceActual + 1) % listaActual.size();
        mostrarActividad(listaActual.get(indiceActual));
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
        /* ya estamos aquí */ }

    @FXML
    private void handleNavReservas(MouseEvent e) {
        navegarA("/proyecto/intermodular/app/views/reservas.fxml", "CentroPlus Connect - Reservas", e);
    }

    @FXML
    private void handleNavIncidencias(MouseEvent e) {
        navegarA("/proyecto/intermodular/app/views/incidencias.fxml", "CentroPlus Connect - Incidencias", e);
    }

    // Carga de datos
    private void cargarActividades(String texto, String tipo) {
        List<Actividad> todas = service.findAll();
        if (todas == null)
            todas = new ArrayList<>();

        listaActual = new ArrayList<>();
        String textoLow = texto.toLowerCase();
        for (Actividad a : todas) {
            boolean coincideTexto = texto.isEmpty()
                    || (a.getNombre() != null && a.getNombre().toLowerCase().contains(textoLow));
            boolean coincideTipo = "Todos".equals(tipo) || tipo == null
                    || tipo.equalsIgnoreCase(a.getTipoActividad());
            if (coincideTexto && coincideTipo)
                listaActual.add(a);
        }

        if (!listaActual.isEmpty()) {
            indiceActual = 0;
            mostrarActividad(listaActual.get(0));
            BEditar.setDisable(false);
            BEliminar.setDisable(false);
            BReservarPlazas.setDisable(false);
        } else {
            indiceActual = -1;
            mostrarPlaceholder();
            limpiarSeleccion();
        }
    }

    private void mostrarActividad(Actividad a) {
        LNombre.setText(a.getNombre() != null ? a.getNombre() : "—");
        LDuracion.setText(a.getDuracion() != null ? a.getDuracion() + " min" : "—");
        LPrecio.setText(String.format("%.2f €", a.getPrecio()));
        LTipoActividad.setText(a.getTipoActividad() != null ? a.getTipoActividad() : "—");
        LContador.setText((indiceActual + 1) + " / " + listaActual.size()); // ← añadir

        if (a.getPlazasMaximas() != null && a.getPlazasMaximas() > 0) {
            double progreso = (double) a.getPlazasOcupadas() / a.getPlazasMaximas();
            PBActividades.setProgress(progreso);
            LPlazasOcupadas.setText("Ocupadas: " + a.getPlazasOcupadas());
            LTotalPlazas.setText("/ " + a.getPlazasMaximas());
        } else {
            PBActividades.setProgress(0.0);
            LPlazasOcupadas.setText("Ocupadas: 0");
            LTotalPlazas.setText("/ 0");
        }

        BEditar.setDisable(false);
        BEliminar.setDisable(false);
        BReservarPlazas.setDisable(false);
    }

    private void mostrarPlaceholder() {
        LNombre.setText("Sin resultados");
        LDuracion.setText("—");
        LPrecio.setText("—");
        LTipoActividad.setText("—");
        PBActividades.setProgress(0.0);
        LPlazasOcupadas.setText("Plazas ocupadas: 0");
        LTotalPlazas.setText("/ 0");
    }

    private void limpiarSeleccion() {
        indiceActual = -1;
        BEditar.setDisable(true);
        BEliminar.setDisable(true);
        BReservarPlazas.setDisable(true);
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
