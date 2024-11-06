/**
 * Simple example of code optimization called "constant folding."
 * Constant folding is a straightforward optimization that evaluates constant expressions
 * during compilation, replacing them with their computed values.
 * 
 * This optimization can help eliminate unnecessary computations and improve the efficiency
 * of the generated code.
 */

#include <iostream>
#include <string>
#include <unordered_map>
#include <cctype>

using namespace std;

enum TokenType { INTEGER, PLUS, MINUS, MUL, DIV, ASSIGN, ID, END };

struct Token {
    TokenType type;
    int value;
};

unordered_map<string, int> variables;
Token currentToken;
string input;
size_t position = 0;

Token getNextToken() {
    // Same as before...
}

int parseExpression();

int parseFactor() {
    Token token = currentToken;

    if (token.type == INTEGER) {
        currentToken = getNextToken();
        return token.value;
    } else if (token.type == ID) {
        currentToken = getNextToken();
        return variables[token.value];
    } else if (token.type == LPAREN) {
        // Same as before...
    }

    cout << "Error: Invalid factor." << endl;
    exit(1);
}

int parseTerm() {
    // Same as before...
}

int parseExpression() {
    int result = parseTerm();

    while (currentToken.type == PLUS || currentToken.type == MINUS) {
        Token token = currentToken;
        currentToken = getNextToken();

        if (token.type == PLUS) {
            result += parseTerm();
        } else if (token.type == MINUS) {
            result -= parseTerm();
        }
    }

    return result;
}

void parseAssignment() {
    // Same as before...
}

void parse() {
    currentToken = getNextToken();
    while (currentToken.type != END) {
        if (currentToken.type == ID) {
            parseAssignment();
        } else {
            int result = parseExpression();
            cout << "Result: " << result << endl;
        }
    }
}

// Constant Folding Optimization
int foldConstants(int value1, char oper, int value2) {
    switch (oper) {
        case '+':
            return value1 + value2;
        case '-':
            return value1 - value2;
        case '*':
            return value1 * value2;
        case '/':
            return value2 != 0 ? value1 / value2 : 0;
        default:
            return 0;
    }
}

int parseTerm() {
    int result = parseFactor();

    while (currentToken.type == MUL || currentToken.type == DIV) {
        Token token = currentToken;
        currentToken = getNextToken();

        int nextFactor = parseFactor();
        // Check if both operands are constants, then perform constant folding
        if (isdigit(result) && isdigit(nextFactor)) {
            result = foldConstants(result, token.type, nextFactor);
        } else {
            if (token.type == MUL) {
                result *= nextFactor;
            } else if (token.type == DIV) {
                if (nextFactor != 0) {
                    result /= nextFactor;
                } else {
                    cout << "Error: Division by zero." << endl;
                    exit(1);
                }
            }
        }
    }

    return result;
}

int main() {
    // Same as before...
}
