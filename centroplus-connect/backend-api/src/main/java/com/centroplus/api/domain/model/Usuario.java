package com.centroplus.api.domain.model;

public class Usuario {
  private Long id;
  private String nombre;
  private String dni;
  private String email;
  private String telefono;
  private String tipoUsuario;

  public Usuario() {
  }

  public Usuario(Long id, String nombre, String dni, String email, String telefono, String tipoUsuario) {
    this.id = id;
    this.nombre = nombre;
    this.dni = dni;
    this.email = email;
    this.telefono = telefono;
    this.tipoUsuario = tipoUsuario;
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

  public String getDni() {
    return dni;
  }

  public void setDni(String dni) {
    this.dni = dni;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getTipoUsuario() {
    return tipoUsuario;
  }

  public void setTipoUsuario(String tipoUsuario) {
    this.tipoUsuario = tipoUsuario;
  }
}
