package com.centroplus.api.adapters.out.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import com.centroplus.api.adapters.mapper.CentroPlusMapper;
import com.centroplus.api.adapters.out.persistence.interfaces.IReservaPersistenceAdapter;
import com.centroplus.api.adapters.out.persistence.jpa.ReservaJpaRepository;
import com.centroplus.api.domain.model.Reserva;

@Component
public class ReservaPersistenceAdapter implements IReservaPersistenceAdapter {
  private final ReservaJpaRepository jpaRepo;
  private final CentroPlusMapper mapper;

  public ReservaPersistenceAdapter(ReservaJpaRepository jpaRepo, CentroPlusMapper mapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = mapper;
  }

  @Override
  public Reserva save(Reserva e) {
    return mapper.toDomain(jpaRepo.save(mapper.toJpa(e)));
  }

  @Override
  public List<Reserva> findAll() {
    return jpaRepo.findAll().stream().map(mapper::toDomain).toList();
  }

  @Override
  public Optional<Reserva> findById(Long id) {
    return jpaRepo.findById(id).map(mapper::toDomain);
  }

  @Override
  public void deleteById(Long id) {
    jpaRepo.deleteById(id);
  }

  @Override
  public boolean existsById(Long id) {
    return jpaRepo.existsById(id);
  }
}
