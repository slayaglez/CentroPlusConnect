package proyecto.intermodular.app.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import proyecto.intermodular.app.model.Reserva;
import proyecto.intermodular.app.model.ReservaDetalle;
import proyecto.intermodular.app.repository.interfaces.IReservaRepository;
import proyecto.intermodular.database.sqlite.SQLiteConnectionManager;

public class ReservaRepository extends SQLiteConnectionManager implements IReservaRepository {

    public ReservaRepository() {
        super(rutaDb);
    }

    @Override
    public boolean create(Reserva reserva) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection
                        .prepareStatement(
                                "INSERT INTO reservas (id, id_usuario, id_actividad, fecha, estado) VALUES (?, ?, ?, ?, ?)")) {

            sentencia.setInt(1, reserva.getId());
            sentencia.setInt(2, reserva.getIdUsuario());
            sentencia.setInt(3, reserva.getIdActividad());
            sentencia.setString(4, reserva.getFecha().toString());
            sentencia.setString(5, reserva.getEstado());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error");
            return false;
        }
    }

    @Override
    public boolean createAutoId(Reserva reserva) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection.prepareStatement(
                        "INSERT INTO reservas (id_usuario, id_actividad, fecha, estado) VALUES (?, ?, ?, ?)")) {

            sentencia.setInt(1, reserva.getIdUsuario());
            sentencia.setInt(2, reserva.getIdActividad());
            sentencia.setString(3, reserva.getFecha().toString());
            sentencia.setString(4, reserva.getEstado());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error creando reserva");
            return false;
        }

    }

    @Override
    public Reserva findById(Integer id) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection.prepareStatement("SELECT * FROM reservas WHERE id=?")) {

            sentencia.setInt(1, id);
            ResultSet resultado = sentencia.executeQuery();

            if (!resultado.next()) {
                return null;
            }

            Integer idUsuario = resultado.getInt("id_usuario");
            Integer idActividad = resultado.getInt("id_actividad");
            LocalDate fecha = LocalDate.parse(resultado.getString("fecha"));
            String estado = resultado.getString("estado");

            return new Reserva(id, idUsuario, idActividad, fecha, estado);

        } catch (Exception e) {
            System.err.println("Error buscando reserva");
            return null;
        }
    }

    @Override
    public List<Reserva> findAll() {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection.prepareStatement("SELECT * FROM reservas")) {

            List<Reserva> reservas = new ArrayList<>();
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                Integer id = resultado.getInt("id");
                Integer idUsuario = resultado.getInt("id_usuario");
                Integer idActividad = resultado.getInt("id_actividad");
                LocalDate fecha = LocalDate.parse(resultado.getString("fecha"));
                String estado = resultado.getString("estado");

                reservas.add(new Reserva(id, idUsuario, idActividad, fecha, estado));

            }

            return reservas;

        } catch (Exception e) {
            System.err.println("Error buscando reservas");
            return null;
        }
    }

    @Override
    public boolean update(Reserva reserva) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection.prepareStatement(
                        "UPDATE reservas SET id_usuario=?, id_actividad=?, fecha=?, estado=? WHERE id=?")) {

            sentencia.setInt(1, reserva.getIdUsuario());
            sentencia.setInt(2, reserva.getIdActividad());
            sentencia.setString(3, reserva.getFecha().toString());
            sentencia.setString(4, reserva.getEstado());
            sentencia.setInt(5, reserva.getId());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error actualizando la reserva: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection
                        .prepareStatement("DELETE FROM reservas WHERE id=?")) {

            sentencia.setInt(1, id);

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error eliminando la reserva");
            return false;
        }
    }

    @Override
    public boolean cancelarReserva(Integer idReserva) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection.prepareStatement(
                        "UPDATE reservas SET estado=? WHERE id=?")) {

            sentencia.setString(1, "CANCELADA");
            sentencia.setInt(2, idReserva);

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error cancelando reserva");
            return false;
        }
    }

    @Override
    public List<Reserva> findDisponibles() {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection.prepareStatement("SELECT * FROM reservas WHERE estado=?")) {

            sentencia.setString(1, "ACTIVA");

            List<Reserva> reservas = new ArrayList<>();

            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {

                Integer id = resultado.getInt("id");
                Integer idUsuario = resultado.getInt("id_usuario");
                Integer idActividad = resultado.getInt("id_actividad");
                LocalDate fecha = LocalDate.parse(resultado.getString("fecha"));
                String estado = resultado.getString("estado");

                reservas.add(new Reserva(id, idUsuario, idActividad, fecha, estado));
            }

            return reservas;

        } catch (Exception e) {
            System.err.println("Error buscando reservas disponibles");
            return null;
        }
    }

    public List<ReservaDetalle> findAllConDetalle() {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection.prepareStatement(
                        "SELECT r.id, r.id_usuario, r.id_actividad, " +
                                "u.nombre AS nombre_usuario, a.nombre AS nombre_actividad, " +
                                "r.fecha, r.estado " +
                                "FROM reservas r " +
                                "INNER JOIN usuarios u ON r.id_usuario = u.id " +
                                "INNER JOIN actividades a ON r.id_actividad = a.id")) {

            List<ReservaDetalle> lista = new ArrayList<>();
            ResultSet rs = sentencia.executeQuery();
            while (rs.next()) {
                lista.add(new ReservaDetalle(
                        rs.getInt("id"),
                        rs.getInt("id_usuario"),
                        rs.getInt("id_actividad"),
                        rs.getString("nombre_usuario"),
                        rs.getString("nombre_actividad"),
                        LocalDate.parse(rs.getString("fecha")),
                        rs.getString("estado")));
            }
            return lista;
        } catch (Exception e) {
            System.err.println("Error buscando reservas con detalle: " + e.getMessage());
            return new ArrayList<>();
        }
    }

}
