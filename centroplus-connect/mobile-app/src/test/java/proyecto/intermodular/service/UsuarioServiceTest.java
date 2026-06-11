package proyecto.intermodular.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import proyecto.intermodular.app.model.Usuario;
import proyecto.intermodular.app.service.UsuarioService;
import proyecto.intermodular.app.service.interfaces.IUsuarioService;
import proyecto.intermodular.database.sqlite.SQLiteConnectionManager;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

public class UsuarioServiceTest {

    private static final String RUTA_TEST = "./database/centroplus_test.db";

    private IUsuarioService service;

    Usuario usuario;
    int id = 1;
    String nombre = "Ana Garcia";
    String dni = "12345678A";
    String email = "anagarcia@email.com";
    String telefono = "+34600111222";
    String tipoUsuario = "SOCIO";
    String contrasenia = "password123";

    @BeforeAll
    static void crearBdTest() throws Exception {
        SQLiteConnectionManager.rutaDb = RUTA_TEST;
        new File("./database").mkdirs();

        try (Connection conn = SQLiteConnectionManager.openConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS usuarios (" +
                            "    id               INTEGER PRIMARY KEY," +
                            "    nombre           TEXT    NOT NULL," +
                            "    dni              TEXT    NOT NULL," +
                            "    email            TEXT    NOT NULL," +
                            "    telefono         TEXT    NOT NULL," +
                            "    tipo_usuario     TEXT    NOT NULL," +
                            "    hashed_password  TEXT    NOT NULL" +
                            ")");
        }
    }

    @BeforeEach
    void setup() throws Exception {
        SQLiteConnectionManager.rutaDb = RUTA_TEST;
        service = new UsuarioService();
        usuario = new Usuario(id, nombre, dni, email, telefono, tipoUsuario);

        try (Connection conn = SQLiteConnectionManager.openConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM usuarios");
        }
    }

    // CREATE

    @DisplayName("Test verifica create true con usuario valido")
    @Order(1)
    @Test
    void createTrueTest() {
        boolean resultado = service.create(usuario, contrasenia);
        Assertions.assertTrue(resultado, "create debe devolver true con usuario valido");
    }

    @DisplayName("Test verifica create con id null usa createAutoId")
    @Order(2)
    @Test
    void createAutoIdTest() {
        Usuario sinId = new Usuario(nombre, dni, email, telefono, tipoUsuario);
        boolean resultado = service.create(sinId, contrasenia);
        Assertions.assertTrue(resultado, "create con id null debe delegar en createAutoId y devolver true");
    }

    @DisplayName("Test verifica create false con id duplicado")
    @Order(3)
    @Test
    void createDuplicadoTest() {
        service.create(usuario, contrasenia);
        boolean resultado = service.create(usuario, contrasenia);
        Assertions.assertFalse(resultado, "create debe devolver false con ID duplicado");
    }

    // FIND BY ID

    @DisplayName("Test verifica findById null con id null")
    @Order(4)
    @Test
    void findByIdNullIdTest() {
        Usuario resultado = service.findById(null);
        Assertions.assertNull(resultado, "findById debe devolver null si el id es null");
    }

    @DisplayName("Test verifica findById null con id inexistente")
    @Order(5)
    @Test
    void findByIdInexistenteTest() {
        Usuario resultado = service.findById(999);
        Assertions.assertNull(resultado, "findById debe devolver null para un ID inexistente");
    }

    @DisplayName("Test verifica findById devuelve usuario correcto")
    @Order(6)
    @Test
    void findByIdCorrectaTest() {
        service.create(usuario, contrasenia);
        Usuario resultado = service.findById(id);
        Assertions.assertEquals(usuario, resultado, "findById debe devolver el usuario insertado");
    }

    // FIND ALL

    @DisplayName("Test verifica findAll not null")
    @Order(7)
    @Test
    void findAllNotNullTest() {
        Assertions.assertNotNull(service.findAll(), "findAll no debe devolver null");
    }

    @DisplayName("Test verifica findAll contiene usuario insertado")
    @Order(8)
    @Test
    void findAllContieneTest() {
        service.create(usuario, contrasenia);
        boolean contiene = service.findAll().contains(usuario);
        Assertions.assertTrue(contiene, "findAll debe contener el usuario insertado");
    }

    // FIND ID BY NAME

    @DisplayName("Test verifica findIdByName null con nombre null")
    @Order(9)
    @Test
    void findIdByNameNullNombreTest() {
        Integer resultado = service.findIdByName(null);
        Assertions.assertNull(resultado, "findIdByName debe devolver null si el nombre es null");
    }

    @DisplayName("Test verifica findIdByName null con nombre en blanco")
    @Order(10)
    @Test
    void findIdByNameBlankNombreTest() {
        Integer resultado = service.findIdByName("   ");
        Assertions.assertNull(resultado, "findIdByName debe devolver null si el nombre está en blanco");
    }

    @DisplayName("Test verifica findIdByName null con nombre inexistente")
    @Order(11)
    @Test
    void findIdByNameInexistenteTest() {
        Integer resultado = service.findIdByName("Nombre Fantasma");
        Assertions.assertNull(resultado, "findIdByName debe devolver null para un nombre inexistente");
    }

    @DisplayName("Test verifica findIdByName devuelve el ID correcto")
    @Order(12)
    @Test
    void findIdByNameCorrectoTest() {
        service.create(usuario, contrasenia);
        Integer resultado = service.findIdByName(nombre);
        Assertions.assertEquals(id, resultado, "findIdByName debe devolver el ID correcto para el nombre dado");
    }

    // UPDATE

    @DisplayName("Test verifica update true con usuario valido")
    @Order(13)
    @Test
    void updateTrueTest() {
        service.create(usuario, contrasenia);
        Usuario modificado = new Usuario(id, "Ana Modificada", dni, email, "+34699000000", tipoUsuario);
        boolean resultado = service.update(modificado);
        Assertions.assertTrue(resultado, "update debe devolver true con usuario valido");
    }

    @DisplayName("Test verifica update false con id inexistente")
    @Order(14)
    @Test
    void updateFalseTest() {
        Usuario fantasma = new Usuario(999, nombre, dni, email, telefono, tipoUsuario);
        boolean resultado = service.update(fantasma);
        Assertions.assertFalse(resultado, "update debe devolver false si el ID no existe");
    }

    @DisplayName("Test verifica update modifica el nombre correctamente")
    @Order(15)
    @Test
    void updateModificaNombreTest() {
        service.create(usuario, contrasenia);
        Usuario modificado = new Usuario(id, "Nombre Nuevo", dni, email, telefono, tipoUsuario);
        service.update(modificado);
        Assertions.assertEquals("Nombre Nuevo", service.findById(id).getNombre(), "El nombre debe haberse actualizado");
    }

    // DELETE BY ID

    @DisplayName("Test verifica deleteById false con id inexistente")
    @Order(16)
    @Test
    void deleteByIdFalseTest() {
        boolean resultado = service.deleteById(999);
        Assertions.assertFalse(resultado, "deleteById debe devolver false para un ID inexistente");
    }

    @DisplayName("Test verifica deleteById true con id existente")
    @Order(17)
    @Test
    void deleteByIdTrueTest() {
        service.create(usuario, contrasenia);
        boolean resultado = service.deleteById(id);
        Assertions.assertTrue(resultado, "deleteById debe devolver true al eliminar un registro existente");
    }

    @DisplayName("Test verifica deleteById elimina el registro")
    @Order(18)
    @Test
    void deleteByIdEliminaTest() {
        service.create(usuario, contrasenia);
        service.deleteById(id);
        Assertions.assertNull(service.findById(id), "El usuario no debe existir tras eliminarlo");
    }
}
