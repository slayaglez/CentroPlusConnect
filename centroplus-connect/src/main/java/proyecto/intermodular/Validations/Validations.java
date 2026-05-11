package proyecto.intermodular.Validations;

import java.util.regex.Pattern;

public final class Validations {

    private Validations() {

    }

    public static boolean isValidTipoUsuario(String tipoUsuario) {
        if (tipoUsuario == null || tipoUsuario.isEmpty()) {
            return false;
        }
        String patron = "^(ALUMNO||SOCIO||alumno||socio){1,2}$";
        return Pattern.matches(patron, tipoUsuario);
    }

    public static boolean isValidTipoActividad(String tipoActividad) {
        if (tipoActividad == null || tipoActividad.isEmpty()) {
            return false;
        }
        String patron = "^ACADEMICA||DEPORTIVA||academica||deportiva$";
        return Pattern.matches(patron, tipoActividad);
    }

    public static boolean isValidEstadoReserva(String estado) {
        if (estado == null || estado.isEmpty()) {
            return false;
        }
        String patron = "^ACTIVA||CANCELADA||activa||cancelada$";
        return Pattern.matches(patron, estado);
    }

    public static boolean isValidEstadoIncidencia(String estado) {
        if (estado == null || estado.isEmpty()) {
            return false;
        }
        String patron = "^ABIERTO||EN_PROCESO||CERRADA||abierto||en_proceso||cerrada$";
        return Pattern.matches(patron, estado);
    }
}