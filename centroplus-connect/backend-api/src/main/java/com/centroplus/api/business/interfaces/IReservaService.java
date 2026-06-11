package com.centroplus.api.business.interfaces;

import java.util.List;
import java.util.Optional;

import com.centroplus.api.domain.model.Reserva;

public interface IReservaService {
  Reserva create(Reserva reserva);

  List<Reserva> findAll();

  Optional<Reserva> findById(Long id);

  Optional<Reserva> update(Long id, Reserva patch);

  boolean deleteById(Long id);

  boolean cancelarReserva(Long idReserva);

  List<Reserva> findDisponibles();
}
