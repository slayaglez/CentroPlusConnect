package proyecto.intermodular.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import proyecto.intermodular.app.model.Incidencia;
import proyecto.intermodular.app.repository.IncidenciaRepository;
import proyecto.intermodular.app.repository.interfaces.IIncidenciaRepository;
import proyecto.intermodular.database.sqlite.SQLiteConnectionManager;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

public class IncidenciaRepositoryTest {

    private static final String RUTA_TEST = "./database/centroplus_test.db";

    private IIncidenciaRepository repository;

    Incidencia incidencia;
    int id = 1;
    int idUsuario = 10;
    String asunto = "Problema acceso";
    String descripcion = "No puedo acceder al sistema";
    LocalDate fecha = LocalDate.of(2026, 1, 15);
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
        repository = new IncidenciaRepository();
        incidencia = new Incidencia(id, idUsuario, asunto, descripcion, fecha, estado);

        try (Connection conn = SQLiteConnectionManager.openConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM incidencias");
        }
    }

    // CREATE

    @DisplayName("Test verifica create not null")
    @Order(1)
    @Test
    void createNotNullTest() {
        boolean resultado = repository.create(incidencia);
        Assertions.assertNotNull(resultado, "El resultado de create no puede ser null");
    }

    @DisplayName("Test verifica create true")
    @Order(2)
    @Test
    void createTrueTest() {
        boolean resultado = repository.create(incidencia);
        Assertions.assertTrue(resultado, "create debe devolver true al insertar correctamente");
    }

    @DisplayName("Test verifica create false con id duplicado")
    @Order(3)
    @Test
    void createFalseTest() {
        repository.create(incidencia);
        boolean resultado = repository.create(incidencia);
        Assertions.assertFalse(resultado, "create debe devolver false con ID duplicado");
    }

    // CREATE AUTO ID

    @DisplayName("Test verifica createAutoId true")
    @Order(4)
    @Test
    void createAutoIdTrueTest() {
        Incidencia sinId = new Incidencia(0, idUsuario, asunto, descripcion, fecha, estado);
        boolean resultado = repository.createAutoId(sinId);
        Assertions.assertTrue(resultado, "createAutoId debe devolver true");
    }

    // FIND BY ID

    @DisplayName("Test verifica findById not null")
    @Order(5)
    @Test
    void findByIdNotNullTest() {
        repository.create(incidencia);
        Incidencia resultado = repository.findById(id);
        Assertions.assertNotNull(resultado, "findById no debe devolver null para un ID existente");
    }

    @DisplayName("Test verifica findById null")
    @Order(6)
    @Test
    void findByIdNullTest() {
        Incidencia resultado = repository.findById(999);
        Assertions.assertNull(resultado, "findById debe devolver null para un ID inexistente");
    }

    @DisplayName("Test verifica findById devuelve incidencia correcta")
    @Order(7)
    @Test
    void findByIdCorrectaTest() {
        repository.create(incidencia);
        Incidencia resultado = repository.findById(id);
        Assertions.assertEquals(incidencia, resultado, "La incidencia encontrada debe coincidir con la insertada");
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
        repository.create(incidencia);
        boolean contiene = repository.findAll().contains(incidencia);
        Assertions.assertTrue(contiene, "findAll debe contener la incidencia insertada");
    }

    @DisplayName("Test verifica findAll con varios registros")
    @Order(10)
    @Test
    void findAllVariosTest() {
        repository.create(incidencia);
        repository.create(new Incidencia(2, 11, "Otro asunto", "Otra descripcion", LocalDate.of(2026, 2, 20), "EN_PROCESO"));
        int tamanio = repository.findAll().size();
        Assertions.assertEquals(2, tamanio, "findAll debe devolver 2 incidencias");
    }

    // UPDATE

    @DisplayName("Test verifica update true")
    @Order(11)
    @Test
    void updateTrueTest() {
        repository.create(incidencia);
        Incidencia modificada = new Incidencia(id, idUsuario, "Asunto modificado", descripcion, fecha, estado);
        boolean resultado = repository.update(modificada);
        Assertions.assertTrue(resultado, "update debe devolver true al actualizar correctamente");
    }

    @DisplayName("Test verifica update false con id inexistente")
    @Order(12)
    @Test
    void updateFalseTest() {
        Incidencia fantasma = new Incidencia(999, idUsuario, asunto, descripcion, fecha, estado);
        boolean resultado = repository.update(fantasma);
        Assertions.assertFalse(resultado, "update debe devolver false si el ID no existe");
    }

    @DisplayName("Test verifica update modifica el asunto")
    @Order(13)
    @Test
    void updateModificaAsuntoTest() {
        repository.create(incidencia);
        Incidencia modificada = new Incidencia(id, idUsuario, "Asunto modificado", descripcion, fecha, estado);
        repository.update(modificada);
        Assertions.assertEquals("Asunto modificado", repository.findById(id).getAsunto(), "El asunto debe haberse actualizado");
    }

    // DELETE BY ID

    @DisplayName("Test verifica deleteById true")
    @Order(14)
    @Test
    void deleteByIdTrueTest() {
        repository.create(incidencia);
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
        repository.create(incidencia);
        repository.deleteById(id);
        Assertions.assertNull(repository.findById(id), "La incidencia no debe existir tras eliminarla");
    }

    // CAMBIAR ESTADO

    @DisplayName("Test verifica cambiarEstadoIncidencia true")
    @Order(17)
    @Test
    void cambiarEstadoTrueTest() {
        repository.create(incidencia);
        boolean resultado = repository.cambiarEstadoIncidencia(id, "EN_PROCESO");
        Assertions.assertTrue(resultado, "cambiarEstadoIncidencia debe devolver true al cambiar el estado");
    }

    @DisplayName("Test verifica cambiarEstadoIncidencia false con id inexistente")
    @Order(18)
    @Test
    void cambiarEstadoFalseTest() {
        boolean resultado = repository.cambiarEstadoIncidencia(999, "EN_PROCESO");
        Assertions.assertFalse(resultado, "cambiarEstadoIncidencia debe devolver false para un ID inexistente");
    }

    @DisplayName("Test verifica cambiarEstadoIncidencia modifica el estado")
    @Order(19)
    @Test
    void cambiarEstadoModificaTest() {
        repository.create(incidencia);
        repository.cambiarEstadoIncidencia(id, "CERRADO");
        Assertions.assertEquals("CERRADO", repository.findById(id).getEstado(), "El estado debe haberse actualizado a CERRADO");
    }

    // FIND BY USUARIO

    @DisplayName("Test verifica findByUsuario not null")
    @Order(20)
    @Test
    void findByUsuarioNotNullTest() {
        Assertions.assertNotNull(repository.findByUsuario(idUsuario), "findByUsuario no debe devolver null");
    }

    @DisplayName("Test verifica findByUsuario contiene incidencia del usuario")
    @Order(21)
    @Test
    void findByUsuarioContieneTest() {
        repository.create(incidencia);
        boolean contiene = repository.findByUsuario(idUsuario).contains(incidencia);
        Assertions.assertTrue(contiene, "findByUsuario debe contener la incidencia del usuario");
    }

    @DisplayName("Test verifica findByUsuario no incluye incidencia de otro usuario")
    @Order(22)
    @Test
    void findByUsuarioNoIncluyeOtroTest() {
        repository.create(incidencia);
        repository.create(new Incidencia(2, 99, "Otro asunto", "Otra descripcion", LocalDate.of(2026, 3, 1), "ABIERTO"));
        List<Incidencia> resultado = repository.findByUsuario(idUsuario);
        Assertions.assertFalse(resultado.contains(new Incidencia(2)), "findByUsuario no debe incluir incidencias de otros usuarios");
    }

    @DisplayName("Test verifica findByUsuario devuelve lista vacia si no tiene incidencias")
    @Order(23)
    @Test
    void findByUsuarioVacioTest() {
        List<Incidencia> resultado = repository.findByUsuario(999);
        Assertions.assertTrue(resultado.isEmpty(), "findByUsuario debe devolver lista vacia si el usuario no tiene incidencias");
    }
}