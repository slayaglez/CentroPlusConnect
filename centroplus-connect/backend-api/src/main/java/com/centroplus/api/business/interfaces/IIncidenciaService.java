package com.centroplus.api.business.interfaces;

import java.util.List;
import java.util.Optional;

import com.centroplus.api.domain.model.Incidencia;

public interface IIncidenciaService {
  Incidencia create(Incidencia incidencia);

  List<Incidencia> findAll();

  Optional<Incidencia> findById(Long id);

  Optional<Incidencia> update(Long id, Incidencia patch);

  boolean deleteById(Long id);

  boolean cambiarEstado(Long id, String nuevoEstado);

  List<Incidencia> findByUsuario(Long idUsuario);
}
