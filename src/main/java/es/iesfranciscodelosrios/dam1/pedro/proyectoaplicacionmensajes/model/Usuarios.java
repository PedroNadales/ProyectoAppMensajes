package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "usuarios")
@XmlAccessorType(XmlAccessType.FIELD)
public class Usuarios {

    @XmlElement(name = "usuario")
    private List<Usuario> listaUsuarios = new ArrayList<>();

    public Usuarios() {
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public void addUsuario(Usuario usuario) {
        listaUsuarios.add(usuario);
    }

    public Usuario buscarPorUsername(String username) {
        return listaUsuarios.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return "Usuarios{" +
                "listaUsuarios=" + listaUsuarios +
                '}';
    }

    public void actualizarUsuario(Usuario usuario) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getUsername().equalsIgnoreCase(usuario.getUsername())) {
                listaUsuarios.set(i, usuario);
                return;
            }
        }
    }
}
