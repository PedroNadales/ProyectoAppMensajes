package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.controllers;

import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model.Usuario;
import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.persistence.UsuarioRepository;
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

        if (usuarioRepo.buscarUsuario(username) != null) {
            showAlert("Error", "El nombre de usuario ya existe", Alert.AlertType.ERROR);
            return;
        }

        Usuario nuevo = new Usuario(username, password, nombre, email);
        usuarioRepo.agregarUsuario(nuevo);

        showAlert("Registro exitoso", "Usuario creado correctamente", Alert.AlertType.INFORMATION);
        abrirPantallaPrincipal(nuevo);

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

            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Chat - " + usuario.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void volverLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/iesfranciscodelosrios/dam1/pedro/proyectoaplicacionmensajes/ui/login.fxml"));
            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
            scene.getStylesheets().add(getClass().getResource(
                    "/es/iesfranciscodelosrios/dam1/pedro/proyectoaplicacionmensajes/ui/styles.css"
            ).toExternalForm());

            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setScene(scene);
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
