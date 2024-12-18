package src;

import org.neo4j.driver.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

import src.Praticas;

/**
 * Main application class for the JavaFX application that interacts with a Neo4j database
 * to add, remove, and search for words. It also allows the user to select tags and display
 * practices associated with those tags.
 */
public class Main extends Application {
    // Declare the 'state' variable at the class level
    private boolean state = true;  // This will enable buttons by default

    // Declare selectedTags como uma variável de classe (para que esteja acessível em toda a classe)
    private static List<String> selectedTags = new ArrayList<>();

    private List<Praticas> searchKeyPractices(List<String> selectedTags) {
        // Chama o método findByTags da classe Praticas para encontrar as práticas associadas às tags selecionadas
        return Praticas.findByTags(selectedTags);
    }
    /**
     * Neo4j Database Connector Class
     * This class manages the connection to the Neo4j database.
     */
    static class Neo4jConnector {
        private final Driver driver;

        /**
         * Constructor to initialize the connection to the Neo4j database.
         *
         * @param uri  URI for the Neo4j database connection (e.g., bolt://localhost:7687).
         * @param user The username for the Neo4j database authentication.
         */
        public Neo4jConnector(String uri, String user) {
            // Retrieve the Neo4j password from the environment variable
            String password = System.getenv("NEO4J_PASSWORD");
            if (password == null) {
                throw new IllegalStateException("NEO4J_PASSWORD environment variable is not set!");
            }
            // Create the driver to connect to Neo4j
            this.driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        }

        /**
         * Returns a session for interacting with the Neo4j database.
         *
         * @return A session to interact with the database.
         */
        public Session getSession() {
            return driver.session();
        }

        /**
         * Closes the connection to the Neo4j database.
         */
        public void close() {
            driver.close();
        }
    }

    /**
     * Adds a word to the Neo4j database.
     *
     * @param neo4jConnector The Neo4jConnector instance to interact with the database.
     * @param word           The word to add to the database.
     * @param resultLabel    The label where the result message will be displayed.
     */
    private void addWord(Neo4jConnector neo4jConnector, String word, Label resultLabel) {
        try (Session session = neo4jConnector.getSession()) {
            word = word.toLowerCase();  // Ensure case-insensitivity for word comparison

            // Query to check if the word already exists in the database
            String queryCheck = "MATCH (w:Word {name: $name}) RETURN w";
            Result resultCheck = session.run(queryCheck, Values.parameters("name", word));

            if (resultCheck.hasNext()) {
                resultLabel.setText("The word '" + word + "' already exists.");
            } else {
                // Add the word if it doesn't already exist
                String queryAdd = "CREATE (w:Word {name: $name})";
                session.run(queryAdd, Values.parameters("name", word));
                resultLabel.setText("Word '" + word + "' added.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultLabel.setText("Error adding the word.");
        }
    }

    /**
     * Removes a word from the Neo4j database.
     *
     * @param neo4jConnector The Neo4jConnector instance to interact with the database.
     * @param word           The word to remove from the database.
     * @param resultLabel    The label where the result message will be displayed.
     */
    private void removeWord(Neo4jConnector neo4jConnector, String word, Label resultLabel) {
        try (Session session = neo4jConnector.getSession()) {
            word = word.toLowerCase();  // Ensure case-insensitivity for word comparison

            // Query to check if the word exists
            String queryCheck = "MATCH (w:Word {name: $name}) RETURN w";
            Result resultCheck = session.run(queryCheck, Values.parameters("name", word));

            if (resultCheck.hasNext()) {
                // Remove the word if it exists in the database
                String queryDelete = "MATCH (w:Word {name: $name}) DELETE w";
                session.run(queryDelete, Values.parameters("name", word));
                resultLabel.setText("Word '" + word + "' removed.");
            } else {
                resultLabel.setText("Word '" + word + "' not found for removal.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultLabel.setText("Error removing the word.");
        }
    }

    /**
     * Searches for a word in the Neo4j database.
     *
     * @param neo4jConnector The Neo4jConnector instance to interact with the database.
     * @param word           The word to search for in the database.
     * @param resultLabel    The label where the result message will be displayed.
     */
    private void searchWord(Neo4jConnector neo4jConnector, String word, Label resultLabel) {
        try (Session session = neo4jConnector.getSession()) {
            word = word.toLowerCase();  // Ensure case-insensitivity for search

            // Query to check if the word exists
            String query = "MATCH (w:Word {name: $name}) RETURN w";
            Result result = session.run(query, Values.parameters("name", word));

            // Display the result message based on whether the word is found
            if (result.hasNext()) {
                resultLabel.setText("Word '" + word + "' found.");
            } else {
                resultLabel.setText("Word '" + word + "' not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultLabel.setText("Error searching for the word.");
        }
    }

    /**
     * Creates a tag selection panel for selecting tags.
     *
     * @return A VBox containing the tag selection controls.
     */
    private VBox createTagSelectionPanel() {
        VBox tagPanel = new VBox(10);
        tagPanel.setAlignment(Pos.TOP_LEFT);

        List<String> tags = Praticas.getAllTags();

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

            // Chama o método searchKeyPractices para buscar as práticas chave associadas às tags selecionadas
            List<Praticas> foundPractices = searchKeyPractices(selectedTags);
            System.out.println("Key Practices associated with selected tags: " + foundPractices);

            // Aqui você pode exibir as práticas encontradas na interface gráfica, por exemplo
        });

        tagPanel.getChildren().addAll(grid, searchKeyPracticesButton);
        return tagPanel;
    }

    /**
    * Toggles the visibility and state of the buttons.
    * This method will either enable or disable the provided buttons based on the given state.
    *
    * @param state  Whether to enable or disable the buttons. 
    *              If true, the buttons will be enabled; if false, they will be disabled.
    * @param buttons The buttons to toggle. This can accept any number of Button objects.
    */
    private void toggleButtons(boolean state, Button... buttons) {
        for (Button button : buttons) {
            button.setDisable(!state);  // Set the button's disable property based on the state
        }
    }

    /**
     * Initializes and starts the JavaFX application window.
     *
     * @param primaryStage The primary stage for the JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
        // Criando a conexão com o Neo4j
        Neo4jConnector neo4jConnector = new Neo4jConnector("bolt://localhost:7687", "neo4j");

        // Criando os botões de ação
        Button registerActionButton = new Button("Register Action");
        Button addButton = new Button("Add Word");
        Button removeButton = new Button("Remove Word");
        Button searchButton = new Button("Search Word");

        // Criando o campo de texto para entrada da palavra
        TextField wordInput = new TextField();
        wordInput.setPromptText("Enter word");
        wordInput.setVisible(false);  // Inicialmente invisível

        // Rótulo para exibir os resultados das operações
        Label resultLabel = new Label();

        // Layout principal: HBox organiza os componentes horizontalmente
        HBox layout = new HBox(20);  // Espaçamento de 20 entre os elementos

        // Painel à esquerda com os botões de ação
        VBox leftPanel = new VBox(10, addButton, removeButton, searchButton, registerActionButton);
        leftPanel.setAlignment(Pos.CENTER);  // Centraliza os botões na coluna

        // Painel à direita com o campo de texto e o rótulo de resultados
        VBox rightPanel = new VBox(10, wordInput, resultLabel);  // Add the word input field and result label here
        rightPanel.setAlignment(Pos.CENTER);  // Center the input field and label vertically

        // Variável para armazenar a operação atual (add, remove, search)
        final String[] operation = {""};

        // Definindo ação para o botão de registro de ação
        registerActionButton.setOnAction(e -> {
            // Exibe o painel de seleção de tags diretamente na mesma janela
            VBox tagSelectionPanel = createTagSelectionPanel();
            // Limpa o painel direito antes de adicionar o novo conteúdo
            rightPanel.getChildren().clear();  // `rightPanel` é o painel onde o conteúdo será exibido
            // Adiciona o painel de seleção de tags ao painel direito
            rightPanel.getChildren().add(tagSelectionPanel);
        });

        // Definindo ação para o botão de adicionar palavra
        addButton.setOnAction(e -> {
            wordInput.setVisible(true);  // Torna o campo de texto visível para a entrada de palavras
            resultLabel.setText("");     // Limpa o rótulo de resultados
            operation[0] = "add";       // Define a operação como "add"
            toggleButtons(false, addButton, removeButton, searchButton);  // Desativa os outros botões
        });

        // Definindo ação para o botão de remover palavra
        removeButton.setOnAction(e -> {
            wordInput.setVisible(true);
            resultLabel.setText("");   
            operation[0] = "remove";
            toggleButtons(false, addButton, removeButton, searchButton);
        });

        // Definindo ação para o botão de pesquisar palavra
        searchButton.setOnAction(e -> {
            wordInput.setVisible(true);
            resultLabel.setText("");   
            operation[0] = "search";
            toggleButtons(false, addButton, removeButton, searchButton);
        });

        // Ação para o campo de entrada de texto (quando o usuário pressiona Enter)
        wordInput.setOnAction(e -> {
            String word = wordInput.getText();  // Obtém a palavra digitada
            if (!word.isEmpty()) {
                // Dependendo da operação selecionada, chama a função correspondente
                if ("add".equals(operation[0])) {
                    addWord(neo4jConnector, word, resultLabel);
                } else if ("remove".equals(operation[0])) {
                    removeWord(neo4jConnector, word, resultLabel);
                } else if ("search".equals(operation[0])) {
                    searchWord(neo4jConnector, word, resultLabel);
                }
                wordInput.clear();          // Limpa o campo de texto
                wordInput.setVisible(false); // Torna o campo invisível após a ação
                toggleButtons(true, addButton, removeButton, searchButton);  // Reativa os botões
            }
        });

        // Usando SplitPane para dividir a janela
        SplitPane splitPane = new SplitPane();
        // Definindo a posição do divisor (0.35 significa 35% da largura para controlPanel)
        splitPane.setDividerPositions(0.35);

        splitPane.getItems().addAll(leftPanel, rightPanel);

        // Adicionando o SplitPane à cena
        Scene scene = new Scene(splitPane, 1000, 700); // Defina o tamanho da janela conforme necessário
        primaryStage.setScene(scene);
        primaryStage.show(); // Mostra a janela
    }

    /**
     * Main method to launch the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
