#!/bin/bash

# Nome do ambiente virtual
ENV_NAME="myenv"

# Função para ativar o ambiente virtual
ativar_ambiente() {
    if [ -d "$ENV_NAME" ]; then
        echo "Ativando o ambiente virtual '$ENV_NAME'..."
        # Usar `source` sem chaves e aspas
        source "$ENV_NAME/bin/activate"
        echo "Ambiente virtual '$ENV_NAME' ativado. Use 'deactivate' para desativá-lo."
    else
        echo "O ambiente virtual '$ENV_NAME' não existe. Execute o script de instalação primeiro."
    fi
}

# Função para desativar o ambiente virtual
desativar_ambiente() {
    if [[ "$VIRTUAL_ENV" != "" ]]; then
        echo "Desativando o ambiente virtual..."
        deactivate
        echo "Ambiente virtual desativado."
    else
        echo "Nenhum ambiente virtual está ativo no momento."
    fi
}

# Menu interativo
while true; do
    echo ""
    echo "----- Menu -----"
    echo "0. Sair"
    echo "1. Ativar ambiente virtual Blender py"
    echo "2. Desativar ambiente virtual Blender py"
    echo "----------------"
    read -p "Escolha uma opção [0-2]: " opcao

    case $opcao in
        1)
            ativar_ambiente
            ;;
        2)
            desativar_ambiente
            ;;
        0)
            echo "Saindo..."
            break
            ;;
        *)
            echo "Opção inválida! Tente novamente."
            ;;
    esac
done
