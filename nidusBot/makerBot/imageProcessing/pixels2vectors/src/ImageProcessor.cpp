#include "ImageProcessor.h"
#include <iostream>

/**
 * Constructor that initializes the image path.
 * @param imgPath: Path to the input image for processing.
 */
ImageProcessor::ImageProcessor(const std::string& imgPath) : imagePath(imgPath) {}

/**
 * Loads the image, applies thresholding to convert it to binary, and extracts contours.
 */
void ImageProcessor::processImage() {
    image = cv::imread(imagePath, cv::IMREAD_GRAYSCALE); // Load image in grayscale
    if (image.empty()) {
        std::cerr << "Failed to load image: " << imagePath << std::endl;
        return; // Exit if image loading fails
    }

    cv::Mat binaryImage;
    cv::threshold(image, binaryImage, 128, 255, cv::THRESH_BINARY); // Binarize the image

    std::vector<std::vector<cv::Point>> allContours;
    cv::findContours(binaryImage, allContours, cv::RETR_TREE, cv::CHAIN_APPROX_SIMPLE); // Extract contours

    // Separate contours based on area size into external and internal
    for (const auto& contour : allContours) {
        if (cv::contourArea(contour) > 500) {
            externalContours.push_back(contour); // Larger areas as external contours
        } else {
            internalContours.push_back(contour); // Smaller areas as internal contours
        }
    }
}

/**
 * Getter for external contours.
 * @return A vector of external contours for cutting paths.
 */
std::vector<std::vector<cv::Point>> ImageProcessor::getExternalContours() const {
    return externalContours;
}

/**
 * Getter for internal contours.
 * @return A vector of internal contours for engraving paths.
 */
std::vector<std::vector<cv::Point>> ImageProcessor::getInternalContours() const {
    return internalContours;
}
