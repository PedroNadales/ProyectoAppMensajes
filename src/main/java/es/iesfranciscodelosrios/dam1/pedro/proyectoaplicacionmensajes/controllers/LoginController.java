package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.controllers;

import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model.Usuario;
import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.persistence.UsuarioRepository;
import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.utils.ScreenUtils;
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

    /**
     * Inicializa el controlador de la pantalla de login.
     * Establece la imagen del logo y permite hacer clic en el enlace
     * "¿No tienes una cuenta?" para abrir la pantalla de registro.
     */
    @FXML
    public void initialize() {
        // Cargar imagen desde classpath
        Image img = new Image(getClass().getResource("/assets/logo.png").toExternalForm());
        logo.setImage(img);
        //Permitir hacer clic en ¿No tienes una cuenta?
        lblcrearcuenta.setOnMouseClicked(event -> abrirRegistro());
    }

    /**
     * Abre la pantalla de registro de usuario.
     * Carga la pantalla de registro desde el archivo FXML y aplica los estilos CSS
     * correspondientes. Configura la pantalla completa y establece el título de la pantalla
     * como "Registro de Usuario".
     */
    private void abrirRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/es/iesfranciscodelosrios/dam1/pedro/proyectoaplicacionmensajes/ui/registro.fxml"));
            Scene scene = new Scene(loader.load());

            // Aplicar estilos
            scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
            scene.getStylesheets().add(getClass().getResource(
                "/es/iesfranciscodelosrios/dam1/pedro/proyectoaplicacionmensajes/ui/styles.css"
            ).toExternalForm());

            // Configurar pantalla completa
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setTitle("Registro de Usuario");
            ScreenUtils.setupFullScreen(stage, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de inicio de sesión.
     * Valida los campos de usuario y contraseña, y si están vacíos, muestra un mensaje de alerta.
     * Si los campos son válidos, verifica la autenticidad del usuario y contraseña.
     * Si la autenticidad es válida, actualiza la última sesión del usuario y abre la pantalla principal.
     * Si la autenticidad es inválida, muestra un mensaje de error.
     */
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

    /**
     * Abre la pantalla principal de la aplicación.
     * Carga la pantalla principal desde el archivo FXML y aplica los estilos CSS
     * correspondientes. Configura la pantalla completa y establece el título de la pantalla
     * como "Chat - [nombre_usuario]" con el nombre del usuario actual.
     * @param usuario El usuario actual que se va a mostrar en la pantalla principal.
     */
    private void abrirPantallaPrincipal(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/es/iesfranciscodelosrios/dam1/pedro/proyectoaplicacionmensajes/ui/main-view.fxml"));
            Scene scene = new Scene(loader.load());

            // Aplicar estilos
            scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
            scene.getStylesheets().add(getClass().getResource(
                "/es/iesfranciscodelosrios/dam1/pedro/proyectoaplicacionmensajes/ui/styles.css"
            ).toExternalForm());

            // Configurar el controlador
            MainController controller = loader.getController();
            controller.setUsuarioActual(usuario);

            // Configurar pantalla completa
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setTitle("Chat - " + usuario.getUsername());
            ScreenUtils.setupFullScreen(stage, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Muestra un diálogo de alerta con un título y un mensaje.
     * El diálogo se muestra con un tipo de alerta determinado por el parámetro type.
     * @param title El título del diálogo
     * @param msg El mensaje del diálogo
     * @param type El tipo de alerta (INFORMATION, WARNING, ERROR)
     */
    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
