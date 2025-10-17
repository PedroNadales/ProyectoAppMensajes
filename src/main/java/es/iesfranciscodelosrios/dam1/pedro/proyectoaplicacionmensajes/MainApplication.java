package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes;


import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model.Usuario;
import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.persistence.UsuarioRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        // Cargar el archivo FXML desde el paquete ui
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/es/iesfranciscodelosrios/dam1/pedro/proyectoaplicacionmensajes/ui/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Cargar BootstrapFX y CSS
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
        scene.getStylesheets().add(getClass().getResource("/es/iesfranciscodelosrios/dam1/pedro/proyectoaplicacionmensajes/ui/styles.css").toExternalForm());

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        var repo = new UsuarioRepository();
        if (repo.buscarUsuario("admin") == null) {
            repo.agregarUsuario(new Usuario("admin", "1234", "Administrador", "admin@example.com"));
            System.out.println("Usuario admin creado en usuarios.xml");
        }
        launch();
    }
}
