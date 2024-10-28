#include <iostream>
#include <vector>
#include <GL/glew.h>
#include <GLFW/glfw3.h>
#include <stb_image.h>

struct Cube {
    float x, y, z;
    float size;
};

// Função para gerar uma grade de cubos a partir de uma imagem
std::vector<Cube> generateCubeGrid(const char* imagePath, float cubeSize, float cubeHeight, float resolutionScale) {
    int width, height, channels;
    unsigned char* data = stbi_load(imagePath, &width, &height, &channels, 0);

    if (!data) {
        std::cerr << "Failed to load image" << std::endl;
        return {};
    }

    width = static_cast<int>(width * resolutionScale);
    height = static_cast<int>(height * resolutionScale);

    std::vector<Cube> cubes;

    // Loop pelos pixels da imagem
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // Verificar se o pixel é "branco" (ou seja, cria um cubo)
            int index = (y * width + x) * channels;
            if (data[index] > 127) {  // Ajuste a condição conforme necessário
                cubes.push_back({x * cubeSize, y * cubeSize, cubeHeight / 2, cubeSize});
            }
        }
    }

    stbi_image_free(data); // Liberar a memória da imagem
    return cubes;
}

// Função para renderizar um cubo
void renderCube(const Cube& cube) {
    glBegin(GL_QUADS);
    
    // Frente
    glVertex3f(cube.x - cube.size / 2, cube.y - cube.size / 2, cube.z + cube.size / 2);
    glVertex3f(cube.x + cube.size / 2, cube.y - cube.size / 2, cube.z + cube.size / 2);
    glVertex3f(cube.x + cube.size / 2, cube.y + cube.size / 2, cube.z + cube.size / 2);
    glVertex3f(cube.x - cube.size / 2, cube.y + cube.size / 2, cube.z + cube.size / 2);

    // Traseira
    glVertex3f(cube.x - cube.size / 2, cube.y - cube.size / 2, cube.z - cube.size / 2);
    glVertex3f(cube.x - cube.size / 2, cube.y + cube.size / 2, cube.z - cube.size / 2);
    glVertex3f(cube.x + cube.size / 2, cube.y + cube.size / 2, cube.z - cube.size / 2);
    glVertex3f(cube.x + cube.size / 2, cube.y - cube.size / 2, cube.z - cube.size / 2);

    // Direita
    glVertex3f(cube.x + cube.size / 2, cube.y - cube.size / 2, cube.z + cube.size / 2);
    glVertex3f(cube.x + cube.size / 2, cube.y - cube.size / 2, cube.z - cube.size / 2);
    glVertex3f(cube.x + cube.size / 2, cube.y + cube.size / 2, cube.z - cube.size / 2);
    glVertex3f(cube.x + cube.size / 2, cube.y + cube.size / 2, cube.z + cube.size / 2);

    // Esquerda
    glVertex3f(cube.x - cube.size / 2, cube.y - cube.size / 2, cube.z + cube.size / 2);
    glVertex3f(cube.x - cube.size / 2, cube.y + cube.size / 2, cube.z + cube.size / 2);
    glVertex3f(cube.x - cube.size / 2, cube.y + cube.size / 2, cube.z - cube.size / 2);
    glVertex3f(cube.x - cube.size / 2, cube.y - cube.size / 2, cube.z - cube.size / 2);

    // Cima
    glVertex3f(cube.x - cube.size / 2, cube.y + cube.size / 2, cube.z + cube.size / 2);
    glVertex3f(cube.x + cube.size / 2, cube.y + cube.size / 2, cube.z + cube.size / 2);
    glVertex3f(cube.x + cube.size / 2, cube.y + cube.size / 2, cube.z - cube.size / 2);
    glVertex3f(cube.x - cube.size / 2, cube.y + cube.size / 2, cube.z - cube.size / 2);

    // Baixo
    glVertex3f(cube.x - cube.size / 2, cube.y - cube.size / 2, cube.z + cube.size / 2);
    glVertex3f(cube.x - cube.size / 2, cube.y - cube.size / 2, cube.z - cube.size / 2);
    glVertex3f(cube.x + cube.size / 2, cube.y - cube.size / 2, cube.z - cube.size / 2);
    glVertex3f(cube.x + cube.size / 2, cube.y - cube.size / 2, cube.z + cube.size / 2);

    glEnd();
}

// Função principal
int main() {
    // Inicializar GLFW
    if (!glfwInit()) {
        std::cerr << "Failed to initialize GLFW" << std::endl;
        return -1;
    }

    GLFWwindow* window = glfwCreateWindow(800, 600, "Cube Grid Generator", NULL, NULL);
    if (!window) {
        std::cerr << "Failed to create window" << std::endl;
        glfwTerminate();
        return -1;
    }

    glfwMakeContextCurrent(window);
    glewInit();
    glEnable(GL_DEPTH_TEST); // Habilitar teste de profundidade

    // Parâmetros da grade de cubos
    const char* imagePath = "images/massa.png";
    float cubeSize = 1.0f; // Tamanho do cubo
    float cubeHeight = 1.0f; // Altura do cubo
    float resolutionScale = 0.1f; // Fator de escala da resolução da imagem

    // Gerar a grade de cubos
    std::vector<Cube> cubes = generateCubeGrid(imagePath, cubeSize, cubeHeight, resolutionScale);

    // Loop de renderização
    while (!glfwWindowShouldClose(window)) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Limpar buffers

        glLoadIdentity();
        glTranslatef(0.0f, 0.0f, -10.0f); // Ajustar a posição da câmera

        // Renderizar cubos
        for (const auto& cube : cubes) {
            renderCube(cube);
        }

        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    glfwDestroyWindow(window);
    glfwTerminate();
    return 0;
}
