package src;

import javafx.concurrent.Task;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import javafx.scene.shape.Box;

public class ExportToSTL {

    public void exportToSTL(TriangleMesh surfaceMesh, ProgressBar progressBar) throws IOException {
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
                        writer.write("solid surface_mesh\n");

                        // Obtém os vértices do TriangleMesh
                        int totalVertices = surfaceMesh.getPoints().size() / 3; // Cada ponto tem 3 valores (x, y, z)
                        int processedVertices = 0;

                        // Percorre todos os vértices e escreve suas posições no arquivo STL
                        for (int i = 0; i < totalVertices; i++) {
                            double x = surfaceMesh.getPoints().get(i * 3);
                            double y = surfaceMesh.getPoints().get(i * 3 + 1);
                            double z = surfaceMesh.getPoints().get(i * 3 + 2);

                            // Escreve as informações do vértice no arquivo STL
                            writeCubeToSTL(writer, x, y, z); // Escreve as informações do cubo no arquivo

                            processedVertices++;
                            updateProgress(processedVertices, totalVertices); // Atualiza o progresso da exportação
                        }

                        writer.write("endsolid surface_mesh\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };

            // Conecta a barra de progresso da exportação à tarefa
            progressBar.progressProperty().bind(exportTask.progressProperty());

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
        writeFace(writer, vertices[0], vertices[1], vertices[2], vertices[3]); // Face inferior
        writeFace(writer, vertices[4], vertices[5], vertices[6], vertices[7]); // Face superior
        writeFace(writer, vertices[0], vertices[1], vertices[5], vertices[4]); // Face frente
        writeFace(writer, vertices[2], vertices[3], vertices[7], vertices[6]); // Face trás
        writeFace(writer, vertices[0], vertices[3], vertices[7], vertices[4]); // Face esquerda
        writeFace(writer, vertices[1], vertices[2], vertices[6], vertices[5]); // Face direita
    }

    // Método para escrever uma face no formato STL
    private void writeFace(OutputStreamWriter writer, double[] v0, double[] v1, double[] v2, double[] v3) throws IOException {
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
}
