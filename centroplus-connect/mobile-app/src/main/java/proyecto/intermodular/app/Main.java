package proyecto.intermodular.app;

import java.util.List;

import proyecto.intermodular.app.model.Usuario;
import proyecto.intermodular.app.service.UsuarioService;

public class Main {
    public static void main(String[] args) {

        //Prueba
        Usuario u1 = new Usuario(1, "admin", "00000000A", "admin@admin.com", "922222222", "Administrador");

        UsuarioService usuarioService = new UsuarioService();

        usuarioService.create(u1, "admin");

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
