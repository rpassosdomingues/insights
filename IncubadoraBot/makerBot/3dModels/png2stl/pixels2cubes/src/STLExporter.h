#ifndef STLEXPORTER_H
#define STLEXPORTER_H

#include "Cube.h"
#include <string>
#include <vector>

// Classe responsável por exportar a grade de cubos para um arquivo STL
class STLExporter {
public:
    // Exporta a lista de cubos para um arquivo STL
    // @param filePath: caminho do arquivo STL
    // @param cubes: vetor de cubos a serem exportados
    void exportToSTL(const std::string& filePath, const std::vector<Cube>& cubes);

private:
    // Gera o formato STL para um cubo específico
    std::string cubeToSTL(const Cube& cube) const;
};

#endif // STLEXPORTER_H
