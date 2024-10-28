#!/bin/bash

# Função para comprimir o PDF
compress_pdf() {
    input_file="src/$1.pdf"
    output_file="out/${1}_compressed.pdf"
    compression_level="$2"

    # Verifica se o arquivo de entrada existe
    if [ ! -f "$input_file" ]; then
        echo "Erro: O arquivo '$input_file' não foi encontrado!"
        exit 1
    fi

    # Comprimindo o PDF usando Ghostscript
    gs -sDEVICE=pdfwrite -dCompatibilityLevel=1.4 -dPDFSETTINGS=/$compression_level -dNOPAUSE -dQUIET -dBATCH -sOutputFile="$output_file" "$input_file"

    if [ $? -eq 0 ]; then
        echo "PDF comprimido com sucesso!"
        echo "Arquivo de saída: $output_file"
    else
        echo "Erro ao comprimir o arquivo PDF."
    fi
}

# Menu para o usuário inserir o nome do arquivo
echo "============================"
echo "       Compress PDF"
echo "============================"
read -p "Informe o nome do arquivo PDF a ser comprimido (sem o caminho): " input_pdf_name

# Menu para selecionar o nível de compressão
echo "Escolha o nível de compressão:"
echo "1 - screen (Baixa qualidade, menor tamanho)"
echo "2 - ebook (Qualidade média)"
echo "3 - printer (Qualidade de impressão)"
echo "4 - prepress (Alta qualidade para impressão final)"
echo "5 - default (Configurações padrão)"
read -p "Informe o número correspondente ao nível de compressão: " compression_choice

# Definir o nível de compressão com base na escolha
case $compression_choice in
    1) compression_level="screen" ;;
    2) compression_level="ebook" ;;
    3) compression_level="printer" ;;
    4) compression_level="prepress" ;;
    5) compression_level="default" ;;
    *) echo "Opção inválida. Usando o nível 'screen' por padrão."; compression_level="screen" ;;
esac

# Chama a função de compressão
compress_pdf "$input_pdf_name" "$compression_level"
