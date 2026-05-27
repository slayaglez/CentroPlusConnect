package proyecto.intermodular.app.service;

import java.util.List;

import proyecto.intermodular.app.model.Actividad;
import proyecto.intermodular.app.repository.ActividadRepository;
import proyecto.intermodular.app.repository.interfaces.IActividadRepository;
import proyecto.intermodular.app.repository.interfaces.IUsuarioRepository;
import proyecto.intermodular.app.service.interfaces.IActividadService;
import proyecto.intermodular.validations.Validations;

public class ActividadService implements IActividadService{

    private final IActividadRepository repository;

    public ActividadService() {
        this.repository = new ActividadRepository();
    }

    public ActividadService(IActividadService actividadService) {
        this.repository = new ActividadRepository();
    }

    @Override
    public boolean create(Actividad actividad) {
        if (actividad.getId() == null) {
            return repository.createAutoId(actividad);
        }
        if (!Validations.isValidActividad(actividad)) {
            return false;
        }
        return repository.create(actividad);
    }

    @Override
    public Actividad findById(Integer id) {
        if (id == null || id < 1) {
            return null;
        }
        return repository.findById(id);
    }

    @Override
    public List<Actividad> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean update(Actividad actividad) {
        if (!Validations.isValidActividad(actividad)) {
            return false;
        }
        return repository.update(actividad);
    }

    @Override
    public boolean deleteById(Integer id) {
        if (id == null) {
            return false;
        }
        return repository.deleteById(id);
    }

    @Override
    public boolean reservarPlaza(Integer idActividad) {
        return repository.reservarPlaza(idActividad);
    }

    @Override
    public boolean cancelarPlaza(Integer idActividad) {
        return repository.cancelarPlaza(idActividad);
    }

    @Override
    public List<Actividad> findCompletas() {
        return repository.findCompletas();
    }

    @Override
    public double calcularIngresosTotales() {
        return repository.calcularIngresosTotales();
    }

}
