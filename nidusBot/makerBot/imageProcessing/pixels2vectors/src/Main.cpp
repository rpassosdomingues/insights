#include "ImageProcessor.h"
#include "GCodeGenerator.h"
#include "LaserCutterController.h"
#include <iostream>

/**
 * Main function that coordinates the pipeline of processing the image,
 * generating the G-code, and sending it to the laser cutter.
 */
int main() {
    // Step 1: Process the input image
    ImageProcessor imgProc("image.png");
    imgProc.processImage();

    // Step 2: Generate G-code based on contours from the image
    GCodeGenerator gcodeGen;
    gcodeGen.generateGCode(imgProc.getExternalContours(), imgProc.getInternalContours(), "output.gcode");

    // Step 3: Send G-code to the laser cutter over the network
    LaserCutterController laserController("192.168.1.100");
    laserController.sendGCode("output.gcode");

    return 0; // Exit program
}
