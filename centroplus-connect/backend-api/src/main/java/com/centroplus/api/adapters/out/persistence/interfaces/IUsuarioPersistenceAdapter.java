package com.centroplus.api.adapters.out.persistence.interfaces;

import java.util.List;
import java.util.Optional;
import com.centroplus.api.domain.model.Usuario;

public interface IUsuarioPersistenceAdapter {
  Usuario save(Usuario usuario);

  List<Usuario> findAll();

  Optional<Usuario> findById(Long id);

  void deleteById(Long id);

  boolean existsById(Long id);
}
