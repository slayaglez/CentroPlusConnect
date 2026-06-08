package proyecto.intermodular.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import proyecto.intermodular.app.model.Actividad;
import proyecto.intermodular.app.model.Reserva;
import proyecto.intermodular.app.service.ActividadService;
import proyecto.intermodular.app.service.IncidenciaService;
import proyecto.intermodular.app.service.ReservaService;
import proyecto.intermodular.app.service.UsuarioService;

public class DashboardController {

    // ── Tarjetas de resumen ─────────────────────────────────────────────────
    @FXML private Label LStatUsuarios;
    @FXML private Label LStatActividades;
    @FXML private Label LStatReservas;
    @FXML private Label LStatIncidencias;

    // ── Barras de plazas ────────────────────────────────────────────────────
    @FXML private VBox VBoxBarras;

    // ── Últimas reservas ────────────────────────────────────────────────────
    @FXML private Label LUltimasReservas;
    @FXML private Label LIngresosTotales;

    // ── Tarjetas clickables antiguas (se mantienen para no romper el FXML) ──
    @FXML private AnchorPane AInicioUsuarios;
    @FXML private AnchorPane AInicioActividades;
    @FXML private AnchorPane AInicioReservas;
    @FXML private AnchorPane AInicioIncidencias;
    @FXML private Label LPlazasOcupadas;

    // ── Nav ─────────────────────────────────────────────────────────────────
    @FXML private Label LNavInicio;
    @FXML private Label LNavUsuarios;
    @FXML private Label LNavActividades;
    @FXML private Label LNavReservas;
    @FXML private Label LNavIncidencias;

    private final UsuarioService    usuarioService    = new UsuarioService();
    private final ActividadService  actividadService  = new ActividadService();
    private final ReservaService    reservaService    = new ReservaService();
    private final IncidenciaService incidenciaService = new IncidenciaService();

    @FXML
    public void initialize() {
        setCursorMano(AInicioUsuarios, AInicioActividades,
                      AInicioReservas, AInicioIncidencias,
                      LNavInicio, LNavUsuarios, LNavActividades,
                      LNavReservas, LNavIncidencias);
        cargarResumen();
    }

    // ── Navegación ──────────────────────────────────────────────────────────
    @FXML private void handleNavInicio(MouseEvent e)      { cargarResumen(); }
    @FXML private void handleNavUsuarios(MouseEvent e)    { navegarA("/proyecto/intermodular/app/views/usuarios.fxml",    "CentroPlus Connect – Usuarios",    e); }
    @FXML private void handleNavActividades(MouseEvent e) { navegarA("/proyecto/intermodular/app/views/actividades.fxml", "CentroPlus Connect – Actividades", e); }
    @FXML private void handleNavReservas(MouseEvent e)    { navegarA("/proyecto/intermodular/app/views/reservas.fxml",    "CentroPlus Connect – Reservas",    e); }
    @FXML private void handleNavIncidencias(MouseEvent e) { navegarA("/proyecto/intermodular/app/views/incidencias.fxml", "CentroPlus Connect – Incidencias", e); }

    // ── Carga de datos ──────────────────────────────────────────────────────
    private void cargarResumen() {
        // Contadores de tarjetas
        try {
            List<?> usuarios = usuarioService.findAll();
            if (LStatUsuarios != null)
                LStatUsuarios.setText(usuarios != null ? String.valueOf(usuarios.size()) : "—");
        } catch (Exception e) { if (LStatUsuarios != null) LStatUsuarios.setText("—"); }

        List<Actividad> actividades = null;
        try {
            actividades = actividadService.findAll();
            if (LStatActividades != null)
                LStatActividades.setText(actividades != null ? String.valueOf(actividades.size()) : "—");
        } catch (Exception e) { if (LStatActividades != null) LStatActividades.setText("—"); }

        try {
            List<?> reservas = reservaService.findAll();
            if (LStatReservas != null)
                LStatReservas.setText(reservas != null ? String.valueOf(reservas.size()) : "—");
        } catch (Exception e) { if (LStatReservas != null) LStatReservas.setText("—"); }

        try {
            List<?> incidencias = incidenciaService.findAll();
            long abiertas = 0;
            if (incidencias != null) {
                for (Object inc : incidencias) {
                    try {
                        String estado = (String) inc.getClass().getMethod("getEstado").invoke(inc);
                        if ("abierta".equalsIgnoreCase(estado)) abiertas++;
                    } catch (Exception ignore) { abiertas++; }
                }
            }
            final long finalAbiertas = abiertas;
            if (LStatIncidencias != null)
                LStatIncidencias.setText(String.valueOf(finalAbiertas));
        } catch (Exception e) { if (LStatIncidencias != null) LStatIncidencias.setText("—"); }

        // Barras de plazas por actividad
        if (VBoxBarras != null && actividades != null) {
            VBoxBarras.getChildren().clear();
            for (Actividad a : actividades) {
                if (a.getPlazasMaximas() == null || a.getPlazasMaximas() == 0) continue;
                double pct = (double) a.getPlazasOcupadas() / a.getPlazasMaximas();

                javafx.scene.layout.HBox fila = new javafx.scene.layout.HBox(8);
                fila.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

                Label nombre = new Label(a.getNombre());
                nombre.setPrefWidth(80);
                nombre.setStyle("-fx-font-size:11px;-fx-text-fill:#666;");

                ProgressBar pb = new ProgressBar(pct);
                pb.setPrefWidth(180);
                pb.setPrefHeight(8);
                pb.setStyle("-fx-accent:#1a3a5c;");
                javafx.scene.layout.HBox.setHgrow(pb, javafx.scene.layout.Priority.ALWAYS);

                Label pctLabel = new Label(Math.round(pct * 100) + "%");
                pctLabel.setPrefWidth(36);
                pctLabel.setStyle("-fx-font-size:10px;-fx-text-fill:#666;-fx-text-alignment:right;");

                fila.getChildren().addAll(nombre, pb, pctLabel);
                VBoxBarras.getChildren().add(fila);
            }
        } else if (LPlazasOcupadas != null) {
            // Fallback: texto simple en el label antiguo
            if (actividades != null && !actividades.isEmpty()) {
                StringBuilder sb = new StringBuilder("Plazas ocupadas:\n");
                for (Actividad a : actividades) {
                    if (a.getPlazasMaximas() != null && a.getPlazasMaximas() > 0) {
                        int pct = (int) Math.round((double) a.getPlazasOcupadas() / a.getPlazasMaximas() * 100);
                        sb.append(a.getNombre()).append(": ").append(pct).append("%\n");
                    }
                }
                LPlazasOcupadas.setText(sb.toString().trim());
            }
        }

        // Ingresos totales
        try {
            double ingresos = actividadService.calcularIngresosTotales();
            if (LIngresosTotales != null)
                LIngresosTotales.setText(String.format("Ingresos totales: %.2f €", ingresos));
        } catch (Exception e) {
            if (LIngresosTotales != null) LIngresosTotales.setText("Ingresos totales: —");
        }

        // Últimas reservas — texto descriptivo
        if (LUltimasReservas != null) {
            try {
                List<?> reservas = reservaService.findAll();
                if (reservas != null && !reservas.isEmpty()) {
                    int mostrar = Math.min(3, reservas.size());
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mostrar; i++) {
                        Object r = reservas.get(i);
                        try {
                            Object uId = r.getClass().getMethod("getIdUsuario").invoke(r);
                            Object aId = r.getClass().getMethod("getIdActividad").invoke(r);
                            sb.append("Usuario ").append(uId).append(" → Actividad ").append(aId).append("\n");
                        } catch (Exception ignore) {
                            sb.append(r.toString()).append("\n");
                        }
                    }
                    LUltimasReservas.setText("Últimas reservas:\n" + sb.toString().trim());
                } else {
                    LUltimasReservas.setText("Últimas reservas: sin datos");
                }
            } catch (Exception e) {
                LUltimasReservas.setText("Últimas reservas: —");
            }
        }
    }

    // ── Utilidades ──────────────────────────────────────────────────────────
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

    private void setCursorMano(Object... nodos) {
        for (Object nodo : nodos) {
            if (nodo instanceof javafx.scene.Node n) n.setCursor(javafx.scene.Cursor.HAND);
        }
    }

    private void mostrarError(String mensaje) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.ERROR
        );
        alert.setTitle("Error de navegación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
