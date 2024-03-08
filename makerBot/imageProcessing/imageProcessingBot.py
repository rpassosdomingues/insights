from PIL import Image, ImageFilter, ImageOps
import os

# Remove the background from the image
def remove_background(input_filename, output_filename):
    input_image = Image.open(input_filename)
    output_image = input_image.convert("RGBA")  # Convert to RGBA to support transparency
    # Make the white color (RGB 255, 255, 255) transparent
    output_image = ImageOps.expand(output_image, border=2, fill='white')  # Add padding to prevent edge cutting
    transparent_area = output_image.convert("L").point(lambda x: 0 if x == 255 else 255)  # Create a mask of the white area
    output_image.putalpha(transparent_area)
    output_image.save(output_filename)
    return output_image

# Apply negative effect to the image
def apply_negative(input_filename, output_filename):
    input_image = Image.open(input_filename)
    # Convert the image to "RGB" mode to ensure it has three color channels (red, green, blue)
    input_image_rgb = input_image.convert("RGB")
    
    # Get the pixels of the image
    pixels = input_image_rgb.load()
    
    # Get the dimensions of the image
    width, height = input_image.size
    
    # Iterate over all pixels of the image and apply the negative effect
    for y in range(height):
        for x in range(width):
            # Get the pixel value
            r, g, b = pixels[x, y]
            
            # Apply the negative effect by inverting the values of each color channel
            pixels[x, y] = (255 - r, 255 - g, 255 - b)
    
    input_image_rgb.save(output_filename)
    return input_image_rgb

# Apply a low-pass filter to smooth the image
def apply_low_pass_filter(input_filename, output_filename, radius=2):
    input_image = Image.open(input_filename)
    output_image = input_image.filter(ImageFilter.GaussianBlur(radius))
    output_image.save(output_filename)
    return output_image

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

# Function to display menu
def display_menu():
    print("---------------------------")
    print("\t Menu ")
    print("---------------------------")
    print("0. Exit")
    print("1. Remove background")
    print("2. Apply negative effect")
    print("3. Apply low-pass filter")
    print("4. Binarize the image")
    print("5. Edge detection")
    print("---------------------------")

# Main function to process the image
def main():
    # Display menu
    display_menu()
    
    while True:
        choice = input("Enter your choice (0-5): ")
        
        if choice == '0':
            # Exit the program
            print("\nExiting program.")
            break
        elif choice == '1':
            # Remove background from the image
            input_filename = os.path.join("images", input("\nInput image name: ") + ".png")
            output_filename = os.path.join("images", input("Output image name: ") + "_no_background.png")
            remove_background(input_filename, output_filename)
            print("Background removed and image saved as", output_filename)
            print("\n")
        elif choice == '2':
            # Apply negative effect to the image
            input_filename = os.path.join("images", input("\nInput image name: ") + ".png")
            output_filename = os.path.join("images", input("Output image name: ") + "_negative.png")
            apply_negative(input_filename, output_filename)
            print("Negative image saved as", output_filename)
            print("\n")
        elif choice == '3':
            # Apply low-pass filter to the image
            input_filename = os.path.join("images", input("\nInput image name: ") + ".png")
            # Prompt for radius of low-pass filter
            radius = int(input("Enter the radius for low-pass filter (default is 2): "))
            output_filename = os.path.join("images", input("Output image name: ") + "_filtered.png")
            apply_low_pass_filter(input_filename, output_filename, radius)
            print("Filtered image saved as", output_filename)
            print("\n")
        elif choice == '4':
            # Binarize the image
            input_filename = os.path.join("images", input("\nInput image name: ") + ".png")
            threshold = int(input("Enter the binarization threshold (default is 60): "))
            output_filename = os.path.join("images", input("Output image name: ") + "_binarized.png")
            binarize(input_filename, threshold, output_filename)
            print("Image binarized and saved as", output_filename)
            print("\n")
        elif choice == '5':
            # Apply edge detection to the image
            input_filename = os.path.join("images", input("\nInput image name: ") + ".png")
            output_filename = os.path.join("images", input("Output image name: ") + "_edges.png")
            apply_edge_detection(input_filename, output_filename)
            print("Edge detection applied and image saved as", output_filename)
            print("\n")
        else:
            print("\nInvalid choice. Please try again.")

if __name__ == "__main__":
    main()
