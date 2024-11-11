#include "ImageLoader.h"
#include <iostream>
#include <opencv2/opencv.hpp>  // Biblioteca OpenCV para carregar a imagem

using namespace std;

// Função que carrega a imagem em tons de cinza e converte para matriz de brilho
bool ImageLoader::loadImage(const string& filePath) {
    // Carrega a imagem em escala de cinza
    cv::Mat image = cv::imread(filePath, cv::IMREAD_GRAYSCALE);

    // Verifica se a imagem foi carregada corretamente
    if (image.empty()) {
        cerr << "Erro ao carregar a imagem!" << endl;
        return false;
    }

    // Redimensiona a matriz para armazenar os valores de brilho
    brightnessMatrix.resize(image.rows, vector<double>(image.cols));

    // Converte cada pixel da imagem em um valor de brilho normalizado (0.0 a 1.0)
    for (int i = 0; i < image.rows; i++) {
        for (int j = 0; j < image.cols; j++) {
            brightnessMatrix[i][j] = image.at<uchar>(i, j) / 255.0; // Normaliza o brilho
        }
    }

    return true; // Retorna verdadeiro se o carregamento foi bem-sucedido
}

// Retorna a matriz de brilho gerada
vector<vector<double>> ImageLoader::getBrightnessMatrix() const {
    return brightnessMatrix;
}
