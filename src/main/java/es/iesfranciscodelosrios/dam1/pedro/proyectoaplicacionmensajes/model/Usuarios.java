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

    /**
     * Devuelve la lista de usuarios.
     * @return Devuelve la lista de usuarios.
     */
    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }


    /**
     * Establece la lista de usuarios.
     * @param listaUsuarios La lista de usuarios a establecer.
     */
    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    /**
     * Agrega un usuario a la lista de usuarios.
     * @param usuario El usuario a agregar.
     */
    public void addUsuario(Usuario usuario) {
        listaUsuarios.add(usuario);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     * @param username El nombre de usuario a buscar.
     * @return El usuario encontrado o null si no se encuentra.
     */
    public Usuario buscarPorUsername(String username) {
        return listaUsuarios.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }


    @Override
    public String toString() {
        return "Usuarios{" +
                "listaUsuarios=" + listaUsuarios +
                '}';
    }

    /**
     * Actualiza un usuario en la lista de usuarios.
     * @param usuario El usuario a actualizar.
     */
    public void actualizarUsuario(Usuario usuario) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getUsername().equalsIgnoreCase(usuario.getUsername())) {
                listaUsuarios.set(i, usuario);
                return;
            }
        }
    }
}
