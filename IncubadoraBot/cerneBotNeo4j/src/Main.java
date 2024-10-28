package src;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main extends Application {
    private Driver driver;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        connectToDatabase(); // Conectar ao Neo4j

        TextField searchField = new TextField();
        searchField.setPromptText("Digite a palavra para buscar");

        Button loadButton = new Button("Carregar CSV");
        Button searchButton = new Button("Buscar");
        Label resultLabel = new Label();

        loadButton.setOnAction(e -> loadCSV(primaryStage));

        searchButton.setOnAction(e -> {
            String keyword = searchField.getText();
            String result = searchWords(keyword);
            resultLabel.setText("Resultado da busca: " + result);
        });

        VBox vbox = new VBox(loadButton, searchField, searchButton, resultLabel);
        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setTitle("Busca de Palavras");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void connectToDatabase() {
        // Conectar ao Neo4j
        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("username", "password"));
    }

    private void loadCSV(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        java.io.File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                try (Session session = driver.session()) {
                    while ((line = br.readLine()) != null) {
                        String[] words = line.split(","); // Supondo que as palavras estão separadas por vírgula
                        for (String word : words) {
                            // Cria um nó para cada palavra no Neo4j
                            session.run("CREATE (:Word {name: $name})", org.neo4j.driver.Values.parameters("name", word.trim()));
                        }
                    }
                    System.out.println("Arquivo CSV carregado com sucesso!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String searchWords(String keyword) {
        try (Session session = driver.session()) {
            // Realiza a busca por palavras no Neo4j
            var result = session.run("MATCH (w:Word {name: $name}) RETURN w.name AS name", org.neo4j.driver.Values.parameters("name", keyword));
            if (result.hasNext()) {
                return "Palavra encontrada: " + result.single().get("name").asString();
            } else {
                return "Palavra não encontrada.";
            }
        }
    }

    @Override
    public void stop() {
        driver.close(); // Fecha a conexão com o banco ao encerrar a aplicação
    }
}
