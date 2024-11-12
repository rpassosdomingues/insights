package src;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;

public class SurfaceGenerator {
    private Group surfaceMeshGroup;
    private Runnable update3DView;

    public SurfaceGenerator(Runnable update3DView) {
        this.surfaceMeshGroup = new Group();
        this.update3DView = update3DView;
    }

    public void generateSurface(Image image, ProgressBar progressBar) {
        if (image == null) return;

        surfaceMeshGroup.getChildren().clear();
        progressBar.setProgress(0);

        int newWidth = 100;
        int newHeight = (int) (newWidth * (image.getHeight() / image.getWidth()));
        Image resizedImage = new Image(image.getUrl(), newWidth, newHeight, true, true);

        PixelReader pixelReader = resizedImage.getPixelReader();
        int width = (int) resizedImage.getWidth();
        int height = (int) resizedImage.getHeight();

        TriangleMesh mesh = new TriangleMesh();

        Task<Void> surfaceGenerationTask = new Task<>() {
            @Override
            protected Void call() {
                int totalPixels = width * height;
                int processedPixels = 0;
                float scale = 5.0f;

                // Adiciona vértices ajustados ao brilho
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Color color = pixelReader.getColor(x, y);
                        float heightValue = (float) (color.getBrightness() * 20); 

                        mesh.getPoints().addAll(x * scale, heightValue, y * scale);
                        processedPixels++;
                        updateProgress(processedPixels, totalPixels);
                    }
                }

                // Conecta vértices para criar faces triangulares
                for (int y = 0; y < height - 1; y++) {
                    for (int x = 0; x < width - 1; x++) {
                        int topLeft = y * width + x;
                        int topRight = topLeft + 1;
                        int bottomLeft = topLeft + width;
                        int bottomRight = bottomLeft + 1;

                        mesh.getFaces().addAll(topLeft, 0, bottomLeft, 0, topRight, 0);
                        mesh.getFaces().addAll(topRight, 0, bottomLeft, 0, bottomRight, 0);
                    }
                }

                // Renderiza a malha
                Platform.runLater(() -> {
                    MeshView surfaceView = new MeshView(mesh);
                    PhongMaterial material = new PhongMaterial();
                    material.setDiffuseColor(Color.GRAY);
                    material.setSpecularColor(Color.WHITE);
                    surfaceView.setMaterial(material);
                    
                    surfaceView.setTranslateX(-width * scale / 2);
                    surfaceView.setTranslateZ(-height * scale / 2);
                    surfaceView.setRotationAxis(Rotate.X_AXIS);
                    surfaceView.setRotate(45);
                    
                    surfaceMeshGroup.getChildren().add(surfaceView);
                    update3DView.run();
                });

                return null;
            }
        };

        progressBar.progressProperty().bind(surfaceGenerationTask.progressProperty());
        new Thread(surfaceGenerationTask).start();
    }

    public Group getSurfaceMeshGroup() {
        return surfaceMeshGroup;
    }

    public void clearSurface() {
        surfaceMeshGroup.getChildren().clear();
    }
}
