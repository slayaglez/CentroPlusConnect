package proyecto.intermodular.app.model;

import java.util.Objects;

/**
 * Usuario
 * @param Integer id
 * @param String nombre
 * @param String dni
 * @param String email
 * @param String telefono
 * @param String tipoUsuario
 */
public class Usuario {
    private Integer id;
    private String nombre;
    private String dni;
    private String email;
    private String telefono;
    private String tipoUsuario;
    
    /**
     * Constructor vacio
     */
    public Usuario(){}

    /**
     * Constructor con identificador
     * @param id identificador
     */
    public Usuario(int id) {
        this.id = id;
    }

    /**
     * Constructor sin ID
     * @param nombre nombre del usuario
     * @param dni dni del usuario
     * @param email email del usuario
     * @param telefono telefono String
     * @param tipoUsuario tipo usuario String
     */
    public Usuario(String nombre, String dni, String email, String telefono, String tipoUsuario) {
        this.nombre = nombre;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * Constructor completo por defecto
     * @param id identificador
     * @param nombre nombre del usuario
     * @param dni dni del usuario
     * @param email email del usuario
     * @param telefono telefono String
     * @param tipoUsuario tipo usuario String
     */
    public Usuario(int id, String nombre, String dni, String email, String telefono, String tipoUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * Constructor sin telefono
     * @param id identificador
     * @param nombre nombre usuario
     * @param dni dni usuario
     * @param email email usuario
     * @param tipoUsuario tipo usuario String
     */
    public Usuario(int id, String nombre, String dni, String email, String tipoUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (this == obj)
            return true;
        
        if (getClass() != obj.getClass())
            return false;
        Usuario other = (Usuario) obj;
        return id == other.id;
    }

    
}
