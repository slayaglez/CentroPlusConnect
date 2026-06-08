package proyecto.intermodular.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class EditarIncidenciaController {

    @FXML private TextField  TIdUsuario;
    @FXML private TextField  TAsunto;
    @FXML private TextField  TDescripcion;
    @FXML private DatePicker DPFecha;
    @FXML private TextField  TEstado;
    @FXML private Button     BGuardarCambios;
    @FXML private Button     BVolver;

    // Datos de la incidencia a editar
    private int       incidenciaId;

    // Inicialización
    @FXML
    public void initialize() {
        // No permitir fechas futuras
        DPFecha.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisabled(empty || date.isAfter(LocalDate.now()));
            }
        });
    }


    public void cargarDatos(int id, String idUsuario, String asunto,
                            String descripcion, LocalDate fecha, String estado) {
        this.incidenciaId = id;
        TIdUsuario.setText(idUsuario);
        TAsunto.setText(asunto);
        TDescripcion.setText(descripcion);
        DPFecha.setValue(fecha);
        TEstado.setText(estado);
    }

    // Guardar cambios
    @FXML
    private void handleGuardarCambios(ActionEvent event) {
        String    idUsuario   = TIdUsuario.getText().trim();
        String    asunto      = TAsunto.getText().trim();
        String    descripcion = TDescripcion.getText().trim();
        LocalDate fecha       = DPFecha.getValue();
        String    estado      = TEstado.getText().trim();

        // Validación: campos obligatorios
        if (idUsuario.isEmpty() || asunto.isEmpty() || descripcion.isEmpty()
                || fecha == null || estado.isEmpty()) {
            mostrarError("Todos los campos son obligatorios.");
            return;
        }

        // Validación: id usuario (número entero positivo)
        if (!idUsuario.matches("^\\d+$") || Integer.parseInt(idUsuario) <= 0) {
            mostrarError("El id del usuario debe ser un número entero positivo.");
            return;
        }

        // Validación: asunto (máximo 100 caracteres)
        if (asunto.length() > 100) {
            mostrarError("El asunto no puede superar los 100 caracteres.");
            return;
        }

        // Validación: descripción (máximo 500 caracteres)
        if (descripcion.length() > 500) {
            mostrarError("La descripción no puede superar los 500 caracteres.");
            return;
        }

        // Validación: fecha no futura
        if (fecha.isAfter(LocalDate.now())) {
            mostrarError("La fecha de la incidencia no puede ser futura.");
            return;
        }

        // Validación: estado permitido
        if (!estado.equalsIgnoreCase("Abierta")
                && !estado.equalsIgnoreCase("En proceso")
                && !estado.equalsIgnoreCase("Resuelta")
                && !estado.equalsIgnoreCase("Cerrada")) {
            mostrarError("El estado debe ser: Abierta, En proceso, Resuelta o Cerrada.");
            return;
        }

        System.out.printf(
            "Incidencia editada → id=%d, idUsuario=%s, asunto='%s', descripcion='%s', fecha=%s, estado=%s%n",
            incidenciaId, idUsuario, asunto, descripcion, fecha, estado
        );

        mostrarExito("Incidencia «" + asunto + "» actualizada correctamente.");
        volverAIncidencias(event);
    }

    // Volver
    @FXML
    private void handleVolver(ActionEvent event) {
        volverAIncidencias(event);
    }

    // Navegación
    private void volverAIncidencias(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/proyecto/intermodular/app/views/incidencias.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CentroPlus Connect – Incidencias");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo volver a la pantalla de Incidencias.");
        }
    }

    // Alertas
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de validación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Incidencia actualizada");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
