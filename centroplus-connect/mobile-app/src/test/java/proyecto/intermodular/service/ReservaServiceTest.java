package proyecto.intermodular.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import proyecto.intermodular.app.model.Reserva;
import proyecto.intermodular.app.service.ReservaService;
import proyecto.intermodular.app.service.interfaces.IReservaService;
import proyecto.intermodular.database.sqlite.SQLiteConnectionManager;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;

public class ReservaServiceTest {

    private static final String RUTA_TEST = "./database/centroplus_test.db";

    private IReservaService service;

    Reserva reserva;
    int id = 1;
    int idUsuario = 10;
    int idActividad = 5;
    LocalDate fecha = LocalDate.of(2025, 6, 15);
    String estado = "ACTIVA";

    @BeforeAll
    static void crearBdTest() throws Exception {
        SQLiteConnectionManager.rutaDb = RUTA_TEST;
        new File("./database").mkdirs();

        try (Connection conn = SQLiteConnectionManager.openConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS reservas (" +
                "    id           INTEGER PRIMARY KEY," +
                "    id_usuario   INTEGER NOT NULL," +
                "    id_actividad INTEGER NOT NULL," +
                "    fecha        TEXT    NOT NULL," +
                "    estado       TEXT    NOT NULL" +
                ")"
            );
        }
    }

    @BeforeEach
    void setup() throws Exception {
        SQLiteConnectionManager.rutaDb = RUTA_TEST;
        service = new ReservaService();
        reserva = new Reserva(id, idUsuario, idActividad, fecha, estado);

        try (Connection conn = SQLiteConnectionManager.openConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM reservas");
        }
    }

    // CREATE

    @DisplayName("Test verifica create true con reserva valida")
    @Order(1)
    @Test
    void createTrueTest() {
        boolean resultado = service.create(reserva);
        Assertions.assertTrue(resultado, "create debe devolver true con reserva valida");
    }

    @DisplayName("Test verifica create false con reserva null")
    @Order(2)
    @Test
    void createNullTest() {
        boolean resultado = service.create(null);
        Assertions.assertFalse(resultado, "create debe devolver false con reserva null");
    }

    @DisplayName("Test verifica create false con estado invalido")
    @Order(3)
    @Test
    void createEstadoInvalidoTest() {
        Reserva invalida = new Reserva(id, idUsuario, idActividad, fecha, "INVALIDO");
        boolean resultado = service.create(invalida);
        Assertions.assertFalse(resultado, "create debe devolver false con estado invalido");
    }

    @DisplayName("Test verifica create con id null usa createAutoId")
    @Order(4)
    @Test
    void createAutoIdTest() {
        Reserva sinId = new Reserva();
        sinId.setIdUsuario(idUsuario);
        sinId.setIdActividad(idActividad);
        sinId.setFecha(fecha);
        sinId.setEstado(estado);
        boolean resultado = service.create(sinId);
        Assertions.assertTrue(resultado, "create con id null debe delegar en createAutoId y devolver true");
    }

    // FIND BY ID 

    @DisplayName("Test verifica findById null con id null")
    @Order(5)
    @Test
    void findByIdNullIdTest() {
        Reserva resultado = service.findById(null);
        Assertions.assertNull(resultado, "findById debe devolver null si el id es null");
    }

    @DisplayName("Test verifica findById null con id inexistente")
    @Order(6)
    @Test
    void findByIdInexistenteTest() {
        Reserva resultado = service.findById(999);
        Assertions.assertNull(resultado, "findById debe devolver null para un ID inexistente");
    }

    @DisplayName("Test verifica findById devuelve reserva correcta")
    @Order(7)
    @Test
    void findByIdCorrectaTest() {
        service.create(reserva);
        Reserva resultado = service.findById(id);
        Assertions.assertEquals(reserva, resultado, "findById debe devolver la reserva insertada");
    }

    // FIND ALL 

    @DisplayName("Test verifica findAll not null")
    @Order(8)
    @Test
    void findAllNotNullTest() {
        Assertions.assertNotNull(service.findAll(), "findAll no debe devolver null");
    }

    @DisplayName("Test verifica findAll contiene reserva insertada")
    @Order(9)
    @Test
    void findAllContieneTest() {
        service.create(reserva);
        boolean contiene = service.findAll().contains(reserva);
        Assertions.assertTrue(contiene, "findAll debe contener la reserva insertada");
    }

    // UPDATE

    @DisplayName("Test verifica update true con reserva valida")
    @Order(10)
    @Test
    void updateTrueTest() {
        service.create(reserva);
        Reserva modificada = new Reserva(id, idUsuario, idActividad, fecha.plusDays(1), estado);
        boolean resultado = service.update(modificada);
        Assertions.assertTrue(resultado, "update debe devolver true con reserva valida");
    }

    @DisplayName("Test verifica update false con reserva null")
    @Order(11)
    @Test
    void updateNullTest() {
        boolean resultado = service.update(null);
        Assertions.assertFalse(resultado, "update debe devolver false con reserva null");
    }


    // DELETE BY ID

    @DisplayName("Test verifica deleteById false con id null")
    @Order(12)
    @Test
    void deleteByIdNullTest() {
        boolean resultado = service.deleteById(null);
        Assertions.assertFalse(resultado, "deleteById debe devolver false si el id es null");
    }

    @DisplayName("Test verifica deleteById true con id existente")
    @Order(13)
    @Test
    void deleteByIdTrueTest() {
        service.create(reserva);
        boolean resultado = service.deleteById(id);
        Assertions.assertTrue(resultado, "deleteById debe devolver true al eliminar un registro existente");
    }

    @DisplayName("Test verifica deleteById false con id inexistente")
    @Order(14)
    @Test
    void deleteByIdFalseTest() {
        boolean resultado = service.deleteById(999);
        Assertions.assertFalse(resultado, "deleteById debe devolver false para un ID inexistente");
    }

    // CANCELAR RESERVA

    @DisplayName("Test verifica cancelarReserva true con id existente")
    @Order(15)
    @Test
    void cancelarReservaTrueTest() {
        service.create(reserva);
        boolean resultado = service.cancelarReserva(id);
        Assertions.assertTrue(resultado, "cancelarReserva debe devolver true con una reserva existente");
    }

    @DisplayName("Test verifica cancelarReserva false con id inexistente")
    @Order(16)
    @Test
    void cancelarReservaFalseTest() {
        boolean resultado = service.cancelarReserva(999);
        Assertions.assertFalse(resultado, "cancelarReserva debe devolver false con un ID inexistente");
    }

    // FIND DISPONIBLES

    @DisplayName("Test verifica findDisponibles not null")
    @Order(17)
    @Test
    void findDisponiblesNotNullTest() {
        Assertions.assertNotNull(service.findDisponibles(), "findDisponibles no debe devolver null");
    }

    @DisplayName("Test verifica findDisponibles contiene reserva activa")
    @Order(18)
    @Test
    void findDisponiblesContieneTest() {
        service.create(reserva);
        boolean contiene = service.findDisponibles().contains(reserva);
        Assertions.assertTrue(contiene, "findDisponibles debe contener la reserva con estado ACTIVA");
    }

    @DisplayName("Test verifica findDisponibles no contiene reserva cancelada")
    @Order(19)
    @Test
    void findDisponiblesNoCanceladaTest() {
        Reserva cancelada = new Reserva(2, idUsuario, idActividad, fecha, "CANCELADA");
        service.create(cancelada);
        boolean contiene = service.findDisponibles().contains(cancelada);
        Assertions.assertFalse(contiene, "findDisponibles no debe contener reservas canceladas");
    }

    // FIND ALL CON DETALLE

    @DisplayName("Test verifica findAllConDetalle not null")
    @Order(20)
    @Test
    void findAllConDetalleNotNullTest() {
        Assertions.assertNotNull(service.findAllConDetalle(), "findAllConDetalle no debe devolver null");
    }

}
