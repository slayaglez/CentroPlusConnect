package proyecto.intermodular.validations;

import java.util.regex.Pattern;

import proyecto.intermodular.app.model.Actividad;
import proyecto.intermodular.app.model.Incidencia;
import proyecto.intermodular.app.model.Reserva;
import proyecto.intermodular.app.model.Usuario;

public final class Validations {

    private Validations() {

    }

    public static boolean isValidTipoUsuario(String tipoUsuario) {
        if (tipoUsuario == null || tipoUsuario.isEmpty()) {
            return false;
        }
        String patron = "^ALUMNO||SOCIO$";
        return Pattern.matches(patron, tipoUsuario.toUpperCase());
    }

    public static boolean isValidTipoActividad(String tipoActividad) {
        if (tipoActividad == null || tipoActividad.isEmpty()) {
            return false;
        }
        String patron = "^ACADEMICA||DEPORTIVA$";
        return Pattern.matches(patron, tipoActividad.toUpperCase());
    }

    public static boolean isValidEstadoReserva(String estado) {
        if (estado == null || estado.isEmpty()) {
            return false;
        }
        String patron = "^ACTIVA||CANCELADA$";
        return Pattern.matches(patron, estado.toUpperCase());
    }

    public static boolean isValidEstadoIncidencia(String estado) {
        if (estado == null || estado.isEmpty()) {
            return false;
        }
        String patron = "^ABIERTA||PROCESANDO||CERRADA$";
        return Pattern.matches(patron, estado.toUpperCase());
    }

    public static boolean isValidDni(String dni) {
        if (dni == null || dni.isEmpty()) {
            return false;
        }
        String patron = "^[0-9]{8}[A-Za-z]$";
        return Pattern.matches(patron, dni);
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String patron = "^[a-z]+@[a-z]+\\.[a-z]{2,}$";
        return Pattern.matches(patron, email);
    }

    public static boolean isValidTelefono(String telefono) {
        if (telefono == null || telefono.isEmpty()) {
            return false;
        }
        String patron = "^\\+[0-9]{2}[0-9]{9}$";
        return Pattern.matches(patron, telefono);
    }

    public static boolean isValidNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return false;
        }
        String patron = "^[A-Za-záéíóúÁÉÍÓÚ]{2,}(?: [A-Za-záéíóúÁÉÍÓÚ]+)*$";
        return Pattern.matches(patron, nombre);
    }

    public static boolean isValidPlazasMaximas(int plazasMaximas) {
        if (plazasMaximas <= 0) {
            return false;
        }
        return true;
    }

    public static boolean isValidPlazasOcupadas(int plazasOcupadas) {
        if (plazasOcupadas < 0) {
            return false;
        }
        return true;
    }

    public static boolean isValidPrecio(double precio) {
        if (precio < 0) {
            return false;
        }
        return true;
    }

    public static boolean isValidDuracion(int duracion) {
        if (duracion < 0) {
            return false;
        }
        return true;
    }

    public static boolean isValidDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isEmpty()) {
            return false;
        }
        String patron = "^[A-Za-záéíóúÁÉÍÓÚñÑ]{2,}(?: [A-Za-záéíóúÁÉÍÓÚñÑ]+)*$";
        return Pattern.matches(patron, descripcion);
    }

    public static boolean isValidAsunto(String asunto) {
        if (asunto == null || asunto.isEmpty()) {
            return false;
        }
        String patron = "^[A-Za-záéíóúÁÉÍÓÚñÑ]{2,}(?: [A-Za-záéíóúÁÉÍÓÚñÑ]+)*$";
        return Pattern.matches(patron, asunto);
    }

    public static boolean isValidActividad(Actividad actividad) {
        if (actividad == null) {
            return false;
        }
        return isValidNombre(actividad.getNombre())
                && isValidTipoActividad(actividad.getTipoActividad())
                && isValidDuracion(actividad.getDuracion())
                && isValidPrecio(actividad.getPrecio())
                && isValidPlazasMaximas(actividad.getPlazasMaximas())
                && isValidPlazasOcupadas(actividad.getPlazasOcupadas());
    }

    public static boolean isValidIncidencia(Incidencia incidencia) {
        if (incidencia == null) {
            return false;
        }
        return isValidAsunto(incidencia.getAsunto())
                && isValidDescripcion(incidencia.getDescripcion())
                && isValidEstadoIncidencia(incidencia.getEstado());
    }

    public static boolean isValidReserva(Reserva reserva) {
        if (reserva == null) {
            return false;
        }
        return isValidEstadoReserva(reserva.getEstado());
    }

    public static boolean isValidUsuario(Usuario usuario) {
        if (usuario == null) {
            return false;
        }
        return isValidNombre(usuario.getNombre())
                && isValidDni(usuario.getDni())
                && isValidEmail(usuario.getEmail())
                && isValidTelefono(usuario.getTelefono())
                && isValidTipoUsuario(usuario.getTipoUsuario());
    }
}