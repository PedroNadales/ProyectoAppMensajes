package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.persistence;

import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model.Conversaciones;
import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model.Mensaje;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MensajesRepository {

    private static final Path XML_PATH = Path.of("data/conversaciones.xml");

    public MensajesRepository() {
        try {
            if (!Files.exists(XML_PATH)) {
                Files.createDirectories(XML_PATH.getParent());
                guardarConversaciones(new Conversaciones());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Conversaciones leerTodas() {
        try {
            if (!Files.exists(XML_PATH) || Files.size(XML_PATH) == 0) {
                return new Conversaciones();
            }
            JAXBContext ctx = JAXBContext.newInstance(Conversaciones.class);
            Unmarshaller un = ctx.createUnmarshaller();
            Conversaciones c = (Conversaciones) un.unmarshal(XML_PATH.toFile());

            // normalizar: asignar timestamp si falta (compatibilidad con versiones antiguas)
            boolean mod = false;
            for (Mensaje m : c.getMensajes()) {
                if (m.getTimestamp() == null) {
                    m.setTimestamp(LocalDateTime.now());
                    mod = true;
                }
            }
            if (mod) guardarConversaciones(c);
            return c;
        } catch (Exception e) {
            e.printStackTrace();
            return new Conversaciones();
        }
    }

    public synchronized void guardarConversaciones(Conversaciones conv) {
        try {
            if (conv == null) conv = new Conversaciones();
            JAXBContext ctx = JAXBContext.newInstance(Conversaciones.class);
            Marshaller mar = ctx.createMarshaller();
            mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            if (!Files.exists(XML_PATH.getParent())) Files.createDirectories(XML_PATH.getParent());
            Path tmp = Files.createTempFile(XML_PATH.getParent(), "conversaciones", ".tmp");
            try {
                mar.marshal(conv, tmp.toFile());
                Files.move(tmp, XML_PATH, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } finally {
                Files.deleteIfExists(tmp);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar conversaciones: " + e.getMessage(), e);
        }
    }

    public synchronized void agregarMensaje(Mensaje m) {
        Conversaciones conv = leerTodas();
        conv.addMensaje(m);
        guardarConversaciones(conv);
    }

    /** Recupera la conversación entre dos usuarios (ordenada por timestamp en ascendente) */
    public List<Mensaje> obtenerConversacion(String a, String b) {
        return leerTodas().getMensajes().stream()
                .filter(m -> (m.getRemitente().equalsIgnoreCase(a) && m.getDestinatario().equalsIgnoreCase(b)) ||
                        (m.getRemitente().equalsIgnoreCase(b) && m.getDestinatario().equalsIgnoreCase(a)))
                .sorted((m1,m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()))
                .collect(Collectors.toList());
    }
}
