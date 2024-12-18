import bpy
import os
import numpy as np
from PIL import Image, ImageOps, ImageFilter
from tqdm import tqdm  # Progress bar library

# Verifica se o add-on 'io_mesh_stl' está disponível
addon_name = "io_mesh_stl"

if addon_name not in bpy.context.preferences.addons:
    # Verifica se o add-on está instalado no diretório de add-ons
    addon_path = os.path.join(bpy.utils.script_path_user(), 'addons', addon_name)
    
    if not os.path.exists(addon_path):
        print(f"Addon {addon_name} not found in Blender add-ons directory. Please install it.")
        exit(1)

    # Tenta ativar o add-on
    bpy.ops.preferences.addon_enable(module=addon_name)


# ImageProcessor class for loading and processing image data
class ImageProcessor:
    def __init__(self, image_path, resolution=(600, 400)):
        """Initializes the ImageProcessor with the path to the image and desired resolution."""
        self.image_path = image_path
        self.resolution = resolution
        self.image = None
        self.pixel_data = None

    def load_and_preprocess_image(self):
        """Loads the image, converts it to grayscale, resizes it, and detects edges for displacement mapping."""
        self.image = Image.open(self.image_path).convert('L')  # Convert to grayscale
        self.image = self.image.resize(self.resolution)  # Resize to the specified resolution
        self.image = self.image.filter(ImageFilter.FIND_EDGES)  # Detect edges
        self.pixel_data = np.array(self.image)  # Convert to a NumPy array for easy pixel access

    def classify_areas_by_brightness(self):
        """Classifies pixels by brightness to optimize displacement mapping."""
        pixel_brightness = np.array(self.image)  # Get pixel brightness values
        sorted_indices = np.argsort(pixel_brightness, axis=None)  # Sort indices by brightness
        return sorted_indices


# DisplacementGenerator class to create a 3D displacement effect based on image brightness
class DisplacementGenerator:
    def __init__(self, image_processor, displacement_strength):
        """Initializes with an ImageProcessor instance and the displacement strength."""
        self.image_processor = image_processor
        self.displacement_strength = displacement_strength

    def apply_displacement(self):
        """Applies displacement mapping to a subdivided plane in Blender using the image as a height map."""
        heightmap_image_path = self.image_processor.image_path
        heightmap_image = bpy.data.images.load(heightmap_image_path)  # Load image into Blender

        # Create a plane and apply high subdivision for fine displacement detail
        plane_size = 10
        subdivisions = 200
        bpy.ops.mesh.primitive_plane_add(size=plane_size, enter_editmode=False, align='WORLD', location=(0, 0, 0))
        plane = bpy.context.object

        # Apply subdivision for displacement detail
        bpy.ops.object.modifier_add(type='SUBSURF')
        plane.modifiers['Subdivision'].levels = subdivisions
        plane.modifiers['Subdivision'].render_levels = subdivisions
        bpy.ops.object.modifier_apply(modifier='Subdivision')

        # Create material for the displacement effect
        material = bpy.data.materials.new(name="HeightmapMaterial")
        material.use_nodes = True
        plane.data.materials.append(material)

        nodes = material.node_tree.nodes
        links = material.node_tree.links

        # Clear default nodes and add new nodes for displacement
        for node in nodes:
            nodes.remove(node)

        # Create and link nodes for displacement
        output_node = nodes.new(type="ShaderNodeOutputMaterial")
        principled_node = nodes.new(type="ShaderNodeBsdfPrincipled")
        displacement_node = nodes.new(type="ShaderNodeDisplacement")
        texture_node = nodes.new(type="ShaderNodeTexImage")

        # Set image texture and displacement scale
        texture_node.image = heightmap_image
        texture_node.interpolation = 'Closest'
        displacement_node.inputs['Scale'].default_value = self.displacement_strength

        # Link nodes in material node tree
        links.new(texture_node.outputs['Color'], displacement_node.inputs['Height'])
        links.new(displacement_node.outputs['Displacement'], output_node.inputs['Displacement'])
        links.new(principled_node.outputs['BSDF'], output_node.inputs['Surface'])

        # Set displacement method for realistic height mapping
        material.cycles.displacement_method = 'DISPLACEMENT'
        bpy.context.scene.render.engine = 'CYCLES'

        # Progress bar for displacement
        progress_bar = tqdm(total=1, desc="Applying Displacement", unit="step")
        progress_bar.update(1)
        progress_bar.close()


# STLExporter class for exporting the Blender model to an STL file
class STLExporter:
    def __init__(self, output_dir="out"):
        """Initializes with a directory to save the STL file, ensuring it exists."""
        self.output_dir = output_dir
        os.makedirs(self.output_dir, exist_ok=True)

    def write_stl(self, filepath):
        """Exports all mesh objects in the scene to an STL file."""
        # Progress bar for the STL export process
        progress_bar = tqdm(total=1, desc="Exporting STL", unit="file")
        bpy.ops.export_mesh.stl(filepath=filepath)
        progress_bar.update(1)
        progress_bar.close()


# ModelGeneratorPipeline class to orchestrate image processing, displacement, and STL export
class ModelGeneratorPipeline:
    def __init__(self, image_path, displacement_strength, resolution=(600, 400)):
        """Sets up the pipeline with image path, displacement strength, and resolution."""
        self.image_path = image_path
        self.displacement_strength = displacement_strength
        self.resolution = resolution

    def run(self):
        """Executes the entire pipeline: image processing, displacement application, and STL export."""
        # Load and preprocess the image
        image_processor = ImageProcessor(self.image_path, resolution=self.resolution)
        image_processor.load_and_preprocess_image()
        
        # Apply displacement mapping based on image data
        displacement_generator = DisplacementGenerator(image_processor, self.displacement_strength)
        displacement_generator.apply_displacement()
        
        # Export the final model as STL
        stl_exporter = STLExporter()
        export_path = os.path.join(stl_exporter.output_dir, "model.stl")
        stl_exporter.write_stl(export_path)
        print(f"Model successfully exported to: {export_path}")


# Example usage of the pipeline
if __name__ == "__main__":
    original_image_path = "images/odin.png"  # Path to the heightmap image
    displacement_strength = 1.0  # Strength of displacement effect

    # Create pipeline instance with given parameters
    pipeline = ModelGeneratorPipeline(
        image_path=original_image_path,
        displacement_strength=displacement_strength,
        resolution=(144, 144)  # Resolution to process the image
    )
    
    # Run the pipeline
    pipeline.run()
