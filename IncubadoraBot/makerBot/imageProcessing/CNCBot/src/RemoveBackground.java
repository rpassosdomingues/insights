package src;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class RemoveBackground {
    public static Image process(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage newImage = new WritableImage(width, height);

        // Lógica para remover o fundo
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = image.getPixelReader().getColor(x, y);
                if (isBackgroundColor(color)) {
                    newImage.getPixelWriter().setColor(x, y, Color.TRANSPARENT);
                } else {
                    newImage.getPixelWriter().setColor(x, y, color);
                }
            }
        }

        return newImage;
    }

    private static boolean isBackgroundColor(Color color) {
        // Definir lógica para detectar cor do fundo
        return color.equals(Color.WHITE);  // Exemplo: fundo branco
    }
}
