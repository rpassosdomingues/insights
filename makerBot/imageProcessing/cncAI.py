from PIL import Image, ImageFilter, ImageOps
import os
import tensorflow as tf
import numpy as np

# Remove the background from the image
def remove_background(input_image):
    output_image = input_image.convert("RGBA")  # Convert to RGBA to support transparency
    # Make the white color (RGB 255, 255, 255) transparent
    output_image = ImageOps.expand(output_image, border=2, fill='white')  # Add padding to prevent edge cutting
    transparent_area = output_image.convert("L").point(lambda x: 0 if x == 255 else 255)  # Create a mask of the white area
    output_image.putalpha(transparent_area)
    return output_image

# Apply negative effect to the image
def apply_negative(input_image):
    input_image_rgb = input_image.convert("RGB")
    pixels = input_image_rgb.load()
    width, height = input_image.size
    for y in range(height):
        for x in range(width):
            r, g, b = pixels[x, y]
            pixels[x, y] = (255 - r, 255 - g, 255 - b)
    return input_image_rgb

# Apply a low-pass filter to smooth the image
def apply_low_pass_filter(input_image, radius=2):
    output_image = input_image.filter(ImageFilter.GaussianBlur(radius))
    return output_image

# Apply edge detection to the image using Sobel operator
def apply_edge_detection(input_image):
    output_image = input_image.filter(ImageFilter.FIND_EDGES)
    return output_image

# Function to apply denoising using a convolutional neural network (CNN)
def apply_denoising(input_image):
    # Convert the input image to grayscale
    grayscale_image = input_image.convert("L")

    # Define the denoising model (you can use a pre-trained model or train your own)
    # For demonstration purposes, let's create a simple CNN model using TensorFlow
    model = tf.keras.Sequential([
        tf.keras.layers.Conv2D(32, (3, 3), activation='relu', padding='same', input_shape=(None, None, 1)), # Adjusted input shape
        tf.keras.layers.Conv2D(32, (3, 3), activation='relu', padding='same'),
        tf.keras.layers.Conv2D(1, (3, 3), activation='sigmoid', padding='same')
    ])

    # Compile the model
    model.compile(optimizer='adam', loss='mse')

    # Convert image to numpy array and normalize pixel values
    input_array = np.array(grayscale_image) / 255.0

    # Expand dimensions to match model input shape
    input_array = np.expand_dims(input_array, axis=0)
    input_array = np.expand_dims(input_array, axis=-1)  # Add channel dimension

    # Apply denoising using the model
    denoised_array = model.predict(input_array)

    # Convert denoised array back to PIL image
    denoised_image = Image.fromarray((denoised_array[0, :, :, 0] * 255).astype(np.uint8), mode='L')

    return denoised_image

# Function to binarize the image using the denoising model
def binarize_with_model(input_image):
    # Apply denoising to the input image
    denoised_image = apply_denoising(input_image)

    # Binarize the denoised image based on a threshold
    threshold = np.mean(denoised_image)
    binarized_image = denoised_image.point(lambda x: 255 if x > threshold else 0)

    return binarized_image

# Main function to process the image
def main(input_filename):
    input_filepath = os.path.join("images", input_filename + ".png")
    output_filename = input_filename + "_mod.png"
    output_filepath = os.path.join("images", output_filename)
    
    # Open the input image
    input_image = Image.open(input_filepath)
    
    # Apply negative effect
    input_image = apply_negative(input_image)
    
    # Apply low-pass filter
    input_image = apply_low_pass_filter(input_image)
    
    # Binarize the image using the denoising model
    input_image = binarize_with_model(input_image)
    
    # Apply edge detection
    input_image = apply_edge_detection(input_image)
    
    # Apply Negative edge detection again
    input_image = apply_negative(input_image)
    
    # Remove background
    output_image = remove_background(input_image)
    
    # Save the resulting image
    output_image.save(output_filepath)
    
    print("\nImage processing complete. Result saved as", output_filepath)
    print("\n")

if __name__ == "__main__":
    input_filename = input("\nEnter the input image filename: ")
    main(input_filename)
