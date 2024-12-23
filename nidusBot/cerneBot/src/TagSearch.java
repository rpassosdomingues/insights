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

/**
 * Main class for managing and displaying tags associated with practices.
 * This system allows the selection of tags and displays the practices associated with those tags.
 */
public class TagSearch {

    // Map to store tags and their associated practices
    private static final Map<String, List<Praticas>> tagToPracticesMap = new HashMap<>();

    // List to store the tags selected by the user
    private static List<String> selectedTags = new ArrayList<>();

    // Initializing the map with tags and associated practices
    static {
        // Iterates over all practices and their tags to fill the tag-to-practice map
        for (Praticas pratica : Praticas.values()) {
            for (String tag : pratica.getTags()) {
                // For each tag, associate the corresponding practice
                tagToPracticesMap.computeIfAbsent(tag, k -> new ArrayList<>()).add(pratica);
            }
        }
    }

    /**
     * Method responsible for finding all practices associated with the selected tags.
     * 
     * @param selectedTags The list of tags selected by the user.
     * @return A list of practices associated with the selected tags.
     */
    public static List<Praticas> findPracticesByTags(List<String> selectedTags) {
        List<Praticas> result = new ArrayList<>();
        // For each selected tag, look for the associated practices
        for (String tag : selectedTags) {
            List<Praticas> praticas = tagToPracticesMap.get(tag);
            if (praticas != null) {
                result.addAll(praticas);  // Adds the found practices to the result list
            }
        }
        return result;
    }

    /**
     * Method that returns all the tags available in the system.
     * 
     * @return A list containing all the tags.
     */
    public static List<String> getAllTags() {
        return new ArrayList<>(tagToPracticesMap.keySet());
    }

    /**
     * Method that handles the selection of a tag.
     * If the tag is already selected, it is removed from the list; otherwise, it is added.
     * 
     * @param button The tag button in the graphical interface.
     * @param tag The tag being selected or deselected.
     */
    public static void handleTagSelection(Button button, String tag) {
        if (selectedTags.contains(tag)) {
            // If the tag is already in the list, remove it (deselecting the tag)
            selectedTags.remove(tag);
            button.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 10;");  // Changes the style to indicate deselection
        } else {
            // Otherwise, add the tag to the selected list
            selectedTags.add(tag);
            button.setStyle("-fx-background-color: #90CAF9; -fx-padding: 10;");  // Changes the style to indicate selection
        }
        // Displays the selected tags in the console, useful for debugging and verification
        System.out.println("Selected tags: " + selectedTags);
    }

    /**
     * Displays the tags as buttons in the graphical interface using JavaFX.
     * 
     * Each button represents a tag and allows the user to select or deselect tags.
     * 
     * @param primaryStage The main stage of the JavaFX application.
     */
    public static void displayTagButtons(Stage primaryStage) {
        FlowPane flowPane = new FlowPane();  // Creates the layout to organize the buttons

        // Creates a button for each available tag and associates a selection/deselection event
        for (String tag : getAllTags()) {
            Button button = new Button(tag);
            button.setFont(new Font(14));  // Sets the font size of the button
            button.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 10;");  // Sets the initial button style
            // Click event that calls the tag selection handler method
            button.setOnAction(e -> handleTagSelection(button, tag));
            flowPane.getChildren().add(button);  // Adds the button to the layout
        }

        // Configures the application scene and the main stage
        Scene scene = new Scene(flowPane, 400, 300);  // Defines the scene with the layout and size
        primaryStage.setTitle("Tag Selection");  // Sets the window title
        primaryStage.setScene(scene);  // Sets the scene on the main window
        primaryStage.show();  // Displays the window
    }

    /**
     * Main method that starts the JavaFX application.
     * 
     * @param args Command-line arguments (not used in this case).
     */
    public static void main(String[] args) {
        Application.launch(args);  // Launches the JavaFX application
    }
}
