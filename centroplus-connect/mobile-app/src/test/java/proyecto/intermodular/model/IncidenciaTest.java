package proyecto.intermodular.model;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import proyecto.intermodular.app.model.Actividad;
import proyecto.intermodular.app.model.Incidencia;

public class IncidenciaTest {

    Incidencia incidencia;

    int id = 1;
    int idUsuario = 1;
    String asunto = "asunto";
    String descripcion = "descripcion";
    String fechaStr = "2026-05-26";
    LocalDate fecha = LocalDate.parse(fechaStr);
    String estado = "ABIERTO";

    @BeforeEach
    void setup() {
        incidencia = new Incidencia(id, idUsuario, asunto, descripcion, fecha, estado);
    }

    @DisplayName("Test verifica not null")
    @Order(1)
    @Test
    void incidenciaNotNull() {
        Assertions.assertNotNull(incidencia);
    }

    @DisplayName("Test verifica equals true")
    @Order(2)
    @Test
    void incidenciaEqualsTrueTest() {
        Incidencia incidenciaNueva = new Incidencia(1);
        Assertions.assertEquals(incidencia, incidenciaNueva, "Debe de ser igual");
    }

    @DisplayName("Test verifica equals false")
    @Order(3)
    @Test
    void actividadEqualsFalseTest() {
        Incidencia incidenciaNueva = new Incidencia(2);
        Assertions.assertNotEquals(incidencia, incidenciaNueva, "Debe de ser diferente");
    }

    @DisplayName("Test verifica equals de la misma clase")
    @Order(4)
    @Test
    void incidenciaEqualsTest() {
        Assertions.assertEquals(incidencia, incidencia, "Debe de ser igual");
    }

    @DisplayName("Test verifica equals con null")
    @Order(5)
    @Test
    public void equalsConNullTestOk() {
        Assertions.assertNotEquals(incidencia, null);
    }

    @DisplayName("Test verifica hash code igual")
    @Order(6)
    @Test
    public void hashCodeIgualTestOk() {
        Incidencia otraIncidencia = new Incidencia(1);
        Assertions.assertEquals(incidencia.hashCode(), otraIncidencia.hashCode());
    }

    @DisplayName("Test verifica hash code diferente")
    @Order(7)
    @Test
    public void hashCodeDiferenteTestOk() {
        Incidencia otraIncidencia = new Incidencia(99);
        Assertions.assertNotEquals(incidencia.hashCode(), otraIncidencia.hashCode());
    }

    @DisplayName("Test verifica to string")
    @Order(8)
    @Test
    public void toStringTestOk() {
        String resultado = incidencia.toString();
        Assertions.assertNotNull(resultado);
    }

    @DisplayName("Test verifica id")
    @Order(9)
    @Test
    public void getIdTestOk() {
        Incidencia incidencia = new Incidencia();
        incidencia.setId(10);
        Assertions.assertEquals(10, incidencia.getId());
    }

    @DisplayName("Test verifica setId")
    @Order(10)
    @Test
    public void setIdTestOk() {
        incidencia.setId(10);
        Assertions.assertEquals(10, incidencia.getId());
    }

    @DisplayName("Test verifica getIdUsuario")
    @Order(11)
    @Test
    public void getIdUsuarioTestOk() {
        Incidencia incidencia = new Incidencia();
        incidencia.setIdUsuario(20);
        Assertions.assertEquals(20, incidencia.getIdUsuario());
    }

    @DisplayName("Test verifica setIdUsuario")
    @Order(12)
    @Test
    public void setIdUsuarioTestOk() {
        incidencia.setIdUsuario(20);
        Assertions.assertEquals(20, incidencia.getIdUsuario());
    }

    @DisplayName("Test verifica getAsunto")
    @Order(13)
    @Test
    public void getAsuntoTestOk() {
        Incidencia incidencia = new Incidencia();
        incidencia.setAsunto("Problema con reserva");
        Assertions.assertEquals("Problema con reserva", incidencia.getAsunto());
    }

    @DisplayName("Test verifica setAsunto")
    @Order(14)
    @Test
    public void setAsuntoTestOk() {
        incidencia.setAsunto("Problema con reserva");
        Assertions.assertEquals("Problema con reserva", incidencia.getAsunto());
    }

    @DisplayName("Test verifica getDescripcion")
    @Order(15)
    @Test
    public void getDescripcionTestOk() {
        Incidencia incidencia = new Incidencia();
        incidencia.setDescripcion("No puedo reservar una plaza");
        Assertions.assertEquals("No puedo reservar una plaza", incidencia.getDescripcion());
    }

    @DisplayName("Test verifica getFecha")
    @Order(16)
    @Test
    public void getFechaTestOk() {
        LocalDate fecha = LocalDate.of(2025, 6, 15);
        incidencia.setFecha(fecha);
        Assertions.assertEquals(LocalDate.of(2025, 6, 15), incidencia.getFecha());
    }

    @DisplayName("Test verifica setFecha")
    @Order(17)
    @Test
    public void setFechaTestOk() {
        LocalDate fechaNueva = LocalDate.of(2026, 1, 1);
        incidencia.setFecha(fechaNueva);
        Assertions.assertEquals(fechaNueva, incidencia.getFecha());
    }

    @DisplayName("Test verifica getEstado")
    @Order(18)
    @Test
    public void getEstadoTestOk() {
        Assertions.assertEquals(estado, incidencia.getEstado());
    }

    @DisplayName("Test verifica setPrecio")
    @Order(19)
    @Test
    public void setEstadoTestOk() {
        incidencia.setEstado("ABIERTA");
        Assertions.assertEquals("ABIERTA", incidencia.getEstado());
    }

}
