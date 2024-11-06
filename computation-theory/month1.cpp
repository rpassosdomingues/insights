/*
 * Simple C++ interpreter that reads specific commands defined by a formal grammar and performs
 * corresponding actions.
 * 
 * In this example, the interpreter will support commands to perform basic arithmetic operations
 * (addition, subtraction, multiplication and division) between two integers.
 */
#include <iostream>
#include <string>
#include <sstream>

using namespace std;

// Function to perform arithmetic operations
int calculate(int num1, int num2, char oper) {
    switch (oper) {
        case '+':
            return num1 + num2;
        case '-':
            return num1 - num2;
        case '*':
            return num1 * num2;
        case '/':
            return num2 != 0 ? num1 / num2 : 0; // Division by zero is handled as 0
        default:
            return 0; // Invalid operator, default return 0
    }
}

int main() {
    string command;
    while (true) {
        cout << "Enter a command (or 'exit' to quit): ";
        getline(cin, command);

        if (command == "exit") {
            cout << "Exiting the interpreter..." << endl;
            break;
        }

        stringstream ss(command);
        char oper;
        int num1, num2;

        // Read values from the command
        ss >> num1 >> oper >> num2;

        // Check if the read was successful and the operator is valid
        if (!ss.fail() && (oper == '+' || oper == '-' || oper == '*' || oper == '/')) {
            // Perform the operation
            int result = calculate(num1, num2, oper);

            // Display the result
            cout << "Result: " << result << endl;
        } else {
            cout << "Invalid command. Please try again." << endl;
        }
    }

    return 0;
}
