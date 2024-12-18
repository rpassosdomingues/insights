#!/bin/bash

# Configurações de pastas
PASTA_BASE="$HOME/insync"
PASTA_MAIN="$PASTA_BASE/main"
PASTA_CACHE="$PASTA_BASE/cache"
PASTA_BUFFER="$PASTA_BASE/buffer"

# Função para criar diretórios se não existirem
criar_diretorios() {
    echo "Criando diretórios principais..."
    mkdir -p "$PASTA_MAIN"
    mkdir -p "$PASTA_CACHE"
    mkdir -p "$PASTA_BUFFER"
}

# Função para obter a data da última modificação do arquivo
obter_data_modificacao() {
    local arquivo="$1"
    date -r "$arquivo" +"%Y-%m-%d"
}

# Função para calcular a diferença em dias entre duas datas
calcular_diferenca_dias() {
    local data1="$1"
    local data2="$2"
    echo $(( ( $(date -d "$data2" +%s) - $(date -d "$data1" +%s) ) / 86400 ))
}

# Função para mover arquivo para uma pasta específica
mover_arquivo() {
    local arquivo="$1"
    local destino="$2"
    echo "Movendo $arquivo para $destino"
    mv "$arquivo" "$destino"
}

# Função para organizar arquivos em pastas main, cache e buffer
organizar_arquivos() {
    echo "Organizando arquivos..."
    local data_atual=$(date +"%Y-%m-%d")
    
    for caminho in "$PASTA_BASE"/*; do
        if [ -d "$caminho" ] && [[ "$caminho" != "$PASTA_MAIN" && "$caminho" != "$PASTA_CACHE" && "$caminho" != "$PASTA_BUFFER" ]]; then
            for arquivo in "$caminho"/*; do
                if [ -f "$arquivo" ]; then
                    local data_modificacao=$(obter_data_modificacao "$arquivo")
                    local diff=$(calcular_diferenca_dias "$data_modificacao" "$data_atual")
                    
                    if [ $diff -le 7 ]; then
                        mover_arquivo "$arquivo" "$PASTA_BUFFER/"
                    elif [ $diff -le 30 ]; then
                        mover_arquivo "$arquivo" "$PASTA_CACHE/"
                    else
                        mover_arquivo "$arquivo" "$PASTA_MAIN/"
                    fi
                fi
            done
        fi
    done
}

# Função para sincronizar a pasta com o Insync
sincronizar_com_insync() {
    echo "Sincronizando com Insync..."
    insync-headless force_sync --path="$PASTA_BASE"
}

# Função principal para executar o script
main() {
    criar_diretorios
    organizar_arquivos
    sincronizar_com_insync
}

# Executa a função principal
main
