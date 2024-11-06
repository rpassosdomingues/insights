#!/bin/bash

# Remove the background from the image
remove_background() {
    input_filename="$1"
    output_filename="$2"
    
    convert "images/$input_filename" -fuzz 20% -transparent white "images/$output_filename"
    echo "Background removed and image saved as images/$output_filename"
}

# Apply negative effect to the image
apply_negative_effect() {
    input_filename="$1"
    output_filename="$2"
    convert "images/$input_filename" -negate "images/$output_filename"
    echo "Negative image saved as images/$output_filename"
}

# Apply a low-pass filter to smooth the image
apply_low_pass_filter() {
    input_filename="$1"
    output_filename="$2"
    radius="$3"
    convert "images/$input_filename" -gaussian-blur "${radius}x${radius}" "images/$output_filename"
    echo "Filtered image saved as images/$output_filename"
}

# Binarize the image based on a threshold entered by the user
binarize_image() {
    input_filename="$1"
    threshold="$2"
    output_filename="$3"
    convert "images/$input_filename" -colorspace Gray -threshold "$threshold%" "images/$output_filename"
    echo "Image binarized and saved as images/$output_filename"
}

# Apply edge detection to the image using Sobel operator
apply_edge_detection() {
    input_filename="$1"
    output_filename="$2"
    convert "images/$input_filename" -colorspace Gray -edge 1 "images/$output_filename"
    echo "Edge detection applied and image saved as images/$output_filename"
}

# Convert image to DXF format
convert_image_to_dxf() {
    input_image="images/$1"       # Caminho da imagem de entrada (PNG)
    output_folder="dxf_output"    # Pasta de saída para o arquivo DXF
    output_filename="$2"          # Nome do arquivo DXF

    # Verifica se a imagem de entrada existe
    if [ ! -f "$input_image" ]; then
        echo "Erro: Arquivo de entrada não encontrado!"
        exit 1
    fi

    # Cria a pasta de saída se não existir
    mkdir -p "$output_folder"

    # Converte PNG para PBM (Portable Bitmap)
    pbm_image="${output_folder}/${output_filename}.pbm"
    echo "Convertendo PNG para PBM..."
    convert "$input_image" "$pbm_image"  # Usando a ferramenta `convert` do ImageMagick

    # Converte PBM para SVG usando Potrace
    svg_image="${output_folder}/${output_filename}.svg"
    echo "Convertendo PBM para SVG..."
    potrace "$pbm_image" -s -o "$svg_image"

    # Converte SVG para DXF usando pstoedit
    dxf_path="${output_folder}/${output_filename}.dxf"
    echo "Convertendo SVG para DXF..."
    pstoedit -dt -f dxf "$svg_image" "$dxf_path"

    # Verifica se a conversão foi bem-sucedida
    if [ -f "$dxf_path" ]; then
        echo "Conversão concluída. Arquivo DXF salvo em: $dxf_path"
    else
        echo "Erro na conversão para DXF."
    fi
}

# Convert image to RLD format
convert_image_to_rld() {
    input_image="images/$1"        # Caminho da imagem de entrada (PNG)
    output_folder="rld_output"     # Pasta de saída para o arquivo RLD
    output_filename="$2"           # Nome do arquivo RLD

    # Verifica se a imagem de entrada existe
    if [ ! -f "$input_image" ]; then
        echo "Erro: Arquivo de entrada não encontrado!"
        exit 1
    fi

    # Cria a pasta de saída se não existir
    mkdir -p "$output_folder"

    # Converte PNG para PBM (Portable Bitmap)
    pbm_image="${output_folder}/${output_filename}.pbm"
    echo "Convertendo PNG para PBM..."
    convert "$input_image" "$pbm_image"

    # Converte PBM para SVG usando Potrace
    svg_image="${output_folder}/${output_filename}.svg"
    echo "Convertendo PBM para SVG..."
    potrace "$pbm_image" -s -o "$svg_image"

    # Converte SVG para RLD (utilizando ferramenta como `inkscape` ou outro conversor apropriado)
    rld_path="${output_folder}/${output_filename}.rld"
    echo "Convertendo SVG para RLD..."
    inkscape "$svg_image" --export-filename="$rld_path"

    # Verifica se a conversão foi bem-sucedida
    if [ -f "$rld_path" ]; then
        echo "Conversão concluída. Arquivo RLD salvo em: $rld_path"
    else
        echo "Erro na conversão para RLD."
    fi
}

# Function to display menu
display_menu() {
    echo "---------------------------"
    echo "        Menu"
    echo "---------------------------"
    echo "0. Exit"
    echo "1. Remove background"
    echo "2. Apply negative effect"
    echo "3. Apply low-pass filter"
    echo "4. Binarize the image"
    echo "5. Edge detection"
    echo "6. Convert image to DXF"
    echo "7. Convert image to RLD"
    echo "---------------------------"
}

# Main function to process the image
main() {
    display_menu
    
    while true; do
        read -p "Enter your choice (0-7): " choice
        
        case $choice in
            0)
                echo "Exiting program."
                break
                ;;
            1)
                read -p "Input image name (without extension): " input_filename
                read -p "Output image name (without extension): " output_filename
                remove_background "$input_filename.png" "${output_filename}_no_background.png"
                echo ""
                ;;
            2)
                read -p "Input image name (without extension): " input_filename
                read -p "Output image name (without extension): " output_filename
                apply_negative_effect "$input_filename.png" "${output_filename}_negative.png"
                echo ""
                ;;
            3)
                read -p "Input image name (without extension): " input_filename
                read -p "Enter the radius for low-pass filter (default is 2): " radius
                read -p "Output image name (without extension): " output_filename
                apply_low_pass_filter "$input_filename.png" "${output_filename}_filtered.png" "$radius"
                echo ""
                ;;
            4)
                read -p "Input image name (without extension): " input_filename
                read -p "Enter the binarization threshold (default is 25): " threshold
                read -p "Output image name (without extension): " output_filename
                binarize_image "$input_filename.png" "$threshold" "${output_filename}_binarized.png"
                echo ""
                ;;
            5)
                read -p "Input image name (without extension): " input_filename
                read -p "Output image name (without extension): " output_filename
                apply_edge_detection "$input_filename.png" "${output_filename}_edges.png"
                echo ""
                ;;
            6)
                read -p "Input image name (without extension): " input_filename
                read -p "Output DXF file name (without extension): " output_filename
                convert_image_to_dxf "$input_filename.png" "$output_filename"
                echo ""
                ;;
            7)
                read -p "Input image name (without extension): " input_filename
                read -p "Output RLD file name (without extension): " output_filename
                convert_image_to_rld "$input_filename.png" "$output_filename"
                echo ""
                ;;
            *)
                echo "Invalid choice. Please try again."
                echo ""
                ;;
        esac
    done
}

# Run the main function
main
