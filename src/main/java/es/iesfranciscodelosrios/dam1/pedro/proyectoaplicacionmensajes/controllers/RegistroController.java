package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.controllers;

import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model.Usuario;
import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.persistence.UsuarioRepository;
import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.utils.ScreenUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegistroController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtNombreCompleto;
    @FXML private TextField txtEmail;

    private final UsuarioRepository usuarioRepo = new UsuarioRepository();

    /**
     * Maneja el evento de registro de un usuario.
     * Valida los campos de usuario, contraseña, nombre completo y correo electrónico.
     * Si los campos son válidos, crea un nuevo usuario y lo agrega a la base de datos.
     * Si el nombre de usuario ya existe, muestra un mensaje de error.
     * Si el registro es exitoso, muestra un mensaje de confirmación y abre la pantalla principal con el nuevo usuario.
     */
    @FXML
    private void handleRegistro() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String nombre = txtNombreCompleto.getText().trim();
        String email = txtEmail.getText().trim();

        if (username.isEmpty() || password.isEmpty() || nombre.isEmpty() || email.isEmpty()) {
            showAlert("Error", "Todos los campos son obligatorios", Alert.AlertType.WARNING);
            return;
        }
        
        // Validar formato de correo electrónico
        if (!email.matches("^[^@]+@[^@]+\\.[^@]+$")) {
            showAlert("Error", "El formato del correo electrónico no es válido. Debe contener '@' y un dominio válido (ejemplo@dominio.com)", Alert.AlertType.WARNING);
            return;
        }

        if (usuarioRepo.buscarUsuario(username) != null) {
            showAlert("Error", "El nombre de usuario ya existe", Alert.AlertType.ERROR);
            return;
        }

        Usuario nuevo = new Usuario(username, password, nombre, email);
        usuarioRepo.agregarUsuario(nuevo);

        showAlert("Registro exitoso", "Usuario creado correctamente", Alert.AlertType.INFORMATION);
        abrirPantallaPrincipal(nuevo);

    }

    /**
     * Abre la pantalla principal de la aplicación con el usuario actual.
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
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setTitle("Chat - " + usuario.getUsername());
            ScreenUtils.setupFullScreen(stage, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Vuelve a la pantalla de inicio de sesión.
     * Carga la pantalla de inicio de sesión desde el archivo FXML y aplica los estilos CSS
     * correspondientes. Configura la pantalla completa y establece el título de la pantalla
     * como "Inicio de Sesión".
     */
    @FXML
    private void volverLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/es/iesfranciscodelosrios/dam1/pedro/proyectoaplicacionmensajes/ui/login.fxml"));
            Scene scene = new Scene(loader.load());

            // Aplicar estilos
            scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
            scene.getStylesheets().add(getClass().getResource(
                "/es/iesfranciscodelosrios/dam1/pedro/proyectoaplicacionmensajes/ui/styles.css"
            ).toExternalForm());

            // Configurar pantalla completa
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setTitle("Inicio de Sesión");
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
