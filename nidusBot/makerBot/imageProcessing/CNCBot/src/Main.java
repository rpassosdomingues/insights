package src;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;

import java.io.File;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;

public class Main extends Application {
    private ImageView imageView;
    private Image originalImage; // Imagem original carregada
    private Image previewImage;  // Imagem para preview em tempo real
    private Image finalImage;    // Imagem final acumulada com todas as mudanças

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CNCBot");

        // Interface components
        imageView = new ImageView();
        imageView.setFitWidth(300); // Limitar largura
        imageView.setFitHeight(300); // Limitar altura
        imageView.setPreserveRatio(true); // Preservar proporção

        Button uploadButton = new Button("Upload Image");
        Button restartButton = new Button("Restart Image");
        Button applyChangesButton = new Button("Apply Changes");
        Button saveButton = new Button("Save Image");

        Button removeBgButton = new Button("Remove Background");
        Button negativeButton = new Button("Apply Negative Effect");
        Button binarizeButton = new Button("Binarize Image");
        Button lowPassButton = new Button("Apply Low Pass Filter");
        Button edgeDetectButton = new Button("Edge Detection");

        // Controles para binarização
        Slider binarizeThresholdSlider = new Slider(0, 300, 10);
        binarizeThresholdSlider.setShowTickMarks(true);
        binarizeThresholdSlider.setShowTickLabels(true);

        // Listener para aplicar a binarização em tempo real
        binarizeThresholdSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (previewImage != null) {
                int threshold = newValue.intValue();
                previewImage = Binarize.process(finalImage, threshold); // Aplicar sobre finalImage, não modificar
                imageView.setImage(previewImage); // Atualizar o preview
            }
        });

        // Controles para filtro passa-baixa
        Slider lowPassRadiusSlider = new Slider(0, 20, 1);
        lowPassRadiusSlider.setShowTickMarks(true);
        lowPassRadiusSlider.setShowTickLabels(true);

        // Listener para aplicar o filtro de passa-baixa em tempo real
        lowPassRadiusSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (previewImage != null) {
                int radius = newValue.intValue();
                previewImage = ApplyLowPassFilter.process(finalImage, radius); // Aplicar sobre finalImage, não modificar
                imageView.setImage(previewImage); // Atualizar o preview
            }
        });

        // Slider para controle de intensidade do Edge Detection
        Slider edgeDetectSlider = new Slider(0, 10, 1);
        edgeDetectSlider.setShowTickMarks(true);
        edgeDetectSlider.setShowTickLabels(true);

        // Listener para aplicar detecção de bordas em tempo real
        edgeDetectSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (previewImage != null) {
                double level = newValue.doubleValue();
                previewImage = EdgeDetect.process(finalImage, level); // Aplicar sobre finalImage, não modificar
                imageView.setImage(previewImage); // Atualizar o preview
            }
        });

        // Upload Image
        uploadButton.setOnAction(e -> uploadImage(primaryStage));

        // Restart Image to original
        restartButton.setOnAction(e -> {
            if (originalImage != null) {
                finalImage = originalImage; // Resetar para a imagem original
                previewImage = originalImage; // Resetar o preview
                imageView.setImage(previewImage);
            }
        });

        // Apply Changes: Acumular as mudanças aplicadas no preview
        applyChangesButton.setOnAction(e -> {
            finalImage = previewImage; // Confirmar as mudanças acumuladas
        });

        // Save Image
        saveButton.setOnAction(e -> saveImage(primaryStage));

        // Remove Background
        removeBgButton.setOnAction(e -> {
            previewImage = RemoveBackground.process(finalImage); // Aplicar no preview
            imageView.setImage(previewImage);
        });

        // Apply Negative Effect
        negativeButton.setOnAction(e -> {
            previewImage = ApplyNegative.process(finalImage); // Aplicar no preview
            imageView.setImage(previewImage);
        });

        // Binarize Image
        binarizeButton.setOnAction(e -> {
            int threshold = (int) binarizeThresholdSlider.getValue();
            previewImage = Binarize.process(finalImage, threshold); // Aplicar no preview
            imageView.setImage(previewImage);
        });

        // Apply Low Pass Filter
        lowPassButton.setOnAction(e -> {
            int radius = (int) lowPassRadiusSlider.getValue();
            previewImage = ApplyLowPassFilter.process(finalImage, radius); // Aplicar no preview
            imageView.setImage(previewImage);
        });

        // Edge Detection
        edgeDetectButton.setOnAction(e -> {
            double level = edgeDetectSlider.getValue();
            previewImage = EdgeDetect.process(finalImage, level); // Aplicar no preview
            imageView.setImage(previewImage);
        });

        // Controles na vertical
        VBox controlsBox = new VBox();
        controlsBox.getChildren().addAll(
            removeBgButton, negativeButton, 
            binarizeButton, binarizeThresholdSlider,
            lowPassButton, lowPassRadiusSlider,
            edgeDetectButton, edgeDetectSlider
        );
        controlsBox.setSpacing(10); // Espaçamento entre botões
        controlsBox.setAlignment(Pos.CENTER); // Centraliza os controles verticalmente

        // Botões na parte inferior centralizada
        HBox bottomButtons = new HBox();
        bottomButtons.getChildren().addAll(uploadButton, restartButton, applyChangesButton, saveButton);
        bottomButtons.setSpacing(10);
        bottomButtons.setStyle("-fx-alignment: center;"); // Centralizar os botões

        // Layout principal
        BorderPane root = new BorderPane();
        root.setLeft(controlsBox); // Controles à esquerda
        root.setCenter(imageView); // Imagem no centro
        root.setBottom(bottomButtons); // Botões centralizados na parte inferior

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void uploadImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            originalImage = new Image(selectedFile.toURI().toString()); // Armazenar a imagem original
            finalImage = originalImage; // Definir como imagem base para acumular transformações
            previewImage = finalImage; // Usar como imagem de preview
            imageView.setImage(previewImage);
        }
    }

    private void saveImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");

        // Sugerir nome do arquivo original para salvar
        fileChooser.setInitialFileName("output" + getFileExtensionFromUrl(originalImage.getUrl()));

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(finalImage, null), "png", file); // Salvar finalImage
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private String getFileExtensionFromUrl(String url) {
        if (url.contains(".")) {
            return url.substring(url.lastIndexOf('.'));
        }
        return "";
    }

    public static void main(String[] args) {
        launch(args);
    }
}
