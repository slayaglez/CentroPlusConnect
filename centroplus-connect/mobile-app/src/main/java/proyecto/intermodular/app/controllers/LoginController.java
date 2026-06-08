package proyecto.intermodular.app.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField     TEmail;
    @FXML private PasswordField TContrasenia;
    @FXML private Button        BEntrar;
    @FXML private Button        BCambiarIdioma;

    // Idioma activo
    private String idiomaActual = "ES";

    // Inicialización
    @FXML
    public void initialize() {
        // Permitir pulsar Enter en el campo contraseña para iniciar sesión
        TContrasenia.setOnAction(this::handleEntrar);
    }

    // Accion: boton Entrar
    @FXML
    private void handleEntrar(ActionEvent event) {
        String email      = TEmail.getText().trim();
        String contrasenia = TContrasenia.getText();

        // Validacion basica de campos vacíos
        if (email.isEmpty() || contrasenia.isEmpty()) {
            mostrarError("Por favor, introduce el email y la contraseña.");
            return;
        }

        // Validacion basica de formato email
        if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            mostrarError("El formato del email no es válido.");
            return;
        }

        boolean credencialesCorrectas = autenticarUsuario(email, contrasenia);

        if (credencialesCorrectas) {
            navegarAlDashboard(event);
        } else {
            mostrarError("Email o contraseña incorrectos.");
            TContrasenia.clear();
        }
    }

    // Accion: boton para cambiar idioma
    @FXML
    private void handleCambiarIdioma(ActionEvent event) {
        // Alterna entre ES y EN 
        idiomaActual = idiomaActual.equals("ES") ? "EN" : "ES";
        BCambiarIdioma.setText(idiomaActual.equals("ES") ? "Cambiar idioma" : "Change language");

        System.out.println("Idioma cambiado a: " + idiomaActual);
    }

    // Navegacion al Dashboard
    private void navegarAlDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/proyecto/intermodular/app/views/dashboard.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) BEntrar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CentroPlus Connect – Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo cargar la pantalla principal.");
        }
    }

    //! Autenticación provisional 
    /**
     * Sustituir este método por la llamada real al servicio/repositorio de usuarios.
     * Por ejemplo: return usuarioService.login(email, contrasenia);
     */
    private boolean autenticarUsuario(String email, String contrasenia) {
        // Credenciales de prueba
        return email.equals("admin@admin.com") && contrasenia.equals("admin");
    }

    // Utilidades
    private void mostrarError(String mensaje) {

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.ERROR
        );
        alert.setTitle("Error de acceso");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
