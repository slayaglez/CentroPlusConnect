package com.centroplus.api.business.interfaces;

import java.util.List;
import java.util.Optional;

import com.centroplus.api.domain.model.Actividad;

public interface IActividadService {
  Actividad create(Actividad actividad);

  List<Actividad> findAll();

  Optional<Actividad> findById(Long id);

  Optional<Actividad> update(Long id, Actividad patch);

  boolean deleteById(Long id);

  boolean reservarPlaza(Long idActividad);

  boolean cancelarPlaza(Long idActividad);

  List<Actividad> findCompletas();

  double calcularIngresosTotales();
}
