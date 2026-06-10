package proyecto.intermodular.app.controllers;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import proyecto.intermodular.app.model.Actividad;
import proyecto.intermodular.app.service.ActividadService;

public class EditarActividadController {

    @FXML private TextField        TNombre;
    @FXML private ComboBox<String> CTipoActividad;
    @FXML private TextField        TDuracion;
    @FXML private TextField        TPrecio;
    @FXML private TextField        TPlazasMaximas;
    @FXML private Button           BGuardarCambios;
    @FXML private Button           BVolver;

    private final ActividadService service = new ActividadService();

    // ID de la actividad que se esta editando
    private int actividadId = -1;

    // Inicializacion
    @FXML
    public void initialize() {
        CTipoActividad.setItems(FXCollections.observableArrayList(
            "Yoga", "Pilates", "Natación", "Musculación", "Spinning", "Zumba", "Artes marciales", "Otro"
        ));
    }


    public void setActividad(Integer id, String nombre, String tipo,
                             Integer duracion, double precio, Integer plazasMaximas) {
        this.actividadId = id;
        TNombre.setText(nombre);
        CTipoActividad.setValue(tipo);
        TDuracion.setText(String.valueOf(duracion));
        TPrecio.setText(String.valueOf(precio));
        TPlazasMaximas.setText(String.valueOf(plazasMaximas));
    }

    // Guardar cambios
    @FXML
    private void handleGuardarCambios(ActionEvent event) {
        String nombre   = TNombre.getText().trim();
        String tipo     = CTipoActividad.getValue();
        String duracion = TDuracion.getText().trim();
        String precio   = TPrecio.getText().trim();
        String plazas   = TPlazasMaximas.getText().trim();

        // Validación: campos obligatorios
        if (nombre.isEmpty() || tipo == null || duracion.isEmpty() || precio.isEmpty() || plazas.isEmpty()) {
            mostrarError("Todos los campos son obligatorios.");
            return;
        }

        // Validación: duración (número entero positivo, en minutos)
        if (!duracion.matches("^\\d+$") || Integer.parseInt(duracion) <= 0) {
            mostrarError("La duración debe ser un número entero de minutos mayor que 0.");
            return;
        }

        // Validación: precio (número decimal positivo)
        if (!precio.matches("^\\d+(\\.\\d{1,2})?$") || Double.parseDouble(precio) < 0) {
            mostrarError("El precio debe ser un número válido (ej: 15 o 15.50).");
            return;
        }

        // Validación: plazas máximas (número entero positivo)
        if (!plazas.matches("^\\d+$") || Integer.parseInt(plazas) <= 0) {
            mostrarError("Las plazas máximas deben ser un número entero mayor que 0.");
            return;
        }

        Actividad actividad = service.findById(actividadId);
        Actividad actividadNueva = new Actividad(actividadId, nombre, tipo, Integer.parseInt(duracion), Double.parseDouble(precio), Integer.parseInt(plazas), actividad.getPlazasOcupadas());
        service.update(actividadNueva);

        mostrarExito("Actividad «" + nombre + "» actualizada correctamente.");
        volverAActividades(event);
    }

    // Volver
    @FXML
    private void handleVolver(ActionEvent event) {
        volverAActividades(event);
    }

    // Navegación
    private void volverAActividades(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/proyecto/intermodular/app/views/actividades.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CentroPlus Connect – Actividades");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo volver a la pantalla de Actividades.");
        }
    }

    // Utilidades
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de validación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Actividad actualizada");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
