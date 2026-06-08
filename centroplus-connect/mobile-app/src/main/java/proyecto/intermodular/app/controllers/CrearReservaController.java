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
import proyecto.intermodular.app.model.Reserva;
import proyecto.intermodular.app.service.ReservaService;

public class CrearReservaController {

    @FXML private TextField  TIdUsuario;
    @FXML private TextField  TIdActividad;
    @FXML private DatePicker DPFecha;
    @FXML private TextField  TEstado;
    @FXML private Button     BCrear;
    @FXML private Button     BVolver;

    private final ReservaService service = new ReservaService();

    @FXML
    public void initialize() {
        DPFecha.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisabled(empty || date.isBefore(LocalDate.now()));
            }
        });
        TEstado.setText("Pendiente");
    }

    @FXML
    private void handleCrear(ActionEvent event) {
        String    idUsuario   = TIdUsuario.getText().trim();
        String    idActividad = TIdActividad.getText().trim();
        LocalDate fecha       = DPFecha.getValue();
        String    estado      = TEstado.getText().trim();

        if (idUsuario.isEmpty() || idActividad.isEmpty() || fecha == null || estado.isEmpty()) {
            mostrarError("Todos los campos son obligatorios.");
            return;
        }
        if (!idUsuario.matches("^\\d+$") || Integer.parseInt(idUsuario) <= 0) {
            mostrarError("El id del usuario debe ser un número entero positivo.");
            return;
        }
        if (!idActividad.matches("^\\d+$") || Integer.parseInt(idActividad) <= 0) {
            mostrarError("El id de la actividad debe ser un número entero positivo.");
            return;
        }
        if (fecha.isBefore(LocalDate.now())) {
            mostrarError("La fecha de la reserva no puede ser anterior a hoy.");
            return;
        }
        if (!estado.equalsIgnoreCase("Confirmada")
                && !estado.equalsIgnoreCase("Pendiente")
                && !estado.equalsIgnoreCase("Cancelada")) {
            mostrarError("El estado debe ser: Confirmada, Pendiente o Cancelada.");
            return;
        }

        Reserva r = new Reserva();
        r.setIdUsuario(Integer.parseInt(idUsuario));
        r.setIdActividad(Integer.parseInt(idActividad));
        r.setFecha(fecha);
        r.setEstado(estado);

        boolean ok = service.create(r);

        if (ok) {
            mostrarExito("Reserva creada correctamente para el " + fecha + ".");
            volverAReservas(event);
        } else {
            mostrarError("No se pudo guardar la reserva. Comprueba que el usuario y la actividad existen.");
        }
    }

    @FXML
    private void handleVolver(ActionEvent event) {
        volverAReservas(event);
    }

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

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de validación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reserva creada");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}