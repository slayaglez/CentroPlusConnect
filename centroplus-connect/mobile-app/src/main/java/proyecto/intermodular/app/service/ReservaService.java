package proyecto.intermodular.app.service;

import java.util.List;

import proyecto.intermodular.app.model.Reserva;
import proyecto.intermodular.app.model.ReservaDetalle;
import proyecto.intermodular.app.repository.ReservaRepository;
import proyecto.intermodular.app.repository.interfaces.IReservaRepository;
import proyecto.intermodular.app.service.interfaces.IReservaService;
import proyecto.intermodular.validations.Validations;

public class ReservaService implements IReservaService {

    private final IReservaRepository repository;

    public ReservaService() {
        this.repository = new ReservaRepository();
    }

    @Override
    public boolean create(Reserva reserva) {
        if (reserva == null) {
            return false;
        }
        if (reserva.getId() == null) {
            return repository.createAutoId(reserva);
        }
        if (!Validations.isValidReserva(reserva)) {
            return false;
        }
        return repository.create(reserva);
    }

    @Override
    public Reserva findById(Integer id) {
        if (id == null) {
            return null;
        }
        return repository.findById(id);
    }

    @Override
    public List<Reserva> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean update(Reserva reserva) {
        if (!Validations.isValidReserva(reserva)) {
            return false;
        }
        return repository.update(reserva);
    }

    @Override
    public boolean deleteById(Integer id) {
        if (id == null) {
            return false;
        }
        return repository.deleteById(id);
    }

    @Override
    public boolean cancelarReserva(Integer idReserva) {
        return repository.cancelarReserva(idReserva);
    }

    @Override
    public List<Reserva> findDisponibles() {
        return repository.findDisponibles();
    }

    @Override
    public List<ReservaDetalle> findAllConDetalle() {
        return repository.findAllConDetalle();
    }

}
