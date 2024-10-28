#include "STLExporter.h"
#include <fstream>

using namespace std;

// Exporta os cubos para um arquivo STL
void STLExporter::exportToSTL(const string& filePath, const vector<Cube>& cubes) {
    ofstream stlFile(filePath); // Abre o arquivo STL para escrita

    // Verifica se o arquivo foi aberto corretamente
    if (!stlFile.is_open()) {
        cerr << "Erro ao abrir o arquivo para escrita: " << filePath << endl; // Mensagem de erro
        return; // Sai da função se não conseguir abrir o arquivo
    }

    // Inicia a definição de um objeto sólido no formato STL
    stlFile << "solid cube_grid\n";

    // Para cada cubo, gera seu formato STL
    for (const Cube& cube : cubes) {
        stlFile << cubeToSTL(cube); // Chama a função para converter cubo em formato STL
    }

    // Finaliza a definição do sólido
    stlFile << "endsolid cube_grid\n";

    stlFile.close(); // Fecha o arquivo
}

// Gera o formato STL para um cubo específico
string STLExporter::cubeToSTL(const Cube& cube) const {
    double halfSize = 0.5; // Define o tamanho padrão do cubo

    // Definição dos vértices do cubo no formato STL
    return "  facet normal 0 0 0\n"
           "    outer loop\n"
           "      vertex " + to_string(cube.getX() - halfSize) + " " +
           to_string(cube.getY() - halfSize) + " " +
           to_string(cube.getZ()) + "\n"
           "      vertex " + to_string(cube.getX() + halfSize) + " " +
           to_string(cube.getY() - halfSize) + " " +
           to_string(cube.getZ()) + "\n"
           "      vertex " + to_string(cube.getX() + halfSize) + " " +
           to_string(cube.getY() + halfSize) + " " +
           to_string(cube.getZ()) + "\n"
           "    endloop\n"
           "  endfacet\n" // Repete o mesmo padrão para os outros lados do cubo
           // (Add the other facets here, similar to the first facet)
           ;
}
