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
    echo "---------------------------"
}

# Main function to process the image
main() {
    display_menu
    
    while true; do
        read -p "Enter your choice (0-5): " choice
        
        case $choice in
            0)
                echo "Exiting program."
                break
                ;;
            1)
                read -p "Input image name (without extension): " input_filename
                #read -p "Enter the background color (RGB format, e.g., 255,255,255 for white): " background_color
                read -p "Output image name (without extension): " output_filename
                remove_background "$input_filename.png" "${output_filename}_no_background.png" "$background_color"
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
            *)
                echo "Invalid choice. Please try again."
                echo ""
                ;;
        esac
    done
}

# Run the main function
main
