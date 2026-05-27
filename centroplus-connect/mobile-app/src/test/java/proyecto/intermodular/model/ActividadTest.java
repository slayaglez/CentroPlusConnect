package proyecto.intermodular.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import proyecto.intermodular.app.model.Actividad;

public class ActividadTest {

    @Test
    public void getNombreTestOk() {

        Actividad actividad = new Actividad();
        actividad.setNombre("Yoga");
        Assertions.assertEquals("Yoga",actividad.getNombre());
    }
}
