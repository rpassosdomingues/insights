import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IncubadoraApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Sistema de Gerenciamento de Incubadora");

        Button reservasButton = new Button("Reservar uma sala");
        Button visitasButton = new Button("Agendar Visita");
        Button pecasButton = new Button("Solicitar Peças do Maker");

        VBox vbox = new VBox(reservasButton, visitasButton, pecasButton);
        Scene scene = new Scene(vbox, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
