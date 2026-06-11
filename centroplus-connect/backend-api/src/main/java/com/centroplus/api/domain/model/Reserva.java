package com.centroplus.api.domain.model;

import java.time.LocalDate;

public class Reserva {
  private Long id;
  private Long idUsuario;
  private Long idActividad;
  private LocalDate fecha;
  private String estado;

  public Reserva() {
  }

  public Reserva(Long id, Long idUsuario, Long idActividad, LocalDate fecha, String estado) {
    this.id = id;
    this.idUsuario = idUsuario;
    this.idActividad = idActividad;
    this.fecha = fecha;
    this.estado = estado;
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
