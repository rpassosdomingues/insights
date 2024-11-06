package src;

import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.application.Platform;

public class CubeGenerator {
    private Group cubeGrid; // Grupo de cubos
    private Runnable update3DView; // Callback to update 3D view

    public CubeGenerator(Runnable update3DView) {
        this.cubeGrid = new Group();
        this.update3DView = update3DView; // Store the update method
    }

    public void generateCubes(Image image, ProgressBar progressBar) {
        if (image == null) {
            return; // Retorna se não houver imagem carregada
        }

        cubeGrid.getChildren().clear();
        progressBar.setProgress(0);

        PixelReader pixelReader = image.getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        PixelClassifier pixelClassifier = new PixelClassifier();

        Task<Void> cubeGenerationTask = new Task<>() {
            @Override
            protected Void call() {
                int totalPixels = width * height;
                int processedPixels = 0;

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        Color color = pixelReader.getColor(x, y);
                        if (pixelClassifier.shouldGenerateCube(color)) {
                            Box cube = pixelClassifier.createCube(x, y, width, height, color);
                            // Add directly to the grid 3D
                            Platform.runLater(() -> cubeGrid.getChildren().add(cube));
                        }
                        processedPixels++;
                        updateProgress(processedPixels, totalPixels);
                    }
                }

                // After generating all cubes, call update3DView
                Platform.runLater(update3DView);

                return null;
            }
        };

        progressBar.progressProperty().bind(cubeGenerationTask.progressProperty());
        new Thread(cubeGenerationTask).start();
    }

    public Group getCubeGrid() {
        return cubeGrid;
    }

    public void clearCubes() {
        cubeGrid.getChildren().clear();
    }
}
