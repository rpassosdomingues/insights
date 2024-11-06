#include "Cube.h"

// Implementação do construtor Cube
Cube::Cube(double x, double y, double z) : x(x), y(y), z(z) {}

// Retorna a posição X do cubo
double Cube::getX() const {
    return x;
}

// Retorna a posição Y do cubo
double Cube::getY() const {
    return y;
}

// Retorna a posição Z (altura) do cubo
double Cube::getZ() const {
    return z;
}
