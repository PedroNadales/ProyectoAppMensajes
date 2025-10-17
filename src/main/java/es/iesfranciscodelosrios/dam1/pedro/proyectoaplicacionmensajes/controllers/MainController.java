package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.controllers;

import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model.Usuario;
import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model.Usuarios;
import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.persistence.UsuarioRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

public class MainController {

    @FXML private ListView<Usuario> listaUsuarios;
    @FXML private VBox panelDerecho;
    @FXML private VBox panelCentral;
    @FXML private Label lblTituloPanelDerecho;
    @FXML private Label lblNombreUsuario;
    @FXML private Label lblEmail;
    @FXML private Button btnAgregarContacto;
    @FXML private ImageView imgPerfil;
    @FXML private Label lblFechaRegistro;
    @FXML private Label lblUltimaSesion;
    @FXML private VBox menuInicial;
    @FXML private VBox chatBox;
    @FXML private VBox chatMensajes;
    @FXML private TextField txtMensaje;
    @FXML private Button btnEnviar;



    private final UsuarioRepository repo = new UsuarioRepository();
    private Usuario usuarioActual; // El usuario logueado

    @FXML
    public void initialize() {
        // Rellenar la lista con los usuarios del XML
        listaUsuarios.setItems(FXCollections.observableArrayList(repo.leerUsuarios().getListaUsuarios()));
        //TODO: REVISAR ESTO
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
                lblNombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                lblEmail = new Label();
                lblEmail.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");

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

                    // Cargar foto de perfil
                    Image img;
                    try {
                        if (usuario.getFotoPath() != null && !usuario.getFotoPath().isEmpty()) {
                            img = new Image(new FileInputStream(usuario.getFotoPath()), 40, 40, true, true);
                        } else {
                            // Foto por defecto
                            img = new Image(getClass().getResourceAsStream("/assets/default-avatar.png"), 40, 40, true, true);
                        }
                    } catch (Exception e) {
                        // En caso de error, usar foto por defecto
                        img = new Image(getClass().getResourceAsStream("/assets/default-avatar.png"), 40, 40, true, true);
                    }

                    imageView.setImage(img);
                    setGraphic(content);
                }
            }
        });



        // Mostrar la info del usuario al hacer clic
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

        try {
            imgPerfil.setImage(new javafx.scene.image.Image(
                    getClass().getResource(usuario.getFotoPath()).toExternalForm()
            ));
        } catch (Exception e) {
            imgPerfil.setImage(new javafx.scene.image.Image(
                    getClass().getResource("/assets/default-avatar.png").toExternalForm()
            ));
        }
    }


    private void mostrarInfoUsuario(Usuario u) {
        // Ocultar el título si no es tu perfil
        if (u.equals(usuarioActual)) {
            lblTituloPanelDerecho.setVisible(true); // o setOpacity(1)
        } else {
            lblTituloPanelDerecho.setVisible(false); // o setOpacity(0)
        }
        lblNombreUsuario.setText("Chat con: " + u.getNombreCompleto());
        lblEmail.setText("Email: " + u.getEmail());
            // Cargar chat en el panel central
        // Mostrar chat
        if (!u.equals(usuarioActual)) {
            menuInicial.setVisible(false);
            menuInicial.setManaged(false);

            chatBox.setVisible(true);
            chatBox.setManaged(true);
            chatMensajes.getChildren().clear(); // Limpiar chat si quieres
        } else {
            menuInicial.setVisible(true);
            menuInicial.setManaged(true);

            chatBox.setVisible(false);
            chatBox.setManaged(false);
        }
    }


    @FXML
    private void handleAgregarContacto() {
        // Por ahora, solo mostramos un mensaje o alerta (luego se implementará)
        System.out.println("Función 'Agregar contacto' en desarrollo.");
    }
}
