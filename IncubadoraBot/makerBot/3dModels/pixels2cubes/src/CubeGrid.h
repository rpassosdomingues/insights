#ifndef CUBEGRID_H
#define CUBEGRID_H

#include "Cube.h"
#include <vector>

// Classe responsável por gerar a grade de cubos baseada na matriz de brilho
class CubeGrid {
public:
    // Função que gera a grade de cubos
    // @param brightnessMatrix: matriz de brilho da imagem
    // @param resolution: número de cubos por unidade de área (controla a separação dos cubos)
    // @param depthFactor: fator de profundidade que controla a altura dos cubos
    void generateGrid(const std::vector<std::vector<double>>& brightnessMatrix, int resolution, double depthFactor);

    // Retorna a lista de cubos gerados
    const std::vector<Cube>& getCubes() const;

private:
    // Vetor que armazena todos os cubos gerados
    std::vector<Cube> cubes;
};

#endif // CUBEGRID_H
