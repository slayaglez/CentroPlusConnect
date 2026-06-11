package com.centroplus.api.adapters.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.centroplus.api.adapters.mapper.CentroPlusMapper;
import com.centroplus.api.adapters.out.persistence.interfaces.IUsuarioPersistenceAdapter;
import com.centroplus.api.adapters.out.persistence.jpa.UsuarioJpaRepository;
import com.centroplus.api.domain.model.Usuario;

@Component
public class UsuarioPersistenceAdapter implements IUsuarioPersistenceAdapter {

  private final UsuarioJpaRepository jpaRepo;
  private final CentroPlusMapper mapper;

  public UsuarioPersistenceAdapter(UsuarioJpaRepository jpaRepo, CentroPlusMapper mapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = mapper;
  }

  @Override
  public Usuario save(Usuario u) {
    return mapper.toDomain(jpaRepo.save(mapper.toJpa(u)));
  }

  @Override
  public List<Usuario> findAll() {
    return jpaRepo.findAll().stream().map(mapper::toDomain).toList();
  }

  @Override
  public Optional<Usuario> findById(Long id) {
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
