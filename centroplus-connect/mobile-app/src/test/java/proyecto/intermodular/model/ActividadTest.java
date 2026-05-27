package proyecto.intermodular.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import proyecto.intermodular.app.model.Actividad;

class ActividadTest {

    Actividad actividad;
    int id = 1;
    String nombre = "Nombre";
    String tipo = "Tipo";
    int duracion = 1;
    int precio = 2;
    int plazas = 5;
    int ocupadas = 2;

    //Se ejecuta antes del test
    @BeforeEach
    void setup() {
        actividad = new Actividad(id, nombre, tipo, duracion, precio, plazas, ocupadas);
    }

    @DisplayName("Test verifica not null")
    @Order(1)
    @Test
    void actividadNotNullTest() {
        Assertions.assertNotNull(actividad, "La clase actividad no puede ser null");
    }

    @DisplayName("Test verifica equals true")
    @Order(2)
    @Test
    void actividadEqualsTrueTest() {
        Actividad actividadNueva = new Actividad(1);
        Assertions.assertEquals(actividad, actividadNueva, "Debe de ser igual");
    }

    @DisplayName("Test verifica equals false")
    @Order(3)
    @Test
    void actividadEqualsFalseTest() {
        Actividad actividadNueva = new Actividad(2);
        Assertions.assertNotEquals(actividad, actividadNueva, "Debe de ser diferente");
    }

    @DisplayName("Test verifica equals de la misma clase")
    @Order(4)
    @Test
    void actividadEqualsTest() {
        Assertions.assertEquals(actividad, actividad, "Debe de ser igual");
    }

    /*@Test
    void actividadPlazasDisponiblesTest() {
        actividad.cancelarPlaza();
        Assertions.assertEquals(plazas-ocupadas+1, actividad.getPlazasDisponibles());
    }*/

    @Test
    public void getNombreTestOk() {

        Actividad actividad = new Actividad();
        actividad.setNombre("Yoga");
        Assertions.assertEquals("Yoga",actividad.getNombre());
    }

}
