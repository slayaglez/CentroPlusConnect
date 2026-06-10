package proyecto.intermodular.app;

import java.util.List;

import proyecto.intermodular.app.model.Usuario;
import proyecto.intermodular.app.service.UsuarioService;

public class Main {
    public static void main(String[] args) {

        UsuarioService usuarioService = new UsuarioService();

        // Usuarios de prueba
        Usuario u1 = new Usuario(1, "admin", "00000000A", "admin@admin.com", "922222222", "Socio");
        usuarioService.create(u1, "admin");
        Usuario u2 = new Usuario(2, "Sebastián", "00000000B", "slayaglez@gmail.com", "922222222", "Alumno");
        usuarioService.create(u2, "123456");
        Usuario u3 = new Usuario(3, "Atteneri", "00000000C", "atthemyg@gmail.com", "922222222", "Alumno");
        usuarioService.create(u3, "654321");

        System.out.println("\n[2] Listando usuarios...");
        List<Usuario> lista = usuarioService.findAll();

        if (lista.isEmpty()) {
            System.out.println("No hay usuarios registrados");
        } else {
            for (Usuario usuario : lista) {
                System.out.println("- ID: " + usuario.getId() + " | Nombre: " + usuario.getNombre());
            }
        }

    }
}
