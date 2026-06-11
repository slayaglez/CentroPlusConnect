package com.centroplus.api.business;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.centroplus.api.adapters.out.persistence.interfaces.IUsuarioPersistenceAdapter;
import com.centroplus.api.business.interfaces.IUsuarioService;
import com.centroplus.api.domain.model.Usuario;

@Service
public class UsuarioService implements IUsuarioService {

  private final IUsuarioPersistenceAdapter repo;

  public UsuarioService(IUsuarioPersistenceAdapter repo) {
    this.repo = repo;
  }

  @Override
  public Usuario create(Usuario usuario) {
    usuario.setId(null);
    return repo.save(usuario);
  }

  @Override
  public List<Usuario> findAll() {
    return repo.findAll();
  }

  @Override
  public Optional<Usuario> findById(Long id) {
    return repo.findById(id);
  }

  @Override
  public Optional<Usuario> update(Long id, Usuario patch) {
    return repo.findById(id).map(existing -> {
      if (patch.getNombre() != null)
        existing.setNombre(patch.getNombre());
      if (patch.getDni() != null)
        existing.setDni(patch.getDni());
      if (patch.getEmail() != null)
        existing.setEmail(patch.getEmail());
      if (patch.getTelefono() != null)
        existing.setTelefono(patch.getTelefono());
      if (patch.getTipoUsuario() != null)
        existing.setTipoUsuario(patch.getTipoUsuario());
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
}
