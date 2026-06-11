package com.centroplus.api.adapters.out.persistence.interfaces;

import java.util.List;
import java.util.Optional;
import com.centroplus.api.domain.model.Actividad;

public interface IActividadPersistenceAdapter {
  Actividad save(Actividad actividad);

  List<Actividad> findAll();

  Optional<Actividad> findById(Long id);

  void deleteById(Long id);

  boolean existsById(Long id);
}
