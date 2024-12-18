#ifndef IMAGEPROCESSOR_H
#define IMAGEPROCESSOR_H

#include <opencv2/opencv.hpp>
#include <string>
#include <vector>

class ImageProcessor {
public:
    /**
     * Constructor to initialize with image path.
     * @param imgPath: Path to the image file.
     */
    ImageProcessor(const std::string& imgPath);

    /**
     * Processes the image to extract contours.
     */
    void processImage();

    /**
     * Gets external contours (cut paths).
     * @return Vector of external contours.
     */
    std::vector<std::vector<cv::Point>> getExternalContours() const;

    /**
     * Gets internal contours (engrave paths).
     * @return Vector of internal contours.
     */
    std::vector<std::vector<cv::Point>> getInternalContours() const;

private:
    std::string imagePath;
    cv::Mat image;
    std::vector<std::vector<cv::Point>> externalContours;
    std::vector<std::vector<cv::Point>> internalContours;
};

#endif // IMAGEPROCESSOR_H
