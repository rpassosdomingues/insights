#ifndef CUBE_H
#define CUBE_H

// Classe que representa um cubo na grade 3D
class Cube {
public:
    // Construtor da classe Cube
    // @param x: posição X do cubo
    // @param y: posição Y do cubo
    // @param z: altura do cubo (determinada pelo brilho do pixel)
    Cube(double x, double y, double z);

    // Funções getter para retornar as posições do cubo
    double getX() const; // Retorna a posição X do cubo
    double getY() const; // Retorna a posição Y do cubo
    double getZ() const; // Retorna a posição Z (altura) do cubo

private:
    // Coordenadas do cubo (x, y, z)
    double x, y, z;
};

#endif // CUBE_H
