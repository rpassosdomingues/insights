/**
 * C++ compiler that handles simple arithmetic expressions and variable assignments
 * and generates assembly-like code for a simple stack-based architecture
 */

#include <iostream>
#include <string>
#include <unordered_map>

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
    if (position >= input.length()) {
        return {END, 0};
    }

    char currentChar = input[position];
    position++;

    if (isdigit(currentChar)) {
        return {INTEGER, currentChar - '0'};
    } else if (currentChar == '+') {
        return {PLUS, 0};
    } else if (currentChar == '-') {
        return {MINUS, 0};
    } else if (currentChar == '*') {
        return {MUL, 0};
    } else if (currentChar == '/') {
        return {DIV, 0};
    } else if (currentChar == '=') {
        return {ASSIGN, 0};
    } else if (isalpha(currentChar)) {
        return {ID, currentChar};
    }

    return {END, 0};
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
        currentToken = getNextToken();
        int result = parseExpression();
        if (currentToken.type != RPAREN) {
            cout << "Error: Missing closing parenthesis." << endl;
            exit(1);
        }
        currentToken = getNextToken();
        return result;
    }

    cout << "Error: Invalid factor." << endl;
    exit(1);
}

int parseTerm() {
    int result = parseFactor();

    while (currentToken.type == MUL || currentToken.type == DIV) {
        Token token = currentToken;
        currentToken = getNextToken();

        if (token.type == MUL) {
            result *= parseFactor();
        } else if (token.type == DIV) {
            int divisor = parseFactor();
            if (divisor != 0) {
                result /= divisor;
            } else {
                cout << "Error: Division by zero." << endl;
                exit(1);
            }
        }
    }

    return result;
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
    Token token = currentToken;
    currentToken = getNextToken();
    if (currentToken.type != ASSIGN) {
        cout << "Error: Invalid assignment." << endl;
        exit(1);
    }
    currentToken = getNextToken();
    int value = parseExpression();
    variables[token.value] = value;
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

int main() {
    cout << "Enter the source code: ";
    getline(cin, input);

    parse();

    return 0;
}
