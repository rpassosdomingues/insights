import bpy
import cv2
import numpy as np
import os
from PIL import Image, ImageFilter
from deepface import DeepFace

# Apply edge detection to the image using Sobel operator
def apply_edge_detection(input_filename, output_filename):
    input_image = Image.open(input_filename)
    output_image = input_image.filter(ImageFilter.FIND_EDGES)
    output_image.save(output_filename)
    return output_image

# Binarize the image based on a threshold entered by the user
def binarize(input_filename, threshold, output_filename):
    input_image = Image.open(input_filename)
    output_image = input_image.convert("L") # Convert image to grayscale
    output_image = output_image.point(lambda x: 0 if x <= threshold else 255)
    output_image.save(output_filename)
    return output_image

# Remove background from the image using OpenCV
def remove_background(input_filename, output_filename):
    input_image = cv2.imread(input_filename)
    gray = cv2.cvtColor(input_image, cv2.COLOR_BGR2GRAY)
    _, thresh = cv2.threshold(gray, 240, 255, cv2.THRESH_BINARY)
    contours, _ = cv2.findContours(thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    mask = np.zeros_like(input_image)
    cv2.drawContours(mask, contours, -1, (255, 255, 255), thickness=cv2.FILLED)
    result = cv2.bitwise_and(input_image, mask)
    cv2.imwrite(output_filename, result)
    return output_filename

def generate_cube_grid(image_path, cube_size, resolution_scale):
    image = Image.open(image_path)
    width, height = int(image.size[0] * resolution_scale), int(image.size[1] * resolution_scale)
    image = image.resize((width, height))

    for x in range(width):
        for y in range(height):
            pixel = image.getpixel((x, y))
            if sum(pixel[:3]) / 3 > 128:  # Exemplo de condição para criar um cubo
                bpy.ops.mesh.primitive_cube_add(size=cube_size, location=(x, y, 0))

def export_model_as_stl(filepath):
    bpy.ops.export_mesh.stl(filepath=filepath)

class FaceModelGenerator:
    def __init__(self, images_folder, cube_size, resolution_scale):
        self.images_folder = images_folder
        self.cube_size = cube_size
        self.resolution_scale = resolution_scale

    def preprocess_images(self):
        processed_images = []
        for filename in os.listdir(self.images_folder):
            if filename.endswith(".png"):
                input_path = os.path.join(self.images_folder, filename)
                output_path = os.path.join(self.images_folder, "processed_" + filename)
                # Apply edge detection
                edge_detected_image = apply_edge_detection(input_path, output_path)
                # Binarize
                binarized_image = binarize(output_path, 128, output_path)
                # Remove background
                background_removed_image = remove_background(output_path, output_path)
                processed_images.append(output_path)
        return processed_images

    def generate_face_model(self):
        processed_images = self.preprocess_images()

        # Combine the four processed images into a single composite image
        composite_image = self.combine_images(processed_images)

        # Perform facial recognition using DeepFace
        result = DeepFace.analyze(composite_image, actions=['gender', 'age', 'race'])

        print("Facial Analysis Result:", result)

        # Generate cube grid from the composite image
        generate_cube_grid(composite_image, self.cube_size, self.resolution_scale)

        # Export the model as STL file
        export_model_as_stl("output/face3D.stl")

    def combine_images(self, image_paths):
        # Load the processed images
        front_image = cv2.imread(image_paths[0])
        back_image = cv2.imread(image_paths[1])
        left_image = cv2.imread(image_paths[2])
        right_image = cv2.imread(image_paths[3])

        # Combine the images into a single composite image
        composite_image = np.hstack((front_image, back_image))
        side_images = np.vstack((left_image, right_image))
        composite_image = np.hstack((composite_image, side_images))

        return composite_image

# Main function
def main(images_folder, cube_size, resolution_scale):
    bpy.ops.object.select_all(action='DESELECT')
    bpy.ops.object.select_by_type(type='MESH')
    bpy.ops.object.delete()

    face_generator = FaceModelGenerator(images_folder, cube_size, resolution_scale)
    face_generator.generate_face_model()

# Example usage
if __name__ == "__main__":
    images_folder = "images"
    cube_size = 1.0
    resolution_scale = 0.1

    main(images_folder, cube_size, resolution_scale)
