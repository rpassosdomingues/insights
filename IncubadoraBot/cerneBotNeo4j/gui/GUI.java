package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {
    private Neo4jConnector neo4jConnector;

    @Override
    public void start(Stage primaryStage) {
        neo4jConnector = new Neo4jConnector("bolt://localhost:7687", "username", "password");

        TextField searchField = new TextField();
        searchField.setPromptText("Digite a palavra para buscar");

        Button searchButton = new Button("Buscar");
        searchButton.setOnAction(e -> {
            String keyword = searchField.getText();
            String result = neo4jConnector.searchWords(keyword);
            System.out.println("Resultado da busca: " + result);
        });

        VBox vbox = new VBox(searchField, searchButton);
        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setTitle("Busca de Palavras");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
