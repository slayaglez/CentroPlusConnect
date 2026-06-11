package com.centroplus.api.adapters.in.api;

public class ActividadRequest {
  private String nombre;
  private String tipoActividad;
  private Integer duracion;
  private Double precio;
  private Integer plazasMaximas;
  private Integer plazasOcupadas;

  public ActividadRequest() {
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
}
