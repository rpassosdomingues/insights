/**
 * Embedded System Objective:
 * The embedded system will control an LED connected to a digital pin of an Arduino microcontroller.
 * The LED will blink in a specific pattern, and the user can change the blinking frequency using a
 * button connected to another digital pin of the Arduino.
 */

#include <Arduino.h>

// Define the used pins
const int ledPin = 13;
const int buttonPin = 2;

// Define the initial blinking frequency (in milliseconds)
unsigned long blinkInterval = 1000;

// Variables for controlling the blinking
bool ledState = false;
unsigned long previousMillis = 0;

void setup() {
    // Configure the pins as output or input
    pinMode(ledPin, OUTPUT);
    pinMode(buttonPin, INPUT_PULLUP);
}

void loop() {
    // Check if the button is pressed and adjust the blinking frequency
    if (digitalRead(buttonPin) == LOW) {
        // Button pressed, change the blinking frequency
        delay(50); // Small delay to avoid button bounce effect
        if (blinkInterval == 1000) {
            blinkInterval = 500;
        } else {
            blinkInterval = 1000;
        }
    }

    // Control the LED blinking
    unsigned long currentMillis = millis();
    if (currentMillis - previousMillis >= blinkInterval) {
        previousMillis = currentMillis;
        ledState = !ledState; // Toggle the LED state
        digitalWrite(ledPin, ledState);
    }
}
