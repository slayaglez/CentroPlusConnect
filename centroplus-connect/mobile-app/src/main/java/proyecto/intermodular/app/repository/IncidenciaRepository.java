package proyecto.intermodular.app.repository;

import proyecto.intermodular.app.repository.interfaces.IIncidenciaRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import proyecto.intermodular.app.model.Incidencia;
import proyecto.intermodular.database.sqlite.SQLiteConnectionManager;

public class IncidenciaRepository extends SQLiteConnectionManager implements IIncidenciaRepository {

    public IncidenciaRepository() {
        super(rutaDb);
    }

    @Override
    public boolean create(Incidencia incidencia) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection
                        .prepareStatement(
                                "INSERT INTO incidencias (id, idUsuario, asunto, descripcion, fecha, estado) VALUES (?, ?, ?, ?, ?, ?)")) {

            sentencia.setInt(1, incidencia.getId());
            sentencia.setInt(2, incidencia.getIdUsuario());
            sentencia.setString(3, incidencia.getAsunto());
            sentencia.setString(4, incidencia.getDescripcion());
            sentencia.setString(5, incidencia.getFecha().toString());
            sentencia.setString(6, incidencia.getEstado());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error");
            return false;
        }
    }

    @Override
    public boolean createAutoId(Incidencia incidencia) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection.prepareStatement(
                        "INSERT INTO incidencias (idUsuario, asunto, descripcion, fecha, estado) VALUES (?, ?, ?, ?, ?)")) {

            sentencia.setInt(1, incidencia.getIdUsuario());
            sentencia.setString(2, incidencia.getAsunto());
            sentencia.setString(3, incidencia.getDescripcion());
            sentencia.setString(4, incidencia.getFecha().toString());
            sentencia.setString(5, incidencia.getEstado());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error creando incidencia");
            return false;
        }
    }

    @Override
    public Incidencia findById(Integer id) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection.prepareStatement("SELECT * FROM incidencias WHERE id=?")) {

            sentencia.setInt(1, id);
            ResultSet resultado = sentencia.executeQuery();

            if (!resultado.next()) {
                return null;
            }

            Integer idUsuario = resultado.getInt("id_usuario");
            String asunto = resultado.getString("asunto");
            String descripcion = resultado.getString("descripcion");
            LocalDate fecha = LocalDate.parse(resultado.getString("fecha"));
            String estado = resultado.getString("estado");

            return new Incidencia(id, idUsuario, asunto, descripcion, fecha, estado);

        } catch (Exception e) {
            System.err.println("Error buscando incidencia");
            return null;
        }
    }

    @Override
    public List<Incidencia> findAll() {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection.prepareStatement("SELECT * FROM incidencias")) {

            List<Incidencia> incidencias = new ArrayList<>();
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                Integer id = resultado.getInt("id");
                Integer idUsuario = resultado.getInt("id_usuario");
                String asunto = resultado.getString("asunto");
                String descripcion = resultado.getString("descripcion");
                LocalDate fecha = LocalDate.parse(resultado.getString("fecha"));
                String estado = resultado.getString("estado");

                incidencias.add(new Incidencia(id, idUsuario, asunto, descripcion, fecha, estado));

            }

            return incidencias;

        } catch (Exception e) {
            System.err.println("Error buscando incidencias");
            return null;
        }
    }

    @Override
    public boolean update(Incidencia incidencia) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection
                        .prepareStatement(
                                "UPDATE incidencias SET id_usuario=?, asunto=?, descripcion=?, fecha=?, estado=? WHERE id=?")) {

            sentencia.setInt(6, incidencia.getId());
            sentencia.setInt(1, incidencia.getIdUsuario());
            sentencia.setString(2, incidencia.getAsunto());
            sentencia.setString(3, incidencia.getDescripcion());
            sentencia.setString(4, incidencia.getFecha().toString());
            sentencia.setString(5, incidencia.getEstado());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error actualizando la incidencia");
            return false;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection
                        .prepareStatement("DELETE FROM incidencias WHERE id=?")) {

            sentencia.setInt(1, id);

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error eliminando la incidencia");
            return false;
        }
    }

    @Override
    public boolean cambiarEstadoIncidencia(Integer id, String estado) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection
                        .prepareStatement("UPDATE incidencias SET estado = ? WHERE id = ?")) {

            sentencia.setString(1, estado);
            sentencia.setInt(2, id);

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error cambiando estado");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Incidencia> findByUsuario(Integer idUsuario) {
        List<Incidencia> incidencias = new ArrayList<>();
        
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection
                        .prepareStatement("SELECT * FROM incidencias WHERE id_usuario = ?")) {

            sentencia.setInt(1, idUsuario);

            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {

                Integer id = resultado.getInt("id");
                String asunto = resultado.getString("asunto");
                String descripcion = resultado.getString("descripcion");
                LocalDate fecha = LocalDate.parse(resultado.getString("fecha"));
                String estado = resultado.getString("estado");

                incidencias.add(new Incidencia(id, idUsuario, asunto, descripcion, fecha, estado));
            }

        } catch (Exception e) {
            System.err.println("Error buscando incidencias");
            e.printStackTrace();
        }

        return incidencias;
    }

}
