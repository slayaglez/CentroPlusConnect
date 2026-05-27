package proyecto.intermodular.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import proyecto.intermodular.app.model.Usuario;

class UsuarioTest {
    Usuario usuario;
    Integer id = 67;
    String nombre = "Atteneri";
    String dni = "41234567A";
    String email = "atteneri@example.com";
    String telefono = "612345678";
    String tipoUsuario = "Socio";

    @BeforeEach
    void setup(){
        usuario = new Usuario(id, nombre, dni, email, telefono, tipoUsuario);
    }

    @DisplayName("Test verifica not null")
    @Order(1)
    @Test
    void usuarioNotNullTest(){
        Assertions.assertNotNull(usuario, "El usuario no puede ser null");
    }

    @DisplayName("Test verifica equals")
    @Order(2)
    @Test
    void usuarioEqualsTrue(){
        Usuario usuarioNuevo = new Usuario(67);
        Assertions.assertEquals(usuario, usuarioNuevo);
    }

    @DisplayName("Test verifica equals")
    @Order(3)
    @Test
    void usuarioEqualsFalse(){
        Usuario usuarioNuevo = new Usuario(69);
        Assertions.assertNotEquals(usuario, usuarioNuevo);
    }

    @DisplayName("Test verifica equals")
    @Order(4)
    @Test
    void usuarioEqualsSameObject(){
        Assertions.assertEquals(usuario, usuario);
    }

    @DisplayName("Test verifica equals con null")
    @Order(4)
    @Test
    void usuarioEqualsFalseWithNull(){
        Assertions.assertNotEquals(usuario, null);
    }

    @DisplayName("Test verifica getters")
    @Order(4)
    @Test
    void usuarioGetters(){
        Integer id = usuario.getId();
        String nombre = usuario.getNombre();
        String dni = usuario.getDni();
        String email = usuario.getEmail();
        String telefono = usuario.getTelefono();
        String tipoUsuario = usuario.getTipoUsuario();

        Assertions.assertEquals(67, id);
        Assertions.assertEquals("Atteneri", nombre);
        Assertions.assertEquals("41234567A", dni);
        Assertions.assertEquals("atteneri@example.com", email);
        Assertions.assertEquals("612345678", telefono);
        Assertions.assertEquals("Socio", tipoUsuario);
    }

    @DisplayName("Test verifica setters")
    @Order(5)
    @Test
    void usuarioSetters(){
        usuario.setId(66);
        usuario.setNombre("Sebas");
        usuario.setDni("47654321A");
        usuario.setEmail("sebas@example.com");
        usuario.setTelefono("666777888");
        usuario.setTipoUsuario("Vip");

        Assertions.assertEquals(66, usuario.getId());
        Assertions.assertEquals("Sebas", usuario.getNombre());
        Assertions.assertEquals("47654321A", usuario.getDni());
        Assertions.assertEquals("sebas@example.com", usuario.getEmail());
        Assertions.assertEquals("666777888", usuario.getTelefono());
        Assertions.assertEquals("Vip", usuario.getTipoUsuario());
    }

    @DisplayName("Test verifica constructor")
    @Order(6)
    @Test
    void usuarioConstructorSinId(){
        Usuario usuarioNuevo = new Usuario("Juan", "4876543B", "juan@example.com", "666555444", "Socio");
        Assertions.assertEquals("Juan", usuarioNuevo.getNombre());
        Assertions.assertEquals("4876543B", usuarioNuevo.getDni());
        Assertions.assertEquals("666555444", usuarioNuevo.getTelefono());
    }

    @DisplayName("Test verifica constructor vacio")
    @Order(6)
    @Test
    void usuarioConstructorVacio(){
        Usuario usuarioVacio = new Usuario();
        Assertions.assertInstanceOf(Usuario.class, usuarioVacio);
    }

    @DisplayName("Test verifica constructor vacio")
    @Order(7)
    @Test
    void usuarioConstructorConId(){
        Usuario usuarioConId = new Usuario(id, nombre, dni, email, tipoUsuario);
        Assertions.assertEquals("Atteneri", usuarioConId.getNombre());
        Assertions.assertEquals("41234567A", usuarioConId.getDni());
        Assertions.assertEquals("atteneri@example.com", usuarioConId.getEmail());
    }
}
