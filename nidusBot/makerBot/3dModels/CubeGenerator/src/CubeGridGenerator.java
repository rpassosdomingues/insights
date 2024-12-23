import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class CubeGridGenerator extends Application {
    private ImageView imageView; // Exibe a imagem carregada
    private Image originalImage; // Armazena a imagem original
    private Group cubeGrid; // Grupo que armazena os cubos gerados
    private ProgressBar progressBar; // Barra de progresso para geração de cubos
    private ProgressBar exportProgressBar; // Barra de progresso para exportação STL

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("3D Print Bot");

        // Inicializa componentes da interface
        imageView = new ImageView();
        imageView.setFitWidth(300); // Limitar largura da visualização da imagem
        imageView.setFitHeight(300); // Limitar altura da visualização da imagem
        imageView.setPreserveRatio(true); // Preservar a proporção da imagem

        // Botões da interface
        Button uploadButton = new Button("Upload PNG Image");
        Button generateButton = new Button("Generate Cubes");
        Button exportSTLButton = new Button("Export to STL"); // Botão para exportar para STL

        // Inicializa o grupo para armazenar os cubos
        cubeGrid = new Group();

        // Ação para upload da imagem
        uploadButton.setOnAction(e -> uploadImage(primaryStage));

        // Ação para gerar cubos
        generateButton.setOnAction(e -> generateCubes());

        // Ação para exportar STL
        exportSTLButton.setOnAction(e -> {
            try {
                exportToSTL(); // Exporta para o arquivo STL
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        // Inicializa as barras de progresso
        progressBar = new ProgressBar(0); // Barra de progresso para geração de cubos
        progressBar.setPrefWidth(150); // Define largura da barra de progresso
        exportProgressBar = new ProgressBar(0); // Barra de progresso para exportação STL
        exportProgressBar.setPrefWidth(150); // Define largura da barra de progresso da exportação

        // Layout de controle para os botões e barras de progresso
        VBox controlsBox = new VBox();
        controlsBox.getChildren().addAll(generateButton, progressBar, exportProgressBar); // Adiciona a barra de progresso da exportação
        controlsBox.setSpacing(10); // Espaçamento entre botões e barras de progresso
        controlsBox.setAlignment(Pos.CENTER); // Centraliza os controles verticalmente

        // Botões na parte inferior centralizados
        HBox bottomButtons = new HBox();
        bottomButtons.getChildren().addAll(uploadButton, exportSTLButton);
        bottomButtons.setSpacing(10);
        bottomButtons.setAlignment(Pos.CENTER); // Centraliza os botões

        // Layout principal
        BorderPane root = new BorderPane();
        root.setLeft(controlsBox); // Controles à esquerda
        root.setCenter(imageView); // Imagem no centro
        root.setBottom(bottomButtons); // Botões centralizados na parte inferior

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para fazer upload da imagem
    private void uploadImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG Images", "*.png") // Filtra apenas imagens PNG
        );
        File selectedFile = fileChooser.showOpenDialog(stage); // Abre o seletor de arquivos
        if (selectedFile != null) {
            originalImage = new Image(selectedFile.toURI().toString()); // Armazena a imagem original
            imageView.setImage(originalImage); // Exibe a imagem carregada
            cubeGrid.getChildren().clear(); // Limpa os cubos ao carregar uma nova imagem
        }
    }

    // Método para gerar cubos a partir da imagem
    private void generateCubes() {
        if (originalImage == null) {
            return; // Retorna se não houver imagem carregada
        }

        // Limpar cubos existentes
        cubeGrid.getChildren().clear();
        progressBar.setProgress(0); // Resetar a barra de progresso da geração

        PixelReader pixelReader = originalImage.getPixelReader();
        int width = (int) originalImage.getWidth(); // Largura da imagem
        int height = (int) originalImage.getHeight(); // Altura da imagem

        double resolution = 0.5; // Resolução (tamanho de cada cubo)
        double maxHeight = 1.0; // Altura máxima de um cubo

        // Criar uma tarefa para processar a geração de cubos em uma thread separada
        Task<Void> cubeGenerationTask = new Task<>() {
            @Override
            protected Void call() {
                // Gerando a grade de cubos
                int totalPixels = width * height;
                int processedPixels = 0;

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        Color color = pixelReader.getColor(x, y);
                        double brightness = color.getBrightness(); // Calcula a luminosidade do pixel
                        if (brightness < 1) { // Apenas pixels não brancos geram cubos
                            double cubeHeight = maxHeight * (1 - brightness); // Altura baseada na luminosidade
                            Box cube = new Box(resolution, resolution, cubeHeight); // Ajusta altura do cubo
                            cube.setMaterial(new PhongMaterial(Color.WHITE)); // Define a cor do cubo
                            cube.setTranslateX(x * resolution);
                            cube.setTranslateY(height * resolution - y * resolution); // Inverte a coordenada Y
                            cube.setTranslateZ(-cubeHeight / 2); // Centraliza o cubo com base na altura

                            // Atualiza o JavaFX na thread do aplicativo
                            javafx.application.Platform.runLater(() -> cubeGrid.getChildren().add(cube));
                        }
                        processedPixels++;
                        updateProgress(processedPixels, totalPixels); // Atualiza o progresso da geração
                    }
                }
                return null;
            }
        };

        // Conecta a barra de progresso à tarefa de geração
        progressBar.progressProperty().bind(cubeGenerationTask.progressProperty());

        // Inicia a tarefa em uma nova thread
        new Thread(cubeGenerationTask).start();
    }

    // Método para exportar o modelo 3D para o arquivo STL
    private void exportToSTL() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("STL Files", "*.stl") // Filtra apenas arquivos STL
        );
        File file = fileChooser.showSaveDialog(null); // Abre o seletor de arquivos para salvar
        if (file != null) {
            // Criar uma tarefa para exportar os cubos em uma thread separada
            Task<Void> exportTask = new Task<>() {
                @Override
                protected Void call() {
                    try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                        writer.write("solid cube_grid\n");
                        int totalCubes = cubeGrid.getChildren().size(); // Total de cubos a serem exportados
                        int processedCubes = 0;

                        // Percorre todos os cubos e escreve suas posições no arquivo STL
                        for (javafx.scene.Node node : cubeGrid.getChildren()) {
                            if (node instanceof Box) {
                                Box cube = (Box) node;
                                double x = cube.getTranslateX();
                                double y = cube.getTranslateY();
                                double z = cube.getTranslateZ();
                                writeCubeToSTL(writer, x, y, z); // Escreve as informações do cubo no arquivo
                            }
                            processedCubes++;
                            updateProgress(processedCubes, totalCubes); // Atualiza o progresso da exportação
                        }
                        writer.write("endsolid cube_grid\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };

            // Conecta a barra de progresso da exportação à tarefa
            exportProgressBar.progressProperty().bind(exportTask.progressProperty());

            // Inicia a tarefa em uma nova thread
            new Thread(exportTask).start();
        }
    }

    // Método para escrever as informações de um cubo no formato STL
    private void writeCubeToSTL(OutputStreamWriter writer, double x, double y, double z) throws IOException {
        // Definindo as 8 vértices de um cubo
        double halfSize = 0.5; // Metade do tamanho do cubo
        double[][] vertices = {
            {x - halfSize, y - halfSize, z - halfSize}, // Vértice 0
            {x + halfSize, y - halfSize, z - halfSize}, // Vértice 1
            {x + halfSize, y + halfSize, z - halfSize}, // Vértice 2
            {x - halfSize, y + halfSize, z - halfSize}, // Vértice 3
            {x - halfSize, y - halfSize, z + halfSize}, // Vértice 4
            {x + halfSize, y - halfSize, z + halfSize}, // Vértice 5
            {x + halfSize, y + halfSize, z + halfSize}, // Vértice 6
            {x - halfSize, y + halfSize, z + halfSize}  // Vértice 7
        };

        // Escrevendo as 6 faces do cubo
        writeFaceToSTL(writer, vertices[0], vertices[1], vertices[2], vertices[3]); // Face inferior
        writeFaceToSTL(writer, vertices[4], vertices[5], vertices[6], vertices[7]); // Face superior
        writeFaceToSTL(writer, vertices[0], vertices[1], vertices[5], vertices[4]); // Face frente
        writeFaceToSTL(writer, vertices[2], vertices[3], vertices[7], vertices[6]); // Face trás
        writeFaceToSTL(writer, vertices[0], vertices[3], vertices[7], vertices[4]); // Face esquerda
        writeFaceToSTL(writer, vertices[1], vertices[2], vertices[6], vertices[5]); // Face direita
    }

    // Método para escrever uma face no formato STL
    private void writeFaceToSTL(OutputStreamWriter writer, double[] v0, double[] v1, double[] v2, double[] v3) throws IOException {
        // Calculando o vetor normal da face (aproximadamente)
        double[] normal = calculateNormal(v0, v1, v2);
        writer.write("  facet normal " + normal[0] + " " + normal[1] + " " + normal[2] + "\n");
        writer.write("    outer loop\n");
        writer.write(String.format("      vertex %.5f %.5f %.5f\n", v0[0], v0[1], v0[2]));
        writer.write(String.format("      vertex %.5f %.5f %.5f\n", v1[0], v1[1], v1[2]));
        writer.write(String.format("      vertex %.5f %.5f %.5f\n", v2[0], v2[1], v2[2]));
        writer.write("    endloop\n");
        writer.write("  endfacet\n");

        writer.write("  facet normal " + normal[0] + " " + normal[1] + " " + normal[2] + "\n");
        writer.write("    outer loop\n");
        writer.write(String.format("      vertex %.5f %.5f %.5f\n", v0[0], v0[1], v0[2]));
        writer.write(String.format("      vertex %.5f %.5f %.5f\n", v2[0], v2[1], v2[2]));
        writer.write(String.format("      vertex %.5f %.5f %.5f\n", v3[0], v3[1], v3[2]));
        writer.write("    endloop\n");
        writer.write("  endfacet\n");
    }

    // Método para calcular o vetor normal de uma face a partir de três vértices
    private double[] calculateNormal(double[] v0, double[] v1, double[] v2) {
        double[] vector1 = {v1[0] - v0[0], v1[1] - v0[1], v1[2] - v0[2]};
        double[] vector2 = {v2[0] - v0[0], v2[1] - v0[1], v2[2] - v0[2]};
        double[] normal = {
            vector1[1] * vector2[2] - vector1[2] * vector2[1],
            vector1[2] * vector2[0] - vector1[0] * vector2[2],
            vector1[0] * vector2[1] - vector1[1] * vector2[0]
        };
        double length = Math.sqrt(normal[0] * normal[0] + normal[1] * normal[1] + normal[2] * normal[2]);
        // Normalizando o vetor normal
        return new double[]{normal[0] / length, normal[1] / length, normal[2] / length};
    }

    // Método principal para executar a aplicação
    public static void main(String[] args) {
        launch(args);
    }
}
