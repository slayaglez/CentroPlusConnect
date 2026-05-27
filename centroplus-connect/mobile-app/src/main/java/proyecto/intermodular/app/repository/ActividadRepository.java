package proyecto.intermodular.app.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import proyecto.intermodular.app.model.Actividad;
import proyecto.intermodular.app.repository.interfaces.IActividadRepository;
import proyecto.intermodular.database.sqlite.SQLiteConnectionManager;

public class ActividadRepository extends SQLiteConnectionManager implements IActividadRepository {

    public ActividadRepository() {
        super(rutaDb);
    }

    @Override
    public boolean create(Actividad actividad) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection
                        .prepareStatement(
                                "INSERT INTO actividades (id, nombre, tipo_actividad, duracion, precio, plazas_maximas, plazas_ocupadas) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            sentencia.setInt(1, actividad.getId());
            sentencia.setString(2, actividad.getNombre());
            sentencia.setString(3, actividad.getTipoActividad());
            sentencia.setInt(4, actividad.getDuracion());
            sentencia.setDouble(5, actividad.getPrecio());
            sentencia.setInt(6, actividad.getPlazasMaximas());
            sentencia.setInt(7, actividad.getPlazasOcupadas());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error");
            return false;
        }
    }

    @Override
    public boolean createAutoId(Actividad actividad) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection.prepareStatement(
                        "INSERT INTO actividades (nombre, tipo_actividad, duracion, precio, plazas_maximas, plazas_ocupadas) VALUES (?, ?, ?, ?, ?, ?)")) {

            sentencia.setString(1, actividad.getNombre());
            sentencia.setString(2, actividad.getTipoActividad());
            sentencia.setInt(3, actividad.getDuracion());
            sentencia.setDouble(4, actividad.getPrecio());
            sentencia.setInt(5, actividad.getPlazasMaximas());
            sentencia.setInt(6, actividad.getPlazasOcupadas());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error creando actividad");
            return false;
        }
    }

    @Override
    public Actividad findById(Integer id) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection.prepareStatement("SELECT * FROM actividades WHERE id = ?")) {

            sentencia.setInt(1, id);
            ResultSet resultado = sentencia.executeQuery();

            if (!resultado.next()) {
                return null;
            }

            String nombre = resultado.getString("nombre");
            String tipoActividad = resultado.getString("tipo_actividad");
            Integer duracion = resultado.getInt("duracion");
            Double precio = resultado.getDouble("precio");
            Integer plazasMaximas = resultado.getInt("plazas_maximas");
            Integer plazasOcupadas = resultado.getInt("plazas_ocupadas");

            return new Actividad(id, nombre, tipoActividad, duracion, precio, plazasMaximas, plazasOcupadas);

        } catch (Exception e) {
            System.err.println("Error buscando actividad");
            return null;
        }
    }

    @Override
    public List<Actividad> findAll() {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection.prepareStatement("SELECT * FROM actividades")) {

            List<Actividad> actividades = new ArrayList<>();
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                Integer id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");
                String tipoActividad = resultado.getString("tipo_actividad");
                Integer duracion = resultado.getInt("duracion");
                Double precio = resultado.getDouble("precio");
                Integer plazasMaximas = resultado.getInt("plazas_maximas");
                Integer plazasOcupadas = resultado.getInt("plazas_ocupadas");

                actividades.add(new Actividad(id, nombre, tipoActividad, duracion, precio, plazasMaximas, plazasOcupadas));
            }

            return actividades;

        } catch (Exception e) {
            System.err.println("Error buscando actividades");
            return null;
        }
    }

    @Override
    public boolean update(Actividad actividad) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection
                        .prepareStatement(
                                "UPDATE actividades SET nombre=?, tipo_actividad=?, duracion=?, precio=?, plazas_maximas=?, plazas_ocupadas=? WHERE id=?")) {

            sentencia.setInt(7, actividad.getId());
            sentencia.setString(1, actividad.getNombre());
            sentencia.setString(2, actividad.getTipoActividad());
            sentencia.setInt(3, actividad.getDuracion());
            sentencia.setDouble(4, actividad.getPrecio());
            sentencia.setInt(5, actividad.getPlazasMaximas());
            sentencia.setInt(6, actividad.getPlazasOcupadas());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error actualizando la actividad");
            return false;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection
                        .prepareStatement("DELETE FROM actividades WHERE id=?")) {

            sentencia.setInt(1, id);

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error eliminando la actividad");
            return false;
        }
    }

    @Override
    public boolean reservarPlaza(Integer idActividad) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection.prepareStatement(
                        "UPDATE actividades " +
                                "SET plazas_ocupadas = plazas_ocupadas + 1 " +
                                "WHERE id = ? AND plazas_ocupadas < plazas_maximas")) {

            sentencia.setInt(1, idActividad);

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error reservando plaza");
            return false;
        }
    }

    @Override
    public boolean cancelarPlaza(Integer idActividad) {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection.prepareStatement(
                        "UPDATE actividades " +
                                "SET plazas_ocupadas = plazas_ocupadas - 1 " +
                                "WHERE id = ? AND plazas_ocupadas > 0")) {

            sentencia.setInt(1, idActividad);

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error cancelando plaza");
            return false;
        }
    }

    @Override
    public List<Actividad> findCompletas() {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection
                        .prepareStatement("SELECT * FROM actividades WHERE plazas_ocupadas = plazas_maximas")) {

            List<Actividad> actividades = new ArrayList<>();
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                Integer id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");
                String tipoActividad = resultado.getString("tipo_actividad");
                Integer duracion = resultado.getInt("duracion");
                Double precio = resultado.getDouble("precio");
                Integer plazasMaximas = resultado.getInt("plazas_maximas");
                Integer plazasOcupadas = resultado.getInt("plazas_ocupadas");

                actividades.add(new Actividad(id, nombre, tipoActividad, duracion, precio, plazasMaximas, plazasOcupadas));
            }

            return actividades;

        } catch (Exception e) {
            System.err.println("Error buscando actividades");
            return null;
        }
    }

    @Override
    public double calcularIngresosTotales() {
        try (Connection connection = getConnection();
                PreparedStatement sentencia = connection
                        .prepareStatement("SELECT SUM(precio * plazas_ocupadas) AS total FROM actividades")) {

            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {
                return resultado.getDouble("total");
            }

        } catch (Exception e) {
            System.err.println("Error calculando ingresos");
        }

        return 0;
    }

}
