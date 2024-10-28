import tensorflow as tf
from PIL import Image, ImageFilter
import numpy as np

# Function to binarize the image
def binarize(input_image):
    # Convert the image to grayscale
    gray_image = input_image.convert("L")
    # Binarize the image based on a threshold
    threshold = np.mean(gray_image)
    binarized_image = gray_image.point(lambda x: 255 if x > threshold else 0)
    return binarized_image

# Function to apply denoising using a convolutional neural network (CNN)
def apply_denoising(input_image):
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
    input_array = np.array(input_image) / 255.0

    # Expand dimensions to match model input shape
    input_array = np.expand_dims(input_array, axis=0)
    input_array = np.expand_dims(input_array, axis=-1)  # Add channel dimension

    # Apply denoising using the model
    denoised_array = model.predict(input_array)

    # Convert denoised array back to PIL image
    denoised_image = Image.fromarray((denoised_array[0, :, :, 0] * 255).astype(np.uint8), mode='L')

    return denoised_image

# Function to apply the best image processing settings
def apply_best_settings(input_image):
    # Binarize the image
    binarized_image = binarize(input_image)

    # Apply denoising using a CNN
    denoised_image = apply_denoising(binarized_image)

    return denoised_image

# Main function
def main(input_filename, output_filename):
    # Read the input image using PIL
    input_image = Image.open(input_filename)

    # Apply the best image processing settings
    output_image = apply_best_settings(input_image)

    # Save the output image
    output_image.save(output_filename)

if __name__ == "__main__":
    # Input and output file paths
    input_filename = "images/sagan_edges_negative.png"
    output_filename = "images/sagan_mod.png"

    # Run the main program
    main(input_filename, output_filename)
