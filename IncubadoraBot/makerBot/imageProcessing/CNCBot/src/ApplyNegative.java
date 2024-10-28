package src;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ApplyNegative {
    public static Image process(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage newImage = new WritableImage(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = image.getPixelReader().getColor(x, y);
                Color negative = Color.color(1 - color.getRed(), 1 - color.getGreen(), 1 - color.getBlue());
                newImage.getPixelWriter().setColor(x, y, negative);
            }
        }

        return newImage;
    }
}
