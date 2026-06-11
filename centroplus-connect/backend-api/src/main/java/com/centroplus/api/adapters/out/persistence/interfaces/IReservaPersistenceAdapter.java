package com.centroplus.api.adapters.out.persistence.interfaces;

import java.util.List;
import java.util.Optional;
import com.centroplus.api.domain.model.Reserva;

public interface IReservaPersistenceAdapter {
  Reserva save(Reserva reserva);

  List<Reserva> findAll();

  Optional<Reserva> findById(Long id);

  void deleteById(Long id);

  boolean existsById(Long id);
}
