package proyecto.intermodular.app.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import proyecto.intermodular.app.repository.UsuarioRepository;
import proyecto.intermodular.app.service.PasswordService;

public class LoginController {

    @FXML private TextField     TEmail;
    @FXML private PasswordField TContrasenia;
    @FXML private Button        BEntrar;
    @FXML private Button        BCambiarIdioma;
    @FXML private Label         LAccesoSistema;
    @FXML private Label         LEmail;
    @FXML private Label         LContrasenia;

    // Password Hasher
    private final PasswordService paswordHasher = new PasswordService();
    private final UsuarioRepository repo = new UsuarioRepository();

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
        idiomaActual = idiomaActual.equals("ES") ? "EN" : "ES";

        if(idiomaActual.equals("ES")){
            BCambiarIdioma.setText("EN");
            LAccesoSistema.setText("Acceso al sistema");
            BEntrar.setText("Entrar");
            LEmail.setText("Correo electrónico");
            LContrasenia.setText("Contraseña");
            TContrasenia.setPromptText("Contraseña");
        } else {
            BCambiarIdioma.setText("ES");
            LAccesoSistema.setText("System access");
            BEntrar.setText("Enter");
            LEmail.setText("E-Mail");
            LContrasenia.setText("Password");
            TContrasenia.setPromptText("Password");
        }
        

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

    private boolean autenticarUsuario(String email, String contrasenia) {
        try {
            String storedHash = repo.findHashByEmail(email);
            return paswordHasher.verify(contrasenia, storedHash);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
