#include "GCodeGenerator.h"
#include <fstream>
#include <iostream>

/**
 * Generates G-code from contours and writes it to the specified file.
 * @param externalContours: Contours for cutting paths.
 * @param internalContours: Contours for engraving paths.
 * @param outputFile: File path where the G-code is saved.
 */
void GCodeGenerator::generateGCode(const std::vector<std::vector<cv::Point>>& externalContours,
                                   const std::vector<std::vector<cv::Point>>& internalContours,
                                   const std::string& outputFile) {
    std::ofstream gcodeFile(outputFile); // Open the output file stream
    if (!gcodeFile.is_open()) {
        std::cerr << "Failed to open file for G-code: " << outputFile << std::endl;
        return; // Exit if file cannot be opened
    }

    // Initialize G-code settings for units and positioning
    gcodeFile << "G21 ; Set units to millimeters\n";
    gcodeFile << "G90 ; Absolute positioning\n";

    // Process each external contour for cutting
    for (const auto& contour : externalContours) {
        gcodeFile << "M03 S65 ; Cutting laser power\n";
        for (size_t i = 0; i < contour.size(); i++) {
            addMovement(gcodeFile, contour[i], i != 0); // Laser on/off control
        }
        gcodeFile << "M05 ; Laser off\n";
    }

    // Process each internal contour for engraving
    for (const auto& contour : internalContours) {
        gcodeFile << "M03 S10 ; Engraving laser power\n";
        for (size_t i = 0; i < contour.size(); i++) {
            addMovement(gcodeFile, contour[i], i != 0); // Laser on/off control
        }
        gcodeFile << "M05 ; Laser off\n";
    }

    gcodeFile << "G00 X0 Y0 ; Return to origin\n"; // Return to starting position
    gcodeFile.close(); // Close the G-code file
}

/**
 * Adds movement commands to the G-code, either rapid or linear depending on laser state.
 * @param gcodeFile: Output file stream for writing G-code.
 * @param point: Coordinate for the next movement.
 * @param laserOn: If true, the laser is on; if false, the laser is off.
 */
void GCodeGenerator::addMovement(std::ofstream& gcodeFile, const cv::Point& point, bool laserOn) {
    // G00 is rapid movement (laser off), G01 is linear movement (laser on)
    gcodeFile << (laserOn ? "G01" : "G00") << " X" << point.x << " Y" << point.y << "\n";
}
