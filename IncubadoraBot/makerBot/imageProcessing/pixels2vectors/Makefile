# Compiler
CXX = g++
CXXFLAGS = -std=c++11 -Iheaders -I/usr/include/opencv4

# Directories
SRC_DIR = src
HDR_DIR = headers
OBJ_DIR = obj
BIN_DIR = bin

# Source files
SOURCES = $(wildcard $(SRC_DIR)/*.cpp)
# Object files
OBJECTS = $(patsubst $(SRC_DIR)/%.cpp,$(OBJ_DIR)/%.o,$(SOURCES))

# Executable name
EXECUTABLE = $(BIN_DIR)/pixels2vectors

# Default target
all: $(EXECUTABLE)

# Link object files to create the executable
$(EXECUTABLE): $(OBJECTS)
	@mkdir -p $(BIN_DIR)
	$(CXX) $(OBJECTS) -o $@

# Compile source files to object files, using headers in headers directory
$(OBJ_DIR)/%.o: $(SRC_DIR)/%.cpp $(HDR_DIR)/%.h
	@mkdir -p $(OBJ_DIR)
	$(CXX) $(CXXFLAGS) -c $< -o $@

# Clean up the build
clean:
	rm -rf $(OBJ_DIR) $(BIN_DIR)

# Run the application
run: $(EXECUTABLE)
	$(EXECUTABLE)

# Phony targets
.PHONY: all clean run
