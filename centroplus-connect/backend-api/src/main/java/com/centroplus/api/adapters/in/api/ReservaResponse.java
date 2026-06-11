package com.centroplus.api.adapters.in.api;

import java.time.LocalDate;

public class ReservaResponse {
  private Long id;
  private Long idUsuario;
  private Long idActividad;
  private LocalDate fecha;
  private String estado;

  public ReservaResponse() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdActividad() {
    return idActividad;
  }

  public void setIdActividad(Long idActividad) {
    this.idActividad = idActividad;
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
}
