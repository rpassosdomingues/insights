package src;

import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.scene.shape.Box;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class ExportToSTL {

    public void exportToSTL(Group cubeGrid, ProgressBar progressBar) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("STL Files", "*.stl"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            Task<Void> exportTask = new Task<>() {
                @Override
                protected Void call() {
                    try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                        writer.write("solid cube_grid\n");
                        int totalCubes = cubeGrid.getChildren().size();
                        int processedCubes = 0;

                        for (javafx.scene.Node node : cubeGrid.getChildren()) {
                            if (node instanceof Box) {
                                Box cube = (Box) node;
                                double x = cube.getTranslateX();
                                double y = cube.getTranslateY();
                                double z = cube.getTranslateZ();
                                writeCubeToSTL(writer, x, y, z);
                            }
                            processedCubes++;
                            updateProgress(processedCubes, totalCubes);
                        }
                        writer.write("endsolid cube_grid\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };

            progressBar.progressProperty().bind(exportTask.progressProperty());
            new Thread(exportTask).start();
        }
    }

    private void writeCubeToSTL(OutputStreamWriter writer, double x, double y, double z) throws IOException {
        double halfSize = 0.5;

        // Definindo as 6 faces do cubo
        // Face superior
        writeFace(writer, x, y, z, halfSize, 0.0, 0.0, 1.0, -1.0, -1.0, 1.0, 1.0);
        // Face inferior
        writeFace(writer, x, y, z, halfSize, 0.0, 0.0, -1.0, -1.0, -1.0, -1.0, 1.0);
        // Face esquerda
        writeFace(writer, x, y, z, halfSize, -1.0, 0.0, 0.0, -1.0, -1.0, -1.0, -1.0);
        // Face direita
        writeFace(writer, x, y, z, halfSize, 1.0, 0.0, 0.0, 1.0, -1.0, 1.0, 1.0);
        // Face frontal
        writeFace(writer, x, y, z, halfSize, 0.0, 1.0, 0.0, -1.0, -1.0, -1.0, 1.0);
        // Face traseira
        writeFace(writer, x, y, z, halfSize, 0.0, -1.0, 0.0, -1.0, -1.0, 1.0, -1.0);
    }

    private void writeFace(OutputStreamWriter writer, double x, double y, double z, double halfSize,
                       double nx, double ny, double nz, double vx1, double vy1, 
                       double vx2, double vy2) throws IOException {
        writer.write("  facet normal " + nx + " " + ny + " " + nz + "\n");
        writer.write("    outer loop\n");
        writer.write(String.format("      vertex %.5f %.5f %.5f\n", x + vx1 * halfSize, y + vy1 * halfSize, z + halfSize));
        writer.write(String.format("      vertex %.5f %.5f %.5f\n", x + vx2 * halfSize, y + vy2 * halfSize, z + halfSize));
        writer.write(String.format("      vertex %.5f %.5f %.5f\n", x - vx1 * halfSize, y - vy1 * halfSize, z + halfSize));
        writer.write("    endloop\n");
        writer.write("  endfacet\n");
    }
}
