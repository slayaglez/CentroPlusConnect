package com.centroplus.api.domain.model;

public class Actividad {
  private Long id;
  private String nombre;
  private String tipoActividad;
  private Integer duracion;
  private Double precio;
  private Integer plazasMaximas;
  private Integer plazasOcupadas;

  public Actividad() {
  }

  public Actividad(Long id, String nombre, String tipoActividad, Integer duracion,
      Double precio, Integer plazasMaximas, Integer plazasOcupadas) {
    this.id = id;
    this.nombre = nombre;
    this.tipoActividad = tipoActividad;
    this.duracion = duracion;
    this.precio = precio;
    this.plazasMaximas = plazasMaximas;
    this.plazasOcupadas = plazasOcupadas;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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

  public void setDuracion(Integer duracion) {
    this.duracion = duracion;
  }

  public Double getPrecio() {
    return precio;
  }

  public void setPrecio(Double precio) {
    this.precio = precio;
  }

  public Integer getPlazasMaximas() {
    return plazasMaximas;
  }

  public void setPlazasMaximas(Integer plazasMaximas) {
    this.plazasMaximas = plazasMaximas;
  }

  public Integer getPlazasOcupadas() {
    return plazasOcupadas;
  }

  public void setPlazasOcupadas(Integer plazasOcupadas) {
    this.plazasOcupadas = plazasOcupadas;
  }

  public int getPlazasDisponibles() {
    return plazasMaximas != null && plazasOcupadas != null ? plazasMaximas - plazasOcupadas : 0;
  }
}
