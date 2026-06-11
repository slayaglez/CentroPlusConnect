package com.centroplus.api.adapters.out.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import com.centroplus.api.adapters.mapper.CentroPlusMapper;
import com.centroplus.api.adapters.out.persistence.interfaces.IActividadPersistenceAdapter;
import com.centroplus.api.adapters.out.persistence.jpa.ActividadJpaRepository;
import com.centroplus.api.domain.model.Actividad;

@Component
public class ActividadPersistenceAdapter implements IActividadPersistenceAdapter {
  private final ActividadJpaRepository jpaRepo;
  private final CentroPlusMapper mapper;

  public ActividadPersistenceAdapter(ActividadJpaRepository jpaRepo, CentroPlusMapper mapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = mapper;
  }

  @Override
  public Actividad save(Actividad e) {
    return mapper.toDomain(jpaRepo.save(mapper.toJpa(e)));
  }

  @Override
  public List<Actividad> findAll() {
    return jpaRepo.findAll().stream().map(mapper::toDomain).toList();
  }

  @Override
  public Optional<Actividad> findById(Long id) {
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
