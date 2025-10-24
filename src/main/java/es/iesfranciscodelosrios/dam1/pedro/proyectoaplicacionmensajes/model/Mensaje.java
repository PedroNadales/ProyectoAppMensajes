package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model;

import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.utils.LocalDateTimeAdapter;
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
    private String rutaAdjunto; // (ruta dentro de media/)

    @XmlElement
    private String nombreAdjunto;

    @XmlElement
    private String tipoAdjunto;

    @XmlElement
    private long tamanoAdjunto;


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

    /**
        * Devuelve el remitente del mensaje.
        * @return El nombre de usuario del remitente.
     */
    public String getRemitente() {
        return remitente;
    }

    /**
        * Devuelve el destinatario del mensaje.
        * @return El nombre de usuario del destinatario.
     */
    public String getDestinatario() {
        return destinatario;
    }

    /**
        * Devuelve el texto del mensaje.
        * @return El contenido textual del mensaje.
     */
    public String getTexto() {
        return texto;
    }

    /**
        * Devuelve la marca de tiempo del mensaje.
        * @return La fecha y hora en que se envió el mensaje.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
        * Devuelve la ruta del archivo adjunto del mensaje.
        * @return La ruta del archivo adjunto dentro del directorio de medios.
     */
    public String getRutaAdjunto() {
        return rutaAdjunto;
    }

    /**
        * Establece el ID del mensaje.
        * @param id El ID único del mensaje.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
        * Establece el remitente del mensaje.
        * @param remitente El nombre de usuario del remitente.
     */
    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    /**
        * Establece el destinatario del mensaje.
        * @param destinatario El nombre de usuario del destinatario.
     */
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    /**
     * Establece el texto del mensaje.
     * @param texto El contenido textual del mensaje.
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Establece la marca de tiempo del mensaje.
     * @param timestamp La fecha y hora en que se envió el mensaje.
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Establece la ruta del archivo adjunto del mensaje.
     * @param rutaAdjunto La ruta del archivo adjunto dentro del directorio de medios.
     */
    public void setRutaAdjunto(String rutaAdjunto) {
        this.rutaAdjunto = rutaAdjunto;
    }

    /**
     * Devuelve el nombre del archivo adjunto del mensaje.
     * @return El nombre del archivo adjunto.
     */
    public String getNombreAdjunto() {
        return nombreAdjunto;
    }

    /**
     * Establece el nombre del archivo adjunto del mensaje.
     * @param nombreAdjunto El nombre del archivo adjunto.
     */
    public void setNombreAdjunto(String nombreAdjunto) {
        this.nombreAdjunto = nombreAdjunto;
    }

    /**
     * Devuelve el tipo MIME del archivo adjunto del mensaje.
     * @return El tipo MIME del archivo adjunto.
     */
    public String getTipoAdjunto() {
        return tipoAdjunto;
    }

    /**
     * Establece el tipo MIME del archivo adjunto del mensaje.
     * @param tipoAdjunto El tipo MIME del archivo adjunto.
     */
    public void setTipoAdjunto(String tipoAdjunto) {
        this.tipoAdjunto = tipoAdjunto;
    }

    /**
     * Devuelve el tamaño del archivo adjunto del mensaje en bytes.
     * @return El tamaño del archivo adjunto.
     */
    public long getTamanoAdjunto() {
        return tamanoAdjunto;
    }

    /**
     * Establece el tamaño del archivo adjunto del mensaje en bytes.
     * @param tamanoAdjunto El tamaño del archivo adjunto.
     */
    public void setTamanoAdjunto(long tamanoAdjunto) {
        this.tamanoAdjunto = tamanoAdjunto;
    }

}
