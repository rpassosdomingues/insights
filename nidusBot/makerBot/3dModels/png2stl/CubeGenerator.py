import bpy
import os
import numpy as np
from PIL import Image, ImageOps, ImageFilter
from tqdm import tqdm

class ImageProcessor:
    def __init__(self, image_path, resolution=(600, 400)):
        self.image_path = image_path
        self.resolution = resolution
        self.image = None
        self.pixel_data = None

    def load_and_preprocess_image(self):
        """Carrega a imagem, redimensiona e converte para escala de cinza."""
        self.image = Image.open(self.image_path).convert('L')
        self.image = self.image.resize(self.resolution)
        self.image = self.image.filter(ImageFilter.FIND_EDGES)  # Detecta os contornos
        self.pixel_data = np.array(self.image)

    def classify_areas_by_brightness(self):
        """Classifica as áreas com base na intensidade do pixel (do mais claro para o mais escuro)."""
        pixel_brightness = np.array(self.image)
        sorted_indices = np.argsort(pixel_brightness, axis=None)
        return sorted_indices

class CubeGenerator:
    def __init__(self, image_processor, cube_size, max_height, resolution_scale=0.1):
        self.image_processor = image_processor
        self.cube_size = cube_size
        self.max_height = max_height
        self.resolution_scale = resolution_scale

    def create_cube(self, x, y, pixel_value):
        """Cria um cubo no Blender com altura proporcional à intensidade do pixel."""
        height = (pixel_value / 255) * self.max_height  # Altura baseada na intensidade do pixel
        bpy.ops.mesh.primitive_cube_add(size=self.cube_size, location=(x * self.resolution_scale, y * self.resolution_scale, height / 2))
        bpy.context.object.scale[2] = height / self.cube_size  # Ajusta a altura do cubo

    def generate_cubes(self, sorted_indices):
        """Gera cubos de acordo com a ordem dos pixels classificados."""
        progress_bar = tqdm(total=len(sorted_indices), desc="Generating Cubes", unit="cube")
        
        height, width = self.image_processor.pixel_data.shape
        
        for idx in sorted_indices:
            y, x = np.unravel_index(idx, (height, width))

            if 0 <= x < width and 0 <= y < height:
                pixel_value = self.image_processor.pixel_data[y, x]
                self.create_cube(x, y, pixel_value)
            
            progress_bar.update(1)
        
        progress_bar.close()

class STLExporter:
    def __init__(self, output_dir="out"):
        self.output_dir = output_dir
        os.makedirs(self.output_dir, exist_ok=True)

    def write_stl(self, filepath):
        """Exporta todos os objetos de malha para um arquivo STL."""
        bpy.ops.export_mesh.stl(filepath=filepath)

class ModelGeneratorPipeline:
    def __init__(self, image_path, cube_size, max_height, resolution=(600, 400), resolution_scale=0.1):
        self.image_path = image_path
        self.cube_size = cube_size
        self.max_height = max_height
        self.resolution = resolution
        self.resolution_scale = resolution_scale

    def run(self):
        # Carrega e processa a imagem
        image_processor = ImageProcessor(self.image_path, resolution=self.resolution)
        image_processor.load_and_preprocess_image()
        
        # Classifica os pixels pela intensidade
        sorted_indices = image_processor.classify_areas_by_brightness()
        
        # Gera os cubos com base na classificação dos pixels
        cube_generator = CubeGenerator(image_processor, self.cube_size, self.max_height, self.resolution_scale)
        cube_generator.generate_cubes(sorted_indices)
        
        # Exporte o modelo para STL
        stl_exporter = STLExporter()
        export_path = os.path.join(stl_exporter.output_dir, "model.stl")
        stl_exporter.write_stl(export_path)
        print(f"Modelo exportado com sucesso para: {export_path}")

# Exemplo de uso
if __name__ == "__main__":
    original_image_path = "images/odin.png"  # Caminho para a imagem original
    cube_size = 0.1  # Tamanho de cada cubo
    max_height = 5.0  # Altura máxima dos cubos

    pipeline = ModelGeneratorPipeline(
        image_path=original_image_path, 
        cube_size=cube_size, 
        max_height=max_height, 
        resolution=(144, 144)
    )
    
    pipeline.run()
