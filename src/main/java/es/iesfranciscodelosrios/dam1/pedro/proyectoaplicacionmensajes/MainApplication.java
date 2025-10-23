package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes;


import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.model.Usuario;
import es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.persistence.UsuarioRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.input.KeyCode;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

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
        
        // Aplicar estilos personalizados al mensaje de pantalla completa
        scene.getStylesheets().add(getClass().getResource(
            "/es/iesfranciscodelosrios/dam1/pedro/proyectoaplicacionmensajes/ui/fullscreen-hint.css"
        ).toExternalForm());
        
        // Configurar pantalla completa
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(javafx.scene.input.KeyCombination.NO_MATCH);
        stage.setFullScreenExitHint("");
        
        // Crear y configurar el mensaje personalizado
        Label hintLabel = new Label("Presiona ESC para salir de pantalla completa");
        StackPane overlay = new StackPane(hintLabel);
        overlay.getStyleClass().add("fullscreen-overlay");
        StackPane.setAlignment(hintLabel, javafx.geometry.Pos.TOP_CENTER);
        
        // Añadir el mensaje a la escena
        ((StackPane) scene.getRoot()).getChildren().add(overlay);
        
        // Temporizador para ocultar el mensaje después de 3 segundos
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            // Aplicar la clase 'hidden' para iniciar la animación de desvanecimiento
            hintLabel.getStyleClass().add("hidden");
            // Eliminar el mensaje después de que termine la animación
            PauseTransition removeDelay = new PauseTransition(Duration.seconds(0.5));
            removeDelay.setOnFinished(e -> {
                ((StackPane) scene.getRoot()).getChildren().remove(overlay);
            });
            removeDelay.play();
        });
        delay.play();
        
        // Manejar la tecla ESC
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                stage.setFullScreen(false);
            }
        });
        
        stage.show();
    }


    public static void main(String[] args) {
        var repo = new UsuarioRepository();
        // Creación de usuario admin si no existe para hacer pruebas
        if (repo.buscarUsuario("admin") == null) {
            repo.agregarUsuario(new Usuario("admin", "1234", "Administrador", "admin@example.com"));
            System.out.println("Usuario admin creado en usuarios.xml");
        }
        launch();
    }
}
