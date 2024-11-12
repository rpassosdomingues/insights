package src;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import java.io.IOException;
import java.io.File;

public class Main extends Application {
    private ImageView imageView; // Displays the loaded image
    private Image originalImage; // The original image loaded by the user
    private ProgressBar progressBar; // Progress bar for displacement mapping
    private ProgressBar exportProgressBar; // Progress bar for STL export
    private DisplacementMapping displacementMapping; // Handles the displacement mapping for creating the 3D surface
    private ExportToSTL stlExporter; // Responsible for exporting the model to STL
    private PerspectiveCamera camera; // 3D camera for viewing the model
    private SubScene subScene; // SubScene to display the 3D model

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("3D Print Bot");

        // Inicializando os elementos gráficos
        imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);

        Button uploadButton = new Button("Upload PNG Image");
        Button generateButton = new Button("Generate Displaced Surface");
        Button exportSTLButton = new Button("Export to STL");

        // Instanciando objetos para mapeamento de deslocamento e exportação
        displacementMapping = new DisplacementMapping(null, 0, 0);
        stlExporter = new ExportToSTL();

        // Ação do botão de upload
        uploadButton.setOnAction(e -> uploadImage(primaryStage));

        // Ação do botão de gerar superfície 3D
        generateButton.setOnAction(e -> {
            if (originalImage != null) {
                System.out.println("Imagem carregada, iniciando mapeamento de deslocamento.");
                displacementMapping.applyDisplacementMapping(originalImage);
        
                if (displacementMapping.getSurfaceMesh() != null) {
                    System.out.println("Mapeamento de deslocamento aplicado, atualizando visualização 3D.");
                    update3DView();  // Atualiza a visualização 3D com a nova malha
                } else {
                    System.out.println("Falha ao gerar a malha. Verifique o método applyDisplacementMapping.");
                }
            } else {
                System.out.println("Nenhuma imagem carregada para o mapeamento de deslocamento.");
            }
        });

        // Ação do botão de exportar para STL
        exportSTLButton.setOnAction(e -> {
            // Exporta para STL com progresso
            exportToSTLWithProgress(); // No need for try-catch here if exportToSTL handles IOException
        });

        // Barra de progresso
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(150);

        exportProgressBar = new ProgressBar(0);
        exportProgressBar.setPrefWidth(150);

        // Caixa de controles com botões
        VBox controlsBox = new VBox(generateButton, progressBar, exportProgressBar);
        controlsBox.setSpacing(10);
        controlsBox.setAlignment(Pos.CENTER);

        // Caixa com os botões de upload e exportação
        HBox bottomButtons = new HBox(uploadButton, exportSTLButton);
        bottomButtons.setSpacing(10);
        bottomButtons.setAlignment(Pos.CENTER);

        // Layout principal
        BorderPane root = new BorderPane();
        root.setLeft(controlsBox);
        root.setCenter(imageView);
        root.setBottom(bottomButtons);

        // Configura a visualização 3D
        setup3DView();  // Chama antes de adicionar a rotação de mouse
        root.setRight(subScene);

        // Cria a cena e exibe a janela
        Scene scene = new Scene(root, 850, 325);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Set up the 3D view with camera and SubScene
    private void setup3DView() {
        Group root3D = new Group();
        camera = new PerspectiveCamera(true);
        camera.setTranslateZ(1); // Set the camera distance from the model

        subScene = new SubScene(root3D, 300, 300, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.LIGHTGRAY); // Set the background color of the SubScene
        subScene.setCamera(camera);

        camera.setTranslateY(-150); // Adjust camera vertical position

        // Set up mouse rotation interaction after initializing subScene
        setupMouseRotation();
    }

    private void update3DView() {
        Group root3D = (Group) subScene.getRoot();

        // Cria uma nova visualização da malha
        MeshView meshView = new MeshView(displacementMapping.getSurfaceMesh());
        meshView.setMaterial(new PhongMaterial(Color.BLUE)); // Define o material da superfície

        // Limpa a cena 3D antes de adicionar a nova malha
        root3D.getChildren().clear();

        // Adiciona a nova malha
        root3D.getChildren().add(meshView);
    }

    // Configura a interação do mouse para rotação da cena 3D
    private void setupMouseRotation() {
        subScene.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - (subScene.getWidth() / 2);
            double deltaY = event.getSceneY() - (subScene.getHeight() / 2);

            // Limitação da rotação da câmera para evitar exageros
            double maxRotation = 90.0; 
            double rotationSpeed = 0.1;

            camera.setRotationAxis(Rotate.Y_AXIS);
            double newRotationY = camera.getRotate() - deltaX * rotationSpeed;
            camera.setRotate(Math.min(Math.max(newRotationY, -maxRotation), maxRotation)); // Limita a rotação no eixo Y

            camera.setRotationAxis(Rotate.X_AXIS);
            double newRotationX = camera.getRotate() + deltaY * rotationSpeed;
            camera.setRotate(Math.min(Math.max(newRotationX, -maxRotation), maxRotation)); // Limita a rotação no eixo X
        });
    }

    // Load an image and configure displacement mapping
    private void uploadImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Images", "*.png"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            originalImage = new Image(selectedFile.toURI().toString());
            imageView.setImage(originalImage);

            displacementMapping = new DisplacementMapping(originalImage.getPixelReader(), (int) originalImage.getWidth(), (int) originalImage.getHeight());
            displacementMapping.clearSurface(); // Clear any previous surface data
        }
    }

    // Function to create a simple test mesh
    private TriangleMesh createSampleMesh() {
        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(
            0, 0, 0, 
            1, 0, 0, 
            0, 1, 0
        );
        mesh.getFaces().addAll(
            0, 0, 1, 1, 2, 2
        );
        return mesh;
    }

    // Export the model to STL and update the progress bar
    private void exportToSTLWithProgress() {
        // Create a new thread to simulate progress while exporting
        new Thread(() -> {
            for (double i = 0; i <= 1; i += 0.1) {
                try {
                    Thread.sleep(500); // Simulate export progress
                    final double progress = i;
                    javafx.application.Platform.runLater(() -> exportProgressBar.setProgress(progress));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            try {
                // Call actual export after simulated progress
                stlExporter.exportToSTL(displacementMapping.getSurfaceMesh(), exportProgressBar);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
