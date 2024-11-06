import bpy
from tqdm import tqdm

# Function to generate a grid of cubes from an image
def generate_cube_grid(image_path, cube_size, cube_height, resolution_scale):
    image = bpy.data.images.load(image_path)
    width, height = int(image.size[0] * resolution_scale), int(image.size[1] * resolution_scale)
    image.scale(width, height)

    total_iterations = width * height
    progress_bar = tqdm(total=total_iterations, desc="Generating Cubes", unit="cube")

    # Create an empty list to store the cube objects
    cubes = []

    for x in range(width):
        for y in range(height):
            if image.pixels[(y * width + x) * 4] > 0.5:  # Example condition to determine if a cube is created
                # Create a cube at the desired location and height
                bpy.ops.mesh.primitive_cube_add(size=cube_size, location=(x, y, cube_height / 2))
                # Get the reference to the newly created cube object
                cube = bpy.context.object
                # Append the cube object to the list
                cubes.append(cube)
            progress_bar.update(1)

    progress_bar.close()

    # Loop through the list of cubes and create a connection between adjacent cubes
    for cube in cubes:
        # Get the x and y coordinates of the cube
        x, y, _ = cube.location
        # Find the cubes that are to the right and above the current cube
        right_cube = next((c for c in cubes if c.location == (x + 1, y, cube_height / 2)), None)
        above_cube = next((c for c in cubes if c.location == (x, y + 1, cube_height / 2)), None)
        # If there is a cube to the right, create a connection between the right faces
        if right_cube:
            # Select the current cube and the right cube
            bpy.ops.object.select_all(action='DESELECT')
            cube.select_set(True)
            right_cube.select_set(True)
            # Switch to edit mode
            bpy.ops.object.mode_set(mode='EDIT')
            # Select the right faces of both cubes
            bpy.ops.mesh.select_all(action='DESELECT')
            bpy.ops.mesh.select_face_by_sides(number=4, type='EQUAL')
            bpy.ops.transform.resize(value=(0.99, 0.99, 0.99))
            # Create a bridge between the faces
            bpy.ops.mesh.bridge_edge_loops()
            # Switch back to object mode
            bpy.ops.object.mode_set(mode='OBJECT')
        # If there is a cube above, create a connection between the top faces
        if above_cube:
            # Select the current cube and the above cube
            bpy.ops.object.select_all(action='DESELECT')
            cube.select_set(True)
            above_cube.select_set(True)
            # Switch to edit mode
            bpy.ops.object.mode_set(mode='EDIT')
            # Select the top faces of both cubes
            bpy.ops.mesh.select_all(action='DESELECT')
            bpy.ops.mesh.select_face_by_sides(number=4, type='EQUAL')
            bpy.ops.transform.resize(value=(0.99, 0.99, 0.99))
            # Create a bridge between the faces
            bpy.ops.mesh.bridge_edge_loops()
            # Switch back to object mode
            bpy.ops.object.mode_set(mode='OBJECT')

# Main function
def main(image_path, cube_size, cube_height, resolution_scale):
    bpy.ops.object.select_all(action='DESELECT')  # Deselect all objects
    bpy.ops.object.select_by_type(type='MESH')  # Select all objects of type MESH
    bpy.ops.object.delete()  # Delete all selected objects

    generate_cube_grid(image_path, cube_size, cube_height, resolution_scale)
    bpy.ops.export_mesh.stl(filepath="out/massa.stl")

# Example usage
if __name__ == "__main__":
    image_path = "images/massa.png"
    cube_size = 1.0  # Cube size
    cube_height = 1.0  # Cube height
    resolution_scale = 0.1  # Image resolution scale factor (0.5 = half resolution)

    main(image_path, cube_size, cube_height, resolution_scale)
