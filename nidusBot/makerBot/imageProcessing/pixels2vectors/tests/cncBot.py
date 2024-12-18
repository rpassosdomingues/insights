from PIL import Image, ImageFilter, ImageOps
import os
import ezdxf

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

# Binarize the image based on a threshold entered by the user
def binarize(input_image, threshold):
    output_image = input_image.convert("L") # Convert image to grayscale
    output_image = output_image.point(lambda x: 0 if x <= threshold else 255)
    return output_image

# Apply edge detection to the image using Sobel operator
def apply_edge_detection(input_image):
    output_image = input_image.filter(ImageFilter.FIND_EDGES)
    return output_image

# Convert image to DXF format
def convert_to_dxf(input_image, output_folder, output_filename):
    dxf_path = os.path.join(output_folder, f"{output_filename}.dxf")
    doc = ezdxf.new()
    msp = doc.modelspace()
    width, height = input_image.size
    for y in range(height):
        for x in range(width):
            if input_image.getpixel((x, y)) != (255, 255, 255, 0):  # Ignore fully transparent pixels
                msp.add_line((x, y), (x+1, y+1))  # Add a line for each non-transparent pixel
    doc.saveas(dxf_path)
    return dxf_path

# Main function to process the image
def main(input_filename):
    input_filepath = os.path.join("images", input_filename + ".png")
    output_folder = "output"
    if not os.path.exists(output_folder):
        os.makedirs(output_folder)
    
    # Open the input image
    input_image = Image.open(input_filepath)
    
    # Apply low-pass filter
    input_image = apply_low_pass_filter(input_image)
    
    # Binarize the image
    #input_image = binarize(input_image, 80)
    
    # Apply edge detection
    input_image = apply_edge_detection(input_image)
    
    # Apply Negative edge detection again
    input_image = apply_negative(input_image)
    
    # Remove background
    output_image = remove_background(input_image)
    
    # Save the resulting image
    output_filename = input_filename + "_mod"
    output_image.save(os.path.join(output_folder, f"{output_filename}.png"))
    
    # Convert to DXF
    dxf_file = convert_to_dxf(output_image, output_folder, output_filename)
    
    print("\nImage processing complete. Result saved as", output_filename + ".png")
    print("Result also saved as DXF:", dxf_file)
    print("\n")

if __name__ == "__main__":
    input_filename = input("\nEnter the input image filename: ")
    main(input_filename)
