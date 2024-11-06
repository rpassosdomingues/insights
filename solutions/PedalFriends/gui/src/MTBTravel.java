import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MTBTravel extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();

        Label titleLabel = new Label("Gerenciamento de Viagens MTB");

        root.getChildren().add(titleLabel);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Gerenciamento de Viagens MTB");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
