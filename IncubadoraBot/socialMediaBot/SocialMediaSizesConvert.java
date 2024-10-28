import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageResizerGUI extends Application {

    private BufferedImage originalImage;
    private Image instagramPostImage;
    private Image instagramStoriesImage;
    private Image facebookPostImage;
    private Image facebookStoriesImage;
    private Image linkedinPostImage;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Image Resizer");

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));

        Button selectImageButton = new Button("Select Image");
        selectImageButton.setOnAction(e -> selectImage());

        HBox imagesBox = new HBox();
        imagesBox.setSpacing(10);

        ImageView instagramPostView = new ImageView();
        ImageView instagramStoriesView = new ImageView();
        ImageView facebookPostView = new ImageView();
        ImageView facebookStoriesView = new ImageView();
        ImageView linkedinPostView = new ImageView();

        imagesBox.getChildren().addAll(
                instagramPostView,
                instagramStoriesView,
                facebookPostView,
                facebookStoriesView,
                linkedinPostView
        );

        vBox.getChildren().addAll(
                selectImageButton,
                new Label("Instagram Post"),
                instagramPostView,
                new Label("Instagram Stories"),
                instagramStoriesView,
                new Label("Facebook Post"),
                facebookPostView,
                new Label("Facebook Stories"),
                facebookStoriesView,
                new Label("LinkedIn Post"),
                linkedinPostView
        );

        Scene scene = new Scene(vBox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                originalImage = ImageIO.read(selectedFile);
                instagramPostImage = resizeImage(originalImage, 1080, 1080);
                instagramStoriesImage = resizeImage(originalImage, 1080, 1920);
                facebookPostImage = resizeImage(originalImage, 1200, 630);
                facebookStoriesImage = resizeImage(originalImage, 1080, 1920);
                linkedinPostImage = resizeImage(originalImage, 1200, 627);

                displayImages();
            } catch (IOException e) {
                System.out.println("Error loading image: " + e.getMessage());
            }
        }
    }

    private Image resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        resizedImage.createGraphics().drawImage(originalImage.getScaledInstance(targetWidth, targetHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        return new Image(resizedImage, targetWidth, targetHeight, false, false);
    }

    private void displayImages() {
        Stage stage = new Stage();
        stage.setTitle("Resized Images");

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));

        vBox.getChildren().addAll(
                new Label("Instagram Post"),
                new ImageView(instagramPostImage),
                new Label("Instagram Stories"),
                new ImageView(instagramStoriesImage),
                new Label("Facebook Post"),
                new ImageView(facebookPostImage),
                new Label("Facebook Stories"),
                new ImageView(facebookStoriesImage),
                new Label("LinkedIn Post"),
                new ImageView(linkedinPostImage)
        );

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

