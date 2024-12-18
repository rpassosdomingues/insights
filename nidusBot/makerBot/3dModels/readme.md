# 3D Model Creator from 2D Images

## Author: Rafael Passos Domingues

This project aims to create a system capable of generating 3D models from 2D images, using image processing techniques and 3D modeling. The code consists of several steps:

1. **Image Preprocessing**:
   - The input image is preprocessed to remove the background and binarize it, keeping only the objects of interest.

2. **Contour Detection**:
   - Contours of objects in the binarized image are identified using image processing techniques.

3. **3D Modeling Creation**:
   - Based on the detected contours, a 3D modeling is created in Blender software, where each black pixel in the binarized image is represented as a vertex in the 3D modeling.

4. **Export as STL File**:
   - The created 3D modeling is exported as an STL file, which can be used in various 3D modeling and 3D printing software.

5. **Interaction with Blender through API**:
   - The Python code interacts with Blender through its API to create the 3D modeling and export the STL file directly from Blender.

## Usage

1. Make sure you have all dependencies installed, including Blender and the Python libraries listed in the `requirements.txt` file.

2. Place your input images in the `images` folder.

3. Run the `main.py` script and follow the instructions to input the filename of the input image.

4. The script will process the image, create the 3D modeling, and export the STL file in the `output` folder.

## Dependencies

- Python 3
- TensorFlow
- OpenCV
- Pillow
- Blender

## Notes

- This project is a basic implementation and can be expanded to handle more complex cases, such as multiple objects in the image, different shapes of objects, etc.
- Make sure to adjust the parameters and code as needed to fit your specific requirements.
