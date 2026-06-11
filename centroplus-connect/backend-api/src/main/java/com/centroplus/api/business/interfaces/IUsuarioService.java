package com.centroplus.api.business.interfaces;

import java.util.List;
import java.util.Optional;

import com.centroplus.api.domain.model.Usuario;

public interface IUsuarioService {
  Usuario create(Usuario usuario);

  List<Usuario> findAll();

  Optional<Usuario> findById(Long id);

  Optional<Usuario> update(Long id, Usuario patch);

  boolean deleteById(Long id);
}
