#!/bin/bash

# Instala as dependências necessárias
echo "Instalando ZFS..."
sudo apt install -y zfsutils-linux

echo "Instalando rsync..."
sudo apt install -y rsync

# Variáveis de configuração
POOL_NAME="raid0pool"
DISK1="/dev/sda"
DISK2="/dev/sdb"
MOUNT_POINT="/mnt/$POOL_NAME"
MAIN_PATH="/mnt/$POOL_NAME/main"
CACHE_PATH="/mnt/$POOL_NAME/cache"
MAX_SIZE="500G"
CREDENTIALS_FILE="credentials.json"
LOCAL_DIR="/local/dir"
REMOTE_DIR="/remote/dir"
TIMESTAMP_FILE="ultima_execucao.txt"

# Função para configurar o RAID 0 com ZFS
configurar_raid0() {
    echo "Configurando RAID 0 com ZFS..."
    zpool create -f $POOL_NAME mirror $DISK1 $DISK2
    zfs set quota=$MAX_SIZE $POOL_NAME
    zfs create $POOL_NAME/main
    zfs create $POOL_NAME/cache
    echo "RAID 0 configurado com sucesso."
}

# Função para corrigir erros da unidade
corrigir_erros() {
    echo "Corrigindo erros da unidade..."
    zpool scrub $POOL_NAME
    echo "Erros corrigidos."
}

# Função para otimizar o armazenamento via ZFS
otimizar_armazenamento() {
    echo "Otimizando o armazenamento via ZFS..."
    zfs set compression=on $POOL_NAME
    zfs set atime=off $POOL_NAME
    echo "Armazenamento otimizado."
}

# Função para monitorar o espaço em disco e priorizar a cópia
monitorar_espaco() {
    local usado=$(zfs get -H -o value used $POOL_NAME)
    local quota=$(zfs get -H -o value quota $POOL_NAME)

    if [ "$usado" -ge "$quota" ]; then
        echo "Erro: Espaço em disco insuficiente para copiar cache"
        return 1
    else
        return 0
    fi
}

# Função para verificar o estado das unidades do pool ZFS
verificar_estado_unidades() {
    echo "Verificando o estado das unidades do pool ZFS..."
    local status=$(zpool status -x $POOL_NAME)
    if [[ $status != "all pools are healthy" ]]; then
        echo "Erro: Uma ou mais unidades do pool ZFS estão com defeito. Status: $status"
        exit 1
    else
        echo "Todas as unidades do pool ZFS estão saudáveis."
    fi
}

# Função para copiar os arquivos para o servidor TrueNAS
copiar_arquivos() {
    local folder_name=$1

    # Lê as credenciais do arquivo JSON
    local ssh_user=$(jq -r '.user' $CREDENTIALS_FILE)
    local ssh_host=$(jq -r '.host' $CREDENTIALS_FILE)
    local ssh_key=$(jq -r '.ssh_key' $CREDENTIALS_FILE)
    local ssh_port=$(jq -r '.port' $CREDENTIALS_FILE)

    echo "Copiando arquivos da pasta $folder_name..."
    rsync -avz -e "ssh -i $ssh_key -p $ssh_port" $LOCAL_DIR/$folder_name/ $ssh_user@$ssh_host:$REMOTE_DIR/$folder_name/
    echo "Cópia de $folder_name concluída."
}

# Função principal
main() {
    # Verifica o estado das unidades antes de qualquer operação
    verificar_estado_unidades

    # Configura o RAID 0 e otimiza o armazenamento
    configurar_raid0
    corrigir_erros
    otimizar_armazenamento

    # Copia e organiza os arquivos
    copiar_arquivos cache

    # Monitora o espaço em disco antes de copiar a main
    if monitorar_espaco; then
        copiar_arquivos main
    fi
}

# Executa a função principal
main
