package proyecto.intermodular.app.model;

import java.util.Objects;

public class Actividad {
    private Integer id;
    private String nombre;
    private String tipoActividad;
    private Integer duracion;
    private double precio;
    private Integer plazasMaximas;
    private Integer plazasOcupadas;

    /**
     * Constructor por defecto
     */
    public Actividad() {
    }

    /**
     * Constructor para busquedas
     * @param id identificador de la clase
     */
    public Actividad(int id) {
        this.id = id;
    }

    /**
     * Constructor de la clase
     * @param id identificador de la actividad
     * @param nombre nombre de la actividad
     * @param tipoActividad tipo de la actividad
     * @param duracion duracion de la actividad
     * @param precio precio de la actividad
     * @param plazasMaximas plazas de la actividad
     * @param plazasOcupadas plazas ocupadas de la actividad
     */
    public Actividad(int id, String nombre, String tipoActividad, int duracion, double precio, int plazasMaximas,
            int plazasOcupadas) {
        this.id = id;
        this.nombre = nombre;
        this.tipoActividad = tipoActividad;
        this.duracion = duracion;
        this.precio = precio;
        this.plazasMaximas = plazasMaximas;
        this.plazasOcupadas = plazasOcupadas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(String tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Integer getPlazasMaximas() {
        return plazasMaximas;
    }

    public void setPlazasMaximas(int plazasMaximas) {
        this.plazasMaximas = plazasMaximas;
    }

    public Integer getPlazasOcupadas() {
        return plazasOcupadas;
    }

    public void setPlazasOcupadas(int plazasOcupadas) {
        this.plazasOcupadas = plazasOcupadas;
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
        Actividad other = (Actividad) obj;
        return id == other.id;
    }

    
}
