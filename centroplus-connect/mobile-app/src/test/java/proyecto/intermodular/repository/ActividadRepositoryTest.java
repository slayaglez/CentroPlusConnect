package proyecto.intermodular.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import proyecto.intermodular.app.model.Actividad;
import proyecto.intermodular.app.repository.interfaces.IActividadRepository;

public class ActividadRepositoryTest {

    private IActividadRepository repository;

    @Test
    public void findByIdTestOk() {

        Actividad actividad = repository.findById(1);

        Assertions.assertNotNull(actividad);
    }
}
