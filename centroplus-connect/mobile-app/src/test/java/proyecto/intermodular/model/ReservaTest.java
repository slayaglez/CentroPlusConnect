package proyecto.intermodular.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import proyecto.intermodular.app.model.Reserva;

import java.time.LocalDate;

class ReservaTest {

    Reserva reserva;
    int id = 1;
    int idUsuario = 10;
    int idActividad = 5;
    LocalDate fecha = LocalDate.of(2025, 6, 15);
    String estado = "ACTIVA";

    @BeforeEach
    void setup() {
        reserva = new Reserva(id, idUsuario, idActividad, fecha, estado);
    }

    @DisplayName("Test verifica not null")
    @Order(1)
    @Test
    void reservaNotNullTest() {
        Assertions.assertNotNull(reserva, "La clase reserva no puede ser null");
    }

    @DisplayName("Test verifica equals true")
    @Order(2)
    @Test
    void reservaEqualsTrueTest() {
        Reserva reservaNueva = new Reserva(1);
        Assertions.assertEquals(reserva, reservaNueva, "Debe de ser igual");
    }

    @DisplayName("Test verifica equals false")
    @Order(3)
    @Test
    void reservaEqualsFalseTest() {
        Reserva reservaNueva = new Reserva(2);
        Assertions.assertNotEquals(reserva, reservaNueva, "Debe de ser diferente");
    }

    @DisplayName("Test verifica equals de la misma clase")
    @Order(4)
    @Test
    void reservaEqualsTest() {
        Assertions.assertEquals(reserva, reserva, "Debe de ser igual");
    }

    @DisplayName("Test verifica equals con null")
    @Order(5)
    @Test
    public void equalsConNullTestOk() {
        Assertions.assertNotEquals(reserva, null);
    }

    @DisplayName("Test verifica hash code igual")
    @Order(6)
    @Test
    public void hashCodeIgualTestOk() {
        Reserva otraReserva = new Reserva(1);
        Assertions.assertEquals(reserva.hashCode(), otraReserva.hashCode());
    }

    @DisplayName("Test verifica hash code diferente")
    @Order(7)
    @Test
    public void hashCodeDiferenteTestOk() {
        Reserva otraReserva = new Reserva(99);
        Assertions.assertNotEquals(reserva.hashCode(), otraReserva.hashCode());
    }

    @DisplayName("Test verifica getId")
    @Order(8)
    @Test
    public void getIdTestOk() {
        Reserva reserva = new Reserva();
        reserva.setId(10);
        Assertions.assertEquals(10, reserva.getId());
    }

    @DisplayName("Test verifica setId")
    @Order(9)
    @Test
    public void setIdTestOk() {
        reserva.setId(20);
        Assertions.assertEquals(20, reserva.getId());
    }

    @DisplayName("Test verifica getIdUsuario")
    @Order(10)
    @Test
    public void getIdUsuarioTestOk() {
        Reserva reserva = new Reserva();
        reserva.setIdUsuario(42);
        Assertions.assertEquals(42, reserva.getIdUsuario());
    }

    @DisplayName("Test verifica setIdUsuario")
    @Order(11)
    @Test
    public void setIdUsuarioTestOk() {
        reserva.setIdUsuario(99);
        Assertions.assertEquals(99, reserva.getIdUsuario());
    }

    @DisplayName("Test verifica getIdActividad")
    @Order(12)
    @Test
    public void getIdActividadTestOk() {
        Reserva reserva = new Reserva();
        reserva.setIdActividad(7);
        Assertions.assertEquals(7, reserva.getIdActividad());
    }

    @DisplayName("Test verifica setIdActividad")
    @Order(13)
    @Test
    public void setIdActividadTestOk() {
        reserva.setIdActividad(3);
        Assertions.assertEquals(3, reserva.getIdActividad());
    }

    @DisplayName("Test verifica getFecha")
    @Order(14)
    @Test
    public void getFechaTestOk() {
        Assertions.assertEquals(fecha, reserva.getFecha());
    }

    @DisplayName("Test verifica setFecha")
    @Order(15)
    @Test
    public void setFechaTestOk() {
        LocalDate nuevaFecha = LocalDate.of(2026, 1, 1);
        reserva.setFecha(nuevaFecha);
        Assertions.assertEquals(nuevaFecha, reserva.getFecha());
    }

    @DisplayName("Test verifica getEstado")
    @Order(16)
    @Test
    public void getEstadoTestOk() {
        Assertions.assertEquals(estado, reserva.getEstado());
    }

    @DisplayName("Test verifica setEstado")
    @Order(17)
    @Test
    public void setEstadoTestOk() {
        reserva.setEstado("CANCELADA");
        Assertions.assertEquals("CANCELADA", reserva.getEstado());
    }

    @DisplayName("Test verifica equals con objeto de distinta clase")
    @Order(18)
    @Test
    public void equalsDistintaClaseTestOk() {
        Assertions.assertNotEquals(reserva, "un string cualquiera");
    }

}
