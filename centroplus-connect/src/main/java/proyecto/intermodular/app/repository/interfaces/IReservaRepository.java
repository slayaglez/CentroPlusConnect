package proyecto.intermodular.app.repository.interfaces;

import java.util.List;

import proyecto.intermodular.app.model.Actividad;
import proyecto.intermodular.app.model.Reserva;

public interface IReservaRepository {

    /**
     * Crea una reserva
     * @param reserva reserva
     * @return boolean
     */
    boolean create(Reserva reserva);

    /**
     * Crea una reserva cuyo id es null
     * 
     * @param reserva reserva
     * @return boolean
     */
    boolean createAutoId(Reserva reserva);

    /**
     * Busca una reserva por su id
     * @param id de la reserva
     * @return reserva
     */
    Reserva findById(Integer id);

    /**
     * Devuelve una lista de todas las reservas
     * @return List reservas
     */
    List<Reserva> findAll();

    /**
     * Actualiza una reserva
     * @param reserva reserva
     * @return boolean
     */
    boolean update(Reserva reserva);

    /**
     * Elimina una reserva
     * @param id id de la reserva
     * @return boolean
     */
    boolean deleteById(Integer id);

    /**
     * Cancela una reserva
     * @param idReserva id de la reserva
     * @return boolean
     */
    boolean cancelarReserva(Integer idReserva);

    /**
     * Devuelve una lista de las reservas disponibles
     * @return List de reservas disponibles
     */
    List<Reserva> findDisponibles();
}
