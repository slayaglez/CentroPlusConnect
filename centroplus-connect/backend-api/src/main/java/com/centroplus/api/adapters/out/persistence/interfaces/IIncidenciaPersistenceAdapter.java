package com.centroplus.api.adapters.out.persistence.interfaces;

import java.util.List;
import java.util.Optional;
import com.centroplus.api.domain.model.Incidencia;

public interface IIncidenciaPersistenceAdapter {
  Incidencia save(Incidencia incidencia);

  List<Incidencia> findAll();

  Optional<Incidencia> findById(Long id);

  void deleteById(Long id);

  boolean existsById(Long id);
}
