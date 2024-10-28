from PIL import Image
import os
import random
import ezdxf

# Function to split the image into pieces
def split_image(image, rows, cols):
    width, height = image.size
    piece_width = width // cols
    piece_height = height // rows
    pieces = []
    for y in range(0, height, piece_height):
        for x in range(0, width, piece_width):
            box = (x, y, x + piece_width, y + piece_height)
            pieces.append(image.crop(box))
    return pieces

# Function to shuffle the pieces randomly
def shuffle_pieces(pieces):
    random.shuffle(pieces)
    return pieces

# Function to create a puzzle from an image
def create_puzzle(image_path, rows, cols):
    original_image = Image.open(image_path)
    pieces = split_image(original_image, rows, cols)
    shuffled_pieces = shuffle_pieces(pieces)
    return shuffled_pieces

# Function to save puzzle pieces in DXF format
def save_as_dxf(pieces, output_folder, image_name):
    doc = ezdxf.new()
    msp = doc.modelspace()
    for i, piece in enumerate(pieces):
        dxf_path = os.path.join(output_folder, f"{image_name}_piece_{i}.dxf")
        piece_points = [(p[0], p[1]) for p in piece.getdata()]
        msp.add_lwpolyline(piece_points, close=True)
    doc.saveas(dxf_path)

# Main function
def main():
    input_image = input("\nEnter the name of the image (without extension): ")
    input_folder = "images"
    output_folder = "output"
    if not os.path.exists(output_folder):
        os.makedirs(output_folder)
    image_path = os.path.join(input_folder, f"{input_image}.png")
    if os.path.exists(image_path):
        puzzle_pieces = create_puzzle(image_path, rows=3, cols=3)
        save_as_dxf(puzzle_pieces, output_folder, f"{input_image}_puzzle")
        print("Puzzle pieces saved as DXF files successfully.")
    else:
        print("Image not found.")

if __name__ == "__main__":
    main()
