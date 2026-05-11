package proyecto.intermodular.app;

import java.util.List;

import proyecto.intermodular.app.model.Usuario;
import proyecto.intermodular.app.service.UsuarioService;

public class Main {
    public static void main(String[] args) {
        Usuario u1 = new Usuario(4, "Ana", "43333333A", "ana@example.com", "922222222", "ALUMNO");
        Usuario u2 = new Usuario(5, "Juan", "48888888B", "juan@example.com", "922333333", "PROFESOR");
        Usuario u3 = new Usuario("Diego", "49999999C", "diego@example.com", "922444444", "ALUMNO");
    
        UsuarioService usuarioService = new UsuarioService();

        System.out.println("\n[1] Creando usuarios...");
        usuarioService.create(u1);
        usuarioService.create(u2);
        usuarioService.create(u3);

        System.out.println("\n[2] Listando usuarios...");
        List<Usuario> lista = usuarioService.findAll();

        if(lista.isEmpty()) {
            System.out.println("No hay usuarios registrados");
        } else {
            for (Usuario usuario : lista) {
                System.out.println("- ID: "+usuario.getId() + " | Nombre: "+usuario.getNombre());
            }
        }

        
    }
}
