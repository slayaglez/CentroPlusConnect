package com.centroplus.api.business;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.centroplus.api.adapters.out.persistence.interfaces.IIncidenciaPersistenceAdapter;
import com.centroplus.api.business.interfaces.IIncidenciaService;
import com.centroplus.api.domain.model.Incidencia;

@Service
public class IncidenciaService implements IIncidenciaService {

  private final IIncidenciaPersistenceAdapter repo;

  public IncidenciaService(IIncidenciaPersistenceAdapter repo) {
    this.repo = repo;
  }

  @Override
  public Incidencia create(Incidencia incidencia) {
    incidencia.setId(null);
    return repo.save(incidencia);
  }

  @Override
  public List<Incidencia> findAll() {
    return repo.findAll();
  }

  @Override
  public Optional<Incidencia> findById(Long id) {
    return repo.findById(id);
  }

  @Override
  public Optional<Incidencia> update(Long id, Incidencia patch) {
    return repo.findById(id).map(existing -> {
      if (patch.getAsunto() != null)
        existing.setAsunto(patch.getAsunto());
      if (patch.getDescripcion() != null)
        existing.setDescripcion(patch.getDescripcion());
      if (patch.getFecha() != null)
        existing.setFecha(patch.getFecha());
      if (patch.getEstado() != null)
        existing.setEstado(patch.getEstado());
      return repo.save(existing);
    });
  }

  @Override
  public boolean deleteById(Long id) {
    if (!repo.existsById(id))
      return false;
    repo.deleteById(id);
    return true;
  }

  @Override
  public boolean cambiarEstado(Long id, String nuevoEstado) {
    return repo.findById(id).map(i -> {
      i.setEstado(nuevoEstado);
      repo.save(i);
      return true;
    }).orElse(false);
  }

  @Override
  public List<Incidencia> findByUsuario(Long idUsuario) {
    return repo.findAll().stream()
        .filter(i -> idUsuario.equals(i.getIdUsuario()))
        .toList();
  }
}
