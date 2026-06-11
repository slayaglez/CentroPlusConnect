package proyecto.intermodular.validations;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import proyecto.intermodular.app.model.Actividad;
import proyecto.intermodular.app.model.Incidencia;
import proyecto.intermodular.app.model.Reserva;
import proyecto.intermodular.app.model.Usuario;

class ValidationsTest {

    // TIPO USUARIO

    @Test
    void testIsValidTipoUsuario() {
        assertTrue(Validations.isValidTipoUsuario("ALUMNO"));
        assertTrue(Validations.isValidTipoUsuario("SOCIO"));
        assertTrue(Validations.isValidTipoUsuario("alumno"));
        assertTrue(Validations.isValidTipoUsuario("socio"));

        assertFalse(Validations.isValidTipoUsuario(null));
        assertFalse(Validations.isValidTipoUsuario(""));
        assertFalse(Validations.isValidTipoUsuario("ADMIN"));
    }

    // TIPO ACTIVIDAD

    @Test
    void testIsValidTipoActividad() {
        assertTrue(Validations.isValidTipoActividad("ACADEMICA"));
        assertTrue(Validations.isValidTipoActividad("DEPORTIVA"));

        assertFalse(Validations.isValidTipoActividad(null));
        assertFalse(Validations.isValidTipoActividad(""));
        assertFalse(Validations.isValidTipoActividad("MUSICAL"));
    }

    // ESTADO RESERVA

    @Test
    void testIsValidEstadoReserva() {
        assertTrue(Validations.isValidEstadoReserva("ACTIVA"));
        assertTrue(Validations.isValidEstadoReserva("CANCELADA"));

        assertFalse(Validations.isValidEstadoReserva(null));
        assertFalse(Validations.isValidEstadoReserva(""));
        assertFalse(Validations.isValidEstadoReserva("PENDIENTE"));
    }

    // ESTADO INCIDENCIA

    @Test
    void testIsValidEstadoIncidencia() {
        assertTrue(Validations.isValidEstadoIncidencia("ABIERTO"));
        assertTrue(Validations.isValidEstadoIncidencia("EN_PROCESO"));
        assertTrue(Validations.isValidEstadoIncidencia("CERRADA"));

        assertFalse(Validations.isValidEstadoIncidencia(null));
        assertFalse(Validations.isValidEstadoIncidencia(""));
        assertFalse(Validations.isValidEstadoIncidencia("FINALIZADA"));
    }

    // DNI

    @Test
    void testIsValidDni() {
        assertTrue(Validations.isValidDni("12345678A"));

        assertFalse(Validations.isValidDni(null));
        assertFalse(Validations.isValidDni(""));
        assertFalse(Validations.isValidDni("1234567A"));
        assertFalse(Validations.isValidDni("ABCDEFGHJ"));
    }

    // EMAIL

    @Test
    void testIsValidEmail() {
        assertTrue(Validations.isValidEmail("juan@test.com"));

        assertFalse(Validations.isValidEmail(null));
        assertFalse(Validations.isValidEmail(""));
        assertFalse(Validations.isValidEmail("juan@test"));
        assertFalse(Validations.isValidEmail("juan123@test.com"));
    }

    // TELEFONO

    @Test
    void testIsValidTelefono() {
        assertTrue(Validations.isValidTelefono("+34123456789"));

        assertFalse(Validations.isValidTelefono(null));
        assertFalse(Validations.isValidTelefono(""));
        assertFalse(Validations.isValidTelefono("123456789"));
        assertFalse(Validations.isValidTelefono("+34123456"));
    }

    // NOMBRE

    @Test
    void testIsValidNombre() {
        assertTrue(Validations.isValidNombre("Juan"));
        assertTrue(Validations.isValidNombre("Juan Perez"));

        assertFalse(Validations.isValidNombre(null));
        assertFalse(Validations.isValidNombre(""));
        assertFalse(Validations.isValidNombre("J"));
        assertFalse(Validations.isValidNombre("Juan123"));
    }

    // PLAZAS MAXIMAS

    @Test
    void testIsValidPlazasMaximas() {
        assertTrue(Validations.isValidPlazasMaximas(10));

        assertFalse(Validations.isValidPlazasMaximas(0));
        assertFalse(Validations.isValidPlazasMaximas(-1));
    }

    // PLAZAS OCUPADAS

    @Test
    void testIsValidPlazasOcupadas() {
        assertTrue(Validations.isValidPlazasOcupadas(0));
        assertTrue(Validations.isValidPlazasOcupadas(5));

        assertFalse(Validations.isValidPlazasOcupadas(-1));
    }

    // PRECIO

    @Test
    void testIsValidPrecio() {
        assertTrue(Validations.isValidPrecio(0));
        assertTrue(Validations.isValidPrecio(10.5));

        assertFalse(Validations.isValidPrecio(-1));
    }

    // DURACION

    @Test
    void testIsValidDuracion() {
        assertTrue(Validations.isValidDuracion(0));
        assertTrue(Validations.isValidDuracion(60));

        assertFalse(Validations.isValidDuracion(-10));
    }

    // DESCRIPCION

    @Test
    void testIsValidDescripcion() {
        assertTrue(Validations.isValidDescripcion("Descripcion valida"));

        assertFalse(Validations.isValidDescripcion(null));
        assertFalse(Validations.isValidDescripcion(""));
        assertFalse(Validations.isValidDescripcion("1descripcion"));
    }

    // ASUNTO

    @Test
    void testIsValidAsunto() {
        assertTrue(Validations.isValidAsunto("Problema tecnico"));

        assertFalse(Validations.isValidAsunto(null));
        assertFalse(Validations.isValidAsunto(""));
        assertFalse(Validations.isValidAsunto("123"));
    }

    // USUARIO

    @Test
    void testIsValidUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan Perez");
        usuario.setDni("12345678A");
        usuario.setEmail("juan@test.com");
        usuario.setTelefono("+34123456789");
        usuario.setTipoUsuario("ALUMNO");

        assertTrue(Validations.isValidUsuario(usuario));

        usuario.setDni("123");
        assertFalse(Validations.isValidUsuario(usuario));

        assertFalse(Validations.isValidUsuario(null));
    }

    // ACTIVIDAD

    @Test
    void testIsValidActividad() {
        Actividad actividad = new Actividad();
        actividad.setNombre("Yoga");
        actividad.setTipoActividad("DEPORTIVA");
        actividad.setDuracion(60);
        actividad.setPrecio(10);
        actividad.setPlazasMaximas(20);
        actividad.setPlazasOcupadas(5);

        assertTrue(Validations.isValidActividad(actividad));

        actividad.setPrecio(-1);
        assertFalse(Validations.isValidActividad(actividad));

        assertFalse(Validations.isValidActividad(null));
    }

    // INCIDENCIA

    @Test
    void testIsValidIncidencia() {
        Incidencia incidencia = new Incidencia();
        incidencia.setAsunto("Pantalla rota");
        incidencia.setDescripcion("Ordenador averiado");
        incidencia.setEstado("ABIERTO");

        assertTrue(Validations.isValidIncidencia(incidencia));

        incidencia.setEstado("INVALIDO");
        assertFalse(Validations.isValidIncidencia(incidencia));

        assertFalse(Validations.isValidIncidencia(null));
    }

    // RESERVA

    @Test
    void testIsValidReserva() {
        Reserva reserva = new Reserva();
        reserva.setEstado("ACTIVA");

        assertTrue(Validations.isValidReserva(reserva));

        reserva.setEstado("PENDIENTE");
        assertFalse(Validations.isValidReserva(reserva));

        assertFalse(Validations.isValidReserva(null));
    }
}