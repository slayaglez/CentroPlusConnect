package proyecto.intermodular.app;

import java.time.LocalDate;
import java.util.List;

import proyecto.intermodular.app.model.Actividad;
import proyecto.intermodular.app.model.Incidencia;
import proyecto.intermodular.app.model.Reserva;
import proyecto.intermodular.app.model.Usuario;
import proyecto.intermodular.app.service.ActividadService;
import proyecto.intermodular.app.service.IncidenciaService;
import proyecto.intermodular.app.service.ReservaService;
import proyecto.intermodular.app.service.UsuarioService;

public class Main {
    public static void main(String[] args) {

        //USUARIOS
        Usuario u1 = new Usuario(4, "Ana", "43333333A", "ana@example.com", "922222222", "ALUMNO");
        Usuario u2 = new Usuario(5, "Juan", "48888888B", "juan@example.com", "922333333", "PROFESOR");
        Usuario u3 = new Usuario("Diego", "49999999C", "diego@example.com", "922444444", "ALUMNO");

        UsuarioService usuarioService = new UsuarioService();

        System.out.println("\n[0] Limpiando usuarios...");
        if (usuarioService.deleteById(4))
            System.out.println("Hecho");
        if (usuarioService.deleteById(5))
            System.out.println("Hecho");
        if (usuarioService.deleteById(6))
            System.out.println("Hecho");

        System.out.println("\n[1] Creando usuarios...");
        usuarioService.create(u1);
        usuarioService.create(u2);
        usuarioService.create(u3);

        System.out.println("\n[2] Listando usuarios...");
        List<Usuario> lista = usuarioService.findAll();

        if (lista.isEmpty()) {
            System.out.println("No hay usuarios registrados");
        } else {
            for (Usuario usuario : lista) {
                System.out.println("- ID: " + usuario.getId() + " | Nombre: " + usuario.getNombre());
            }
        }

        //ACTIVIDADES
        Actividad a1 = new Actividad(3, "Yoga", "DEPORTIVA", 60, 30.50, 15, 12);
        Actividad a2 = new Actividad(6, "Lenguaje de marcas", "ACADEMICA", 55, 40.00, 30, 28);
        Actividad a3 = new Actividad(8, "Baloncesto", "Deportiva", 120, 25.50, 17, 17);

        ActividadService actividadService = new ActividadService();

        System.out.println("\n[1] Creando actividades...");
        actividadService.create(a1);
        actividadService.create(a2);
        actividadService.create(a3);

        System.out.println("\n[2] Listando actividades...");
        List<Actividad> listaActividades = actividadService.findAll();

        if (listaActividades.isEmpty()) {
            System.out.println("No hay actividades registradas");
        } else {
            for (Actividad actividad : listaActividades) {
                System.out.println("- ID: " + actividad.getId() + " | Nombre: " + actividad.getNombre());
            }
        }

        //INCIDENCIAS
        Incidencia i1 = new Incidencia(4, 7, "Problema al reservar", "No puedo reservar plazas", LocalDate.of(2026, 03, 04), "EN_PROCESO");
        Incidencia i2 = new Incidencia(8, 2, "Cambio en el horario", "La hora no coincide", LocalDate.of(2026, 05, 10), "ABIERTO");

        IncidenciaService incidenciaService = new IncidenciaService();

        System.out.println("\n[1] Creando incidencias...");
        incidenciaService.create(i1);
        incidenciaService.create(i2);

        System.out.println("\n[2] Listando incidencias...");
        List<Incidencia> listaIncidencias = incidenciaService.findAll();

        if (listaIncidencias.isEmpty()) {
            System.out.println("No hay incidencias registradas");
        } else {
            for (Incidencia incidencia : listaIncidencias) {
                System.out.println("- ID: " + incidencia.getId() + " | Asunto: " + incidencia.getAsunto());
            }
        }

        //RESERVAS
        Reserva r1 = new Reserva(5, 1, 3, LocalDate.of(2025, 10, 15), "ACTIVA");
        Reserva r2 = new Reserva(3, 10, 7, LocalDate.of(2026, 01, 27), "CANCELADA");

        ReservaService reservaService = new ReservaService();

        System.out.println("\n[1] Creando reservas...");
        reservaService.create(r1);
        reservaService.create(r2);

        System.out.println("\n[2] Listando reservas...");
        List<Reserva> listaReservas = reservaService.findAll();

        if (listaReservas.isEmpty()) {
            System.out.println("No hay reservas registradas");
        } else {
            for (Reserva reserva : listaReservas) {
                System.out.println("- ID: " + reserva.getId() + " | Estado: " + reserva.getEstado());
            }
        }
    }
}
