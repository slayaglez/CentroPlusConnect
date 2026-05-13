package proyecto.intermodular.app.repository;

import java.nio.channels.UnsupportedAddressTypeException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import proyecto.intermodular.app.model.Reserva;
import proyecto.intermodular.app.repository.interfaces.IReservaRepository;
import proyecto.intermodular.database.sqlite.SQLiteConnectionManager;

public class ReservaRepository extends SQLiteConnectionManager implements IReservaRepository {

    public ReservaRepository(String rutaDb) {
        super(rutaDb);
    }

    @Override
    public boolean create(Reserva reserva) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public boolean createAutoId(Reserva reserva) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAutoId'");
    }

    @Override
    public Reserva findById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public List<Reserva> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public boolean update(Reserva reserva) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean deleteById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public boolean cancelarReserva(Integer idReserva) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cancelarReserva'");
    }

    @Override
    public List<Reserva> findDisponibles() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findDisponibles'");
    }

}
