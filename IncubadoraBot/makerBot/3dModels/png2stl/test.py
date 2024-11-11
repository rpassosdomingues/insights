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
        self.contours = None
        self.pixel_data = None

    def load_and_preprocess_image(self):
        """Carrega a imagem, redimensiona e converte para escala de cinza."""
        self.image = Image.open(self.image_path).convert('L')
        self.image = self.image.resize(self.resolution)
        self.image = self.image.filter(ImageFilter.FIND_EDGES)  # Detecta os contornos
        self.pixel_data = np.array(self.image)

    def get_contours(self):
        """Extrai as áreas delimitadas pelos contornos e classifica pela intensidade."""
        contours = np.where(self.pixel_data > 50, 1, 0)  # Definindo um limite de intensidade
        return contours

    def classify_areas_by_brightness(self):
        """Classifica as áreas com base na intensidade do pixel (do mais claro para o mais escuro)."""
        pixel_brightness = np.array(self.image)
        sorted_indices = np.argsort(pixel_brightness, axis=None)
        return sorted_indices

class CubeGenerator:
    def __init__(self, image_processor, cube_size, cube_height, resolution_scale=0.1):
        self.image_processor = image_processor
        self.cube_size = cube_size
        self.cube_height = cube_height
        self.resolution_scale = resolution_scale

    def create_cube(self, x, y):
        """Cria um cubo no Blender se o pixel tiver uma intensidade suficiente."""
        pixel_value = self.image_processor.pixel_data[y, x]
        if pixel_value > 0:
            bpy.ops.mesh.primitive_cube_add(size=self.cube_size, location=(x, y, self.cube_height / 2))

    def generate_cubes(self, sorted_indices):
        """Gera cubos de acordo com a ordem dos pixels classificados."""
        progress_bar = tqdm(total=len(sorted_indices), desc="Generating Cubes", unit="cube")
        
        # Corrigindo o cálculo de índices
        height, width = self.image_processor.pixel_data.shape  # Obter as dimensões da imagem
        
        for idx in sorted_indices:
            # Convertendo o índice linear para coordenadas (x, y)
            x, y = np.unravel_index(idx, (width, height))  # Ajuste a ordem das dimensões aqui

            # Verificar se os índices estão dentro dos limites
            if x < width and y < height:
                self.create_cube(x, y)
            
            progress_bar.update(1)
        
        progress_bar.close()

class STLExporter:
    def __init__(self, output_dir="out"):
        self.output_dir = output_dir
        os.makedirs(self.output_dir, exist_ok=True)

    def write_stl(self, filepath):
        """Exporta todos os objetos de malha para um arquivo STL."""
        meshes = [obj for obj in bpy.context.scene.objects if obj.type == 'MESH']
        
        if not meshes:
            print("No mesh objects found to export.")
            return

        # Begin writing STL file
        with open(filepath, 'w') as f:
            f.write("solid BlenderSTL\n")
            
            progress_bar = tqdm(total=len(meshes), desc="Exporting STL", unit="mesh")

            for mesh in meshes:
                # Acessa os dados da malha de forma segura
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

                # Não exclua os objetos diretamente durante a exportação
                # Deixe-os para ser deletado depois de todos os processos concluídos
                bpy.context.view_layer.objects.active = mesh
                # Não remova o mesh agora. Iremos deleta-los mais tarde, após a exportação
                # bpy.ops.object.delete()

                progress_bar.update(1)

            progress_bar.close()
            f.write("endsolid BlenderSTL\n")
        print(f"Model successfully exported to: {filepath}")


class ModelGeneratorPipeline:
    def __init__(self, image_path, cube_size, cube_height, resolution=(600, 400), resolution_scale=0.1):
        self.image_path = image_path
        self.cube_size = cube_size
        self.cube_height = cube_height
        self.resolution = resolution
        self.resolution_scale = resolution_scale

    def run(self):
        # Carrega e processa a imagem
        image_processor = ImageProcessor(self.image_path, resolution=self.resolution)
        image_processor.load_and_preprocess_image()
        
        # Classifica os pixels pela intensidade
        sorted_indices = image_processor.classify_areas_by_brightness()
        
        # Gera os cubos com base na classificação dos pixels
        cube_generator = CubeGenerator(image_processor, self.cube_size, self.cube_height, self.resolution_scale)
        cube_generator.generate_cubes(sorted_indices)
        
        # Exporte o modelo para STL
        stl_exporter = STLExporter()
        export_path = os.path.join(stl_exporter.output_dir, "model.stl")
        stl_exporter.write_stl(export_path)


# Exemplo de uso
if __name__ == "__main__":
    original_image_path = "images/odin.png"  # Caminho para a imagem original
    cube_size = 1.0  # Tamanho de cada cubo
    cube_height = 1.0  # Altura de cada cubo

    pipeline = ModelGeneratorPipeline(
        image_path=original_image_path, 
        cube_size=cube_size, 
        cube_height=cube_height, 
        resolution=(600, 400)
    )
    
    pipeline.run()
