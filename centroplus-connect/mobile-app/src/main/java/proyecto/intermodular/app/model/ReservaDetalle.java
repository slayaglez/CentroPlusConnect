package proyecto.intermodular.app.model;

import java.time.LocalDate;

public class ReservaDetalle {
    public final int id;
    public final int idUsuario;
    public final int idActividad;
    public final String nombreUsuario;
    public final String nombreActividad;
    public final LocalDate fecha;
    public final String estado;

    /**
     * Constructor completo por defecto
     * @param id identificador
     * @param idUsuario id del usuario
     * @param idActividad id de la actividad
     * @param nombreUsuario nombre del usuario
     * @param nombreActividad nombre de la actividad
     * @param fecha fecha de la reserva
     * @param estado estado de la reserva
     */
    public ReservaDetalle(int id, int idUsuario, int idActividad,
                          String nombreUsuario, String nombreActividad,
                          LocalDate fecha, String estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idActividad = idActividad;
        this.nombreUsuario = nombreUsuario;
        this.nombreActividad = nombreActividad;
        this.fecha = fecha;
        this.estado = estado;
    }
}