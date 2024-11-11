#include <opencv2/opencv.hpp>
#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>

class ImageProcessor {
public:
    ImageProcessor(const std::string& imagePath, cv::Size resolution = cv::Size(600, 400))
        : imagePath(imagePath), resolution(resolution) {}

    void loadAndPreprocessImage() {
        // Carregar a imagem e converter para escala de cinza
        image = cv::imread(imagePath, cv::IMREAD_GRAYSCALE);
        if (image.empty()) {
            std::cerr << "Erro ao carregar a imagem!" << std::endl;
            return;
        }
        // Redimensionar a imagem
        cv::resize(image, image, resolution);
        // Detectar bordas (similar ao filtro FIND_EDGES)
        cv::Canny(image, image, 100, 200);
    }

    // Extrair contornos da imagem
    std::vector<cv::Point> getContours() {
        std::vector<cv::Point> contours;
        for (int y = 0; y < image.rows; ++y) {
            for (int x = 0; x < image.cols; ++x) {
                if (image.at<uchar>(y, x) > 50) {  // Usando limite de intensidade
                    contours.push_back(cv::Point(x, y));
                }
            }
        }
        return contours;
    }

    cv::Mat getImage() {
        return image;
    }

private:
    std::string imagePath;
    cv::Size resolution;
    cv::Mat image;
};

class CubeGenerator {
public:
    CubeGenerator(ImageProcessor& imageProcessor, float cubeSize, float cubeHeight)
        : imageProcessor(imageProcessor), cubeSize(cubeSize), cubeHeight(cubeHeight) {}

    void generateCubes(const std::vector<cv::Point>& contours) {
        // Gerar cubos de acordo com os contornos detectados
        for (const auto& point : contours) {
            createCube(point.x, point.y);
        }
    }

    void createCube(int x, int y) {
        // Criar cubos no formato STL (sem Blender)
        // Aqui apenas simulamos a criação de cubos, você pode gerar coordenadas para STL
        cubes.push_back({x * cubeSize, y * cubeSize, cubeHeight / 2.0f});
    }

    std::vector<std::array<float, 3>> getCubes() {
        return cubes;
    }

private:
    ImageProcessor& imageProcessor;
    float cubeSize;
    float cubeHeight;
    std::vector<std::array<float, 3>> cubes;  // Lista de cubos com suas posições
};

class STLExporter {
public:
    void writeSTL(const std::vector<std::array<float, 3>>& cubes, const std::string& filepath) {
        std::ofstream file(filepath, std::ios::out | std::ios::binary);
        if (!file.is_open()) {
            std::cerr << "Erro ao abrir o arquivo STL!" << std::endl;
            return;
        }

        file.write("solid BlenderSTL\n", 17);  // Cabeçalho para o arquivo STL

        for (const auto& cube : cubes) {
            // Gerar um cubo simples (6 faces)
            generateCubeFaces(file, cube[0], cube[1], cube[2]);
        }

        file.write("endsolid BlenderSTL\n", 19);  // Finalização do arquivo STL
        file.close();
        std::cout << "Modelo exportado com sucesso para: " << filepath << std::endl;
    }

private:
    void generateCubeFaces(std::ofstream& file, float x, float y, float z) {
        // Gerar as faces do cubo
        // Aqui, estamos gerando um cubo simples com 6 faces.
        // Você pode expandir isso para um modelo mais detalhado, caso necessário.

        // Apenas um exemplo de como escrever as coordenadas dos vértices no arquivo STL
        // (Gerar um cubo com 2x2x2 de lado, centrado nas coordenadas x, y, z)
        float vertices[8][3] = {
            {x - 0.5f, y - 0.5f, z - 0.5f},
            {x + 0.5f, y - 0.5f, z - 0.5f},
            {x + 0.5f, y + 0.5f, z - 0.5f},
            {x - 0.5f, y + 0.5f, z - 0.5f},
            {x - 0.5f, y - 0.5f, z + 0.5f},
            {x + 0.5f, y - 0.5f, z + 0.5f},
            {x + 0.5f, y + 0.5f, z + 0.5f},
            {x - 0.5f, y + 0.5f, z + 0.5f}
        };

        // Exemplo de uma face (triângulo) do cubo
        file.write("  facet normal 0 0 0\n  outer loop\n", 30);
        file.write("    vertex " + std::to_string(vertices[0][0]) + " " + std::to_string(vertices[0][1]) + " " + std::to_string(vertices[0][2]) + "\n", 100);
        file.write("    vertex " + std::to_string(vertices[1][0]) + " " + std::to_string(vertices[1][1]) + " " + std::to_string(vertices[1][2]) + "\n", 100);
        file.write("    vertex " + std::to_string(vertices[2][0]) + " " + std::to_string(vertices[2][1]) + " " + std::to_string(vertices[2][2]) + "\n", 100);
        file.write("  endloop\n  endfacet\n", 23);
    }
};

int main() {
    // Caminho da imagem original
    std::string imagePath = "images/facebook.png";
    // Tamanho do cubo
    float cubeSize = 1.0f;
    // Altura do cubo
    float cubeHeight = 1.0f;

    // Processar a imagem
    ImageProcessor imageProcessor(imagePath);
    imageProcessor.loadAndPreprocessImage();

    // Obter contornos e gerar cubos
    CubeGenerator cubeGenerator(imageProcessor, cubeSize, cubeHeight);
    std::vector<cv::Point> contours = imageProcessor.getContours();
    cubeGenerator.generateCubes(contours);

    // Exportar para STL
    STLExporter stlExporter;
    stlExporter.writeSTL(cubeGenerator.getCubes(), "output_model.stl");

    return 0;
}
