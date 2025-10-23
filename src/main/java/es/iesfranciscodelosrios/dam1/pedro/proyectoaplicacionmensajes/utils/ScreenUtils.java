package es.iesfranciscodelosrios.dam1.pedro.proyectoaplicacionmensajes.utils;

import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ScreenUtils {
    
    /**
     * Configura la pantalla completa para un Stage
     * @param stage El Stage que se configurará en pantalla completa
     * @param scene La escena que se mostrará en el Stage
     */
    public static void setupFullScreen(Stage stage, Scene scene) {
        // Aplicar estilos personalizados
        scene.getStylesheets().add(ScreenUtils.class.getResource(
            "/es/iesfranciscodelosrios/dam1/pedro/proyectoaplicacionmensajes/ui/fullscreen-hint.css"
        ).toExternalForm());
        
        // Configurar pantalla completa
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(null); // Deshabilitar atajo de teclado
        
        // Crear y configurar el mensaje personalizado
        Label hintLabel = new Label("Presiona ESC para salir de pantalla completa");
        StackPane overlay = new StackPane(hintLabel);
        overlay.getStyleClass().add("fullscreen-overlay");
        StackPane.setAlignment(hintLabel, javafx.geometry.Pos.TOP_CENTER);
        
        // Añadir el mensaje a la escena
        if (scene.getRoot() instanceof StackPane) {
            ((StackPane) scene.getRoot()).getChildren().add(overlay);
        } else {
            StackPane root = new StackPane(scene.getRoot(), overlay);
            scene.setRoot(root);
        }
        
        // Temporizador para ocultar el mensaje después de 3 segundos
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            hintLabel.getStyleClass().add("hidden");
            PauseTransition removeDelay = new PauseTransition(Duration.seconds(0.5));
            removeDelay.setOnFinished(e -> {
                if (scene.getRoot() instanceof StackPane) {
                    ((StackPane) scene.getRoot()).getChildren().remove(overlay);
                }
            });
            removeDelay.play();
        });
        delay.play();
        
        // Manejar la tecla ESC
        scene.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.isShiftDown() && event.getCode() == javafx.scene.input.KeyCode.F) {
                stage.setFullScreen(!stage.isFullScreen());
            } else if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                stage.setFullScreen(false);
            }
        });
    }
}
