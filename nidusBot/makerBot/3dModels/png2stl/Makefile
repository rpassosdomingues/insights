# Caminho para o executável do Blender e para o script Python
BLENDER_PATH = /opt/blender-4.2/blender
SCRIPT_PATH = DisplacementMapping.py

# Alvo principal para rodar o script
run:
	$(BLENDER_PATH) --background --python $(SCRIPT_PATH)

# Limpeza de arquivos temporários ou cache (se aplicável)
clean:
	rm -rf *.tmp
