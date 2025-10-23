package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.utils;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

public class AdjuntoUtils {

    private static final Path MEDIA_DIR = Paths.get("media");

    public static String guardarAdjunto(File archivo) throws IOException {
        if (!Files.exists(MEDIA_DIR)) {
            Files.createDirectories(MEDIA_DIR);
        }

        String extension = "";
        int dotIndex = archivo.getName().lastIndexOf(".");
        if (dotIndex != -1) extension = archivo.getName().substring(dotIndex);
        String nuevoNombre = UUID.randomUUID() + extension;
        Path destino = MEDIA_DIR.resolve(nuevoNombre);

        try (InputStream in = new FileInputStream(archivo);
             OutputStream out = new FileOutputStream(destino.toFile())) {
            in.transferTo(out);
        }

        return destino.toString(); // ruta del archivo guardado
    }

    public static boolean validarAdjunto(File archivo) {
        long maxSize = 10 * 1024 * 1024; // 10 MB máximo
        String nombre = archivo.getName().toLowerCase();
        return archivo.length() <= maxSize &&
                (nombre.endsWith(".jpg") || nombre.endsWith(".png") ||
                        nombre.endsWith(".pdf") || nombre.endsWith(".txt"));
    }
}
