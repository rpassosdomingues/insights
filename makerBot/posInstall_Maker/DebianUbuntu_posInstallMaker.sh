#!/bin/bash

# -------------------------------------------------------------------------------------
# [Linux Base Debian/Ubuntu v.24.04 LTS] Script de instalação de Softwares Maker
# -------------------------------------------------------------------------------------
# 1. Verifica se o sistema é Ubuntu ou Linux Mint.
# 2. Remove todos os snaps (no caso do Ubuntu).
# 3. Instala o Flatpak e adiciona o repositório Flathub (no caso do Ubuntu).
# 4. Clona e compila o ImageMagick a partir do código-fonte.
# 5. Instala dependências do ImageMagick usando o apt.
# 6. Instala uma série de aplicativos Flatpak do repositório Flathub.
# 7. Instala pacotes Python via pip, incluindo bibliotecas para computação científica,
# aprendizado de máquina, visualização e automação.
# 8. Atualiza todos os flatpaks instalados.
# 9. Atualiza o sistema usando o apt.
# 10. Limpa pacotes desnecessários para liberar espaço.
# -------------------------------------------------------------------------------------
# Autor: Rafael Passos Domingues
# Última Atualização: 2024 - 07 - 08
# -------------------------------------------------------------------------------------

# Função para tratamento de erro
handle_error() {
    echo "Erro na linha $1"
    exit 1
}

# Capturar erros
trap 'handle_error $LINENO' ERR

# Verificar se o sistema é Ubuntu ou Mint
if grep -q "Ubuntu" /etc/os-release; then
    echo "Sistema: Ubuntu"
    SYSTEM="Ubuntu"

    # Remover todos os snaps
    echo "Removendo snaps..."
    sudo snap remove --purge $(snap list | awk '!/^Name|^core|^snapd/ {print $1}') || true
    sudo apt-get remove --purge snapd -y
    sudo rm -rf ~/snap /snap /var/snap /var/lib/snapd /var/cache/snapd /usr/lib/snapd

elif grep -q "Linux Mint" /etc/os-release; then
    echo "Sistema: Linux Mint"
    SYSTEM="Mint"
else
    echo "Sistema não suportado. Este script é compatível apenas com Ubuntu e Linux Mint."
    exit 1
fi

# Instalar Flatpak e adicionar repositórios adicionais
echo "Instalando Flatpak e adicionando repositórios..."
sudo apt update
sudo apt install -y flatpak
flatpak remote-add --if-not-exists flathub https://flathub.org/repo/flathub.flatpakrepo

# Clone e compile o ImageMagick
echo "Clonando e compilando o ImageMagick..."
git clone https://github.com/ImageMagick/ImageMagick.git ImageMagick-7.0.10
cd ImageMagick-7.0.10
./configure && make && sudo make install
cd ..

# Instalar dependências do ImageMagick via apt
echo "Instalando dependências do ImageMagick..."
sudo apt build-dep imagemagick -y

# Instalar programas Flatpak
echo "Instalando programas Flatpak..."
flatpak install -y flathub \
    com.visualstudio.code \
    org.darktable.Darktable \
    org.upscayl.Upscayl \
    org.audacityteam.Audacity \
    com.github.jeromerobert.pdfarranger \
    org.kde.kdenlive \
    com.obsproject.Studio \
    org.raspberrypi.rpi-imager \
    org.inkscape.Inkscape \
    org.flozz.yoga-image-optimizer \
    net.codeindustry.MasterPDFEditor \
    org.jabref.jabref \
    org.vim.Vim \
    org.gnome.gitlab.somas.Apostrophe \
    com.gopeed.Gopeed \
    org.librecad.librecad \
    com.prusa3d.PrusaSlicer \
    com.google.Chrome \
    org.videolan.VLC \
    org.openscad.OpenSCAD \
    com.github.PopoutApps.popout3d \
    md.obsidian.Obsidian \
    org.gimp.GIMP \
    org.kde.krita \
    org.blender.Blender \
    org.inkscape.Inkscape \
    org.freecadweb.FreeCAD \
    com.ultimaker.cura \
    cc.arduino.arduinoide \
    org.fritzing.Fritzing \
    art.taunoerik.tauno-serial-plotter \
    com.simulide.simulide \
    com.github.reds.LogisimEvolution \
    org.librepcb.LibrePCB \
    org.kicad.KiCad \
    com.anydesk.Anydesk \
    io.github.veusz.Veusz \
    org.pymol.PyMOL \
    org.onlyoffice.desktopeditors \
    me.kozec.syncthingtk

# Instalar pacotes Python via pip
echo "Instalando pacotes Python via pip..."
sudo apt install -y python3-pip
pip3 install opencv-python pillow scikit-image mahotas PyOpenGL mayavi pyvtk pymesh matplotlib numpy pandas bpy ezdxf scikit-learn tensorflow keras seaborn selenium jpype1 pymysql sqlalchemy

# Atualizar os flatpaks
echo "Atualizando flatpaks..."
flatpak update -y

# Atualizar o sistema via apt
echo "Atualizando o sistema..."
sudo apt update -y
sudo apt upgrade -y

# Limpar os pacotes desnecessários
echo "Limpando pacotes desnecessários..."
sudo apt autoclean -y
sudo apt autoremove -y

echo "Instalação concluída!"
