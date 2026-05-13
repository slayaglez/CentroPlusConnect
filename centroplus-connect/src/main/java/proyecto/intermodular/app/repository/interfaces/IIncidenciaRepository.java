package main.java.proyecto.intermodular.app.repository.interfaces;


import proyecto.intermodular.app.model.Incidencia;

public interface IIncidenciaRepository {

     /**
     * Crea una incidencia
     * 
     * @param incidencia Incidencia
     * @return boolean
     */
    boolean create(Incidencia incidencia);

    /**
     * Crea una incidencia cuyo id es null
     * 
     * @param incidencia incidencia
     * @return boolean
     */
    boolean createAutoId(Incidencia incidencia);

    /**
     * Encuentra una incidencia por su id
     * @param id Integer identificador unico
     * @return Incidencia
     */
    Incidencia findById(Integer id);

    /**
     * Devuelve una lista de todos las Incidencias
     * @return List Incidencia
     */
    List<Incidencia> findAll();

    /**
     * Actualiza una incidencia usando el id del argumento
     * @param incidencia incidencia nueva
     * @return boolean
     */
    boolean update(Incidencia incidencia);

    /**
     * Elimina una incidencia por su ID
     * @param id Integer identificador unico
     * @return boolean
     */
    boolean deleteById(Integer id);
}
