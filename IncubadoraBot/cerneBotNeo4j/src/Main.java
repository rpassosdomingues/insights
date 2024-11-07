package src;

import org.neo4j.driver.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import src.Praticas;

/**
 * Main application class for the JavaFX application that interacts with a Neo4j database
 * to add, remove, and search for words. It also allows the user to select tags and display
 * practices associated with those tags.
 */
public class Main extends Application {

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
    * Initializes and starts the JavaFX application window.
    *
    * @param primaryStage The primary stage for the JavaFX application.
    */
    @Override
    public void start(Stage primaryStage) {
        // Initialize Neo4j connection
        Neo4jConnector neo4jConnector = new Neo4jConnector("bolt://localhost:7687", "neo4j");

        // UI Elements
        TextField wordInput = new TextField();
        wordInput.setPromptText("Enter word");
        wordInput.setVisible(false); // Initially invisible

        Label resultLabel = new Label();

        Button registerActionButton = new Button("Register Action");
        Button addButton = new Button("Add Word");
        Button removeButton = new Button("Remove Word");
        Button searchButton = new Button("Search Word");

        // Operation control variable
        final String[] operation = {""};

        // Event Handlers for buttons
        registerActionButton.setOnAction(e -> {
            // Create a new tag selection panel for actions
            VBox tagSelectionPanel = createTagSelectionPanel();
            Scene secondaryScene = new Scene(tagSelectionPanel, 400, 300);

            Stage tagSelectionStage = new Stage();
            tagSelectionStage.setTitle("Select Tags");
            tagSelectionStage.setScene(secondaryScene);
            tagSelectionStage.show();
        });

        addButton.setOnAction(e -> {
            wordInput.setVisible(true);
            resultLabel.setText("");  // Clear previous results
            operation[0] = "add";  // Set operation to "add"
            toggleButtons(addButton, removeButton, searchButton, false);  // Disable buttons
        });

        removeButton.setOnAction(e -> {
            wordInput.setVisible(true);
            resultLabel.setText("");  // Clear previous results
            operation[0] = "remove";  // Set operation to "remove"
            toggleButtons(addButton, removeButton, searchButton, false);  // Disable buttons
        });

        searchButton.setOnAction(e -> {
            wordInput.setVisible(true);
            resultLabel.setText("");  // Clear previous results
            operation[0] = "search";  // Set operation to "search"
            toggleButtons(addButton, removeButton, searchButton, false);  // Disable buttons
        });

        // Word Input Action: Submit word and perform the selected operation
        wordInput.setOnAction(e -> {
            String word = wordInput.getText();
            if (!word.isEmpty()) {
                if ("add".equals(operation[0])) {
                    addWord(neo4jConnector, word, resultLabel);
                } else if ("remove".equals(operation[0])) {
                    removeWord(neo4jConnector, word, resultLabel);
                } else if ("search".equals(operation[0])) {
                    searchWord(neo4jConnector, word, resultLabel);
                }
                wordInput.clear();
                wordInput.setVisible(false); // Hide text input field
                toggleButtons(addButton, removeButton, searchButton, true); // Enable buttons
            }
        });

        // Main layout setup
        VBox layout = new VBox(10, addButton, removeButton, searchButton, registerActionButton, wordInput, resultLabel);
        layout.setAlignment(Pos.CENTER);

        // Scene setup
        Scene scene = new Scene(layout, 400, 200);
        primaryStage.setTitle("Neo4j Word Operations");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    /**
     * Creates a panel with checkboxes for each tag to allow the user to select tags.
     * The selected tags can then be used to display associated practices.
     *
     * @return A VBox containing checkboxes for each tag and a button to trigger the search.
     */
    private VBox createTagSelectionPanel() {
        List<String> allTags = TagSearch.getAllTags();  // Retrieve all tags from TagSearch
        VBox vbox = new VBox(10);  // Main container with spacing
        List<CheckBox> checkBoxList = new ArrayList<>();  // List to keep track of each checkbox

        // Add a checkbox for each tag
        for (String tag : allTags) {
            CheckBox checkBox = new CheckBox(tag);
            checkBoxList.add(checkBox);  // Store checkboxes for later access
            vbox.getChildren().add(checkBox);  // Add checkbox to VBox
        }

        // Text area to display results
        TextArea resultsArea = new TextArea();
        resultsArea.setEditable(false);
        resultsArea.setPromptText("Práticas encontradas serão exibidas aqui...");

        // Button to initiate search based on selected tags
        Button identifyPracticesButton = new Button("Search Key Pratice");
        identifyPracticesButton.setOnAction(e -> {
            // Collect tags from selected checkboxes
            List<String> selectedTags = checkBoxList.stream()
                    .filter(CheckBox::isSelected)  // Only selected checkboxes
                    .map(CheckBox::getText)  // Get tag name from each checkbox
                    .collect(Collectors.toList());

            // Find practices matching selected tags
            List<Praticas> practices = TagSearch.findPracticesByTags(selectedTags);

            // Display practices or indicate no results
            if (practices.isEmpty()) {
                resultsArea.setText("Nenhuma prática encontrada para as tags selecionadas.");
            } else {
                resultsArea.setText("Práticas encontradas:\n" + practices.stream()
                        .map(Praticas::getKeyPractice)  // Adjust to show practice names or details
                        .collect(Collectors.joining("\n")));
            }
        });

        // Add the button and result area to the VBox
        vbox.getChildren().addAll(identifyPracticesButton, resultsArea);
        return vbox;
    }

    /**
     * Toggles the enable state of the operation buttons.
     *
     * @param addButton    The button for adding a word.
     * @param removeButton The button for removing a word.
     * @param searchButton The button for searching a word.
     * @param enable       Boolean flag indicating whether to enable or disable the buttons.
     */
    private void toggleButtons(Button addButton, Button removeButton, Button searchButton, boolean enable) {
        addButton.setDisable(!enable);
        removeButton.setDisable(!enable);
        searchButton.setDisable(!enable);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
