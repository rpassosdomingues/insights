#ifndef IMAGELOADER_H
#define IMAGELOADER_H

#include <string>
#include <vector>

// Classe responsável por carregar a imagem PNG e gerar uma matriz de brilho
class ImageLoader {
public:
    // Carrega a imagem a partir de um caminho de arquivo
    // @param filePath: caminho da imagem PNG
    // @return bool: retorna verdadeiro se a imagem foi carregada com sucesso
    bool loadImage(const std::string& filePath);

    // Retorna a matriz de brilho gerada
    std::vector<std::vector<double>> getBrightnessMatrix() const;

private:
    // Matriz que armazena os valores de brilho (0.0 a 1.0) da imagem
    std::vector<std::vector<double>> brightnessMatrix;
};

#endif // IMAGELOADER_H
