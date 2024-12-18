# Variáveis de compilação
CXX = g++
CXXFLAGS = -std=c++11 -Wall -O2
LDFLAGS = -lstdc++ -lOpenImageIO -lBlender -lopencv_core -lopencv_imgproc -lopencv_highgui

# Caminho completo para o ambiente 'myenv' no WSL
MYENV_DIR = /mnt/e/cache/IncubadoraBot/makerBot/3dModels/png2stl/myenv

# Caminhos específicos para o 'myenv'
MYENV_BIN_DIR = $(MYENV_DIR)/bin
MYENV_INCLUDE_DIR = $(MYENV_DIR)/include
MYENV_LIB_DIR = $(MYENV_DIR)/lib

# Diretórios de fonte, objetos e binários
SRC_DIR = src
BUILD_DIR = build
OUT_DIR = bin

# Arquivos fontes e objetos
SOURCES = $(SRC_DIR)/main.cpp
OBJECTS = $(SOURCES:$(SRC_DIR)/%.cpp=$(BUILD_DIR)/%.o)
EXEC = $(OUT_DIR)/model_generator

# Caminhos para o OpenCV do sistema
OPENCV_INCLUDE_DIR = /usr/include/opencv4
OPENCV_LIB_DIR = /usr/lib/x86_64-linux-gnu

# Regra padrão
all: clean $(EXEC) run

# Como compilar os arquivos .cpp em arquivos .o
$(BUILD_DIR)/%.o: $(SRC_DIR)/%.cpp
	$(CXX) $(CXXFLAGS) -I$(OPENCV_INCLUDE_DIR) -I$(MYENV_INCLUDE_DIR) -c $< -o $@

# Como linkar os arquivos objetos e gerar o executável
$(EXEC): $(OBJECTS)
	$(CXX) $(CXXFLAGS) $(OBJECTS) -o $(EXEC) -L$(OPENCV_LIB_DIR) -L$(MYENV_LIB_DIR) $(LDFLAGS)

# Limpeza de arquivos gerados
clean:
	rm -rf $(BUILD_DIR)/*.o $(EXEC)

# Rodar o programa gerado
run: $(EXEC)
	./$(EXEC)
