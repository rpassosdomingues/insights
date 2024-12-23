package src;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class SearchKeyPractices {

    private static List<String> selectedTags = new ArrayList<>();

    /**
     * Creates a tag selection panel for selecting tags.
     *
     * @return A VBox containing the tag selection controls.
     */
    public static VBox createTagSelectionPanel() {
        VBox tagPanel = new VBox(10);
        tagPanel.setAlignment(Pos.TOP_LEFT);

        List<String> tags = TagSearch.getAllTags();

        // GridPane para organizar os checkboxes em colunas
        GridPane grid = new GridPane();
        grid.setHgap(10);  // Espaçamento horizontal entre as colunas
        grid.setVgap(10);  // Espaçamento vertical entre as linhas

        // Cria os checkboxes e os adiciona ao GridPane
        int row = 0, col = 0;
        for (String tag : tags) {
            CheckBox checkBox = new CheckBox(tag);
            grid.add(checkBox, col, row);

            // Muda para a próxima coluna após 3 checkboxes
            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }

        // Botão para pesquisar as práticas chave associadas às tags selecionadas
        Button searchKeyPracticesButton = new Button("Search Key Practices");
        searchKeyPracticesButton.setOnAction(e -> {
            selectedTags.clear();
            grid.getChildren().stream()
                .filter(node -> node instanceof CheckBox && ((CheckBox) node).isSelected())
                .map(node -> ((CheckBox) node).getText())
                .forEach(selectedTags::add);

            // Busca as práticas chave associadas às tags selecionadas
            List<Praticas> foundPractices = TagSearch.findPracticesByTags(selectedTags);
            System.out.println("Key Practices associated with selected tags: " + foundPractices);

            // Aqui você pode implementar a lógica para exibir as práticas encontradas na interface gráfica
        });

        tagPanel.getChildren().addAll(grid, searchKeyPracticesButton);
        return tagPanel;
    }

    /**
     * Toggles the visibility and state of the buttons.
     * This method will either enable or disable the provided buttons based on the given state.
     *
     * @param state   Whether to enable or disable the buttons.
     * @param buttons The buttons to toggle.
     */
    public static void toggleButtons(boolean state, Button... buttons) {
        for (Button button : buttons) {
            button.setDisable(!state); // Define o estado habilitado/desabilitado
        }
    }
}
