package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model;

import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.util.LocalDateTimeAdapter;
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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public LocalDateTime getUltimaSesion() {
        return ultimaSesion;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public void setUltimaSesion(LocalDateTime ultimaSesion) {
        this.ultimaSesion = ultimaSesion;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}
