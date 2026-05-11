package proyecto.intermodular.app.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import proyecto.intermodular.app.model.Usuario;
import proyecto.intermodular.app.repository.interfaces.IUsuarioRepository;
import proyecto.intermodular.database.sqlite.SQLiteConnectionManager;

public class UsuarioRepository extends SQLiteConnectionManager implements IUsuarioRepository{

    public UsuarioRepository(String rutaDb) {
        super(rutaDb);
    }

    @Override
    public boolean create(Usuario usuario) {
        try (Connection connection = getConnection();
            PreparedStatement sentencia = connection.prepareStatement("INSERT INTO usuario VALUES (?, ?, ?, ?, ?, ?)")) {

            sentencia.setInt(1, usuario.getId());
            sentencia.setString(2, usuario.getNombre());
            sentencia.setString(3, usuario.getDni());
            sentencia.setString(4, usuario.getEmail());
            sentencia.setString(5, usuario.getTelefono());
            sentencia.setString(6, usuario.getTipoUsuario());

            return sentencia.executeUpdate() > 0;
            
        } catch (Exception e) {
            System.err.println("Error");
            return false;
        }
    }

    @Override
    public boolean createAutoId(Usuario usuario) {
        try (Connection connection = getConnection();
            PreparedStatement sentencia = connection.prepareStatement("INSERT INTO usuario (nombre, dni, email, telefono, tipo_usuario) VALUES (?, ?, ?, ?, ?)")) {

            
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getDni());
            sentencia.setString(3, usuario.getEmail());
            sentencia.setString(4, usuario.getTelefono());
            sentencia.setString(5, usuario.getTipoUsuario());

            return sentencia.executeUpdate() > 0;
            
        } catch (Exception e) {
            System.err.println("Error");
            return false;
        }
    }

    @Override
    public Usuario findById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public List<Usuario> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public boolean update(Usuario usuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean deleteById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

}
