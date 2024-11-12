package src;

import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;

public class DisplacementMapping {
    
    // PixelReader used to read the pixel data from the image
    private PixelReader pixelReader;
    
    // Width and height of the image for iterating over pixels
    private int width;
    private int height;
    
    // The TriangleMesh that represents the 3D surface generated from the image
    private TriangleMesh surfaceMesh;

    // Constructor that initializes the PixelReader, image dimensions, and the surface mesh
    public DisplacementMapping(PixelReader pixelReader, int width, int height) {
        this.pixelReader = pixelReader;  // PixelReader to read pixel data from an image
        this.width = width;              // Width of the image to iterate through
        this.height = height;            // Height of the image to iterate through
        this.surfaceMesh = new TriangleMesh(); // Initializes an empty TriangleMesh for 3D surface
    }

    /**
     * This method applies displacement mapping to generate a 3D surface based on an image.
     * The brightness of each pixel is used to determine the elevation of a point in the mesh.
     * 
     * @param image The image used for generating the displacement map.
     */
    public void applyDisplacementMapping(Image image) {
        // Get the PixelReader from the image to access the pixel data
        pixelReader = image.getPixelReader();

        // Clear any existing points in the surface mesh before adding new ones
        surfaceMesh.getPoints().clear();

        // Iterate over each pixel in the image to create the 3D surface
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
            
                // Get the color of the current pixel at coordinates (x, y)
                Color color = pixelReader.getColor(x, y);
            
                // Use the brightness of the pixel to calculate the elevation (height)
                // Brightness is between 0 (dark) and 1 (bright), so we multiply by 10 to scale it.
                float elevation = (float) (color.getBrightness() * 10);  // Ensure it's a float
            
                // Add the x, y, and elevation (z) as a point in the 3D mesh
                surfaceMesh.getPoints().addAll((float) x, (float) y, elevation);  // Cast to float
            }
        }

        // Generate the faces (triangles) of the 3D surface based on the points
        // This loop will connect adjacent points to form quadrilateral faces
        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {

                // Calculate the indices of the four neighboring points (top-left, top-right, bottom-left, bottom-right)
                int topLeft = (y * width) + x;
                int topRight = (y * width) + (x + 1);
                int bottomLeft = ((y + 1) * width) + x;
                int bottomRight = ((y + 1) * width) + (x + 1);

                // Add the two triangles that form a rectangle
                surfaceMesh.getFaces().addAll(topLeft, 0, topRight, 0, bottomRight, 0);
                surfaceMesh.getFaces().addAll(topLeft, 0, bottomRight, 0, bottomLeft, 0);
            }
        }
    }

    /**
     * Returns the generated 3D surface mesh.
     * 
     * @return The TriangleMesh representing the displaced surface.
     */
    public TriangleMesh getSurfaceMesh() {
        return surfaceMesh;
    }

    /**
     * Clears the surface mesh by removing all points and faces.
     * This is useful when resetting the surface before applying new displacement mapping.
     */
    public void clearSurface() {
        surfaceMesh.getPoints().clear();
        surfaceMesh.getFaces().clear();
    }
}
