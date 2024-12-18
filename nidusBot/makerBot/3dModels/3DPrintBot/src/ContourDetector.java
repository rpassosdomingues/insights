package src;

import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;
import java.util.List;

public class ContourDetector {
    private PixelReader pixelReader;
    private int width;
    private int height;
    private List<int[]> contours;

    public ContourDetector(PixelReader pixelReader, int width, int height) {
        this.pixelReader = pixelReader;
        this.width = width;
        this.height = height;
        this.contours = new ArrayList<>();
    }

    public void detectContours() {
        double threshold = 0.5; // Threshold brightness for contour detection

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                Color color = pixelReader.getColor(x, y);
                double brightness = color.getBrightness();

                // If this pixel is bright enough, it's considered an edge point
                if (brightness > threshold && isEdge(x, y, threshold)) {
                    contours.add(new int[]{x, y});
                }
            }
        }
    }

    private boolean isEdge(int x, int y, double threshold) {
        // Check the gradient by comparing with adjacent pixels
        double centerBrightness = pixelReader.getColor(x, y).getBrightness();
        
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx == 0 && dy == 0) continue; // Skip the center pixel
                
                int nx = x + dx;
                int ny = y + dy;

                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    double neighborBrightness = pixelReader.getColor(nx, ny).getBrightness();
                    if (Math.abs(centerBrightness - neighborBrightness) > threshold) {
                        return true; // There is a significant brightness change, so it's an edge
                    }
                }
            }
        }
        return false;
    }

    public void applyContoursToMesh(TriangleMesh mesh) {
        // Add points to the mesh
        for (int[] point : contours) {
            int x = point[0];
            int y = point[1];
            mesh.getPoints().addAll(x, 0, y); // Z-coordinate set to 0
        }

        // Create triangular faces for the mesh by connecting contour points
        for (int i = 0; i < contours.size() - 2; i++) {
            int[] p1 = contours.get(i);
            int[] p2 = contours.get(i + 1);
            int[] p3 = contours.get((i + 2) % contours.size()); // Wrap around to form closed faces
            
            // Add a triangular face (each face has three vertices)
            mesh.getFaces().addAll(i, 0, i + 1, 0, i + 2, 0);
        }
    }

    public List<int[]> getContours() {
        return contours;
    }
}
