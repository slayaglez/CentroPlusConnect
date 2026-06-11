package proyecto.intermodular.app.service;

import java.util.List;

import proyecto.intermodular.app.model.Usuario;
import proyecto.intermodular.app.repository.UsuarioRepository;
import proyecto.intermodular.app.repository.interfaces.IUsuarioRepository;
import proyecto.intermodular.app.service.interfaces.IUsuarioService;
import proyecto.intermodular.validations.Validations;

public class UsuarioService implements IUsuarioService{

    private final IUsuarioRepository repository;

    public UsuarioService() {
        this.repository = new UsuarioRepository();
    }

    @Override
    public boolean create(Usuario usuario, String password) {
        if (usuario == null) {
            return false;
        }
        if(usuario.getId() == null) {
            return repository.createAutoId(usuario, password);
        }
        if (!Validations.isValidUsuario(usuario)) {
            return false;
        }
        return repository.create(usuario, password);
    }

    @Override
    public Integer findIdByName(String nombre) {
        if(nombre == null || nombre.isBlank()) return null;
        return repository.findIdByName(nombre);
    }

    @Override
    public Usuario findById(Integer id) {
        if(id == null) {
            return null;
        }
        return repository.findById(id);
    }

    @Override
    public List<Usuario> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean update(Usuario usuario) {
        if (!Validations.isValidUsuario(usuario)) {
            return false;
        }
        return repository.update(usuario);
    }

    @Override
    public boolean deleteById(Integer id) {
        if (id == null) {
            return false;
        }
        return repository.deleteById(id);
    }

}
