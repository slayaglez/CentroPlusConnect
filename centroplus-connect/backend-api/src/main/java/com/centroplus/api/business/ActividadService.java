package com.centroplus.api.business;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.centroplus.api.adapters.out.persistence.interfaces.IActividadPersistenceAdapter;
import com.centroplus.api.business.interfaces.IActividadService;
import com.centroplus.api.domain.model.Actividad;

@Service
public class ActividadService implements IActividadService {

  private final IActividadPersistenceAdapter repo;

  public ActividadService(IActividadPersistenceAdapter repo) {
    this.repo = repo;
  }

  @Override
  public Actividad create(Actividad actividad) {
    actividad.setId(null);
    return repo.save(actividad);
  }

  @Override
  public List<Actividad> findAll() {
    return repo.findAll();
  }

  @Override
  public Optional<Actividad> findById(Long id) {
    return repo.findById(id);
  }

  @Override
  public Optional<Actividad> update(Long id, Actividad patch) {
    return repo.findById(id).map(existing -> {
      if (patch.getNombre() != null)
        existing.setNombre(patch.getNombre());
      if (patch.getTipoActividad() != null)
        existing.setTipoActividad(patch.getTipoActividad());
      if (patch.getDuracion() != null)
        existing.setDuracion(patch.getDuracion());
      if (patch.getPrecio() != null)
        existing.setPrecio(patch.getPrecio());
      if (patch.getPlazasMaximas() != null)
        existing.setPlazasMaximas(patch.getPlazasMaximas());
      if (patch.getPlazasOcupadas() != null)
        existing.setPlazasOcupadas(patch.getPlazasOcupadas());
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
  public boolean reservarPlaza(Long idActividad) {
    return repo.findById(idActividad).map(a -> {
      if (a.getPlazasDisponibles() <= 0)
        return false;
      a.setPlazasOcupadas(a.getPlazasOcupadas() + 1);
      repo.save(a);
      return true;
    }).orElse(false);
  }

  @Override
  public boolean cancelarPlaza(Long idActividad) {
    return repo.findById(idActividad).map(a -> {
      if (a.getPlazasOcupadas() <= 0)
        return false;
      a.setPlazasOcupadas(a.getPlazasOcupadas() - 1);
      repo.save(a);
      return true;
    }).orElse(false);
  }

  @Override
  public List<Actividad> findCompletas() {
    return repo.findAll().stream()
        .filter(a -> a.getPlazasDisponibles() <= 0)
        .toList();
  }

  @Override
  public double calcularIngresosTotales() {
    return repo.findAll().stream()
        .mapToDouble(a -> a.getPrecio() * a.getPlazasOcupadas())
        .sum();
  }
}
