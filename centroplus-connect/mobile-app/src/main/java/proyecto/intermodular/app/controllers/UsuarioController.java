package proyecto.intermodular.app.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import proyecto.intermodular.app.model.Usuario;
import proyecto.intermodular.app.service.UsuarioService;

public class UsuarioController {

    @FXML
    private TextField TBuscarUsuario;
    @FXML
    private ComboBox<String> CUsuarios;

    @FXML
    private Button BCrear;
    @FXML
    private Button BEditar;
    @FXML
    private Button BEliminar;

    @FXML
    private Label LNombre;
    @FXML
    private Label LDNI;
    @FXML
    private Label LEmail;
    @FXML
    private Label LTelefono;
    @FXML
    private Label LTipoUsuario;

    @FXML
    private Label LNavUsuarios;

    @FXML
    private Label LContador;

    private final UsuarioService service = new UsuarioService();
    private List<Usuario> listaActual = new ArrayList<>();
    private int indiceActual = -1;

    @FXML
    public void initialize() {

        ObservableList<String> tipos = FXCollections.observableArrayList(
                "Todos", "Alumno", "Socio");
        CUsuarios.setItems(tipos);
        CUsuarios.setValue("Todos");

        BEditar.setDisable(true);
        BEliminar.setDisable(true);

        cargarUsuarios("", "Todos");
    }

    @FXML
    private void handleBuscar(KeyEvent event) {
        cargarUsuarios(TBuscarUsuario.getText().trim(), CUsuarios.getValue());
    }

    @FXML
    private void handleFiltro(ActionEvent event) {
        cargarUsuarios(TBuscarUsuario.getText().trim(), CUsuarios.getValue());
    }

    @FXML
    private void handleCrear(ActionEvent event) {
        navegarA("/proyecto/intermodular/app/views/crear_usuario.fxml",
                "CentroPlus Connect – Crear usuario", event);
    }

    @FXML
    private void handleEditar(ActionEvent event) {
        if (indiceActual < 0 || indiceActual >= listaActual.size())
            return;
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/proyecto/intermodular/app/views/editar_usuario.fxml"));
            Parent root = loader.load();
            EditarUsuarioController ctrl = loader.getController();
            Usuario u = listaActual.get(indiceActual);
            ctrl.setUsuario(u.getId(), u.getNombre(), u.getDni(),
                    u.getEmail(), u.getTelefono(), u.getTipoUsuario());
            Stage stage = (Stage) BEditar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CentroPlus Connect – Editar usuario");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo cargar la pantalla de edición.");
        }
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        if (indiceActual < 0 || indiceActual >= listaActual.size())
            return;
        Usuario u = listaActual.get(indiceActual);

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar usuario");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Seguro que quieres eliminar a «" + u.getNombre() + "»?");

        Optional<ButtonType> resultado = confirm.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean ok = service.deleteById(u.getId());
            if (ok) {
                limpiarSeleccion();
                cargarUsuarios(TBuscarUsuario.getText().trim(), CUsuarios.getValue());
            } else {
                mostrarError("No se pudo eliminar al usuario.");
            }
        }
    }

    // Navegación por tarjeta (anterior / siguiente)
    @FXML
    private void handleAnterior(MouseEvent event) {
        if (listaActual.isEmpty())
            return;
        indiceActual = (indiceActual - 1 + listaActual.size()) % listaActual.size();
        mostrarUsuario(listaActual.get(indiceActual));
    }

    @FXML
    private void handleSiguiente(MouseEvent event) {
        if (listaActual.isEmpty())
            return;
        indiceActual = (indiceActual + 1) % listaActual.size();
        mostrarUsuario(listaActual.get(indiceActual));
    }

    @FXML
    private void handleNavInicio(MouseEvent e) {
        navegarA("/proyecto/intermodular/app/views/dashboard.fxml", "CentroPlus Connect – Inicio", e);
    }

    @FXML
    private void handleNavUsuarios(MouseEvent e) {
        /* ya estamos aquí */ }

    @FXML
    private void handleNavActividades(MouseEvent e) {
        navegarA("/proyecto/intermodular/app/views/actividades.fxml", "CentroPlus Connect – Actividades", e);
    }

    @FXML
    private void handleNavReservas(MouseEvent e) {
        navegarA("/proyecto/intermodular/app/views/reservas.fxml", "CentroPlus Connect – Reservas", e);
    }

    @FXML
    private void handleNavIncidencias(MouseEvent e) {
        navegarA("/proyecto/intermodular/app/views/incidencias.fxml", "CentroPlus Connect – Incidencias", e);
    }

    // Carga de datos
    private void cargarUsuarios(String texto, String tipo) {
        List<Usuario> todos = service.findAll();
        if (todos == null)
            todos = new ArrayList<>();

        listaActual = new ArrayList<>();
        String textoLow = texto.toLowerCase();
        for (Usuario u : todos) {
            boolean coincideTexto = texto.isEmpty()
                    || u.getNombre().toLowerCase().contains(textoLow)
                    || (u.getDni() != null && u.getDni().toLowerCase().contains(textoLow))
                    || (u.getEmail() != null && u.getEmail().toLowerCase().contains(textoLow));
            boolean coincideTipo = "Todos".equals(tipo) || tipo == null
                    || tipo.equalsIgnoreCase(u.getTipoUsuario());
            if (coincideTexto && coincideTipo)
                listaActual.add(u);
        }

        if (!listaActual.isEmpty()) {
            indiceActual = 0;
            mostrarUsuario(listaActual.get(0));
            BEditar.setDisable(false);
            BEliminar.setDisable(false);
        } else {
            indiceActual = -1;
            mostrarPlaceholder();
            limpiarSeleccion();
        }
    }

    private void mostrarUsuario(Usuario u) {
        LNombre.setText(u.getNombre() != null ? u.getNombre() : "—");
        LDNI.setText(u.getDni() != null ? u.getDni() : "—");
        LEmail.setText(u.getEmail() != null ? u.getEmail() : "—");
        LTelefono.setText(u.getTelefono() != null && !u.getTelefono().isBlank()
                ? u.getTelefono()
                : "—");
        LTipoUsuario.setText(u.getTipoUsuario() != null ? u.getTipoUsuario() : "—");
        LContador.setText((indiceActual + 1) + " / " + listaActual.size());
        BEditar.setDisable(false);
        BEliminar.setDisable(false);
    }

    private void mostrarPlaceholder() {
        LNombre.setText("Sin resultados");
        LDNI.setText("—");
        LEmail.setText("—");
        LTelefono.setText("—");
        LTipoUsuario.setText("—");
        LContador.setText("0 / 0");
    }

    private void limpiarSeleccion() {
        BEditar.setDisable(true);
        BEliminar.setDisable(true);
    }

    // Navegación
    private void navegarA(String fxmlPath, String titulo, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo cargar: " + fxmlPath);
        }
    }

    private void navegarA(String fxmlPath, String titulo, MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo cargar: " + fxmlPath);
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
