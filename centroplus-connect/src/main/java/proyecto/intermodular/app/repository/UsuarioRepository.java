package proyecto.intermodular.app.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import proyecto.intermodular.app.model.Usuario;
import proyecto.intermodular.app.repository.interfaces.IUsuarioRepository;
import proyecto.intermodular.database.sqlite.SQLiteConnectionManager;

public class UsuarioRepository extends SQLiteConnectionManager implements IUsuarioRepository {

    public UsuarioRepository() {
        super(rutaDb);
    }

    @Override
    public boolean create(Usuario usuario) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection
                        .prepareStatement("INSERT INTO usuarios VALUES (?, ?, ?, ?, ?, ?)")) {

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
                PreparedStatement sentencia = connection.prepareStatement(
                        "INSERT INTO usuarios (nombre, dni, email, telefono, tipo_usuario) VALUES (?, ?, ?, ?, ?)")) {

            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getDni());
            sentencia.setString(3, usuario.getEmail());
            sentencia.setString(4, usuario.getTelefono());
            sentencia.setString(5, usuario.getTipoUsuario());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error creando usuario");
            return false;
        }
    }

    @Override
    public Usuario findById(Integer id) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection.prepareStatement("SELECT * FROM usuarios WHERE id=?")) {

            sentencia.setInt(1, id);
            ResultSet resultado = sentencia.executeQuery();

            if (!resultado.next()) {
                return null;
            }

            String nombre = resultado.getString("nombre");
            String dni = resultado.getString("dni");
            String email = resultado.getString("email");
            String telefono = resultado.getString("telefono");
            String tipoUsuario = resultado.getString("tipo_usuario");

            return new Usuario(id, nombre, dni, email, telefono, tipoUsuario);

        } catch (Exception e) {
            System.err.println("Error buscando usuario");
            return null;
        }
    }

    @Override
    public List<Usuario> findAll() {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection.prepareStatement("SELECT * FROM usuarios")) {

            List<Usuario> usuarios = new ArrayList<>();
            ResultSet resultado = sentencia.executeQuery();

            if (!resultado.next()) {
                return null;
            }

            while (resultado.next()) {
                Integer id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");
                String dni = resultado.getString("dni");
                String email = resultado.getString("email");
                String telefono = resultado.getString("telefono");
                String tipoUsuario = resultado.getString("tipo_usuario");

                usuarios.add(new Usuario(id, nombre, dni, email, telefono, tipoUsuario));
            }

            return usuarios;

        } catch (Exception e) {
            System.err.println("Error buscando usuarios");
            return null;
        }
    }

    @Override
    public boolean update(Usuario usuario) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection
                        .prepareStatement("UPDATE usuarios SET nombre=?, dni=?, email=?, telefono=?, tipo_usuario=? WHERE id=?")) {

            sentencia.setInt(6, usuario.getId());
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getDni());
            sentencia.setString(3, usuario.getEmail());
            sentencia.setString(4, usuario.getTelefono());
            sentencia.setString(5, usuario.getTipoUsuario());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error actualizando el usuario");
            return false;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection
                        .prepareStatement("DELETE FROM usuario WHERE id=?")) {

            sentencia.setInt(1, id);

            return sentencia.executeUpdate() == 1;

        } catch (Exception e) {
            System.err.println("Error eliminando el usuario");
            return false;
        }
    }

}
