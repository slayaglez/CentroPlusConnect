package proyecto.intermodular.app.controllers;

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

import java.io.IOException;


public class EditarUsuarioController {

    @FXML private TextField        TNombre;
    @FXML private TextField        TDNI;
    @FXML private TextField        TEmail;
    @FXML private TextField        TTelefono;
    @FXML private ComboBox<String> CTipoUsuario;
    @FXML private Button           BGuardarCambios;
    @FXML private Button           BVolver;

    // ID interno del usuario que se está editando (para pasarlo al servicio)
    private int usuarioId = -1;

    // Inicialización
    @FXML
    public void initialize() {
        CTipoUsuario.setItems(FXCollections.observableArrayList(
            "Administrador", "Empleado", "Cliente"
        ));
    }

    // API pública: pre-rellenar datos del usuario
    public void setUsuario(int id, String nombre, String dni,
                           String email, String telefono, String tipo) {
        this.usuarioId = id;
        TNombre.setText(nombre);
        TDNI.setText(dni);
        TEmail.setText(email);
        TTelefono.setText(telefono != null ? telefono : "");
        CTipoUsuario.setValue(tipo);
    }

    // Guardar cambios
    @FXML
    private void handleGuardarCambios(ActionEvent event) {
        String nombre   = TNombre.getText().trim();
        String dni      = TDNI.getText().trim();
        String email    = TEmail.getText().trim();
        String telefono = TTelefono.getText().trim();
        String tipo     = CTipoUsuario.getValue();

        // Validación: campos obligatorios
        if (nombre.isEmpty() || dni.isEmpty() || email.isEmpty() || tipo == null) {
            mostrarError("Nombre, DNI, Email y Tipo de usuario son obligatorios.");
            return;
        }

        // Validación: formato DNI (8 dígitos + letra)
        if (!dni.matches("^\\d{8}[A-Za-z]$")) {
            mostrarError("El DNI no tiene un formato válido (ej: 12345678A).");
            return;
        }

        // Validación: formato email
        if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            mostrarError("El formato del email no es válido.");
            return;
        }

        // Validación: teléfono (9 dígito)
        if (!telefono.isEmpty() && !telefono.matches("^\\d{9}$")) {
            mostrarError("El teléfono debe tener 9 dígitos.");
            return;
        }

        System.out.printf("Usuario actualizado → id=%d, nombre=%s, dni=%s, email=%s, tel=%s, tipo=%s%n",
                          usuarioId, nombre, dni, email, telefono, tipo);

        mostrarExito("Usuario «" + nombre + "» actualizado correctamente.");
        volverAUsuarios(event);
    }

    // Volver
    @FXML
    private void handleVolver(ActionEvent event) {
        volverAUsuarios(event);
    }

    // Navegación
    private void volverAUsuarios(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/proyecto/intermodular/app/views/usuarios.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CentroPlus Connect – Usuarios");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo volver a la pantalla de Usuarios.");
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
        alert.setTitle("Usuario actualizado");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
