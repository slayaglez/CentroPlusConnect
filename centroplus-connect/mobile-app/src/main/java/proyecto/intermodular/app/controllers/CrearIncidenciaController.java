package proyecto.intermodular.app.controllers;

import java.io.IOException;
import java.time.LocalDate;

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
import proyecto.intermodular.app.model.Incidencia;
import proyecto.intermodular.app.service.IncidenciaService;

public class CrearIncidenciaController {

    @FXML private TextField  TIdUsuario;
    @FXML private TextField  TAsunto;
    @FXML private TextField  TDescripcion;
    @FXML private DatePicker DPFecha;
    @FXML private TextField  TEstado;
    @FXML private Button     BCrear;
    @FXML private Button     BVolver;

    private final IncidenciaService service = new IncidenciaService();

    @FXML
    public void initialize() {
        DPFecha.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisabled(empty || date.isAfter(LocalDate.now()));
            }
        });
        DPFecha.setValue(LocalDate.now());
        TEstado.setText("Abierta");
    }

    @FXML
    private void handleCrear(ActionEvent event) {
        String    idUsuario   = TIdUsuario.getText().trim();
        String    asunto      = TAsunto.getText().trim();
        String    descripcion = TDescripcion.getText().trim();
        LocalDate fecha       = DPFecha.getValue();
        String    estado      = TEstado.getText().trim();

        if (idUsuario.isEmpty() || asunto.isEmpty() || descripcion.isEmpty()
                || fecha == null || estado.isEmpty()) {
            mostrarError("Todos los campos son obligatorios.");
            return;
        }
        if (!idUsuario.matches("^\\d+$") || Integer.parseInt(idUsuario) <= 0) {
            mostrarError("El id del usuario debe ser un número entero positivo.");
            return;
        }
        if (asunto.length() > 100) {
            mostrarError("El asunto no puede superar los 100 caracteres.");
            return;
        }
        if (descripcion.length() > 500) {
            mostrarError("La descripción no puede superar los 500 caracteres.");
            return;
        }
        if (fecha.isAfter(LocalDate.now())) {
            mostrarError("La fecha de la incidencia no puede ser futura.");
            return;
        }
        if (!estado.equalsIgnoreCase("Abierta")
                && !estado.equalsIgnoreCase("En proceso")
                && !estado.equalsIgnoreCase("Resuelta")
                && !estado.equalsIgnoreCase("Cerrada")) {
            mostrarError("El estado debe ser: Abierta, En proceso, Resuelta o Cerrada.");
            return;
        }

        Incidencia i = new Incidencia();
        i.setIdUsuario(Integer.parseInt(idUsuario));
        i.setAsunto(asunto);
        i.setDescripcion(descripcion);
        i.setFecha(fecha);
        i.setEstado(estado);

        boolean ok = service.create(i);

        if (ok) {
            mostrarExito("Incidencia «" + asunto + "» creada correctamente.");
            volverAIncidencias(event);
        } else {
            mostrarError("No se pudo guardar la incidencia. Comprueba que el usuario existe.");
        }
    }

    @FXML
    private void handleVolver(ActionEvent event) {
        volverAIncidencias(event);
    }

    private void volverAIncidencias(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/proyecto/intermodular/app/views/incidencias.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CentroPlus Connect - Incidencias");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo volver a la pantalla de Incidencias.");
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de validación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Incidencia creada");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}