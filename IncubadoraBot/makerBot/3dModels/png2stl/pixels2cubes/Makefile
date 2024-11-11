# Variáveis
CXX = g++
CXXFLAGS = -std=c++11 -I/usr/include/opencv4
SRC = src/main.cpp src/Cube.cpp src/ImageLoader.cpp src/CubeGrid.cpp src/STLExporter.cpp
OBJ = $(SRC:.cpp=.o)
TARGET = CubeImage

# Regra padrão: compila, limpa e executa o programa
all: clean $(TARGET) run

# Linkando os objetos para criar o executável
$(TARGET): $(OBJ)
	$(CXX) $(OBJ) -o $@

# Regra para compilar arquivos fonte em objetos
%.o: %.cpp
	$(CXX) $(CXXFLAGS) -c $< -o $@

# Limpar arquivos objeto e executável
clean:
	rm -f $(OBJ) $(TARGET)

# Regra para executar o programa
run: $(TARGET)
	./$(TARGET)

.PHONY: all clean run  # Declara regras que não são arquivos
