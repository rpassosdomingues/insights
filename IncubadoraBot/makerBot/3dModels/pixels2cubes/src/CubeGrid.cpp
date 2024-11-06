#include "CubeGrid.h"

using namespace std;

// Gera a grade de cubos com base na matriz de brilho da imagem
void CubeGrid::generateGrid(const vector<vector<double>>& brightnessMatrix, int resolution, double depthFactor) {
    // Limpa a lista de cubos
    cubes.clear();

    int rows = brightnessMatrix.size(); // Número de linhas da matriz
    int cols = brightnessMatrix[0].size(); // Número de colunas da matriz

    // Para cada pixel da imagem, cria um cubo com altura proporcional ao brilho do pixel
    for (int i = 0; i < rows; i += resolution) { // Passa por cada 'resolução' linha
        for (int j = 0; j < cols; j += resolution) { // Passa por cada 'resolução' coluna
            double brightness = brightnessMatrix[i][j]; // Obtém o brilho do pixel
            if (brightness < 1.0) { // Apenas cria cubos se o brilho for menor que 1.0
                // Altura do cubo é baseada no brilho e no fator de profundidade
                cubes.emplace_back(i, j, depthFactor * (1 - brightness)); // Cria um cubo e adiciona à lista
            }
        }
    }
}

// Retorna a lista de cubos gerados
const vector<Cube>& CubeGrid::getCubes() const {
    return cubes; // Retorna todos os cubos gerados
}
