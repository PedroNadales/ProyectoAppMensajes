package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.persistence;

import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model.Usuario;
import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model.Usuarios;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.nio.file.Files;
import java.nio.file.Path;

public class UsuarioRepository {

    private static final Path XML_PATH = Path.of("data/usuarios.xml");

    /**
     * Constructor de la clase UsuarioRepository.
     * Crea el archivo XML si no existe y lo inicializa con un objeto Usuarios vacío.
     */
    public UsuarioRepository() {
        // Si el archivo no existe, se crea uno vacío
        try {
            if (!Files.exists(XML_PATH)) {
                Files.createDirectories(XML_PATH.getParent());
                guardarUsuarios(new Usuarios()); // crea archivo vacío <usuarios/>
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lee todos los usuarios desde el archivo XML.
     * Si el archivo no existe o está vacío, devuelve un objeto Usuarios vacío.
     * Si hay error al leer, devuelve un objeto Usuarios vacío y muestra un mensaje de error.
     * @return La lista de usuarios leída desde el archivo XML.
     */
    public Usuarios leerUsuarios() {
        try {
            if (!Files.exists(XML_PATH) || Files.size(XML_PATH) == 0) {
                return new Usuarios();
            }
            JAXBContext context = JAXBContext.newInstance(Usuarios.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Usuarios) unmarshaller.unmarshal(XML_PATH.toFile());
        } catch (Exception e) {
            e.printStackTrace();
            return new Usuarios(); // Si hay error, devolver lista vacía
        }
    }


    /**
     * Guarda la lista de usuarios en el archivo XML.
     * Si el objeto usuarios es null, crea uno vacío antes de guardar.
     * Utiliza un archivo temporal para evitar corrupción de datos en caso de error.
     * @param usuarios El objeto Usuarios a guardar en el archivo XML.
     */
    public void guardarUsuarios(Usuarios usuarios) {
        try {
            if (usuarios == null) {
                usuarios = new Usuarios();
            }
            JAXBContext context = JAXBContext.newInstance(Usuarios.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Asegurarse de que el directorio existe
            if (!Files.exists(XML_PATH.getParent())) {
                Files.createDirectories(XML_PATH.getParent());
            }

            // Escribir a un archivo temporal primero
            Path tempFile = Files.createTempFile(XML_PATH.getParent(), "usuarios", ".tmp");
            try {
                marshaller.marshal(usuarios, tempFile.toFile());
                // Reemplazar el archivo original con el temporal
                Files.move(tempFile, XML_PATH, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } finally {
                // Asegurarse de eliminar el archivo temporal si algo sale mal
                Files.deleteIfExists(tempFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar los usuarios: " + e.getMessage(), e);
        }
    }

    /**
     * Añade un nuevo usuario a la lista de usuarios y lo guarda en el archivo XML.
     * @param nuevo El usuario a añadir.
     */
    public void agregarUsuario(Usuario nuevo) {
        Usuarios usuarios = leerUsuarios();
        usuarios.addUsuario(nuevo);
        guardarUsuarios(usuarios);
    }

    /**
     * Actualiza un usuario en la lista de usuarios y lo guarda en el archivo XML.
     * @param usuario El usuario a actualizar.
     */
    public void actualizarUsuario(Usuario usuario) {
        Usuarios usuarios = leerUsuarios();
        usuarios.actualizarUsuario(usuario);
        guardarUsuarios(usuarios);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     * @param username El nombre de usuario a buscar.
     * @return El usuario encontrado o null si no se encuentra.
     */
    public Usuario buscarUsuario(String username) {
        return leerUsuarios().buscarPorUsername(username);
    }

    /**
     * Valida las credenciales de un usuario.
     * @param username El nombre de usuario.
     * @param password La contraseña.
     * @return true si las credenciales son válidas, false en caso contrario.
     */
    public boolean validarLogin(String username, String password) {
        Usuario u = buscarUsuario(username);
        return (u != null && u.getPassword().equals(password));
    }


    /**
     * Actualiza la última sesión de un usuario en la lista de usuarios y lo guarda en el archivo XML.
     * @param username El nombre de usuario del usuario a actualizar.
     */
    public void actualizarUltimaSesion(String username) {
        Usuarios usuarios = leerUsuarios();
        Usuario usuario = usuarios.buscarPorUsername(username);
        if (usuario != null) {
            usuario.setUltimaSesion(java.time.LocalDateTime.now());
            guardarUsuarios(usuarios);
        }
    }
}
