package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model;

import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.utils.LocalDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalDateTime;
import java.util.UUID;

@XmlRootElement(name = "usuario")
@XmlAccessorType(XmlAccessType.FIELD)
public class Usuario {

    @XmlElement
    private String id;

    @XmlElement
    private String username;

    @XmlElement
    private String password;

    @XmlElement
    private String nombreCompleto;

    @XmlElement
    private String email;

    @XmlElement
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime fechaRegistro;

    @XmlElement
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime ultimaSesion;

    @XmlElement
    private String fotoPath; // Ruta de la foto de perfil (opcional)

    // Constructor vacío (obligatorio JAXB)
    public Usuario() {
    }

    // Constructor completo
    public Usuario(String username, String password, String nombreCompleto, String email) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.fechaRegistro = LocalDateTime.now();
        this.fotoPath = ""; // Foto vacía por defecto
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    /**
     * Devuelve el nombre de usuario.
     * @return El nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Devuelve la contraseña del usuario.
     * @return La contraseña del usuario.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Devuelve el nombre completo del usuario.
     * @return El nombre completo del usuario.
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Devuelve el correo electrónico del usuario.
     * @return El correo electrónico del usuario.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Devuelve la fecha de registro del usuario.
     * @return La fecha de registro del usuario.
     */
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    /**
     * Devuelve la fecha y hora de la última sesión del usuario.
     * @return La fecha y hora de la última sesión del usuario.
     */
    public LocalDateTime getUltimaSesion() {
        return ultimaSesion;
    }

    /**
     * Devuelve la ruta de la foto de perfil del usuario.
     * @return La ruta de la foto de perfil del usuario.
     */
    public String getFotoPath() {
        return fotoPath;
    }

    /**
     * Establece el ID del usuario, este id lo uso a la hora de crear los nombres
     * de los archivos de los usuarios para que tengan nombres unicos.
     * @param id El ID del usuario.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Establece el nombre de usuario del usuario.
     * @param username El nombre de usuario del usuario.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Establece la contraseña del usuario.
     * @param password La contraseña del usuario.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Establece el nombre completo del usuario.
     * @param nombreCompleto El nombre completo del usuario.
     */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }


    /**
     * Establece el correo electrónico del usuario.
     * @param email El correo electrónico del usuario.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Establece la fecha de registro del usuario.
     * @param fechaRegistro La fecha de registro del usuario.
     */
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    /**
     * Establece la fecha y hora de la última sesión del usuario.
     * @param ultimaSesion La fecha y hora de la última sesión del usuario.
     */
    public void setUltimaSesion(LocalDateTime ultimaSesion) {
        this.ultimaSesion = ultimaSesion;
    }

    /**
     * Establece la ruta de la foto de perfil del usuario.
     * @param fotoPath La ruta de la foto de perfil del usuario.
     */
    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

    /**
     * Devuelve una representación en cadena del objeto Usuario.
     * @return Una representación en cadena del objeto Usuario.
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}
