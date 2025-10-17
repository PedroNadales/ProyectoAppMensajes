package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ChatController {

    @FXML private ListView<String> listaMensajes;
    @FXML private TextField txtMensaje;
    @FXML private Button btnEnviar;

    private String usuarioDestino;

    public void setUsuarioDestino(String username) {
        this.usuarioDestino = username;
        listaMensajes.setItems(FXCollections.observableArrayList()); // cargar mensajes de ser necesario
    }

    @FXML
    private void enviarMensaje() {
        String texto = txtMensaje.getText().trim();
        if (!texto.isEmpty()) {
            listaMensajes.getItems().add("Tú: " + texto); // agregar al ListView
            txtMensaje.clear();
            // TODO: Aquí se guardaría o enviaría el mensaje
        }
    }
}
