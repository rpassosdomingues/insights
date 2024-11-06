package src;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class ApplyLowPassFilter {
    public static Image process(Image inputImage, double radius) {
        int width = (int) inputImage.getWidth();
        int height = (int) inputImage.getHeight();
        WritableImage outputImage = new WritableImage(width, height);
        PixelReader pixelReader = inputImage.getPixelReader();
        PixelWriter pixelWriter = outputImage.getPixelWriter();

        // Aplicação de um filtro de passa-baixa básico (blur) com raio determinado
        int filterSize = (int) radius;
        double[][] filter = createGaussianFilter(filterSize);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                applyFilter(x, y, pixelReader, pixelWriter, filter, filterSize, width, height);
            }
        }

        return outputImage;
    }

    private static void applyFilter(int x, int y, PixelReader pixelReader, PixelWriter pixelWriter, double[][] filter, int filterSize, int width, int height) {
        double red = 0, green = 0, blue = 0;
        double weightSum = 0;

        int halfSize = filterSize / 2;

        for (int fy = -halfSize; fy <= halfSize; fy++) {
            for (int fx = -halfSize; fx <= halfSize; fx++) {
                int imageX = Math.min(Math.max(x + fx, 0), width - 1);
                int imageY = Math.min(Math.max(y + fy, 0), height - 1);

                int argb = pixelReader.getArgb(imageX, imageY);
                double weight = filter[fx + halfSize][fy + halfSize];

                red += ((argb >> 16) & 0xFF) * weight;
                green += ((argb >> 8) & 0xFF) * weight;
                blue += (argb & 0xFF) * weight;

                weightSum += weight;
            }
        }

        int r = (int) Math.min(Math.max(red / weightSum, 0), 255);
        int g = (int) Math.min(Math.max(green / weightSum, 0), 255);
        int b = (int) Math.min(Math.max(blue / weightSum, 0), 255);

        int newArgb = (0xFF << 24) | (r << 16) | (g << 8) | b;
        pixelWriter.setArgb(x, y, newArgb);
    }

    private static double[][] createGaussianFilter(int size) {
        double[][] filter = new double[size][size];
        double sigma = size / 2.0;
        double sum = 0;
        int halfSize = size / 2;

        for (int y = -halfSize; y <= halfSize; y++) {
            for (int x = -halfSize; x <= halfSize; x++) {
                filter[x + halfSize][y + halfSize] = Math.exp(-(x * x + y * y) / (2 * sigma * sigma));
                sum += filter[x + halfSize][y + halfSize];
            }
        }

        // Normaliza o filtro
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                filter[x][y] /= sum;
            }
        }

        return filter;
    }
}
