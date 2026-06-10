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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import proyecto.intermodular.app.model.Usuario;
import proyecto.intermodular.app.service.UsuarioService;

public class CrearUsuarioController {

    @FXML private TextField        TNombre;
    @FXML private TextField        TDNI;
    @FXML private TextField        TEmail;
    @FXML private TextField        TTelefono;
    @FXML private PasswordField    TPassword;
    @FXML private PasswordField    TPasswordConfirm;
    @FXML private ComboBox<String> CTipoUsuario;
    @FXML private Button           BCrear;
    @FXML private Button           BVolver;

    private final UsuarioService service = new UsuarioService();

    @FXML
    public void initialize() {
        CTipoUsuario.setItems(FXCollections.observableArrayList(
            "Socio", "Alumno"
        ));
    }

    @FXML
    private void handleCrear(ActionEvent event) {
        String nombre    = TNombre.getText().trim();
        String dni       = TDNI.getText().trim();
        String email     = TEmail.getText().trim();
        String telefono  = TTelefono.getText().trim();
        String password  = TPassword.getText();
        String confirm   = TPasswordConfirm.getText();
        String tipo      = CTipoUsuario.getValue();

        if (nombre.isBlank() || dni.isBlank() || email.isBlank()
                || password.isBlank() || tipo == null) {
            mostrarError("Nombre, DNI, Email, Contraseña y Tipo de usuario son obligatorios.");
            return;
        }
        if (!dni.matches("^\\d{8}[A-Za-z]$")) {
            mostrarError("El DNI no tiene un formato válido (ej: 12345678A).");
            return;
        }
        if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            mostrarError("El formato del email no es válido.");
            return;
        }
        if (!telefono.isBlank() && !telefono.matches("^\\d{9}$")) {
            mostrarError("El teléfono debe tener 9 dígitos.");
            return;
        }
        if (password.length() < 6) {
            mostrarError("La contraseña debe tener al menos 6 caracteres.");
            return;
        }
        if (!password.equals(confirm)) {
            mostrarError("Las contraseñas no coinciden.");
            return;
        }

        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setDni(dni);
        u.setEmail(email);
        u.setTelefono(telefono.isBlank() ? null : telefono);
        u.setTipoUsuario(tipo);

        boolean ok = service.create(u, password);
        if (ok) {
            mostrarExito("Usuario «" + nombre + "» creado correctamente.");
            volverAUsuarios(event);
        } else {
            mostrarError("No se pudo guardar el usuario. Comprueba que el DNI no esté repetido.");
        }
    }

    @FXML
    private void handleVolver(ActionEvent event) {
        volverAUsuarios(event);
    }

    private void volverAUsuarios(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/proyecto/intermodular/app/views/usuarios.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CentroPlus Connect - Usuarios");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo volver a la pantalla de Usuarios.");
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
        alert.setTitle("Usuario creado");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}