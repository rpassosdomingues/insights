# Nome do diretório de origem e saída
SRC_DIR = src
OUT_DIR = out

# Classe principal
MAIN_CLASS = Main

# Arquivos-fonte
SOURCES = $(wildcard $(SRC_DIR)/*.java)

# Definir o compilador e os parâmetros de compilação
JAVAC = javac
JAVA = java

# Caminho do JavaFX
JAVA_FX=/opt/javafx-21/lib

# Parâmetros de compilação com JavaFX
JAVAC_FLAGS = --module-path $(JAVA_FX) --add-modules javafx.controls,javafx.swing

# Parâmetros de execução com JavaFX
JAVA_FLAGS = --module-path $(JAVA_FX) --add-modules javafx.controls,javafx.swing

# Comando "all" compila e executa o programa
all: compile run

# Compilar todos os arquivos Java e redirecionar para a pasta 'out'
compile:
	@mkdir -p $(OUT_DIR)
	$(JAVAC) $(JAVAC_FLAGS) -d $(OUT_DIR) $(SOURCES)

# Executa o programa a partir da pasta 'out'
run: compile
	$(JAVA) $(JAVA_FLAGS) -cp $(OUT_DIR) $(SRC_DIR).$(MAIN_CLASS)

# Limpa os arquivos .class
clean:
	rm -rf $(OUT_DIR)/*.class
