package proyecto.intermodular.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import proyecto.intermodular.app.model.Actividad;
import proyecto.intermodular.app.repository.ActividadRepository;
import proyecto.intermodular.app.repository.interfaces.IActividadRepository;
import proyecto.intermodular.database.sqlite.SQLiteConnectionManager;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class ActividadRepositoryTest {

    // Fichero de BD exclusivo para tests, separado de la BD real
    private static final String RUTA_TEST = "./database/centroplus_test.db";

    private IActividadRepository repository;

    Actividad actividad;
    int id = 1;
    String nombre = "Yoga";
    String tipo = "Relajacion";
    int duracion = 60;
    double precio = 15.0;
    int plazas = 20;
    int ocupadas = 5;

    @BeforeAll
    static void crearBdTest() throws Exception {
        // Apuntar siempre al fichero de test
        SQLiteConnectionManager.rutaDb = RUTA_TEST;

        // Crear carpeta si no existe
        new File("./database").mkdirs();

        // Crear la tabla (si no existe ya)
        try (Connection conn = SQLiteConnectionManager.openConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS actividades (" +
                "    id              INTEGER PRIMARY KEY," +
                "    nombre          TEXT    NOT NULL," +
                "    tipo_actividad  TEXT    NOT NULL," +
                "    duracion        INTEGER NOT NULL," +
                "    precio          REAL    NOT NULL," +
                "    plazas_maximas  INTEGER NOT NULL," +
                "    plazas_ocupadas INTEGER NOT NULL" +
                ")"
            );
        }
    }

    @BeforeEach
    void setup() throws Exception {
        // Aseguramos la ruta de test en cada test (por si Maven la resetea)
        SQLiteConnectionManager.rutaDb = RUTA_TEST;

        repository = new ActividadRepository();
        actividad = new Actividad(id, nombre, tipo, duracion, precio, plazas, ocupadas);

        // Limpiar tabla antes de cada test con SQL directo
        try (Connection conn = SQLiteConnectionManager.openConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM actividades");
        }
    }

    // CREATE

    @DisplayName("Test verifica create not null")
    @Order(1)
    @Test
    void createNotNullTest() {
        boolean resultado = repository.create(actividad);
        Assertions.assertNotNull(resultado, "El resultado de create no puede ser null");
    }

    @DisplayName("Test verifica create true")
    @Order(2)
    @Test
    void createTrueTest() {
        boolean resultado = repository.create(actividad);
        Assertions.assertTrue(resultado, "create debe devolver true al insertar correctamente");
    }

    @DisplayName("Test verifica create false con id duplicado")
    @Order(3)
    @Test
    void createFalseTest() {
        repository.create(actividad);
        boolean resultado = repository.create(actividad);
        Assertions.assertFalse(resultado, "create debe devolver false con ID duplicado");
    }

    // CREATE AUTO ID

    @DisplayName("Test verifica createAutoId true")
    @Order(4)
    @Test
    void createAutoIdTrueTest() {
        Actividad sinId = new Actividad(0, nombre, tipo, duracion, precio, plazas, ocupadas);
        boolean resultado = repository.createAutoId(sinId);
        Assertions.assertTrue(resultado, "createAutoId debe devolver true");
    }

    // FIND BY ID

    @DisplayName("Test verifica findById not null")
    @Order(5)
    @Test
    void findByIdNotNullTest() {
        repository.create(actividad);
        Actividad resultado = repository.findById(id);
        Assertions.assertNotNull(resultado, "findById no debe devolver null para un ID existente");
    }

    @DisplayName("Test verifica findById null")
    @Order(6)
    @Test
    void findByIdNullTest() {
        Actividad resultado = repository.findById(999);
        Assertions.assertNull(resultado, "findById debe devolver null para un ID inexistente");
    }

    @DisplayName("Test verifica findById devuelve actividad correcta")
    @Order(7)
    @Test
    void findByIdCorrectaTest() {
        repository.create(actividad);
        Actividad resultado = repository.findById(id);
        Assertions.assertEquals(actividad, resultado, "La actividad encontrada debe coincidir con la insertada");
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
        repository.create(actividad);
        boolean contiene = repository.findAll().contains(actividad);
        Assertions.assertTrue(contiene, "findAll debe contener la actividad insertada");
    }

    @DisplayName("Test verifica findAll con varios registros")
    @Order(10)
    @Test
    void findAllVariosTest() {
        repository.create(actividad);
        repository.create(new Actividad(2, "Pilates", "Tonificacion", 45, 12.5, 10, 10));
        int tamanio = repository.findAll().size();
        Assertions.assertEquals(2, tamanio, "findAll debe devolver 2 actividades");
    }

    // UPDATE

    @DisplayName("Test verifica update true")
    @Order(11)
    @Test
    void updateTrueTest() {
        repository.create(actividad);
        Actividad modificada = new Actividad(id, "Yoga Avanzado", tipo, 75, 18.0, plazas, ocupadas);
        boolean resultado = repository.update(modificada);
        Assertions.assertTrue(resultado, "update debe devolver true al actualizar correctamente");
    }

    @DisplayName("Test verifica update false con id inexistente")
    @Order(12)
    @Test
    void updateFalseTest() {
        Actividad fantasma = new Actividad(999, nombre, tipo, duracion, precio, plazas, ocupadas);
        boolean resultado = repository.update(fantasma);
        Assertions.assertFalse(resultado, "update debe devolver false si el ID no existe");
    }

    @DisplayName("Test verifica update modifica el nombre")
    @Order(13)
    @Test
    void updateModificaNombreTest() {
        repository.create(actividad);
        Actividad modificada = new Actividad(id, "Yoga Avanzado", tipo, duracion, precio, plazas, ocupadas);
        repository.update(modificada);
        Assertions.assertEquals("Yoga Avanzado", repository.findById(id).getNombre(), "El nombre debe haberse actualizado");
    }

    // DELETE BY ID

    @DisplayName("Test verifica deleteById true")
    @Order(14)
    @Test
    void deleteByIdTrueTest() {
        repository.create(actividad);
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
        repository.create(actividad);
        repository.deleteById(id);
        Assertions.assertNull(repository.findById(id), "La actividad no debe existir tras eliminarla");
    }

    // RESERVAR PLAZA

    @DisplayName("Test verifica reservarPlaza true")
    @Order(17)
    @Test
    void reservarPlazaTrueTest() {
        repository.create(actividad);
        boolean resultado = repository.reservarPlaza(id);
        Assertions.assertTrue(resultado, "reservarPlaza debe devolver true cuando hay plazas libres");
    }

    @DisplayName("Test verifica reservarPlaza false cuando esta completa")
    @Order(18)
    @Test
    void reservarPlazaFalseTest() {
        repository.create(new Actividad(2, "Pilates", "Tonificacion", 45, 12.5, 10, 10));
        boolean resultado = repository.reservarPlaza(2);
        Assertions.assertFalse(resultado, "reservarPlaza debe devolver false si no hay plazas libres");
    }

    @DisplayName("Test verifica reservarPlaza incrementa plazas ocupadas")
    @Order(19)
    @Test
    void reservarPlazaIncrementaTest() {
        repository.create(actividad);
        repository.reservarPlaza(id);
        Assertions.assertEquals(ocupadas + 1, repository.findById(id).getPlazasOcupadas(), "Las plazas ocupadas deben incrementarse en 1");
    }

    // CANCELAR PLAZA

    @DisplayName("Test verifica cancelarPlaza true")
    @Order(20)
    @Test
    void cancelarPlazaTrueTest() {
        repository.create(actividad);
        boolean resultado = repository.cancelarPlaza(id);
        Assertions.assertTrue(resultado, "cancelarPlaza debe devolver true cuando hay plazas ocupadas");
    }

    @DisplayName("Test verifica cancelarPlaza false cuando ocupadas es 0")
    @Order(21)
    @Test
    void cancelarPlazaFalseTest() {
        repository.create(new Actividad(3, "Meditacion", "Bienestar", 30, 10.0, 15, 0));
        boolean resultado = repository.cancelarPlaza(3);
        Assertions.assertFalse(resultado, "cancelarPlaza debe devolver false si no hay plazas ocupadas");
    }

    @DisplayName("Test verifica cancelarPlaza decrementa plazas ocupadas")
    @Order(22)
    @Test
    void cancelarPlazaDecrementaTest() {
        repository.create(actividad);
        repository.cancelarPlaza(id);
        Assertions.assertEquals(ocupadas - 1, repository.findById(id).getPlazasOcupadas(), "Las plazas ocupadas deben decrementarse en 1");
    }

    // FIND COMPLETAS

    @DisplayName("Test verifica findCompletas not null")
    @Order(23)
    @Test
    void findCompletasNotNullTest() {
        Assertions.assertNotNull(repository.findCompletas(), "findCompletas no debe devolver null");
    }

    @DisplayName("Test verifica findCompletas devuelve actividad completa")
    @Order(24)
    @Test
    void findCompletasContieneTest() {
        Actividad completa = new Actividad(2, "Pilates", "Tonificacion", 45, 12.5, 10, 10);
        repository.create(completa);
        boolean contiene = repository.findCompletas().contains(completa);
        Assertions.assertTrue(contiene, "findCompletas debe contener la actividad con aforo completo");
    }

    @DisplayName("Test verifica findCompletas no incluye actividad con hueco")
    @Order(25)
    @Test
    void findCompletasNoIncluyeConHuecoTest() {
        repository.create(actividad);
        boolean contiene = repository.findCompletas().contains(actividad);
        Assertions.assertFalse(contiene, "findCompletas no debe incluir actividades con plazas disponibles");
    }

    // CALCULAR INGRESOS TOTALES

    @DisplayName("Test verifica calcularIngresosTotales no es negativo")
    @Order(26)
    @Test
    void calcularIngresosTotalesPositivoTest() {
        double resultado = repository.calcularIngresosTotales();
        Assertions.assertTrue(resultado >= 0, "Los ingresos totales no pueden ser negativos");
    }

    @DisplayName("Test verifica calcularIngresosTotales calculo correcto")
    @Order(27)
    @Test
    void calcularIngresosTotalesCorrectoTest() {
        repository.create(actividad); // 15.0 * 5 = 75.0
        repository.create(new Actividad(2, "Pilates", "Tonificacion", 45, 12.5, 10, 10)); // 12.5 * 10 = 125.0
        double resultado = repository.calcularIngresosTotales();
        Assertions.assertEquals(200.0, resultado, 0.001, "Los ingresos totales deben ser 200.0");
    }

    @DisplayName("Test verifica calcularIngresosTotales con actividad sin ocupadas")
    @Order(28)
    @Test
    void calcularIngresosTotalesCeroOcupadasTest() {
        repository.create(new Actividad(3, "Meditacion", "Bienestar", 30, 10.0, 15, 0));
        double resultado = repository.calcularIngresosTotales();
        Assertions.assertEquals(0.0, resultado, 0.001, "Los ingresos deben ser 0 si no hay plazas ocupadas");
    }
}