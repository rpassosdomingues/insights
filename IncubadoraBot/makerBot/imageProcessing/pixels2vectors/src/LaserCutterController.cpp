#include "LaserCutterController.h"
#include <iostream>
#include <cstdlib>

/**
 * Constructor initializing the controller with laser cutter's IP address.
 * @param ip: IP address of the laser cutter machine.
 */
LaserCutterController::LaserCutterController(const std::string& ip) : cutterIP(ip) {}

/**
 * Sends the G-code file to the laser cutter over the network using SCP.
 * @param gcodeFile: The path to the G-code file for cutting/engraving.
 */
void LaserCutterController::sendGCode(const std::string& gcodeFile) {
    // Construct the SCP command to transfer the file to the laser cutter
    std::string command = "scp " + gcodeFile + " user@" + cutterIP + ":/path/to/laser/machine/";
    int status = std::system(command.c_str()); // Execute the SCP command
    if (status == 0) {
        std::cout << "G-code sent successfully.\n"; // Confirm successful transfer
    } else {
        std::cerr << "Failed to send G-code.\n"; // Report failure
    }
}
