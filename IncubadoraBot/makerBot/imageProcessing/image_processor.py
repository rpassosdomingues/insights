from gimpfu import *
import subprocess
import os

def remove_background(image, drawable):
    pdb.gimp_image_undo_group_start(image)
    # Remover fundo branco
    pdb.gimp_context_set_foreground((255, 255, 255))  # Cor de fundo (branco)
    pdb.gimp_edit_clear(drawable)
    pdb.gimp_image_undo_group_end(image)
    gimp.displays_flush()

def apply_negative_effect(image, drawable):
    pdb.gimp_invert(drawable)
    gimp.displays_flush()

def apply_low_pass_filter(image, drawable, radius):
    pdb.plug_in_gauss_rle(image, drawable, radius, radius, 0)
    gimp.displays_flush()

def binarize_image(image, drawable, threshold):
    pdb.gimp_image_convert_rgb(image)
    pdb.gimp_threshold(drawable, threshold * 255, 255)
    gimp.displays_flush()

def apply_edge_detection(image, drawable):
    pdb.plug_in_edge(image, drawable, 1)  # Sobel operator
    gimp.displays_flush()

def convert_image_to_dxf(image, drawable, output_filename):
    temp_pbm = "/tmp/temp.pbm"
    temp_svg = "/tmp/temp.svg"
    
    # Converte PNG para PBM
    pdb.gimp_file_save(image, drawable, temp_pbm, temp_pbm)
    subprocess.call(['convert', temp_pbm, temp_svg])  # Usando ImageMagick
    
    # Converte SVG para DXF
    dxf_path = f"{output_filename}.dxf"
    subprocess.call(['pstoedit', '-dt', '-f', 'dxf', temp_svg, dxf_path])
    
    os.remove(temp_pbm)
    os.remove(temp_svg)
    print(f"DXF convertido e salvo como {dxf_path}")

def convert_image_to_rld(image, drawable, output_filename):
    temp_pbm = "/tmp/temp.pbm"
    temp_svg = "/tmp/temp.svg"
    
    # Converte PNG para PBM
    pdb.gimp_file_save(image, drawable, temp_pbm, temp_pbm)
    subprocess.call(['convert', temp_pbm, temp_svg])  # Usando ImageMagick
    
    # Converte SVG para RLD usando Inkscape
    rld_path = f"{output_filename}.rld"
    subprocess.call(['inkscape', temp_svg, '--export-filename', rld_path])
    
    os.remove(temp_pbm)
    os.remove(temp_svg)
    print(f"RLD convertido e salvo como {rld_path}")

def python_menu(image, drawable):
    dialog = gimp.Dialog("Menu")
    dialog.add_button("OK", 1)
    dialog.add_button("Cancel", 0)

    options = {
        1: "Remove Background",
        2: "Apply Negative Effect",
        3: "Apply Low-Pass Filter",
        4: "Binarize Image",
        5: "Edge Detection",
        6: "Convert to DXF",
        7: "Convert to RLD"
    }

    for key, value in options.items():
        dialog.add_button(value, key)

    result = dialog.run()
    dialog.destroy()

    if result == 1:
        remove_background(image, drawable)
    elif result == 2:
        apply_negative_effect(image, drawable)
    elif result == 3:
        radius = 2  # Defina o valor padrão ou obtenha do usuário
        apply_low_pass_filter(image, drawable, radius)
    elif result == 4:
        threshold = 25  # Defina o valor padrão ou obtenha do usuário
        binarize_image(image, drawable, threshold)
    elif result == 5:
        apply_edge_detection(image, drawable)
    elif result == 6:
        output_filename = "output"  # Defina como preferir
        convert_image_to_dxf(image, drawable, output_filename)
    elif result == 7:
        output_filename = "output"  # Defina como preferir
        convert_image_to_rld(image, drawable, output_filename)

register(
    "python_fu_menu",
    "Image Processing Menu",
    "Displays a menu for various image processing tasks",
    "Your Name",
    "Your Name",
    "2024",
    "<Image>/Python-Fu/Image Processing Menu...",
    "*",
    [],
    [],
    python_menu)

main()
