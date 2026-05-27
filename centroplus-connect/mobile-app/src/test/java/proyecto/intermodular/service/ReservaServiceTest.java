package proyecto.intermodular.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import proyecto.intermodular.app.model.Actividad;
import proyecto.intermodular.app.model.Reserva;

public class ReservaServiceTest {

    Reserva reserva;

    @BeforeEach
    void setup(){
        System.out.println("Se ejecuta antes del test");
    }

    @Test 
    void testDePrueba(){
        System.out.println("Test de prueba");
    }
}
