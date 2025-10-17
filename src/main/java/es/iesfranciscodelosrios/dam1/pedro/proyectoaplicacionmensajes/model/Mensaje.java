package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model;

import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.util.LocalDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalDateTime;
import java.util.UUID;

@XmlRootElement(name = "mensaje")
@XmlAccessorType(XmlAccessType.FIELD)
public class Mensaje {

    @XmlElement
    private String id;

    @XmlElement
    private String remitente; // username

    @XmlElement
    private String destinatario; // username

    @XmlElement
    private String texto;

    @XmlElement
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime timestamp;

    @XmlElement
    private String rutaAdjunto; // opcional (ruta dentro de media/)

    public Mensaje() {
    }

    public Mensaje(String remitente, String destinatario, String texto, String rutaAdjunto) {
        this.id = UUID.randomUUID().toString();
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.texto = texto;
        this.timestamp = LocalDateTime.now();
        this.rutaAdjunto = rutaAdjunto;
    }

    // getters y setters
    public String getId() {
        return id;
    }

    public String getRemitente() {
        return remitente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getTexto() {
        return texto;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getRutaAdjunto() {
        return rutaAdjunto;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setRutaAdjunto(String rutaAdjunto) {
        this.rutaAdjunto = rutaAdjunto;
    }
}
