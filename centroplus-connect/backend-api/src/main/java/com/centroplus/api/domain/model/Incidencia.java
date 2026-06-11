package com.centroplus.api.domain.model;

import java.time.LocalDate;

public class Incidencia {
  private Long id;
  private Long idUsuario;
  private String asunto;
  private String descripcion;
  private LocalDate fecha;
  private String estado;

  public Incidencia() {
  }

  public Incidencia(Long id, Long idUsuario, String asunto, String descripcion, LocalDate fecha, String estado) {
    this.id = id;
    this.idUsuario = idUsuario;
    this.asunto = asunto;
    this.descripcion = descripcion;
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
}
