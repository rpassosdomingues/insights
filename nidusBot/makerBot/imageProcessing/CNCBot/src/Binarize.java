package src;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Binarize {
    public static Image process(Image image, int threshold) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage newImage = new WritableImage(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = image.getPixelReader().getColor(x, y);
                double brightness = color.getBrightness();
                if (brightness * 255 > threshold) {
                    newImage.getPixelWriter().setColor(x, y, Color.WHITE);
                } else {
                    newImage.getPixelWriter().setColor(x, y, Color.BLACK);
                }
            }
        }

        return newImage;
    }
}
