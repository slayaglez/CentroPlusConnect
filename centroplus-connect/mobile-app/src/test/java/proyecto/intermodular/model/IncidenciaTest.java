package proyecto.intermodular.model;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import proyecto.intermodular.app.model.Incidencia;

public class IncidenciaTest {

    Incidencia incidencia;

    int id = 1;
    int idUsuario = 1;
    String asunto = "asunto";
    String descripcion = "descripcion";
    String fechaStr = "2026-05-26";
    LocalDate fecha = LocalDate.parse(fechaStr);
    String estado = "ABIERTO";


    @BeforeEach
    void setup() {
        incidencia = new Incidencia(id, idUsuario, asunto, descripcion, fecha, estado);
    }

    @Test
    void incidenciaNotNull() {
        Assertions.assertNotNull(incidencia);
    }
    

}
