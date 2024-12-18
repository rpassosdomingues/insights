#ifndef GCODEGENERATOR_H
#define GCODEGENERATOR_H

#include <vector>
#include <string>
#include <opencv2/core.hpp>

class GCodeGenerator {
public:
    /**
     * Generates G-code based on contours provided.
     * @param externalContours: Contours for cutting paths.
     * @param internalContours: Contours for engraving paths.
     * @param outputFile: File path to save the G-code.
     */
    void generateGCode(const std::vector<std::vector<cv::Point>>& externalContours,
                       const std::vector<std::vector<cv::Point>>& internalContours,
                       const std::string& outputFile);

private:
    /**
     * Adds G-code movement commands.
     * @param gcodeFile: Output file stream for G-code.
     * @param point: Target coordinate for movement.
     * @param laserOn: Indicates if the laser should be active.
     */
    void addMovement(std::ofstream& gcodeFile, const cv::Point& point, bool laserOn);
};

#endif // GCODEGENERATOR_H
