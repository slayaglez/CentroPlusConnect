package proyecto.intermodular.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import proyecto.intermodular.app.model.Actividad;
import proyecto.intermodular.app.repository.interfaces.IActividadRepository;
import proyecto.intermodular.app.service.ActividadService;
import proyecto.intermodular.app.service.interfaces.IActividadService;

public class ActividadServiceTest {

    IActividadService actividadService;
    @Mock
    IActividadRepository actividadRepositoryMock;

    /*@BeforeEach
    void setup() {
        actividadService = new ActividadService(actividadRepositoryMock);
    }*/

    @Test
    void findBy0IdTest() {
        Actividad actividad = actividadService.findById(0);
        Assertions.assertNull(actividad);
    }

    @Test
    void findByIdTest() {
        Actividad actividad = new Actividad(1);

        //when(actividadRepository.findById(anyInt())).thenReturn(actividad);
        Actividad actividadFind = actividadService.findById(1);
        Assertions.assertNotNull(actividad);
    }
}
