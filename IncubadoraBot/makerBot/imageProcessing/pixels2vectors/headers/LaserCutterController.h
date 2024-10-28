#ifndef LASERCUTTERCONTROLLER_H
#define LASERCUTTERCONTROLLER_H

#include <string>

class LaserCutterController {
public:
    /**
     * Constructor to initialize with laser cutter IP.
     * @param ip: IP address of the laser cutter.
     */
    LaserCutterController(const std::string& ip);

    /**
     * Sends G-code file to the laser cutter.
     * @param gcodeFile: Path to the G-code file.
     */
    void sendGCode(const std::string& gcodeFile);

private:
    std::string cutterIP;
};

#endif // LASERCUTTERCONTROLLER_H
