JAVAC=javac
JAVA=java

# Diretórios
SRC_DIR=src
OUT_DIR=out

# Classe principal
MAIN_CLASS=CubeGridGenerator

# Caminho do JavaFX
JAVA_FX=/opt/javafx-21/lib

# Definindo os arquivos .java
SRC_FILES=$(wildcard $(SRC_DIR)/*.java)

# Alvo padrão
all: run

# Compilar arquivos Java
compile: | $(OUT_DIR)
	@echo "Limpando arquivos compilados..."
	rm -f $(OUT_DIR)/*.class
	@echo "Compilando arquivos..."
	$(JAVAC) --module-path $(JAVA_FX) --add-modules javafx.controls,javafx.swing -d $(OUT_DIR) $(SRC_FILES)

# Executar a aplicação
run: compile
	@echo "Executando a aplicação..."
	$(JAVA) --module-path $(JAVA_FX) --add-modules javafx.controls,javafx.swing -cp $(OUT_DIR) $(MAIN_CLASS)

# Criar diretório de saída se não existir
$(OUT_DIR):
	mkdir -p $(OUT_DIR)

.PHONY: all compile run
