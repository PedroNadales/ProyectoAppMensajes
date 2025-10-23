package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.controllers;

import javafx.application.Platform;
import javafx.scene.image.WritableImage;
import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model.Mensaje;
import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model.Usuario;
import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.persistence.UsuarioRepository;
import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.persistence.MensajesRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MainController {

    @FXML private ListView<Usuario> listaUsuarios;
    @FXML private VBox panelDerecho;
    @FXML private StackPane panelCentral;
    @FXML private Label lblTituloPanelDerecho;
    @FXML private Label lblNombreUsuario;
    @FXML private Label lblEmail;
    @FXML private Button btnAgregarContacto;
    @FXML private ImageView imgPerfil;
    @FXML private Label lblFechaRegistro;
    @FXML private Label lblUltimaSesion;
    @FXML private VBox menuInicial;
    @FXML private BorderPane chatBox;
    @FXML private VBox chatMensajes;
    @FXML private ScrollPane scrollPaneChat;
    @FXML private TextField txtMensaje;
    @FXML private Button btnEnviar;
    @FXML private Button btnResumen;
    @FXML private VBox boxResumen;
    @FXML private Button btnExportar;
    @FXML private Button btnAdjuntar;
    @FXML private Button btnCambiarFoto;
    @FXML private Button btnCerrarSesion;
    @FXML private Button btnVolverMenu;


    private final UsuarioRepository repo = new UsuarioRepository();
    private final MensajesRepository mensajesRepo = new MensajesRepository();
    private Usuario usuarioActual; // El usuario logueado

    @FXML
    public void initialize() {
        listaUsuarios.setItems(FXCollections.observableArrayList(repo.leerUsuarios().getListaUsuarios()));
        listaUsuarios.setCellFactory(lv -> new ListCell<Usuario>() {
            private final HBox content;
            private final ImageView imageView;
            private final VBox vbox;
            private final Label lblNombre;
            private final Label lblEmail;

            {
                imageView = new ImageView();
                imageView.setFitWidth(40);
                imageView.setFitHeight(40);
                imageView.setPreserveRatio(true);
                imageView.setClip(new Circle(20, 20, 20));
                lblNombre = new Label();
                lblEmail = new Label();
                vbox = new VBox(lblNombre, lblEmail);
                vbox.setSpacing(2);
                content = new HBox(10, imageView, vbox);
                content.setPadding(new Insets(5));
                content.setAlignment(Pos.CENTER_LEFT);
            }

            @Override
            protected void updateItem(Usuario usuario, boolean empty) {
                super.updateItem(usuario, empty);
                if (empty || usuario == null) {
                    setGraphic(null);
                } else {
                    lblNombre.setText(usuario.getUsername());
                    lblEmail.setText(usuario.getEmail());
                    Image img;
                    try {
                        if (usuario.getFotoPath() != null && !usuario.getFotoPath().isEmpty()) {
                            // Intentar cargar la imagen personalizada del usuario
                            try {
                                // Primero intentar como archivo del sistema (rutas absolutas)
                                File imgFile = new File(usuario.getFotoPath());
                                if (imgFile.exists()) {
                                    img = new Image(new FileInputStream(imgFile), 40, 40, true, true);
                                } else {
                                    // Si no existe como archivo, intentar como recurso interno
                                    img = new Image(getClass().getResourceAsStream(usuario.getFotoPath()), 40, 40, true, true);
                                }
                            } catch (Exception e) {
                                // Si falla todo, usar la imagen por defecto
                                throw e;
                            }
                        } else {
                            // Cargar la imagen predeterminada desde la carpeta de recursos
                            img = new Image("/assets/default-avatar.png", 40, 40, true, true);
                        }
                    } catch (Exception e) {
                        // En caso de error, intentar cargar la imagen predeterminada de otra manera
                        try {
                            img = new Image(getClass().getResource("/assets/default-avatar.png").toExternalForm(), 40, 40, true, true);
                        } catch (Exception ex) {
                            // Si todo falla, crear una imagen en blanco
                            img = new WritableImage(40, 40);
                        }
                    }
                    imageView.setImage(img);
                    setGraphic(content);
                }
            }
        });

        listaUsuarios.setOnMouseClicked(e -> {
            Usuario seleccionado = listaUsuarios.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                mostrarInfoUsuario(seleccionado);
            }
        });
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        lblNombreUsuario.setText("Nombre: " + usuario.getNombreCompleto());
        lblEmail.setText("Email: " + usuario.getEmail());
        if (usuario.getFechaRegistro() != null) {
            lblFechaRegistro.setText("Miembro desde: " + usuario.getFechaRegistro().toLocalDate().toString());
        } else {
            lblFechaRegistro.setText("Miembro desde: (desconocido)");
        }
        if (usuario.getUltimaSesion() != null) {
            lblUltimaSesion.setText("Última sesión: " + usuario.getUltimaSesion().toLocalDate().toString());
        } else {
            lblUltimaSesion.setText("Última sesión: primera vez");
        }
        
        // Cargar la imagen de perfil
        try {
            Image img;
            if (usuario.getFotoPath() != null && !usuario.getFotoPath().isEmpty()) {
                try {
                    // Primero intentar cargar como ruta de recurso
                    img = new Image(getClass().getResourceAsStream(usuario.getFotoPath()), 100, 100, true, true);
                } catch (Exception e) {
                    // Si falla, intentar como ruta de archivo
                    try {
                        img = new Image(new FileInputStream(usuario.getFotoPath()), 100, 100, true, true);
                    } catch (Exception ex) {
                        // Si todo falla, usar la imagen por defecto
                        throw new Exception("No se pudo cargar la imagen");
                    }
                }
            } else {
                // Si no hay ruta de imagen, usar la imagen por defecto
                img = new Image(getClass().getResourceAsStream("/assets/default-avatar.png"), 100, 100, true, true);
            }
            imgPerfil.setImage(img);
            // Hacer la imagen circular
            Circle clip = new Circle(50, 50, 50);
            imgPerfil.setClip(clip);
        } catch (Exception e) {
            try {
                // Si hay algún error, intentar cargar la imagen por defecto de otra manera
                Image img = new Image(getClass().getResource("/assets/default-avatar.png").toExternalForm(), 100, 100, true, true);
                imgPerfil.setImage(img);
                Circle clip = new Circle(50, 50, 50);
                imgPerfil.setClip(clip);
            } catch (Exception ex) {
                // Si todo falla, mostrar un mensaje de error en la consola
                System.err.println("No se pudo cargar la imagen de perfil: " + ex.getMessage());
            }
        }
    }

    private void mostrarInfoUsuario(Usuario usuarioSeleccionado) {
        // Mostrar información del usuario seleccionado en el panel derecho
        lblTituloPanelDerecho.setText("Perfil de " + usuarioSeleccionado.getUsername());
        lblTituloPanelDerecho.setVisible(true);
        
        // Mostrar nombre y email
        lblNombreUsuario.setText("Nombre: " + (usuarioSeleccionado.getNombreCompleto() != null ? 
            usuarioSeleccionado.getNombreCompleto() : usuarioSeleccionado.getUsername()));
        lblEmail.setText("Email: " + usuarioSeleccionado.getEmail());
        
        // Mostrar fecha de registro
        if (usuarioSeleccionado.getFechaRegistro() != null) {
            lblFechaRegistro.setText("Miembro desde: " + 
                usuarioSeleccionado.getFechaRegistro().toLocalDate().toString());
        } else {
            lblFechaRegistro.setText("Miembro desde: (desconocido)");
        }
        
        // Mostrar última sesión con formato
        if (usuarioSeleccionado.getUltimaSesion() != null) {
            lblUltimaSesion.setText("Última sesión: " +
                String.format("%02d/%02d/%04d %02d:%02d",
                    usuarioSeleccionado.getUltimaSesion().getDayOfMonth(),
                    usuarioSeleccionado.getUltimaSesion().getMonthValue(),
                    usuarioSeleccionado.getUltimaSesion().getYear(),
                    usuarioSeleccionado.getUltimaSesion().getHour(),
                    usuarioSeleccionado.getUltimaSesion().getMinute()));
        } else {
            lblUltimaSesion.setText("Última sesión: Nunca");
        }
        
        // Cargar la imagen de perfil del usuario seleccionado
        cargarImagenPerfil(usuarioSeleccionado);
        
        // Si el usuario seleccionado no es el actual, mostrar el chat
        if (!usuarioSeleccionado.equals(usuarioActual)) {
            menuInicial.setVisible(false);
            menuInicial.setManaged(false);
            chatBox.setVisible(true);
            chatBox.setManaged(true);
            chatMensajes.getChildren().clear();

            // Cargar la conversación con el usuario seleccionado
            var mensajes = mensajesRepo.obtenerConversacion(usuarioActual.getUsername(), 
                usuarioSeleccionado.getUsername());
            for (Mensaje m : mensajes) {
                mostrarMensajeEnChat(m);
            }

            // Configurar el botón de enviar mensaje
            btnEnviar.setOnAction(event -> handleEnviarMensaje(usuarioSeleccionado));
            
            // Configurar el botón de resumen
            btnResumen.setVisible(true);
            btnResumen.setManaged(true);
            boxResumen.setVisible(false);
            boxResumen.setManaged(false);
            boxResumen.getChildren().clear();
            btnResumen.setOnAction(e -> mostrarResumenConversacion(usuarioSeleccionado));
        }

        if (!usuarioSeleccionado.equals(usuarioActual)) {
            menuInicial.setVisible(false);
            menuInicial.setManaged(false);
            chatBox.setVisible(true);
            chatBox.setManaged(true);
            btnVolverMenu.setVisible(true);
            btnVolverMenu.setManaged(true);
            chatMensajes.getChildren().clear();

            Usuario usuarioDestino = usuarioSeleccionado;

            var mensajes = mensajesRepo.obtenerConversacion(usuarioActual.getUsername(), usuarioDestino.getUsername());
            for (Mensaje m : mensajes) {
                mostrarMensajeEnChat(m);
            }

            btnEnviar.setOnAction(event -> handleEnviarMensaje(usuarioDestino));
            btnResumen.setVisible(true);
            btnResumen.setManaged(true);
            boxResumen.setVisible(false);
            boxResumen.setManaged(false);
            boxResumen.getChildren().clear();
            btnResumen.setOnAction(e -> mostrarResumenConversacion(usuarioDestino));
            btnExportar.setVisible(true);
            btnExportar.setManaged(true);
            btnExportar.setOnAction(e -> mostrarDialogoExportacion(usuarioDestino));
            btnAdjuntar.setVisible(true);
            btnAdjuntar.setManaged(true);
            btnAdjuntar.setOnAction(e -> adjuntarArchivo(usuarioDestino));


        } else {
            menuInicial.setVisible(true);
            menuInicial.setManaged(true);
            chatBox.setVisible(false);
            chatBox.setManaged(false);
            btnResumen.setVisible(false);
            btnResumen.setManaged(false);
            boxResumen.setVisible(false);
            boxResumen.setManaged(false);
            btnExportar.setVisible(false);
            btnExportar.setManaged(false);
            btnAdjuntar.setVisible(false);
            btnAdjuntar.setManaged(false);
            btnVolverMenu.setVisible(false);
            btnVolverMenu.setManaged(false);
        }
    }

    private void cargarImagenPerfil(Usuario usuarioSeleccionado) {
        try {
            Image img;
            if (usuarioSeleccionado.getFotoPath() != null && !usuarioSeleccionado.getFotoPath().isEmpty()) {
                try {
                    img = new Image(getClass().getResourceAsStream(usuarioSeleccionado.getFotoPath()), 100, 100, true, true);
                } catch (Exception e) {
                    try {
                        img = new Image(new FileInputStream(usuarioSeleccionado.getFotoPath()), 100, 100, true, true);
                    } catch (Exception ex) {
                        throw new Exception("No se pudo cargar la imagen");
                    }
                }
            } else {
                img = new Image(getClass().getResourceAsStream("/assets/default-avatar.png"), 100, 100, true, true);
            }
            imgPerfil.setImage(img);
            Circle clip = new Circle(50, 50, 50);
            imgPerfil.setClip(clip);
        } catch (Exception e) {
            try {
                Image img = new Image(getClass().getResource("/assets/default-avatar.png").toExternalForm(), 100, 100, true, true);
                imgPerfil.setImage(img);
                Circle clip = new Circle(50, 50, 50);
                imgPerfil.setClip(clip);
            } catch (Exception ex) {
                System.err.println("No se pudo cargar la imagen de perfil: " + ex.getMessage());
            }
        }
    }

    private void handleEnviarMensaje(Usuario destinatario) {
        String texto = txtMensaje.getText().trim();
        if (texto.isEmpty() || destinatario == null) return;

        Mensaje mensaje = new Mensaje(usuarioActual.getUsername(), destinatario.getUsername(), texto, null);
        mensajesRepo.agregarMensaje(mensaje);
        mostrarMensajeEnChat(mensaje);
        txtMensaje.clear();
    }

    private void mostrarMensajeEnChat(Mensaje m) {
        boolean esPropio = m.getRemitente().equals(usuarioActual.getUsername());
        
        // Crear el contenedor del mensaje
        HBox mensajeBox = new HBox();
        // Verificar si es propio o recibido para alinear en la pantalla, con un operador ternario (if else pero mas compacto)
        mensajeBox.setAlignment(esPropio ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        mensajeBox.setPadding(new Insets(5, 10, 5, 10));
        
        // Crear el contenedor del texto del mensaje
        VBox contenidoBox = new VBox();
        contenidoBox.setMaxWidth(chatMensajes.getWidth() * 0.8); // Ancho máximo del 80% del chat
        
        // Crear etiqueta del remitente (solo para mensajes recibidos)
        if (!esPropio) {
            Label lblRemitente = new Label(m.getRemitente());
            lblRemitente.setStyle("-fx-font-weight: bold; -fx-text-fill: #666; -fx-font-size: 0.8em;");
            contenidoBox.getChildren().add(lblRemitente);
        }
        
        // Crear etiqueta del mensaje
        Label lblMensaje = new Label(m.getTexto());
        lblMensaje.setWrapText(true);
        lblMensaje.setPadding(new Insets(8, 12, 8, 12));
        
        // Aplicar estilos según si es propio o recibido
        if (esPropio) {
            contenidoBox.setStyle(
                "-fx-background-color: #27ba16; " +  // Verde claro para mensajes propios
                "-fx-background-radius: 15 15 0 15; " + // Bordes redondeados
                "-fx-padding: 8px 12px 8px 12px;" + // Pading interno
                "-fx-border-color: #62ff4f;" + // Borde verde brillante
                "-fx-border-radius: 15 15 0 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(27,255,0,0.85), 5, 0, 0, 2);" // Sombra ligera
            );
            contenidoBox.setAlignment(Pos.CENTER_RIGHT);
        } else {
            contenidoBox.setStyle(
                "-fx-background-color: #a3a3a3; " +  // Gris claro para mensajes recibidos
                "-fx-background-radius: 15 15 15 0; " +  // Bordes redondeados
                "-fx-padding: 8px 12px 8px 12px;" + // Pading interno
                "-f-border-color: #dddddd;" + // Borde gris claro
                "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.85), 5, 0, 0, 2);" // Sombra ligera
            );
            contenidoBox.setAlignment(Pos.CENTER_LEFT);
        }
        
        // Añadir la hora del mensaje
        String hora = String.format("%02d:%02d", 
            m.getTimestamp().getHour(), 
            m.getTimestamp().getMinute()
        );
        
        Label lblHora = new Label(hora);
        lblHora.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 0.7em; -fx-padding: 2 0 0 0;");
        
        // Alinear la hora a la derecha o izquierda según el tipo de mensaje
        HBox horaBox = new HBox();
        horaBox.setAlignment(esPropio ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        horaBox.getChildren().add(lblHora);
        
        // Añadir todo al contenedor
        contenidoBox.getChildren().addAll(lblMensaje, horaBox);
        mensajeBox.getChildren().add(contenidoBox);
        
        // Añadir margen inferior entre mensajes
        VBox contenedorFinal = new VBox(mensajeBox);
        contenedorFinal.setPadding(new Insets(0, 0, 5, 0));
        
        chatMensajes.getChildren().add(contenedorFinal);
        
        // Desplazarse al final del chat automáticamente
        Platform.runLater(() -> {
            if (scrollPaneChat != null) {
                scrollPaneChat.setVvalue(1.0);
                scrollPaneChat.layout();
            }
        });

        if (m.getRutaAdjunto() != null && !m.getRutaAdjunto().isEmpty()) {
            File f = new File(m.getRutaAdjunto());
            if (f.exists()) {
                String tipo = m.getTipoAdjunto();
                if (tipo != null && tipo.startsWith("image")) {
                    try {
                        Image imgAdjunto = new Image(new FileInputStream(f), 200, 0, true, true);
                        ImageView iv = new ImageView(imgAdjunto);
                        iv.setPreserveRatio(true);
                        iv.setSmooth(true);
                        iv.setCache(true);

                        // Alineación según tipo de mensaje
                        HBox contenedorImg = new HBox(iv);
                        contenedorImg.setAlignment(esPropio ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
                        contenedorImg.setPadding(new Insets(5, 0, 0, 0));

                        // Añadir dentro del mismo contenidoBox para mantener la burbuja
                        contenidoBox.getChildren().add(contenedorImg);

                    } catch (Exception e) {
                        Label aviso = new Label("[Error al mostrar imagen: " + m.getNombreAdjunto() + "]");
                        aviso.setStyle("-fx-text-fill: red; -fx-font-style: italic;");
                        contenidoBox.getChildren().add(aviso);
                    }
                } else {
                    Hyperlink link = new Hyperlink("[Ver adjunto: " + m.getNombreAdjunto() + "]");
                    link.setOnAction(ev -> {
                        try {
                            java.awt.Desktop.getDesktop().open(f);
                        } catch (Exception e) {
                            mostrarAlerta("Error", "No se pudo abrir el adjunto.", Alert.AlertType.ERROR);
                        }
                    });
                    HBox contenedorLink = new HBox(link);
                    contenedorLink.setAlignment(esPropio ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
                    contenedorLink.setPadding(new Insets(5,0,0,0));
                    contenidoBox.getChildren().add(contenedorLink);
                }
            } else {
                Label aviso = new Label("[Adjunto faltante: " + m.getNombreAdjunto() + "]");
                aviso.setStyle("-fx-text-fill: red; -fx-font-style: italic;");
                contenidoBox.getChildren().add(aviso);
            }
        }

    }

    private void mostrarResumenConversacion(Usuario usuarioDestino) {
        var mensajes = mensajesRepo.obtenerConversacion(usuarioActual.getUsername(), usuarioDestino.getUsername());

        boxResumen.getChildren().clear();

        if (mensajes.isEmpty()) {
            Label lblVacio = new Label("No hay mensajes en esta conversación.");
            lblVacio.setStyle("-fx-text-fill: #000000; -fx-font-style: italic;");
            boxResumen.getChildren().add(lblVacio);
        } else {
            // Calcular resumen con Streams
            long totalMensajes = mensajes.size();

            var mensajesPorUsuario = mensajes.stream()
                    .collect(java.util.stream.Collectors.groupingBy(
                            Mensaje::getRemitente,
                            java.util.stream.Collectors.counting()
                    ));

            var palabrasFrecuentes = mensajes.stream()
                    .flatMap(m -> java.util.Arrays.stream(m.getTexto().toLowerCase().split("\\W+")))
                    .filter(p -> p.length() > 3)
                    .collect(java.util.stream.Collectors.groupingBy(p -> p, java.util.stream.Collectors.counting()));

            String palabraMasFrecuente = palabrasFrecuentes.entrySet().stream()
                    .max(java.util.Map.Entry.comparingByValue())
                    .map(java.util.Map.Entry::getKey)
                    .orElse("Ninguna");

            Label lblTitulo = new Label("Resumen de conversación");
            lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            Label lblTotal = new Label("Total de mensajes: " + totalMensajes);
            Label lblPorUsuario = new Label("Por usuario: " + mensajesPorUsuario);
            Label lblPalabra = new Label("Palabra más usada: " + palabraMasFrecuente);

            boxResumen.getChildren().addAll(lblTitulo, lblTotal, lblPorUsuario, lblPalabra);
        }

        boxResumen.setVisible(true);
        boxResumen.setManaged(true);
    }

    private void exportarConversacion(Usuario usuarioDestino) {
        try {
            var mensajes = mensajesRepo.obtenerConversacion(usuarioActual.getUsername(), usuarioDestino.getUsername());

            if (mensajes.isEmpty()) {
                mostrarAlerta("Exportación", "No hay mensajes para exportar.", Alert.AlertType.INFORMATION);
                return;
            }

            // Crear el archivo
            String fileName = "conversacion_" + usuarioActual.getUsername() + "_con_" + usuarioDestino.getUsername() + ".csv";
            java.nio.file.Path path = java.nio.file.Paths.get(System.getProperty("user.home"), "Desktop", fileName);

            // Exportar usando Streams
            java.nio.file.Files.write(
                    path,
                    mensajes.stream()
                            .map(m -> String.format("\"%s\",\"%s\",\"%s\",\"%s\"",
                                    m.getTimestamp().toString(),
                                    m.getRemitente(),
                                    m.getDestinatario(),
                                    m.getTexto().replace("\"", "\"\"")))
                            .toList()
            );

            mostrarAlerta("Exportación exitosa",
                    "La conversación se exportó correctamente a:\n" + path.toString(),
                    Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo exportar la conversación: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


     // Muestra un diálogo para elegir entre exportar conversación en CSV o en ZIP (con adjuntos).

    private void mostrarDialogoExportacion(Usuario usuarioDestino) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exportar conversación");
        alert.setHeaderText("Selecciona el formato de exportación");
        alert.setContentText("Puedes exportar solo los mensajes (CSV) o incluir también los archivos adjuntos (ZIP).");

        ButtonType btnCSV = new ButtonType("Solo CSV");
        ButtonType btnZIP = new ButtonType("ZIP con adjuntos");
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnCSV, btnZIP, btnCancelar);

        alert.showAndWait().ifPresent(respuesta -> {
            if (respuesta == btnCSV) {
                exportarConversacion(usuarioDestino);
            } else if (respuesta == btnZIP) {
                exportarConversacionZIP(usuarioDestino);
            }
        });
    }


     // Exporta la conversación a un archivo ZIP con los mensajes y los adjuntos.

    private void exportarConversacionZIP(Usuario usuarioDestino) {
        try {
            var mensajes = mensajesRepo.obtenerConversacion(usuarioActual.getUsername(), usuarioDestino.getUsername());

            if (mensajes.isEmpty()) {
                mostrarAlerta("Exportación", "No hay mensajes para exportar.", Alert.AlertType.INFORMATION);
                return;
            }

            // Crear el archivo ZIP en el escritorio del usuario
            String fileName = "Conversacion_" + usuarioActual.getUsername() + "_con_" + usuarioDestino.getUsername() + ".zip";
            File zipFile = new File(System.getProperty("user.home") + File.separator + "Desktop" + File.separator + fileName);

            try (java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(zipFile))) {

                // Agregar CSV al ZIP
                String csvName = "conversacion.csv";
                java.io.ByteArrayOutputStream csvStream = new java.io.ByteArrayOutputStream();
                java.io.PrintWriter writer = new java.io.PrintWriter(csvStream);

                mensajes.forEach(m -> writer.printf("\"%s\",\"%s\",\"%s\",\"%s\"%n",
                        m.getTimestamp(), m.getRemitente(), m.getDestinatario(),
                        m.getTexto().replace("\"", "\"\"")));
                writer.flush();

                zos.putNextEntry(new java.util.zip.ZipEntry(csvName));
                zos.write(csvStream.toByteArray());
                zos.closeEntry();

                // Agregar los adjuntos
                for (Mensaje m : mensajes) {
                    if (m.getRutaAdjunto() != null && !m.getRutaAdjunto().isEmpty()) {
                        File adj = new File(m.getRutaAdjunto());
                        if (adj.exists()) {
                            try (java.io.FileInputStream fis = new java.io.FileInputStream(adj)) {
                                zos.putNextEntry(new java.util.zip.ZipEntry("media/" + adj.getName()));
                                fis.transferTo(zos);
                                zos.closeEntry();
                            }
                        } else {
                            // Si el archivo no existe, agregamos un aviso de texto
                            zos.putNextEntry(new java.util.zip.ZipEntry("media/FALTANTE_" + m.getNombreAdjunto() + ".txt"));
                            String aviso = "El archivo \"" + m.getNombreAdjunto() + "\" no se encontró en la carpeta media.";
                            zos.write(aviso.getBytes());
                            zos.closeEntry();
                        }
                    }
                }

                // Agregar un pequeño README informativo
                zos.putNextEntry(new java.util.zip.ZipEntry("README.txt"));
                String info = "Conversación exportada desde la aplicación de mensajería.\n" +
                        "Participantes: " + usuarioActual.getUsername() + " y " + usuarioDestino.getUsername() + "\n" +
                        "Total de mensajes: " + mensajes.size() + "\n" +
                        "Adjuntos: " + mensajes.stream().filter(m -> m.getRutaAdjunto() != null && !m.getRutaAdjunto().isEmpty()).count() + "\n";
                zos.write(info.getBytes());
                zos.closeEntry();
            }

            mostrarAlerta("Exportación completada",
                    "Conversación y adjuntos exportados en:\n" + zipFile.getAbsolutePath(),
                    Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo exportar la conversación: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void adjuntarArchivo(Usuario usuarioDestino) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Seleccionar archivo adjunto");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos válidos", "*.jpg", "*.png", "*.pdf", "*.txt")
        );

        File archivo = chooser.showOpenDialog(btnAdjuntar.getScene().getWindow());
        if (archivo == null) return;

        // Validar archivo
        if (!es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.utils.AdjuntoUtils.validarAdjunto(archivo)) {
            mostrarAlerta(
                    "Archivo no válido",
                    "Solo se permiten imágenes, PDFs o textos menores de 10 MB.",
                    Alert.AlertType.WARNING
            );
            return;
        }

        try {
            // Guardar archivo en carpeta /media/
            String ruta = es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.utils.AdjuntoUtils.guardarAdjunto(archivo);

            // Crear mensaje con referencia al adjunto
            Mensaje mensaje = new Mensaje(
                    usuarioActual.getUsername(),
                    usuarioDestino.getUsername(),
                    "",
                    ruta
            );

            mensaje.setNombreAdjunto(archivo.getName());
            mensaje.setTipoAdjunto(Files.probeContentType(archivo.toPath()));
            mensaje.setTamanoAdjunto(archivo.length());

            // Guardar mensaje en XML
            mensajesRepo.agregarMensaje(mensaje);

            // Mostrar en la interfaz
            mostrarMensajeEnChat(mensaje);

        } catch (Exception ex) {
            mostrarAlerta(
                    "Error al adjuntar archivo",
                    "No se pudo guardar el archivo: " + ex.getMessage(),
                    Alert.AlertType.ERROR
            );
        }
    }


    @FXML
    private void handleAgregarContacto() {
        System.out.println("Función 'Agregar contacto' en desarrollo.");
    }
    
    /**
     * Refresca la lista de usuarios cargándolos nuevamente desde el repositorio.
     * Esto actualiza las fotos de perfil y datos de todos los usuarios en la lista.
     */
    private void refrescarListaUsuarios() {
        listaUsuarios.setItems(FXCollections.observableArrayList(repo.leerUsuarios().getListaUsuarios()));
    }
    
    @FXML
    private void handleCambiarFoto() {
        try {
            // Crear el selector de archivos
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar imagen de perfil");
            
            // Filtrar solo archivos de imagen
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Archivos de imagen", "*.png", "*.jpg", "*.jpeg", "*.gif");
            fileChooser.getExtensionFilters().add(extFilter);
            
            // Mostrar diálogo de selección de archivo
            File archivoSeleccionado = fileChooser.showOpenDialog(panelCentral.getScene().getWindow());
            
            if (archivoSeleccionado != null) {
                // Crear la carpeta de uploads si no existe
                File uploadsDir = new File("src/main/resources/uploads");
                if (!uploadsDir.exists()) {
                    uploadsDir.mkdirs();
                }
                
                // Generar un nombre de archivo único para evitar colisiones
                String nombreArchivo = "user_" + usuarioActual.getId() + "_" + System.currentTimeMillis() + "." + 
                    archivoSeleccionado.getName().substring(archivoSeleccionado.getName().lastIndexOf(".") + 1);
                
                // Ruta de destino en la carpeta uploads
                File destino = new File(uploadsDir, nombreArchivo);
                
                // Copiar el archivo a la carpeta de uploads
                try {
                    Files.copy(archivoSeleccionado.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    mostrarAlerta("Error", "No se pudo copiar el archivo a la carpeta de uploads.", Alert.AlertType.ERROR);
                    e.printStackTrace();
                    return;
                }
                
                // Actualizar la imagen de perfil en la interfaz
                Image nuevaImagen = new Image(destino.toURI().toString());
                imgPerfil.setImage(nuevaImagen);
                
                // Guardar la ruta absoluta del archivo en la base de datos
                usuarioActual.setFotoPath(destino.getAbsolutePath());
                
                // Actualizar el usuario en la base de datos
                repo.actualizarUsuario(usuarioActual);
                
                // Refrescar la lista de usuarios para mostrar la nueva foto
                refrescarListaUsuarios();
                
                // Mostrar mensaje de éxito
                mostrarAlerta("Foto actualizada", "La foto de perfil se ha actualizado correctamente.", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            // Mostrar mensaje de error
            mostrarAlerta("Error", "No se pudo cargar la imagen seleccionada.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    @FXML
    private void volverAlMenu() {
        // Limpiar la selección de la lista de usuarios primero
        listaUsuarios.getSelectionModel().clearSelection();
        
        // Ocultar el chat y mostrar el menú principal
        chatBox.setVisible(false);
        chatBox.setManaged(false);
        menuInicial.setVisible(true);
        menuInicial.setManaged(true);
        
        // Ocultar botones de chat
        btnVolverMenu.setVisible(false);
        btnVolverMenu.setManaged(false);
        btnResumen.setVisible(false);
        btnResumen.setManaged(false);
        btnExportar.setVisible(false);
        btnExportar.setManaged(false);
        btnAdjuntar.setVisible(false);
        btnAdjuntar.setManaged(false);
        boxResumen.setVisible(false);
        boxResumen.setManaged(false);
        
        // Actualizar el título del panel derecho
        lblTituloPanelDerecho.setText("Tu perfil");
        
        // Mostrar información del usuario actual
        lblNombreUsuario.setText("Nombre: " + usuarioActual.getNombreCompleto());
        lblEmail.setText("Email: " + usuarioActual.getEmail());
        
        if (usuarioActual.getFechaRegistro() != null) {
            lblFechaRegistro.setText("Miembro desde: " + usuarioActual.getFechaRegistro().toLocalDate().toString());
        } else {
            lblFechaRegistro.setText("Miembro desde: (desconocido)");
        }
        
        if (usuarioActual.getUltimaSesion() != null) {
            lblUltimaSesion.setText("Última sesión: " +
                String.format("%02d/%02d/%04d %02d:%02d",
                    usuarioActual.getUltimaSesion().getDayOfMonth(),
                    usuarioActual.getUltimaSesion().getMonthValue(),
                    usuarioActual.getUltimaSesion().getYear(),
                    usuarioActual.getUltimaSesion().getHour(),
                    usuarioActual.getUltimaSesion().getMinute()));
        } else {
            lblUltimaSesion.setText("Última sesión: primera vez");
        }
        
        // Cargar la imagen de perfil del usuario actual
        cargarImagenPerfil(usuarioActual);
    }
    
    @FXML
    private void handleCerrarSesion() {
        try {
            // Mostrar diálogo de confirmación
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cerrar sesión");
            alert.setHeaderText("¿Estás seguro de que quieres cerrar sesión?");
            alert.setContentText("Se cerrará la aplicación.");
            
            // Configurar botones personalizados
            ButtonType buttonTypeYes = new ButtonType("Sí, salir");
            ButtonType buttonTypeNo = new ButtonType("No, cancelar");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            
            // Mostrar diálogo y procesar respuesta
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == buttonTypeYes) {
                    // Cerrar la ventana actual
                    btnCerrarSesion.getScene().getWindow().hide();
                }
            });
        } catch (Exception e) {
            // Mostrar mensaje de error si algo falla
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Error al cerrar sesión");
            errorAlert.setContentText("No se pudo cerrar la sesión correctamente.");
            errorAlert.showAndWait();
            e.printStackTrace();
        }
    }
}
