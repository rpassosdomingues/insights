import bpy
import os

# Criando um cubo
bpy.ops.mesh.primitive_cube_add(size=2)

# Movendo o cubo para uma posição específica
bpy.context.object.location = (1, 2, 3)

# Salvando o arquivo Blender temporariamente
temp_blend_file = os.path.join(os.path.dirname(bpy.data.filepath), "temp.blend")
bpy.ops.wm.save_as_mainfile(filepath=temp_blend_file)

# Renderizando o cubo para um arquivo SCAD
bpy.ops.export_mesh.stl(filepath="voxel.stl", check_existing=False)

# Convertendo o arquivo SCAD para um arquivo STL usando o OpenSCAD
os.system('openscad -o voxel.stl voxel.scad')

# Removendo o arquivo Blender temporário
os.remove(temp_blend_file)
