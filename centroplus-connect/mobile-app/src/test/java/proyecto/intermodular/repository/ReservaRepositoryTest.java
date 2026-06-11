package proyecto.intermodular.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import proyecto.intermodular.app.model.Reserva;
import proyecto.intermodular.app.repository.ReservaRepository;
import proyecto.intermodular.app.repository.interfaces.IReservaRepository;
import proyecto.intermodular.database.sqlite.SQLiteConnectionManager;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

public class ReservaRepositoryTest {

    private static final String RUTA_TEST = "./database/centroplus_test.db";

    private IReservaRepository repository;

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

        repository = new ReservaRepository();
        reserva = new Reserva(id, idUsuario, idActividad, fecha, estado);

        try (Connection conn = SQLiteConnectionManager.openConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM reservas");
        }
    }

    // CREATE

    @DisplayName("Test verifica create not null")
    @Order(1)
    @Test
    void createNotNullTest() {
        boolean resultado = repository.create(reserva);
        Assertions.assertNotNull(resultado, "El resultado de create no puede ser null");
    }

    @DisplayName("Test verifica create true")
    @Order(2)
    @Test
    void createTrueTest() {
        boolean resultado = repository.create(reserva);
        Assertions.assertTrue(resultado, "create debe devolver true al insertar correctamente");
    }

    @DisplayName("Test verifica create false con id duplicado")
    @Order(3)
    @Test
    void createFalseTest() {
        repository.create(reserva);
        boolean resultado = repository.create(reserva);
        Assertions.assertFalse(resultado, "create debe devolver false con ID duplicado");
    }

    // CREATE AUTO ID

    @DisplayName("Test verifica createAutoId true")
    @Order(4)
    @Test
    void createAutoIdTrueTest() {
        Reserva sinId = new Reserva(0, idUsuario, idActividad, fecha, estado);
        boolean resultado = repository.createAutoId(sinId);
        Assertions.assertTrue(resultado, "createAutoId debe devolver true");
    }

    // FIND BY ID

    @DisplayName("Test verifica findById not null")
    @Order(5)
    @Test
    void findByIdNotNullTest() {
        repository.create(reserva);
        Reserva resultado = repository.findById(id);
        Assertions.assertNotNull(resultado, "findById no debe devolver null para un ID existente");
    }

    @DisplayName("Test verifica findById null")
    @Order(6)
    @Test
    void findByIdNullTest() {
        Reserva resultado = repository.findById(999);
        Assertions.assertNull(resultado, "findById debe devolver null para un ID inexistente");
    }

    @DisplayName("Test verifica findById devuelve reserva correcta")
    @Order(7)
    @Test
    void findByIdCorrectaTest() {
        repository.create(reserva);
        Reserva resultado = repository.findById(id);
        Assertions.assertEquals(reserva, resultado, "La reserva encontrada debe coincidir con la insertada");
    }

    // FIND ALL

    @DisplayName("Test verifica findAll not null")
    @Order(8)
    @Test
    void findAllNotNullTest() {
        Assertions.assertNotNull(repository.findAll(), "findAll no debe devolver null");
    }

    @DisplayName("Test verifica findAll contiene elemento insertado")
    @Order(9)
    @Test
    void findAllContieneElementoTest() {
        repository.create(reserva);
        boolean contiene = repository.findAll().contains(reserva);
        Assertions.assertTrue(contiene, "findAll debe contener la reserva insertada");
    }

    @DisplayName("Test verifica findAll con varios registros")
    @Order(10)
    @Test
    void findAllVariosTest() {
        repository.create(reserva);
        repository.create(new Reserva(2, 11, 6, LocalDate.of(2025, 7, 20), "ACTIVA"));
        int tamanio = repository.findAll().size();
        Assertions.assertEquals(2, tamanio, "findAll debe devolver 2 reservas");
    }

    // UPDATE

    @DisplayName("Test verifica update true")
    @Order(11)
    @Test
    void updateTrueTest() {
        repository.create(reserva);
        Reserva modificada = new Reserva(id, idUsuario, idActividad, LocalDate.of(2025, 8, 1), "ACTIVA");
        boolean resultado = repository.update(modificada);
        Assertions.assertTrue(resultado, "update debe devolver true al actualizar correctamente");
    }

    @DisplayName("Test verifica update false con id inexistente")
    @Order(12)
    @Test
    void updateFalseTest() {
        Reserva fantasma = new Reserva(999, idUsuario, idActividad, fecha, estado);
        boolean resultado = repository.update(fantasma);
        Assertions.assertFalse(resultado, "update debe devolver false si el ID no existe");
    }

    @DisplayName("Test verifica update modifica el estado")
    @Order(13)
    @Test
    void updateModificaEstadoTest() {
        repository.create(reserva);
        Reserva modificada = new Reserva(id, idUsuario, idActividad, fecha, "CANCELADA");
        repository.update(modificada);
        Assertions.assertEquals("CANCELADA", repository.findById(id).getEstado(), "El estado debe haberse actualizado");
    }

    // DELETE BY ID

    @DisplayName("Test verifica deleteById true")
    @Order(14)
    @Test
    void deleteByIdTrueTest() {
        repository.create(reserva);
        boolean resultado = repository.deleteById(id);
        Assertions.assertTrue(resultado, "deleteById debe devolver true al eliminar correctamente");
    }

    @DisplayName("Test verifica deleteById false con id inexistente")
    @Order(15)
    @Test
    void deleteByIdFalseTest() {
        boolean resultado = repository.deleteById(999);
        Assertions.assertFalse(resultado, "deleteById debe devolver false para un ID inexistente");
    }

    @DisplayName("Test verifica deleteById elimina el registro")
    @Order(16)
    @Test
    void deleteByIdEliminaTest() {
        repository.create(reserva);
        repository.deleteById(id);
        Assertions.assertNull(repository.findById(id), "La reserva no debe existir tras eliminarla");
    }

    // CANCELAR RESERVA

    @DisplayName("Test verifica cancelarReserva true")
    @Order(17)
    @Test
    void cancelarReservaTrueTest() {
        repository.create(reserva);
        boolean resultado = repository.cancelarReserva(id);
        Assertions.assertTrue(resultado, "cancelarReserva debe devolver true para un ID existente");
    }

    @DisplayName("Test verifica cancelarReserva false con id inexistente")
    @Order(18)
    @Test
    void cancelarReservaFalseTest() {
        boolean resultado = repository.cancelarReserva(999);
        Assertions.assertFalse(resultado, "cancelarReserva debe devolver false para un ID inexistente");
    }

    @DisplayName("Test verifica cancelarReserva cambia estado a CANCELADA")
    @Order(19)
    @Test
    void cancelarReservaCambiaEstadoTest() {
        repository.create(reserva);
        repository.cancelarReserva(id);
        Assertions.assertEquals("CANCELADA", repository.findById(id).getEstado(), "El estado debe ser CANCELADA tras cancelar");
    }

    // FIND DISPONIBLES

    @DisplayName("Test verifica findDisponibles not null")
    @Order(20)
    @Test
    void findDisponiblesNotNullTest() {
        Assertions.assertNotNull(repository.findDisponibles(), "findDisponibles no debe devolver null");
    }

    @DisplayName("Test verifica findDisponibles contiene reserva activa")
    @Order(21)
    @Test
    void findDisponiblesContieneActivaTest() {
        repository.create(reserva);
        boolean contiene = repository.findDisponibles().contains(reserva);
        Assertions.assertTrue(contiene, "findDisponibles debe contener la reserva con estado ACTIVA");
    }

    @DisplayName("Test verifica findDisponibles no incluye reserva cancelada")
    @Order(22)
    @Test
    void findDisponiblesNoIncluyeCanceladaTest() {
        Reserva cancelada = new Reserva(2, idUsuario, idActividad, fecha, "CANCELADA");
        repository.create(cancelada);
        boolean contiene = repository.findDisponibles().contains(cancelada);
        Assertions.assertFalse(contiene, "findDisponibles no debe incluir reservas canceladas");
    }
}
