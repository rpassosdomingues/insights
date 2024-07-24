# -------------------------------------------------------------
# [Windows 10 ou 11] Script de instalação de Softwares Maker
# -------------------------------------------------------------
# Autor: Rafael Passos Domingues
# Última atualização: 2024-07-08
# -------------------------------------------------------------

# Função para tratamento de erro
function Handle-Error {
    param (
        [string]$message
    )
    Write-Error $message
    exit 1
}

# Função para instalar o winget
function Install-Winget {
    Write-Host "Instalando winget..."
    # URL do instalador do winget (você pode precisar atualizar este link para a versão mais recente)
    $wingetInstallerUrl = "https://aka.ms/getwinget"
    $installerPath = "$env:TEMP\wingetInstaller.msixbundle"

    try {
        Invoke-WebRequest -Uri $wingetInstallerUrl -OutFile $installerPath
        Add-AppxPackage -Path $installerPath
    } catch {
        Handle-Error "Erro ao tentar instalar o winget. Certifique-se de que o script está sendo executado com privilégios de administrador."
    }

    # Verificar novamente se o winget foi instalado com sucesso
    if (-not (Get-Command winget -ErrorAction SilentlyContinue)) {
        Handle-Error "A instalação do winget falhou. Por favor, instale o winget manualmente."
    }
}

# Verificar se o winget está instalado
if (-not (Get-Command winget -ErrorAction SilentlyContinue)) {
    Install-Winget
}

# Lista de aplicativos a serem instalados
$apps = @(
    "BlenderFoundation.Blender.LTS.3.6  -e",
    "Prusa3D.PrusaSlicer  -e",
    "Ultimaker.Cura  -e",
    "Google.Chrome  -e",
    "ONLYOFFICE.DesktopEditors  -e",
    "AnyDeskSoftwareGmbH.AnyDesk  -e",
    "ArduinoSA.IDE.stable  -e",
    "RaspberryPiFoundation.RaspberryPiImager  -e",
    "Microsoft.VisualStudioCode  -e",
    "OpenSCAD.OpenSCAD  -e",
    "Schrodinger.Pymol  -e",
    "EclipseAdoptium.Temurin.21.JRE  -e",
    "Upscayl.Upscayl  -e",
    "darktable.darktable  -e",
    "Elsevier.MendeleyReferenceManager  -e",
    "Inkscape.Inkscape  -e",
    "KDE.Krita  -e",
    "LibreCAD.LibreCAD  -e",
    "FreeCAD.FreeCAD  -e",
    "Syncthing.Syncthing"
)

# Instalar cada aplicativo usando o winget
foreach ($app in $apps) {
    Write-Host "Instalando $app..."
    winget install --id $app --silent --accept-package-agreements --accept-source-agreements
}

Write-Host "Instalação concluída!"
