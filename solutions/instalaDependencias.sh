#!/bin/bash

# Atualizar pacotes e instalar o Python e o pip
sudo apt update
sudo apt install -y python3 python3-pip

# Instalar as bibliotecas necessárias
pip3 install requests beautifulsoup4 selenium pandas openpyxl

# Verificar se o geckodriver está instalado e, se não, instalá-lo
if ! command -v geckodriver &> /dev/null; then
    # Baixar e extrair o geckodriver
    wget https://github.com/mozilla/geckodriver/releases/download/v0.30.0/geckodriver-v0.30.0-linux64.tar.gz
    tar -xzf geckodriver-v0.30.0-linux64.tar.gz

    # Mover o geckodriver para o diretório de execução
    sudo mv geckodriver /usr/local/bin/

    # Remover arquivos temporários
    rm geckodriver-v0.30.0-linux64.tar.gz
fi
