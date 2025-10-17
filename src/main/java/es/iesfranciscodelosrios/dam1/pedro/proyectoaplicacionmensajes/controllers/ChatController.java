package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.controllers;

import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model.Mensaje;
import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.persistence.MensajesRepository;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.text.Font;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatController {

    @FXML private VBox chatMessagesBox;
    @FXML private TextField txtMensaje;
    @FXML private Button btnEnviar;
    @FXML private ScrollPane scrollChat;

    private String usuarioDestino;
    private String usuarioActual;
    private final MensajesRepository repo = new MensajesRepository();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void setUsuarioDestino(String username) {
        this.usuarioDestino = username;
        cargarConversacion();
    }

    public void setUsuarioActual(String username) {
        this.usuarioActual = username;
    }

    @FXML
    private void initialize() {
        // autoscroll cuando cambie la altura
        chatMessagesBox.heightProperty().addListener((obs, oldV, newV) -> scrollChat.setVvalue(1.0));

        btnEnviar.setOnAction(e -> handleEnviarMensaje());
    }

    private void cargarConversacion() {
        chatMessagesBox.getChildren().clear();
        if (usuarioActual == null || usuarioDestino == null) return;

        List<Mensaje> msgs = repo.obtenerConversacion(usuarioActual, usuarioDestino);
        for (Mensaje m : msgs) {
            boolean esPropio = m.getRemitente().equalsIgnoreCase(usuarioActual);
            agregarMensajeALaVista(m.getTexto(), esPropio, m.getTimestamp().format(fmt));
        }
    }

    private void agregarMensajeALaVista(String texto, boolean esPropio, String fechaLabel) {
        Label lbl = new Label(texto + "\n" + fechaLabel);
        lbl.setWrapText(true);
        lbl.setFont(Font.font("Segoe UI", 14));

        HBox cont = new HBox(lbl);
        cont.setAlignment(esPropio ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        chatMessagesBox.getChildren().add(cont);
    }

    private void handleEnviarMensaje() {
        String texto = txtMensaje.getText().trim();
        if (texto.isEmpty()) return;

        // 1) Crear mensaje y guardarlo en XML
        Mensaje m = new Mensaje(usuarioActual, usuarioDestino, texto, null); // null = sin adjunto por ahora
        repo.agregarMensaje(m);

        // 2) Añadir a la vista (propio)
        agregarMensajeALaVista(texto, true, m.getTimestamp().format(fmt));

        // 3) limpiar campo
        txtMensaje.clear();

        // autoscroll al final (por si)
        Platform.runLater(() -> scrollChat.setVvalue(1.0));
    }
}
