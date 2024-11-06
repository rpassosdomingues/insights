#!/bin/bash

# Atualiza os pacotes do sistema
sudo apt update

# Dependências do Blender
sudo apt install git build-essential cmake libx11-dev libxi-dev libxxf86vm-dev \
libxrandr-dev libpng-dev libjpeg-dev libtiff-dev libopenexr-dev \
libfreetype6-dev libpython3-dev python3-numpy python3-all-dev \
libsdl1.2-dev libfftw3-dev libopenal-dev libalut-dev libglew-dev \
libboost-all-dev libspnav-dev libyaml-cpp-dev libjemalloc-dev libssl-dev

# Instala o pacote python3-venv se ainda não estiver instalado
sudo apt install -y python3-venv

# Cria o ambiente virtual chamado 'myenv'
python3 -m venv myenv

# Ativa o ambiente virtual
source myenv/bin/activate

# Instala o bpy no ambiente virtual
pip install bpy

# Exibe uma mensagem de conclusão
echo "bpy instalado no ambiente virtual 'myenv'. Para ativar novamente o ambiente virtual, execute 'source myenv/bin/activate'."
