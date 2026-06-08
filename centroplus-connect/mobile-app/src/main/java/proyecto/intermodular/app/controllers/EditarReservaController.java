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

public class EditarReservaController {

    @FXML private TextField  TIdUsuario;
    @FXML private TextField  TIdActividad;
    @FXML private DatePicker DPFecha;
    @FXML private TextField  TEstado;
    @FXML private Button     BGuardarCambios;
    @FXML private Button     BVolver;

    // ID de la reserva que se está editando
    private int reservaId = -1;

    // Inicialización
    @FXML
    public void initialize() {
        // Deshabilitar días anteriores a hoy en el DatePicker
        DPFecha.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisabled(empty || date.isBefore(LocalDate.now()));
            }
        });
    }


    public void setReserva(int id, int idUsuario, int idActividad,
                           LocalDate fecha, String estado) {
        this.reservaId = id;
        TIdUsuario.setText(String.valueOf(idUsuario));
        TIdActividad.setText(String.valueOf(idActividad));
        DPFecha.setValue(fecha);
        TEstado.setText(estado);
    }

    // Guardar cambios
    @FXML
    private void handleGuardarCambios(ActionEvent event) {
        String    idUsuario   = TIdUsuario.getText().trim();
        String    idActividad = TIdActividad.getText().trim();
        LocalDate fecha       = DPFecha.getValue();
        String    estado      = TEstado.getText().trim();

        // Validación: campos obligatorios
        if (idUsuario.isEmpty() || idActividad.isEmpty() || fecha == null || estado.isEmpty()) {
            mostrarError("Todos los campos son obligatorios.");
            return;
        }

        // Validación: id usuario (número entero positivo)
        if (!idUsuario.matches("^\\d+$") || Integer.parseInt(idUsuario) <= 0) {
            mostrarError("El id del usuario debe ser un número entero positivo.");
            return;
        }

        // Validación: id actividad (número entero positivo)
        if (!idActividad.matches("^\\d+$") || Integer.parseInt(idActividad) <= 0) {
            mostrarError("El id de la actividad debe ser un número entero positivo.");
            return;
        }

        // Validación: fecha no anterior a hoy
        if (fecha.isBefore(LocalDate.now())) {
            mostrarError("La fecha de la reserva no puede ser anterior a hoy.");
            return;
        }

        // Validación: estado permitido
        if (!estado.equalsIgnoreCase("Confirmada")
                && !estado.equalsIgnoreCase("Pendiente")
                && !estado.equalsIgnoreCase("Cancelada")) {
            mostrarError("El estado debe ser: Confirmada, Pendiente o Cancelada.");
            return;
        }

        //TODO persistencia en BBDD
        System.out.printf("Reserva actualizada → id=%d, idUsuario=%s, idActividad=%s, fecha=%s, estado=%s%n",
                          reservaId, idUsuario, idActividad, fecha, estado);

        mostrarExito("Reserva actualizada correctamente.");
        volverAReservas(event);
    }

    // Volver
    @FXML
    private void handleVolver(ActionEvent event) {
        volverAReservas(event);
    }

    // Navegación
    private void volverAReservas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/proyecto/intermodular/app/views/reservas.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CentroPlus Connect – Reservas");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo volver a la pantalla de Reservas.");
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
        alert.setTitle("Reserva actualizada");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
