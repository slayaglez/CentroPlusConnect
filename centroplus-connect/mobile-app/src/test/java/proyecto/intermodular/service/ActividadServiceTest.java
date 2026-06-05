package proyecto.intermodular.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import proyecto.intermodular.app.model.Actividad;
import proyecto.intermodular.app.service.ActividadService;
import proyecto.intermodular.app.service.interfaces.IActividadService;
import proyecto.intermodular.database.sqlite.SQLiteConnectionManager;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

public class ActividadServiceTest {

    private static final String RUTA_TEST = "./database/centroplus_test.db";

    private IActividadService service;

    Actividad actividad;
    int id = 1;
    String nombre = "Yoga";
    String tipo = "DEPORTIVA";
    int duracion = 60;
    double precio = 15.0;
    int plazas = 20;
    int ocupadas = 5;

    @BeforeAll
    static void crearBdTest() throws Exception {
        SQLiteConnectionManager.rutaDb = RUTA_TEST;
        new File("./database").mkdirs();

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
        SQLiteConnectionManager.rutaDb = RUTA_TEST;
        service = new ActividadService();
        actividad = new Actividad(id, nombre, tipo, duracion, precio, plazas, ocupadas);

        try (Connection conn = SQLiteConnectionManager.openConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM actividades");
        }
    }

    // CREATE

    @DisplayName("Test verifica create true con actividad valida")
    @Order(1)
    @Test
    void createTrueTest() {
        boolean resultado = service.create(actividad);
        Assertions.assertTrue(resultado, "create debe devolver true con actividad valida");
    }

    @DisplayName("Test verifica create false con actividad null")
    @Order(2)
    @Test
    void createNullTest() {
        boolean resultado = service.create(null);
        Assertions.assertFalse(resultado, "create debe devolver false con actividad null");
    }

    @DisplayName("Test verifica create false con tipo de actividad invalido")
    @Order(3)
    @Test
    void createTipoInvalidoTest() {
        Actividad invalida = new Actividad(id, nombre, "INVALIDO", duracion, precio, plazas, ocupadas);
        boolean resultado = service.create(invalida);
        Assertions.assertFalse(resultado, "create debe devolver false con tipo de actividad invalido");
    }

    @DisplayName("Test verifica create false con precio negativo")
    @Order(4)
    @Test
    void createPrecioNegativoTest() {
        Actividad invalida = new Actividad(id, nombre, tipo, duracion, -1.0, plazas, ocupadas);
        boolean resultado = service.create(invalida);
        Assertions.assertFalse(resultado, "create debe devolver false con precio negativo");
    }

    @DisplayName("Test verifica create false con plazas maximas cero")
    @Order(5)
    @Test
    void createPlazasMaximasCeroTest() {
        Actividad invalida = new Actividad(id, nombre, tipo, duracion, precio, 0, ocupadas);
        boolean resultado = service.create(invalida);
        Assertions.assertFalse(resultado, "create debe devolver false con plazas maximas cero");
    }

    @DisplayName("Test verifica create false con plazas ocupadas negativas")
    @Order(6)
    @Test
    void createPlazasOcupadasNegativasTest() {
        Actividad invalida = new Actividad(id, nombre, tipo, duracion, precio, plazas, -1);
        boolean resultado = service.create(invalida);
        Assertions.assertFalse(resultado, "create debe devolver false con plazas ocupadas negativas");
    }

    @DisplayName("Test verifica create false con duracion negativa")
    @Order(7)
    @Test
    void createDuracionNegativaTest() {
        Actividad invalida = new Actividad(id, nombre, tipo, -1, precio, plazas, ocupadas);
        boolean resultado = service.create(invalida);
        Assertions.assertFalse(resultado, "create debe devolver false con duracion negativa");
    }

    @DisplayName("Test verifica create con id null usa createAutoId")
    @Order(8)
    @Test
    void createAutoIdTest() {
        Actividad sinId = new Actividad();
        sinId.setNombre(nombre);
        sinId.setTipoActividad(tipo);
        sinId.setDuracion(duracion);
        sinId.setPrecio(precio);
        sinId.setPlazasMaximas(plazas);
        sinId.setPlazasOcupadas(ocupadas);
        // id es null → debe usar createAutoId
        boolean resultado = service.create(sinId);
        Assertions.assertTrue(resultado, "create con id null debe delegar en createAutoId y devolver true");
    }

    // FIND BY ID

    @DisplayName("Test verifica findById null con id null")
    @Order(9)
    @Test
    void findByIdNullIdTest() {
        Actividad resultado = service.findById(null);
        Assertions.assertNull(resultado, "findById debe devolver null si el id es null");
    }

    @DisplayName("Test verifica findById null con id inexistente")
    @Order(10)
    @Test
    void findByIdInexistenteTest() {
        Actividad resultado = service.findById(999);
        Assertions.assertNull(resultado, "findById debe devolver null para un ID inexistente");
    }

    @DisplayName("Test verifica findById devuelve actividad correcta")
    @Order(11)
    @Test
    void findByIdCorrectaTest() {
        service.create(actividad);
        Actividad resultado = service.findById(id);
        Assertions.assertEquals(actividad, resultado, "findById debe devolver la actividad insertada");
    }

    // FIND ALL

    @DisplayName("Test verifica findAll not null")
    @Order(12)
    @Test
    void findAllNotNullTest() {
        Assertions.assertNotNull(service.findAll(), "findAll no debe devolver null");
    }

    @DisplayName("Test verifica findAll contiene actividad insertada")
    @Order(13)
    @Test
    void findAllContieneTest() {
        service.create(actividad);
        boolean contiene = service.findAll().contains(actividad);
        Assertions.assertTrue(contiene, "findAll debe contener la actividad insertada");
    }

    // UPDATE

    @DisplayName("Test verifica update true con actividad valida")
    @Order(14)
    @Test
    void updateTrueTest() {
        service.create(actividad);
        Actividad modificada = new Actividad(id, "Pilates", tipo, duracion, precio, plazas, ocupadas);
        boolean resultado = service.update(modificada);
        Assertions.assertTrue(resultado, "update debe devolver true con actividad valida");
    }

    @DisplayName("Test verifica update false con actividad null")
    @Order(15)
    @Test
    void updateNullTest() {
        boolean resultado = service.update(null);
        Assertions.assertFalse(resultado, "update debe devolver false con actividad null");
    }

    @DisplayName("Test verifica update false con tipo invalido")
    @Order(16)
    @Test
    void updateTipoInvalidoTest() {
        service.create(actividad);
        Actividad invalida = new Actividad(id, nombre, "INVALIDO", duracion, precio, plazas, ocupadas);
        boolean resultado = service.update(invalida);
        Assertions.assertFalse(resultado, "update debe devolver false con tipo de actividad invalido");
    }

    @DisplayName("Test verifica update false con precio negativo")
    @Order(17)
    @Test
    void updatePrecioNegativoTest() {
        service.create(actividad);
        Actividad invalida = new Actividad(id, nombre, tipo, duracion, -5.0, plazas, ocupadas);
        boolean resultado = service.update(invalida);
        Assertions.assertFalse(resultado, "update debe devolver false con precio negativo");
    }

    // DELETE BY ID

    @DisplayName("Test verifica deleteById false con id null")
    @Order(18)
    @Test
    void deleteByIdNullTest() {
        boolean resultado = service.deleteById(null);
        Assertions.assertFalse(resultado, "deleteById debe devolver false si el id es null");
    }

    @DisplayName("Test verifica deleteById true con id existente")
    @Order(19)
    @Test
    void deleteByIdTrueTest() {
        service.create(actividad);
        boolean resultado = service.deleteById(id);
        Assertions.assertTrue(resultado, "deleteById debe devolver true al eliminar un registro existente");
    }

    @DisplayName("Test verifica deleteById false con id inexistente")
    @Order(20)
    @Test
    void deleteByIdFalseTest() {
        boolean resultado = service.deleteById(999);
        Assertions.assertFalse(resultado, "deleteById debe devolver false para un ID inexistente");
    }

    // RESERVAR PLAZA

    @DisplayName("Test verifica reservarPlaza true cuando hay hueco")
    @Order(21)
    @Test
    void reservarPlazaTrueTest() {
        service.create(actividad);
        boolean resultado = service.reservarPlaza(id);
        Assertions.assertTrue(resultado, "reservarPlaza debe devolver true cuando hay plazas libres");
    }

    @DisplayName("Test verifica reservarPlaza false cuando esta completa")
    @Order(22)
    @Test
    void reservarPlazaFalseTest() {
        service.create(new Actividad(2, "Pilates", tipo, 45, 12.5, 10, 10));
        boolean resultado = service.reservarPlaza(2);
        Assertions.assertFalse(resultado, "reservarPlaza debe devolver false si no hay plazas libres");
    }

    // CANCELAR PLAZA

    @DisplayName("Test verifica cancelarPlaza true cuando hay ocupadas")
    @Order(23)
    @Test
    void cancelarPlazaTrueTest() {
        service.create(actividad);
        boolean resultado = service.cancelarPlaza(id);
        Assertions.assertTrue(resultado, "cancelarPlaza debe devolver true cuando hay plazas ocupadas");
    }

    @DisplayName("Test verifica cancelarPlaza false cuando ocupadas es 0")
    @Order(24)
    @Test
    void cancelarPlazaFalseTest() {
        service.create(new Actividad(2, "Pilates", tipo, 45, 12.5, 10, 0));
        boolean resultado = service.cancelarPlaza(2);
        Assertions.assertFalse(resultado, "cancelarPlaza debe devolver false si no hay plazas ocupadas");
    }

    // FIND COMPLETAS

    @DisplayName("Test verifica findCompletas not null")
    @Order(25)
    @Test
    void findCompletasNotNullTest() {
        Assertions.assertNotNull(service.findCompletas(), "findCompletas no debe devolver null");
    }

    @DisplayName("Test verifica findCompletas contiene actividad completa")
    @Order(26)
    @Test
    void findCompletasContieneTest() {
        Actividad completa = new Actividad(2, "Pilates", tipo, 45, 12.5, 10, 10);
        service.create(completa);
        boolean contiene = service.findCompletas().contains(completa);
        Assertions.assertTrue(contiene, "findCompletas debe contener la actividad con aforo completo");
    }

    // CALCULAR INGRESOS TOTALES

    @DisplayName("Test verifica calcularIngresosTotales no es negativo")
    @Order(27)
    @Test
    void calcularIngresosTotalesPositivoTest() {
        double resultado = service.calcularIngresosTotales();
        Assertions.assertTrue(resultado >= 0, "Los ingresos totales no pueden ser negativos");
    }

    @DisplayName("Test verifica calcularIngresosTotales calculo correcto")
    @Order(28)
    @Test
    void calcularIngresosTotalesCorrectoTest() {
        service.create(actividad); // 15.0 * 5 = 75.0
        service.create(new Actividad(2, "Pilates", tipo, 45, 12.5, 10, 10)); // 12.5 * 10 = 125.0
        double resultado = service.calcularIngresosTotales();
        Assertions.assertEquals(200.0, resultado, 0.001, "Los ingresos totales deben ser 200.0");
    }
}