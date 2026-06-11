package com.centroplus.api.adapters.out.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import com.centroplus.api.adapters.mapper.CentroPlusMapper;
import com.centroplus.api.adapters.out.persistence.interfaces.IIncidenciaPersistenceAdapter;
import com.centroplus.api.adapters.out.persistence.jpa.IncidenciaJpaRepository;
import com.centroplus.api.domain.model.Incidencia;

@Component
public class IncidenciaPersistenceAdapter implements IIncidenciaPersistenceAdapter {
  private final IncidenciaJpaRepository jpaRepo;
  private final CentroPlusMapper mapper;

  public IncidenciaPersistenceAdapter(IncidenciaJpaRepository jpaRepo, CentroPlusMapper mapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = mapper;
  }

  @Override
  public Incidencia save(Incidencia e) {
    return mapper.toDomain(jpaRepo.save(mapper.toJpa(e)));
  }

  @Override
  public List<Incidencia> findAll() {
    return jpaRepo.findAll().stream().map(mapper::toDomain).toList();
  }

  @Override
  public Optional<Incidencia> findById(Long id) {
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
