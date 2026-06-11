package com.centroplus.api.business;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.centroplus.api.adapters.out.persistence.interfaces.IReservaPersistenceAdapter;
import com.centroplus.api.business.interfaces.IReservaService;
import com.centroplus.api.domain.model.Reserva;

@Service
public class ReservaService implements IReservaService {

  private final IReservaPersistenceAdapter repo;

  public ReservaService(IReservaPersistenceAdapter repo) {
    this.repo = repo;
  }

  @Override
  public Reserva create(Reserva reserva) {
    reserva.setId(null);
    return repo.save(reserva);
  }

  @Override
  public List<Reserva> findAll() {
    return repo.findAll();
  }

  @Override
  public Optional<Reserva> findById(Long id) {
    return repo.findById(id);
  }

  @Override
  public Optional<Reserva> update(Long id, Reserva patch) {
    return repo.findById(id).map(existing -> {
      if (patch.getIdUsuario() != null)
        existing.setIdUsuario(patch.getIdUsuario());
      if (patch.getIdActividad() != null)
        existing.setIdActividad(patch.getIdActividad());
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
  public boolean cancelarReserva(Long idReserva) {
    return repo.findById(idReserva).map(r -> {
      r.setEstado("CANCELADA");
      repo.save(r);
      return true;
    }).orElse(false);
  }

  @Override
  public List<Reserva> findDisponibles() {
    return repo.findAll().stream()
        .filter(r -> "ACTIVA".equals(r.getEstado()))
        .toList();
  }
}
