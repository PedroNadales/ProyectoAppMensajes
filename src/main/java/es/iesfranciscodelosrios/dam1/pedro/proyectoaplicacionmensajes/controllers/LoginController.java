package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.controllers;

import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model.Usuario;
import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.persistence.UsuarioRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private ImageView logo;
    @FXML private Label lblcrearcuenta;


    private final UsuarioRepository usuarioRepo = new UsuarioRepository();

    @FXML
    public void initialize() {
        // Cargar imagen desde classpath
        Image img = new Image(getClass().getResource("/assets/logo.png").toExternalForm());
        logo.setImage(img);
        //Permitir hacer clic en ¿No tienes una cuenta?
        lblcrearcuenta.setOnMouseClicked(event -> abrirRegistro());
    }

    private void abrirRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/iesfranciscodelosrios/dam1/pedro/proyectoaplicacionmensajes/ui/registro.fxml"));
            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
            scene.getStylesheets().add(getClass().getResource(
                    "/es/iesfranciscodelosrios/dam1/pedro/proyectoaplicacionmensajes/ui/styles.css"
            ).toExternalForm());


            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogin() {
        String user = txtUsuario.getText().trim();
        String pass = txtPassword.getText().trim();
        Boolean valido = usuarioRepo.validarLogin(user, pass);
        if (user.isEmpty() || pass.isEmpty()) {
            showAlert("Campos vacíos", "Introduce usuario y contraseña", Alert.AlertType.WARNING);
            return;
        }

        if (valido) {
            Usuario usuario = usuarioRepo.buscarUsuario(user);
            // Actualizar la última sesión del usuario
            usuarioRepo.actualizarUltimaSesion(user);
            abrirPantallaPrincipal(usuario);
        } else {
            showAlert("Error", "Usuario o contraseña incorrectos", Alert.AlertType.ERROR);
        }
    }

    private void abrirPantallaPrincipal(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/es/iesfranciscodelosrios/dam1/pedro/proyectoaplicacionmensajes/ui/main-view.fxml"));
            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
            scene.getStylesheets().add(getClass().getResource(
                    "/es/iesfranciscodelosrios/dam1/pedro/proyectoaplicacionmensajes/ui/styles.css"
            ).toExternalForm());


            MainController controller = loader.getController();
            controller.setUsuarioActual(usuario);

            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Chat - " + usuario.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
