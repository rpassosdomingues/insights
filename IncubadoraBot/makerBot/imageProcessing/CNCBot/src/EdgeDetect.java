package src;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class EdgeDetect {
    public static Image process(Image image, double level) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage newImage = new WritableImage(width, height);

        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = newImage.getPixelWriter();

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                // Lendo os valores dos pixels ao redor
                Color pixelColor = pixelReader.getColor(x, y);
                Color pixelColorLeft = pixelReader.getColor(x - 1, y);
                Color pixelColorRight = pixelReader.getColor(x + 1, y);
                Color pixelColorTop = pixelReader.getColor(x, y - 1);
                Color pixelColorBottom = pixelReader.getColor(x, y + 1);

                // Calculando a intensidade da borda (diferença de cores)
                double edgeX = Math.abs(pixelColorRight.getBrightness() - pixelColorLeft.getBrightness());
                double edgeY = Math.abs(pixelColorBottom.getBrightness() - pixelColorTop.getBrightness());
                double edgeValue = Math.sqrt(edgeX * edgeX + edgeY * edgeY) * level;

                // Limitando o valor entre 0 e 1
                edgeValue = Math.min(Math.max(edgeValue, 0), 1);

                // Aplicando o valor de borda como escala de cinza
                Color edgeColor = new Color(edgeValue, edgeValue, edgeValue, 1.0);
                pixelWriter.setColor(x, y, edgeColor);
            }
        }
        return newImage;
    }
}
