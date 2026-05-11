package proyecto.intermodular.app.service;

import java.util.List;

import proyecto.intermodular.app.model.Actividad;
import proyecto.intermodular.app.repository.UsuarioRepository;
import proyecto.intermodular.app.repository.interfaces.IUsuarioRepository;
import proyecto.intermodular.app.service.interfaces.IActividadService;
import proyecto.intermodular.validations.Validations;

//public class ActividadService implements IActividadService{

    /*private final IActividadRepository repository;

    public ActividadService() {
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
        if (id == null) {
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
    public boolean reservarPlaza() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reservarPlaza'");
    }

    @Override
    public boolean cancelarPlaza() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cancelarPlaza'");
    }

    @Override
    public Actividad findCompletas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findCompletas'");
    }

    @Override
    public double calcularIngresosTotales() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calcularIngresosTotales'");
    }*/

//}
