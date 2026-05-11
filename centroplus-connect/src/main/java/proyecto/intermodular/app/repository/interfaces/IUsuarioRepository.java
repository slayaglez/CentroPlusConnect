package proyecto.intermodular.app.repository.interfaces;

import java.util.List;

import proyecto.intermodular.app.model.Usuario;

public interface IUsuarioRepository {

    /**
     * Crea un usuario
     * 
     * @param usuario Usuario
     * @return boolean
     */
    boolean create(Usuario usuario);

    /**
     * Crea un usuario cuyo id es null
     * 
     * @param usuario Usuario
     * @return boolean
     */
    boolean createAutoId(Usuario usuario);

    /**
     * Encuentra un usuario por su id
     * @param id Integer identificador unico
     * @return Usuario
     */
    Usuario findById(Integer id);

    /**
     * Devuelve una lista de todos los Usuarios
     * @return List Usuario
     */
    List<Usuario> findAll();

    /**
     * Actualiza un usuario usando el id del argumento
     * @param usuario Usuario nuevo
     * @return boolean
     */
    boolean update(Usuario usuario);

    /**
     * Elimina un usuario por su ID
     * @param id Integer identificador unico
     * @return boolean
     */
    boolean deleteById(Integer id);
}
