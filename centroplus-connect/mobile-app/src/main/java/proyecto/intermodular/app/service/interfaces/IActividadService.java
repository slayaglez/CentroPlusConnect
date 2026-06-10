package proyecto.intermodular.app.service.interfaces;

import java.util.List;

import proyecto.intermodular.app.model.Actividad;

public interface IActividadService {

    /**
     * Crea una actividad
     * @param actividad Actividad 
     * @return boolean
     */
    boolean create(Actividad actividad);

    /**
     * Busca una actividad por su id
     * @param id de la actividad
     * @return actividad
     */
    Actividad findById(Integer id);

    /**
     * Devuelve una lista de todas las actividades
     * @return List actividades
     */
    List<Actividad> findAll();

    /**
     * Actualiza una actividad
     * @param actividad actividad
     * @return boolean
     */
    boolean update(Actividad actividad);

    /**
     * Elimina una actividad
     * @param id id de la actividad
     * @return boolean
     */
    boolean deleteById(Integer id);

    /**
     * Reserva una plaza
     * @return boolean
     */
    boolean reservarPlaza(Integer idActividad);

    /**
     * Cancela una plaza
     * @return boolean
     */
    boolean cancelarPlaza(Integer idActividad);

    /**
     * Busca las actividades completas
     * @return List ctividades completas
     */
    List<Actividad> findCompletas();

    /**
     * Calcula los ingresos totales
     * @return double
     */
    double calcularIngresosTotales();

    /**
     * Busca un id de actividad por su nombre
     * @param nombre nombre actividad String
     * @return id actividad Integer
     */
    Integer findIdByName(String nombre);
}
