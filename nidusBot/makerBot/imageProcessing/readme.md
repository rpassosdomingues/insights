# Images Processing

Author: Rafael Passos Domingues

This application reads an image in PNG format as input. It then creates an interactive menu where the user can choose different operations to be performed on the input image. Each operation is implemented as a separate function.

## Available Operations:

- **Binarize the Image**: Converts the image into a binary image based on a threshold entered by the user. Pixels with intensities above the threshold are set to white, while pixels with intensities below or equal to the threshold are set to black.

- **Apply Low-Pass Filter**: Smooths the image by applying a low-pass filter. This filter helps to reduce noise and detail in the image, resulting in a smoother appearance. The user can specify the radius of the filter, which controls the extent of smoothing applied to the image.

- **Apply Negative Effect**: Inverts the colors of the image, creating a negative image. Each pixel's color channels are subtracted from 255 to produce the inverted effect.

- **Apply Edge Detection**: Detects edges in the image using the Sobel operator.

- **Remove Background**: Removes the background from the image.

## Integration with Other Technologies:

I initially prototyped this image processing functionality using shell script commands from Image Magick. Now, I am exploring the integration of machine learning techniques to prepare any type of image for CNC processing. This integration aims to automate and enhance the image preparation process for CNC machining.

## Usage:

After performing each operation, the application writes the resulting image to a separate file.

Additionally, all operations ensure that the pixel grayscale values are within the range of 0 to a specified maximum value.
