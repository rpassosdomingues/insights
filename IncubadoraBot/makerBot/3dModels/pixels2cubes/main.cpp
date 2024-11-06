#include <iostream>
#include "ImageLoader.h"
#include "CubeGrid.h"
#include "STLExporter.h"

using namespace std;

// Constantes para configuração
const string IMAGE_PATH = "images/rompe_tormentas.png";  // Caminho da imagem
const string OUTPUT_STL_PATH = "output.stl";         // Caminho para o arquivo STL de saída
const int RESOLUTION = 1.0;                             // Resolução dos cubos
const double DEPTH_FACTOR = 3.0;                       // Fator de profundidade

int main() {
    ImageLoader loader; // Cria um objeto ImageLoader
    if (!loader.loadImage(IMAGE_PATH)) { // Tenta carregar a imagem
        return 1; // Sai do programa se não conseguir carregar a imagem
    }

    // Obtém a matriz de brilho da imagem carregada
    vector<vector<double>> brightnessMatrix = loader.getBrightnessMatrix();

    CubeGrid grid; // Cria um objeto CubeGrid
    grid.generateGrid(brightnessMatrix, RESOLUTION, DEPTH_FACTOR); // Gera a grade de cubos

    STLExporter exporter; // Cria um objeto STLExporter
    exporter.exportToSTL(OUTPUT_STL_PATH, grid.getCubes()); // Exporta a grade de cubos para um arquivo STL

    cout << "Exportação concluída com sucesso!" << endl; // Mensagem de sucesso

    return 0; // Finaliza o programa com sucesso
}
