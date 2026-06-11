package proyecto.intermodular.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import proyecto.intermodular.app.model.Usuario;
import proyecto.intermodular.app.repository.UsuarioRepository;
import proyecto.intermodular.app.repository.interfaces.IUsuarioRepository;
import proyecto.intermodular.database.sqlite.SQLiteConnectionManager;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class UsuarioRepositoryTest {

    private static final String RUTA_TEST = "./database/centroplus_test.db";

    private IUsuarioRepository repository;

    Usuario usuario;
    int id = 1;
    String nombre = "Ana García";
    String dni = "12345678A";
    String email = "ana.garcia@email.com";
    String telefono = "600111222";
    String tipoUsuario = "CLIENTE";
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
                ")"
            );
        }
    }

    @BeforeEach
    void setup() throws Exception {
        SQLiteConnectionManager.rutaDb = RUTA_TEST;

        repository = new UsuarioRepository();
        usuario = new Usuario(id, nombre, dni, email, telefono, tipoUsuario);

        try (Connection conn = SQLiteConnectionManager.openConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM usuarios");
        }
    }

    // CREATE

    @DisplayName("Test verifica create not null")
    @Order(1)
    @Test
    void createNotNullTest() {
        boolean resultado = repository.create(usuario, contrasenia);
        Assertions.assertNotNull(resultado, "El resultado de create no puede ser null");
    }

    @DisplayName("Test verifica create true")
    @Order(2)
    @Test
    void createTrueTest() {
        boolean resultado = repository.create(usuario, contrasenia);
        Assertions.assertTrue(resultado, "create debe devolver true al insertar correctamente");
    }

    @DisplayName("Test verifica create false con id duplicado")
    @Order(3)
    @Test
    void createFalseTest() {
        repository.create(usuario, contrasenia);
        boolean resultado = repository.create(usuario, contrasenia);
        Assertions.assertFalse(resultado, "create debe devolver false con ID duplicado");
    }

    // CREATE AUTO ID

    @DisplayName("Test verifica createAutoId true")
    @Order(4)
    @Test
    void createAutoIdTrueTest() {
        Usuario sinId = new Usuario(0, nombre, dni, email, telefono, tipoUsuario);
        boolean resultado = repository.createAutoId(sinId, contrasenia);
        Assertions.assertTrue(resultado, "createAutoId debe devolver true");
    }

    // FIND BY ID

    @DisplayName("Test verifica findById not null")
    @Order(5)
    @Test
    void findByIdNotNullTest() {
        repository.create(usuario, contrasenia);
        Usuario resultado = repository.findById(id);
        Assertions.assertNotNull(resultado, "findById no debe devolver null para un ID existente");
    }

    @DisplayName("Test verifica findById null")
    @Order(6)
    @Test
    void findByIdNullTest() {
        Usuario resultado = repository.findById(999);
        Assertions.assertNull(resultado, "findById debe devolver null para un ID inexistente");
    }

    @DisplayName("Test verifica findById devuelve usuario correcto")
    @Order(7)
    @Test
    void findByIdCorrectaTest() {
        repository.create(usuario, contrasenia);
        Usuario resultado = repository.findById(id);
        Assertions.assertEquals(usuario, resultado, "El usuario encontrado debe coincidir con el insertado");
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
        repository.create(usuario, contrasenia);
        boolean contiene = repository.findAll().contains(usuario);
        Assertions.assertTrue(contiene, "findAll debe contener el usuario insertado");
    }

    @DisplayName("Test verifica findAll con varios registros")
    @Order(10)
    @Test
    void findAllVariosTest() {
        repository.create(usuario, contrasenia);
        repository.create(new Usuario(2, "Carlos López", "87654321B", "carlos@email.com", "611222333", "ADMIN"), "otraPass");
        int tamanio = repository.findAll().size();
        Assertions.assertEquals(2, tamanio, "findAll debe devolver 2 usuarios");
    }


    // FIND ID BY NAME

    @DisplayName("Test verifica findIdByName not null")
    @Order(11)
    @Test
    void findIdByNameNotNullTest() {
        repository.create(usuario, contrasenia);
        Integer resultado = repository.findIdByName(nombre);
        Assertions.assertNotNull(resultado, "findIdByName no debe devolver null para un nombre existente");
    }

    @DisplayName("Test verifica findIdByName null con nombre inexistente")
    @Order(12)
    @Test
    void findIdByNameNullTest() {
        Integer resultado = repository.findIdByName("Nombre Fantasma");
        Assertions.assertNull(resultado, "findIdByName debe devolver null para un nombre inexistente");
    }

    @DisplayName("Test verifica findIdByName devuelve el ID correcto")
    @Order(13)
    @Test
    void findIdByNameCorrectoTest() {
        repository.create(usuario, contrasenia);
        Integer resultado = repository.findIdByName(nombre);
        Assertions.assertEquals(id, resultado, "findIdByName debe devolver el ID correcto para el nombre dado");
    }

    // UPDATE

    @DisplayName("Test verifica update true")
    @Order(14)
    @Test
    void updateTrueTest() {
        repository.create(usuario, contrasenia);
        Usuario modificado = new Usuario(id, "Ana Modificada", dni, email, "699000000", tipoUsuario);
        boolean resultado = repository.update(modificado);
        Assertions.assertTrue(resultado, "update debe devolver true al actualizar correctamente");
    }

    @DisplayName("Test verifica update false con id inexistente")
    @Order(15)
    @Test
    void updateFalseTest() {
        Usuario fantasma = new Usuario(999, nombre, dni, email, telefono, tipoUsuario);
        boolean resultado = repository.update(fantasma);
        Assertions.assertFalse(resultado, "update debe devolver false si el ID no existe");
    }

    @DisplayName("Test verifica update modifica el nombre")
    @Order(16)
    @Test
    void updateModificaNombreTest() {
        repository.create(usuario, contrasenia);
        Usuario modificado = new Usuario(id, "Nombre Nuevo", dni, email, telefono, tipoUsuario);
        repository.update(modificado);
        Assertions.assertEquals("Nombre Nuevo", repository.findById(id).getNombre(), "El nombre debe haberse actualizado");
    }

    // DELETE BY ID

    @DisplayName("Test verifica deleteById true")
    @Order(17)
    @Test
    void deleteByIdTrueTest() {
        repository.create(usuario, contrasenia);
        boolean resultado = repository.deleteById(id);
        Assertions.assertTrue(resultado, "deleteById debe devolver true al eliminar correctamente");
    }

    @DisplayName("Test verifica deleteById false con id inexistente")
    @Order(18)
    @Test
    void deleteByIdFalseTest() {
        boolean resultado = repository.deleteById(999);
        Assertions.assertFalse(resultado, "deleteById debe devolver false para un ID inexistente");
    }

    @DisplayName("Test verifica deleteById elimina el registro")
    @Order(19)
    @Test
    void deleteByIdEliminaTest() {
        repository.create(usuario, contrasenia);
        repository.deleteById(id);
        Assertions.assertNull(repository.findById(id), "El usuario no debe existir tras eliminarlo");
    }
}
