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

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public void addMensaje(Mensaje m) {
        mensajes.add(m);
    }
}
