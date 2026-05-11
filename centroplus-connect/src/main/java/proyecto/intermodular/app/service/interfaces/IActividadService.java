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
     * Busca una
     * @param id
     * @return
     */
    Actividad findById(Integer id);

    List<Actividad> findAll();

    boolean update(Actividad actividad);

    boolean deleteById(Integer id);

    boolean reservarPlaza();

    boolean cancelarPlaza();

    Actividad findCompletas();

    double calcularIngresosTotales();

}
