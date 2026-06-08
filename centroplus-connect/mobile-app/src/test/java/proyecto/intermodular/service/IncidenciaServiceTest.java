package proyecto.intermodular.service;

import org.junit.jupiter.api.*;

import proyecto.intermodular.app.model.Incidencia;
import proyecto.intermodular.app.service.IncidenciaService;
import proyecto.intermodular.app.service.interfaces.IIncidenciaService;
import proyecto.intermodular.database.sqlite.SQLiteConnectionManager;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;

public class IncidenciaServiceTest {

    private static final String RUTA_TEST = "./database/centroplus_test.db";

    private IIncidenciaService service;

    Incidencia incidencia;
    int id = 1;
    int idUsuario = 10;
    String asunto = "Problema con reserva";
    String descripcion = "No puedo reservar una plaza";
    LocalDate fecha = LocalDate.now();
    String estado = "ABIERTO";

    @BeforeAll
    static void crearBdTest() throws Exception {
        SQLiteConnectionManager.rutaDb = RUTA_TEST;
        new File("./database").mkdirs();

        try (Connection conn = SQLiteConnectionManager.openConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS incidencias (" +
                "    id          INTEGER PRIMARY KEY," +
                "    id_usuario  INTEGER NOT NULL," +
                "    asunto      TEXT    NOT NULL," +
                "    descripcion TEXT    NOT NULL," +
                "    fecha       TEXT    NOT NULL," +
                "    estado      TEXT    NOT NULL" +
                ")"
            );
        }
    }

    @BeforeEach
    void setup() throws Exception {
        SQLiteConnectionManager.rutaDb = RUTA_TEST;
        service = new IncidenciaService();
        incidencia = new Incidencia(id, idUsuario, asunto, descripcion, fecha, estado);

        try (Connection conn = SQLiteConnectionManager.openConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM incidencias");
        }
    }

    // -----------------------------------------------------------------------
    // CREATE
    // -----------------------------------------------------------------------

    @DisplayName("Test verifica create true con incidencia valida")
    @Order(1)
    @Test
    void createTrueTest() {
        boolean resultado = service.create(incidencia);
        Assertions.assertTrue(resultado, "create debe devolver true con incidencia valida");
    }

    @DisplayName("Test verifica create false con incidencia null")
    @Order(2)
    @Test
    void createNullTest() {
        boolean resultado = service.create(null);
        Assertions.assertFalse(resultado, "create debe devolver false con incidencia null");
    }

    @DisplayName("Test verifica create false con estado invalido")
    @Order(3)
    @Test
    void createEstadoInvalidoTest() {
        Incidencia invalida = new Incidencia(id, idUsuario, asunto, descripcion, fecha, "INVALIDO");
        boolean resultado = service.create(invalida);
        Assertions.assertFalse(resultado, "create debe devolver false con estado invalido");
    }

    @DisplayName("Test verifica create false con asunto null")
    @Order(4)
    @Test
    void createAsuntoNullTest() {
        Incidencia invalida = new Incidencia(id, idUsuario, null, descripcion, fecha, estado);
        boolean resultado = service.create(invalida);
        Assertions.assertFalse(resultado, "create debe devolver false con asunto null");
    }

    @DisplayName("Test verifica create false con descripcion null")
    @Order(5)
    @Test
    void createDescripcionNullTest() {
        Incidencia invalida = new Incidencia(id, idUsuario, asunto, null, fecha, estado);
        boolean resultado = service.create(invalida);
        Assertions.assertFalse(resultado, "create debe devolver false con descripcion null");
    }

    @DisplayName("Test verifica create con id null usa createAutoId")
    @Order(6)
    @Test
    void createAutoIdTest() {
        Incidencia sinId = new Incidencia();
        sinId.setIdUsuario(idUsuario);
        sinId.setAsunto(asunto);
        sinId.setDescripcion(descripcion);
        sinId.setFecha(fecha);
        sinId.setEstado(estado);
        boolean resultado = service.create(sinId);
        Assertions.assertTrue(resultado, "create con id null debe delegar en createAutoId y devolver true");
    }

    // -----------------------------------------------------------------------
    // FIND BY ID
    // -----------------------------------------------------------------------

    @DisplayName("Test verifica findById null con id null")
    @Order(7)
    @Test
    void findByIdNullIdTest() {
        Incidencia resultado = service.findById(null);
        Assertions.assertNull(resultado, "findById debe devolver null si el id es null");
    }

    @DisplayName("Test verifica findById null con id inexistente")
    @Order(8)
    @Test
    void findByIdInexistenteTest() {
        Incidencia resultado = service.findById(999);
        Assertions.assertNull(resultado, "findById debe devolver null para un ID inexistente");
    }

    @DisplayName("Test verifica findById devuelve incidencia correcta")
    @Order(9)
    @Test
    void findByIdCorrectaTest() {
        service.create(incidencia);
        Incidencia resultado = service.findById(id);
        Assertions.assertEquals(incidencia, resultado, "findById debe devolver la incidencia insertada");
    }

    // -----------------------------------------------------------------------
    // FIND ALL
    // -----------------------------------------------------------------------

    @DisplayName("Test verifica findAll not null")
    @Order(10)
    @Test
    void findAllNotNullTest() {
        Assertions.assertNotNull(service.findAll(), "findAll no debe devolver null");
    }

    @DisplayName("Test verifica findAll contiene incidencia insertada")
    @Order(11)
    @Test
    void findAllContieneTest() {
        service.create(incidencia);
        boolean contiene = service.findAll().contains(incidencia);
        Assertions.assertTrue(contiene, "findAll debe contener la incidencia insertada");
    }

    // -----------------------------------------------------------------------
    // UPDATE
    // -----------------------------------------------------------------------

    @DisplayName("Test verifica update true con incidencia valida")
    @Order(12)
    @Test
    void updateTrueTest() {
        service.create(incidencia);
        Incidencia modificada = new Incidencia(id, idUsuario, asunto, descripcion, fecha, "EN_PROCESO");
        boolean resultado = service.update(modificada);
        Assertions.assertTrue(resultado, "update debe devolver true con incidencia valida");
    }

    @DisplayName("Test verifica update false con incidencia null")
    @Order(13)
    @Test
    void updateNullTest() {
        boolean resultado = service.update(null);
        Assertions.assertFalse(resultado, "update debe devolver false con incidencia null");
    }

    @DisplayName("Test verifica update false con estado invalido")
    @Order(14)
    @Test
    void updateEstadoInvalidoTest() {
        service.create(incidencia);
        Incidencia invalida = new Incidencia(id, idUsuario, asunto, descripcion, fecha, "INVALIDO");
        boolean resultado = service.update(invalida);
        Assertions.assertFalse(resultado, "update debe devolver false con estado invalido");
    }

    // -----------------------------------------------------------------------
    // DELETE BY ID
    // -----------------------------------------------------------------------

    @DisplayName("Test verifica deleteById false con id null")
    @Order(15)
    @Test
    void deleteByIdNullTest() {
        boolean resultado = service.deleteById(null);
        Assertions.assertFalse(resultado, "deleteById debe devolver false si el id es null");
    }

    @DisplayName("Test verifica deleteById true con id existente")
    @Order(16)
    @Test
    void deleteByIdTrueTest() {
        service.create(incidencia);
        boolean resultado = service.deleteById(id);
        Assertions.assertTrue(resultado, "deleteById debe devolver true al eliminar un registro existente");
    }

    @DisplayName("Test verifica deleteById false con id inexistente")
    @Order(17)
    @Test
    void deleteByIdFalseTest() {
        boolean resultado = service.deleteById(999);
        Assertions.assertFalse(resultado, "deleteById debe devolver false para un ID inexistente");
    }

    // -----------------------------------------------------------------------
    // CAMBIAR ESTADO
    // -----------------------------------------------------------------------

    @DisplayName("Test verifica cambiarEstadoIncidencia true con estado valido")
    @Order(18)
    @Test
    void cambiarEstadoTrueTest() {
        service.create(incidencia);
        boolean resultado = service.cambiarEstadoIncidencia(id, "EN_PROCESO");
        Assertions.assertTrue(resultado, "cambiarEstadoIncidencia debe devolver true con estado valido");
    }

    @DisplayName("Test verifica cambiarEstadoIncidencia false con estado invalido")
    @Order(19)
    @Test
    void cambiarEstadoInvalidoTest() {
        boolean resultado = service.cambiarEstadoIncidencia(id, "INVALIDO");
        Assertions.assertFalse(resultado, "cambiarEstadoIncidencia debe devolver false con estado invalido");
    }

    @DisplayName("Test verifica cambiarEstadoIncidencia false con estado null")
    @Order(20)
    @Test
    void cambiarEstadoNullTest() {
        boolean resultado = service.cambiarEstadoIncidencia(id, null);
        Assertions.assertFalse(resultado, "cambiarEstadoIncidencia debe devolver false con estado null");
    }

    @DisplayName("Test verifica cambiarEstadoIncidencia a CERRADA")
    @Order(21)
    @Test
    void cambiarEstadoCerradaTest() {
        service.create(incidencia);
        boolean resultado = service.cambiarEstadoIncidencia(id, "CERRADA");
        Assertions.assertTrue(resultado, "cambiarEstadoIncidencia debe devolver true al cambiar a CERRADA");
    }

    // -----------------------------------------------------------------------
    // FIND BY USUARIO
    // -----------------------------------------------------------------------

    @DisplayName("Test verifica findByUsuario not null")
    @Order(22)
    @Test
    void findByUsuarioNotNullTest() {
        Assertions.assertNotNull(service.findByUsuario(idUsuario), "findByUsuario no debe devolver null");
    }

    @DisplayName("Test verifica findByUsuario contiene incidencia del usuario")
    @Order(23)
    @Test
    void findByUsuarioContieneTest() {
        service.create(incidencia);
        boolean contiene = service.findByUsuario(idUsuario).contains(incidencia);
        Assertions.assertTrue(contiene, "findByUsuario debe contener la incidencia del usuario");
    }

    @DisplayName("Test verifica findByUsuario no contiene incidencias de otro usuario")
    @Order(24)
    @Test
    void findByUsuarioOtroUsuarioTest() {
        service.create(incidencia);
        boolean contiene = service.findByUsuario(99).contains(incidencia);
        Assertions.assertFalse(contiene, "findByUsuario no debe contener incidencias de otro usuario");
    }
}