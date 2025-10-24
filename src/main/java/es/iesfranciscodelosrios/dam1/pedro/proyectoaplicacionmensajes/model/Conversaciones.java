package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "conversaciones")
@XmlAccessorType(XmlAccessType.FIELD)
public class Conversaciones {

    @XmlElement(name = "mensaje")
    private List<Mensaje> mensajes = new ArrayList<>();

    public Conversaciones() {
    }

    /**
     * Devuelve la lista de mensajes en la conversación.
     * @return La lista de mensajes en la conversación.
     */
    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    /**
     * Establece la lista de mensajes en la conversación.
     * @param mensajes La lista de mensajes a establecer.
     */
    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    /**
     * Agrega un mensaje a la conversación.
     * @param m El mensaje a agregar.
     */
    public void addMensaje(Mensaje m) {
        mensajes.add(m);
    }
}
