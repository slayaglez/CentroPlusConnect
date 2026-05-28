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

    // Se ejecuta antes del test
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

    @DisplayName("Test verifica equals con null")
    @Order(5)
    @Test
    public void equalsConNullTestOk() {
        Assertions.assertNotEquals(actividad, null);
    }

    @DisplayName("Test verifica hash code igual")
    @Order(6)
    @Test
    public void hashCodeIgualTestOk() {
        Actividad otraActividad = new Actividad(1);
        Assertions.assertEquals(actividad.hashCode(), otraActividad.hashCode());
    }

    @DisplayName("Test verifica hash code diferente")
    @Order(7)
    @Test
    public void hashCodeDiferenteTestOk() {
        Actividad otraActividad = new Actividad(99);
        Assertions.assertNotEquals(actividad.hashCode(), otraActividad.hashCode());
    }

    @DisplayName("Test verifica to string")
    @Order(8)
    @Test
    public void toStringTestOk() {
        String resultado = actividad.toString();
        Assertions.assertNotNull(resultado);
    }

    @DisplayName("Test verifica plazas disponibles")
    @Order(9)
    @Test
    void actividadPlazasDisponiblesTest() {
        actividad.cancelarPlaza();
        Assertions.assertEquals(plazas - ocupadas + 1, actividad.getPlazasDisponibles());
    }

    @DisplayName("Test verifica id")
    @Order(10)
    @Test
    public void getIdTestOk() {
        Actividad actividad = new Actividad();
        actividad.setId(10);
        Assertions.assertEquals(10, actividad.getId());
    }

    @DisplayName("Test verifica setId")
    @Order(11)
    @Test
    public void setIdTestOk() {
        actividad.setId(10);
        Assertions.assertEquals(10, actividad.getId());
    }

    @DisplayName("Test verifica getNombre")
    @Order(12)
    @Test
    public void getNombreTestOk() {
        Actividad actividad = new Actividad();
        actividad.setNombre("Yoga");
        Assertions.assertEquals("Yoga", actividad.getNombre());
    }

    @DisplayName("Test verifica setNombre")
    @Order(13)
    @Test
    public void setNombreTestOk() {
        actividad.setNombre("Pilates");
        Assertions.assertEquals("Pilates", actividad.getNombre());
    }

    @DisplayName("Test verifica getTipoActividad")
    @Order(14)
    @Test
    public void getTipoActividadTestOk() {
        Actividad actividad = new Actividad();
        actividad.setTipoActividad("DEPORTIVA");
        Assertions.assertEquals("DEPORTIVA", actividad.getTipoActividad());
    }

    @DisplayName("Test verifica setTipoActividad")
    @Order(15)
    @Test
    public void setTipoActividadTestOk() {
        actividad.setTipoActividad("ACADEMICA");
        Assertions.assertEquals("ACADEMICA", actividad.getTipoActividad());
    }

    @DisplayName("Test verifica getDuracion")
    @Order(16)
    @Test
    public void getDuracionTestOk() {
        Actividad actividad = new Actividad();
        actividad.setDuracion(55);
        Assertions.assertEquals(55, actividad.getDuracion());
    }

    @DisplayName("Test verifica setDuracion")
    @Order(17)
    @Test
    public void setDuracionTestOk() {
        actividad.setDuracion(60);
        Assertions.assertEquals(60, actividad.getDuracion());
    }

    @DisplayName("Test verifica getPrecio")
    @Order(18)
    @Test
    public void getPrecioTestOk() {
        Assertions.assertEquals(precio, actividad.getPrecio());
    }

    @DisplayName("Test verifica setPrecio")
    @Order(19)
    @Test
    public void setPrecioTestOk() {
        actividad.setPrecio(99.99);
        Assertions.assertEquals(99.99, actividad.getPrecio());
    }

    @DisplayName("Test verifica getPlazasMaximas")
    @Order(20)
    @Test
    public void getPlazasMaximasTestOk() {
        Assertions.assertEquals(plazas, actividad.getPlazasMaximas());
    }

    @DisplayName("Test verifica setPlazasMaximas")
    @Order(21)
    @Test
    public void setPlazasMaximasTestOk() {
        actividad.setPlazasMaximas(20);
        Assertions.assertEquals(20, actividad.getPlazasMaximas());
    }

    @DisplayName("Test verifica getPlazasOcupadas")
    @Order(22)
    @Test
    public void getPlazasOcupadasTestOk() {
        Assertions.assertEquals(ocupadas, actividad.getPlazasOcupadas());
    }

    @DisplayName("Test verifica setPlazasOcupadas")
    @Order(23)
    @Test
    public void setPlazasOcupadasTestOk() {
        actividad.setPlazasOcupadas(3);
        Assertions.assertEquals(3, actividad.getPlazasOcupadas());
    }

}
