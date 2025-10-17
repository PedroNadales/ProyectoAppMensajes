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

    //Leer todos los usuarios desde XML
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



    // Guardar objeto Usuarios completo en XML
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

    // Añadir un nuevo usuario y guardar
    public void agregarUsuario(Usuario nuevo) {
        Usuarios usuarios = leerUsuarios();
        usuarios.addUsuario(nuevo);
        guardarUsuarios(usuarios);
    }

    // Buscar usuario por username
    public Usuario buscarUsuario(String username) {
        return leerUsuarios().buscarPorUsername(username);
    }

    // Validar login
    public boolean validarLogin(String username, String password) {
        Usuario u = buscarUsuario(username);
        return (u != null && u.getPassword().equals(password));
    }

    // Actualizar la última sesión de un usuario
    public void actualizarUltimaSesion(String username) {
        Usuarios usuarios = leerUsuarios();
        Usuario usuario = usuarios.buscarPorUsername(username);
        if (usuario != null) {
            usuario.setUltimaSesion(java.time.LocalDateTime.now());
            guardarUsuarios(usuarios);
        }
    }
}
