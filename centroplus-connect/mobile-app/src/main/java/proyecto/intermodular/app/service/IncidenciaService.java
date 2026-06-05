package proyecto.intermodular.app.service;

import java.util.List;

import proyecto.intermodular.app.model.Incidencia;
import proyecto.intermodular.app.repository.IncidenciaRepository;
import proyecto.intermodular.app.repository.interfaces.IIncidenciaRepository;
import proyecto.intermodular.app.service.interfaces.IIncidenciaService;
import proyecto.intermodular.validations.Validations;

public class IncidenciaService implements IIncidenciaService {

    private final IIncidenciaRepository repository;

    public IncidenciaService() {
        this.repository = new IncidenciaRepository();
    }

    @Override
    public boolean create(Incidencia incidencia) {
        if (incidencia == null) {
            return false;
        }
        if (incidencia.getId() == null) {
            return repository.createAutoId(incidencia);
        }
        if (!Validations.isValidIncidencia(incidencia)) {
            return false;
        }
        return repository.create(incidencia);
    }

    @Override
    public Incidencia findById(Integer id) {
        if (id == null) {
            return null;
        }
        return repository.findById(id);
    }

    @Override
    public List<Incidencia> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean update(Incidencia incidencia) {
        if (!Validations.isValidIncidencia(incidencia)) {
            return false;
        }
        return repository.update(incidencia);
    }

    @Override
    public boolean deleteById(Integer id) {
        if (id == null) {
            return false;
        }
        return repository.deleteById(id);
    }

    @Override
    public boolean cambiarEstadoIncidencia(Integer id, String estado) {
        if (!Validations.isValidEstadoIncidencia(estado)) {
            return false;
        }
        return repository.cambiarEstadoIncidencia(id, estado);
    }

    @Override
    public List<Incidencia> findByUsuario(Integer idUsuario) {
        return repository.findByUsuario(idUsuario);
    }

}
