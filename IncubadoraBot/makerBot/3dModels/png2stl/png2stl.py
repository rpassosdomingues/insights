import bpy
import os
from tqdm import tqdm
from bpy_extras.io_utils import ExportHelper
from PIL import Image, ImageOps

def preprocess_image(input_path, output_path, brightness_threshold=50):
    """
    Preprocesses an image to ensure the background is black and contours are white.
    
    Args:
        input_path (str): Path to the original image.
        output_path (str): Path where the processed image will be saved.
        brightness_threshold (int): Threshold for differentiating background and contours.
    """
    # Open the image and convert it to grayscale
    image = Image.open(input_path).convert("L")
    
    # Invert the colors for black background and white shapes
    image = ImageOps.invert(image)
    
    # Apply a threshold to create a binary image (black and white)
    binary_image = image.point(lambda p: 255 if p > brightness_threshold else 0)

    # Save the processed image
    binary_image.save(output_path)
    print(f"Processed image saved at: {output_path}")

def write_stl(filepath):
    """Exporta todos os objetos de malha para um arquivo STL."""
    meshes = [obj for obj in bpy.context.scene.objects if obj.type == 'MESH']
    
    if not meshes:
        print("No mesh objects found to export.")
        return

    with open(filepath, 'w') as f:
        f.write("solid BlenderSTL\n")
        
        progress_bar = tqdm(total=len(meshes), desc="Exporting STL", unit="mesh")

        for mesh in meshes:
            # Verifique se o objeto está na cena antes de tentar acessá-lo
            if mesh in bpy.context.scene.objects:
                mesh_data = mesh.to_mesh()
                mesh_data.calc_loop_triangles()

                for triangle in mesh_data.loop_triangles:
                    normal = triangle.normal
                    f.write(f"  facet normal {normal.x} {normal.y} {normal.z}\n")
                    f.write("    outer loop\n")
                    
                    for index in triangle.vertices:
                        vertex = mesh_data.vertices[index].co
                        f.write(f"      vertex {vertex.x} {vertex.y} {vertex.z}\n")

                    f.write("    endloop\n")
                    f.write("  endfacet\n")

                progress_bar.update(1)

                # Limpeza do mesh após exportação
                bpy.context.view_layer.objects.active = mesh
                bpy.ops.object.select_all(action='DESELECT')
                mesh.select_set(True)
                bpy.ops.object.delete()  # Remover o objeto da cena

                # Remover mesh temporário da base de dados do Blender (somente se ele ainda estiver lá)
                if mesh_data.is_loaded:
                    bpy.data.meshes.remove(mesh_data)

        progress_bar.close()
        f.write("endsolid BlenderSTL\n")

    print(f"Model successfully exported to: {filepath}")

def generate_cube_grid(image_path, cube_size, cube_height, resolution_scale):
    """
    Generates a grid of cubes based on non-zero pixels in a preprocessed image.
    
    Args:
        image_path (str): Path to the processed image.
        cube_size (float): Size of each cube.
        cube_height (float): Height of each cube.
        resolution_scale (float): Scale factor for the image resolution.
    """
    try:
        image = bpy.data.images.load(image_path)
    except RuntimeError as e:
        print(f"Error loading image: {e}")
        return
    
    width, height = int(image.size[0] * resolution_scale), int(image.size[1] * resolution_scale)
    image.scale(width, height)

    total_iterations = width * height
    progress_bar = tqdm(total=total_iterations, desc="Generating Cubes", unit="cube")

    cubes = []
    for x in range(width):
        for y in range(height):
            if image.pixels[(y * width + x) * 4] > 0.5:
                bpy.ops.mesh.primitive_cube_add(size=cube_size, location=(x, y, cube_height / 2))
                cubes.append(bpy.context.object)
                
                # Force the screen to update (this ensures the cube appears in real-time)
                bpy.context.view_layer.update()
                
            progress_bar.update(1)

    progress_bar.close()
    bridge_adjacent_cubes(cubes, cube_height)

def bridge_adjacent_cubes(cubes, cube_height):
    """
    Bridges adjacent cubes to create a unified 3D structure.
    
    Args:
        cubes (list): List of created cubes.
        cube_height (float): Height of each cube.
    """
    for cube in cubes:
        x, y, _ = cube.location
        right_cube = next((c for c in cubes if c.location == (x + 1, y, cube_height / 2)), None)
        above_cube = next((c for c in cubes if c.location == (x, y + 1, cube_height / 2)), None)

        if right_cube:
            join_cubes(cube, right_cube)
        if above_cube:
            join_cubes(cube, above_cube)

def join_cubes(cube1, cube2):
    """
    Joins two cubes by bridging their edge loops.
    
    Args:
        cube1 (bpy.types.Object): The first cube.
        cube2 (bpy.types.Object): The second cube.
    """
    bpy.ops.object.select_all(action='DESELECT')
    cube1.select_set(True)
    cube2.select_set(True)
    bpy.context.view_layer.objects.active = cube1
    bpy.ops.object.mode_set(mode='EDIT')
    bpy.ops.mesh.select_all(action='SELECT')
    bpy.ops.mesh.bridge_edge_loops()
    bpy.ops.object.mode_set(mode='OBJECT')

def main(original_image_path, processed_image_path, cube_size, cube_height, resolution_scale):
    """
    Main function to preprocess the image, generate the 3D model, and export it to STL.
    
    Args:
        original_image_path (str): Path to the original image file.
        processed_image_path (str): Path for the preprocessed image file.
        cube_size (float): Size of each cube.
        cube_height (float): Height of each cube.
        resolution_scale (float): Scale factor for the image resolution.
    """
    # Preprocess image for black background and white contours
    preprocess_image(original_image_path, processed_image_path)

    # Clear any existing mesh objects in the scene
    bpy.ops.object.select_all(action='DESELECT')
    bpy.ops.object.select_by_type(type='MESH')
    bpy.ops.object.delete()

    # Generate cube grid based on the processed image
    generate_cube_grid(processed_image_path, cube_size, cube_height, resolution_scale)

    # Define output path for STL export
    output_dir = "out"
    os.makedirs(output_dir, exist_ok=True)
    export_path = os.path.join(output_dir, "model.stl")

    # Export the model to STL format
    write_stl(export_path)  # This will now correctly pass the export path

# Example usage
if __name__ == "__main__":
    original_image_path = "images/thor.png"  # Path to the original PNG
    processed_image_path = "images/thor_processed_image.png"  # Path for the preprocessed image
    cube_size = 1.0                                  # Size of each cube
    cube_height = 1.0                                # Height of each cube
    resolution_scale = 0.1                           # Resolution scale factor

    main(original_image_path, processed_image_path, cube_size, cube_height, resolution_scale)
