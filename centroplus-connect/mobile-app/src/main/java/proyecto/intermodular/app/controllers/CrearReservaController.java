package proyecto.intermodular.app.controllers;

import java.io.IOException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import proyecto.intermodular.app.model.Reserva;
import proyecto.intermodular.app.service.ActividadService;
import proyecto.intermodular.app.service.ReservaService;
import proyecto.intermodular.app.service.UsuarioService;

public class CrearReservaController {

    @FXML private TextField  TIdUsuario;
    @FXML private TextField  TIdActividad;
    @FXML private DatePicker DPFecha;
    @FXML private ComboBox<String>  CEstado;
    @FXML private Button     BCrear;
    @FXML private Button     BVolver;

    private final ReservaService serviceR = new ReservaService();
    private final ActividadService serviceA = new ActividadService();
    private final UsuarioService serviceU = new UsuarioService();

    @FXML
    public void initialize() {
        DPFecha.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisabled(empty || date.isBefore(LocalDate.now()));
            }
        });
        CEstado.setItems(FXCollections.observableArrayList("Activa", "Cancelada"));
    }

    @FXML
    private void handleCrear(ActionEvent event) {
        String    nombreUsuario   = TIdUsuario.getText().trim();
        String    nombreActividad = TIdActividad.getText().trim();
        LocalDate fecha       = DPFecha.getValue();
        String    estado      = CEstado.getValue();

        if (nombreUsuario.isEmpty() || nombreActividad.isEmpty() || fecha == null || estado.isEmpty()) {
            mostrarError("Todos los campos son obligatorios.");
            return;
        }
        if (fecha.isBefore(LocalDate.now())) {
            mostrarError("La fecha de la reserva no puede ser anterior a hoy.");
            return;
        }
        if (!estado.equalsIgnoreCase("Activa")
                && !estado.equalsIgnoreCase("Cancelada")) {
            mostrarError("El estado debe ser: Activa o Cancelada.");
            return;
        }

        Integer idUsuario = serviceU.findIdByName(nombreUsuario);
        Integer idActividad = serviceA.findIdByName(nombreActividad);

        if(idUsuario == null){
            mostrarError("El usuario no existe");
        }
        if(idActividad == null){
            mostrarError("La actividad no existe");
        }

        Reserva r = new Reserva();
        r.setIdUsuario(idUsuario);
        r.setIdActividad(idActividad);
        r.setFecha(fecha);
        r.setEstado(estado);

        boolean ok = serviceR.create(r);

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