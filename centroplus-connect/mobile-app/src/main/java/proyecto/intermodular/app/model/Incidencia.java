package proyecto.intermodular.app.model;

import java.time.LocalDate;
import java.util.Objects;

public class Incidencia {
    private Integer id;
    private Integer idUsuario;
    private String asunto;
    private String descripcion;
    private LocalDate fecha;
    private String estado;

    /**
     * Constructor por defecto
     */
    public Incidencia() {
    }

    /**
     * Constructor para busquedas
     * @param id identificador de la incidencia
     */
    public Incidencia(int id) {
        this.id = id;
    }

    /**
     * Constructor de la incidencia
     * @param id identificador de la incidencia
     * @param idUsuario identificador del usuario
     * @param asunto asunto de la incidencia
     * @param descripcion descripcion de la incidencia
     * @param fecha fecha de la incidencia
     * @param estado estado de la incidencia
     */
    public Incidencia(int id, int idUsuario, String asunto, String descripcion, LocalDate fecha, String estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Incidencia other = (Incidencia) obj;
        return id == other.id;
    }


    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", idUsuario='" + getIdUsuario() + "'" +
            ", asunto='" + getAsunto() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }

}
