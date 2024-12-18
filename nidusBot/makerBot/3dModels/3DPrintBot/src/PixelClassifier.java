package src;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import java.util.Random;

public class PixelClassifier {
    private final double resolution = 1.0; // Largura e profundidade dos cubos
    private final double maxHeight = 10.0;   // Altura máxima dos cubos

    public boolean shouldGenerateCube(Color color) {
        return color.getBrightness() < 1;
    }

    public Box createCube(int x, int y, int width, int height, Color color) {
        double brightness = color.getBrightness();
        double cubeHeight = maxHeight * (1 - brightness);
        Box cube = new Box(resolution, resolution, cubeHeight);
        cube.setMaterial(new PhongMaterial(color)); // Usando a cor original

        // Definindo a posição dos cubos
        double randomX = x * resolution;              // Posição X baseada na coluna
        double randomY = (height - y - 1) * resolution; // Posição Y baseada na linha (invertendo para que 0 fique na parte inferior)
        double randomZ = -cubeHeight / 2;             // Centralizando em relação à altura

        cube.setTranslateX(randomX);
        cube.setTranslateY(randomY);
        cube.setTranslateZ(randomZ);

        return cube;
    }
}
