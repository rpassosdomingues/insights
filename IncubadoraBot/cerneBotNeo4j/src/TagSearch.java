package src;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagSearch {

    // Mapa para armazenar as tags e suas práticas associadas
    private static final Map<String, List<Praticas>> tagToPracticesMap = new HashMap<>();

    // Lista para armazenar as tags selecionadas
    private static List<String> selectedTags = new ArrayList<>();

    // Inicializando o mapa com as tags e práticas associadas
    static {
        for (Praticas pratica : Praticas.values()) {
            for (String tag : pratica.getTags()) {
                tagToPracticesMap.computeIfAbsent(tag, k -> new ArrayList<>()).add(pratica);
            }
        }
    }

    // Método para encontrar práticas associadas às tags selecionadas
    public static List<Praticas> findPracticesByTags(List<String> selectedTags) {
        List<Praticas> result = new ArrayList<>();
        for (String tag : selectedTags) {
            List<Praticas> praticas = tagToPracticesMap.get(tag);
            if (praticas != null) {
                result.addAll(praticas);
            }
        }
        return result;
    }

    // Método para obter todas as tags
    public static List<String> getAllTags() {
        return new ArrayList<>(tagToPracticesMap.keySet());
    }

    // Método que lida com a seleção de uma tag (adiciona ou remove da lista de tags selecionadas)
    public static void handleTagSelection(Button button, String tag) {
        if (selectedTags.contains(tag)) {
            selectedTags.remove(tag);  // Deselect tag if already selected
            button.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 10;");
        } else {
            selectedTags.add(tag);  // Add tag to the selected list
            button.setStyle("-fx-background-color: #90CAF9; -fx-padding: 10;");  // Change color to indicate selection
        }
        // Exibe a lista de tags selecionadas no console (para uso posterior)
        System.out.println("Tags selecionadas: " + selectedTags);
    }

    // Exibição de tags como botões na interface gráfica (usando JavaFX)
    public static void displayTagButtons(Stage primaryStage) {
        FlowPane flowPane = new FlowPane();

        // Criando um botão para cada tag
        for (String tag : getAllTags()) {
            Button button = new Button(tag);
            button.setFont(new Font(14));
            button.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 10;");
            button.setOnAction(e -> handleTagSelection(button, tag));
            flowPane.getChildren().add(button);
        }

        // Configurando o layout e a cena
        Scene scene = new Scene(flowPane, 400, 300);
        primaryStage.setTitle("Seleção de Tags");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Main para rodar a aplicação JavaFX
    public static void main(String[] args) {
        Application.launch(args);
    }
}
