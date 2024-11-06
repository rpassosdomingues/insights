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
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

import java.io.File;

public class Main extends Application {
    private ImageView imageView; // Exibe a imagem carregada
    private Image originalImage; // Armazena a imagem original
    private ProgressBar progressBar; // Barra de progresso para geração de cubos
    private ProgressBar exportProgressBar; // Barra de progresso para exportação STL
    private CubeGenerator cubeGenerator; // Gerador de cubos
    private ExportToSTL stlExporter; // Exportador para STL

    // Camera and SubScene for 3D visualization
    private PerspectiveCamera camera;
    private SubScene subScene;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("3D Print Bot");

        imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);

        Button uploadButton = new Button("Upload PNG Image");
        Button generateButton = new Button("Generate Cubes");
        Button exportSTLButton = new Button("Export to STL");

        // Use the class-level variable
        cubeGenerator = new CubeGenerator(this::update3DView); // Correção na inicialização
        stlExporter = new ExportToSTL();

        uploadButton.setOnAction(e -> uploadImage(primaryStage));

        generateButton.setOnAction(e -> {
            cubeGenerator.generateCubes(originalImage, progressBar);
        });

        exportSTLButton.setOnAction(e -> stlExporter.exportToSTL(cubeGenerator.getCubeGrid(), exportProgressBar));

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(150);

        exportProgressBar = new ProgressBar(0);
        exportProgressBar.setPrefWidth(150);

        VBox controlsBox = new VBox(generateButton, progressBar, exportProgressBar);
        controlsBox.setSpacing(10);
        controlsBox.setAlignment(Pos.CENTER);

        HBox bottomButtons = new HBox(uploadButton, exportSTLButton);
        bottomButtons.setSpacing(10);
        bottomButtons.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setLeft(controlsBox);
        root.setCenter(imageView);
        root.setBottom(bottomButtons);

        // Configurando a visualização 3D
        setup3DView();
        root.setRight(subScene); // Adiciona a subscena 3D ao layout

        Scene scene = new Scene(root, 850, 325);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setup3DView() {
        Group root3D = new Group();
        camera = new PerspectiveCamera(true);
        camera.setTranslateZ(1); // Define a distância da câmera

        // Criar SubScene com tamanho especificado
        subScene = new SubScene(root3D, 300, 300, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.LIGHTGRAY);
        subScene.setCamera(camera);

        // Centralizar a câmera verticalmente
        camera.setTranslateY(-150); // Ajuste para centralizar verticalmente

        // Permitir controle do mouse
        subScene.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - (subScene.getWidth() / 2); // Centro da cena
            double deltaY = event.getSceneY() - (subScene.getHeight() / 2); // Centro da cena

            // Ajustar a rotação da câmera com base no movimento do mouse
            camera.setRotationAxis(Rotate.Y_AXIS);
            camera.setRotate(camera.getRotate() - deltaX * 0.1);
            camera.setRotationAxis(Rotate.X_AXIS);
            camera.setRotate(camera.getRotate() + deltaY * 0.1);
        });
    }

    private void update3DView() {
        Group root3D = (Group) subScene.getRoot();

        // Adiciona cubos à visualização 3D
        for (Node cube : cubeGenerator.getCubeGrid().getChildren()) {
            Box box = (Box) cube;
            box.setMaterial(new PhongMaterial(Color.BLUE)); // Define material para visualização
            root3D.getChildren().add(box);
        }
    }

    private void uploadImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Images", "*.png"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            originalImage = new Image(selectedFile.toURI().toString());
            imageView.setImage(originalImage);
            cubeGenerator.clearCubes(); // Limpa os cubos ao carregar uma nova imagem
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
