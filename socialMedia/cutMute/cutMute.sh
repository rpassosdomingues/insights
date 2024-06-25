#!/bin/bash

# Função para detectar segmentos de silêncio usando ffmpeg
detectar_silencio() {
    local video_path="$1"
    local limite_amplitude="$2"
    local min_duracao="$3"

    # Executar silencedetect para detectar segmentos de silêncio
    ffmpeg_output=$(ffmpeg -hide_banner -loglevel panic -i "$video_path" -af "silencedetect=noise=$limite_amplitude:d=$min_duracao" -f null -)

    # Capturar linhas contendo "silence_start:"
    segmentos_silencio=$(echo "$ffmpeg_output" | grep "silence_start:")

    # Retornar os segmentos de silêncio encontrados
    echo "$segmentos_silencio"
}

# Função para acelerar o vídeo
acelerar_video() {
    local input_video="$1"
    local output_video="$2"
    local speed="$3"

    # Acelerar o vídeo usando ffmpeg
    ffmpeg -hide_banner -loglevel panic -i "$input_video" -filter:v "setpts=$speed*PTS" -filter:a "atempo=$((1/$speed))" -c:v h264 -c:a aac -strict experimental "$output_video"
}

# Função para equalizar o áudio
equalizar_audio() {
    local input_video="$1"
    local output_video="$2"

    # Equalizar o áudio usando o filtro acompressor
    ffmpeg -hide_banner -loglevel panic -i "$input_video" -af "volume=2.0" -c:v copy -c:a aac -strict experimental "$output_video"
}

# Função principal para processar o vídeo
processar_video() {
    local video_path="$1"
    local limite_amplitude="$2"
    local min_duracao="$3"
    local speed="$4"

    # Definir diretórios de entrada e saída
    dir_in="in"
    dir_out="out"

    # Criar diretório de saída se não existir
    mkdir -p "$dir_out"

    # Caminhos completos para vídeo de entrada e saída
    video_in="$dir_in/$video_path"
    video_out_temp="$dir_out/video_temp.mp4"
    video_out_sem_silencio="$dir_out/video_sem_silencio.mp4"
    video_out_final="$dir_out/video_Final.mp4"

    # Cortar os primeiros 2 segundos e os últimos 2 segundos do vídeo original
    ffmpeg -hide_banner -loglevel panic -i "$video_in" -ss 2 -to "$(ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 "$video_in" | awk '{print int($1 - 2)}')" -c:v h264 -c:a aac -strict experimental "$video_out_temp"

    # Detectar segmentos de silêncio no vídeo cortado
    segmentos_silencio=$(detectar_silencio "$video_out_temp" "$limite_amplitude" "$min_duracao")

    # Converter a saída em array usando IFS
    IFS=$'\n' read -r -a linhas <<< "$segmentos_silencio"

    # Arquivo temporário para listar os segmentos não silenciosos
    temp_file_list="$dir_out/temp_list.txt"
    echo "" > "$temp_file_list"
    start=0

    # Iterar pelos segmentos de silêncio encontrados
    for linha in "${linhas[@]}"
    do
        # Extrair tempos de início e fim do segmento de silêncio
        inicio=$(echo "$linha" | awk '{print $5}')
        fim=$(echo "$linha" | awk '{print $8}')

        # Converter para segundos
        inicio_sec=$(echo "$inicio" | awk -F: '{print ($1 * 3600) + ($2 * 60) + $3}')
        fim_sec=$(echo "$fim" | awk -F: '{print ($1 * 3600) + ($2 * 60) + $3}')

        # Salvar segmento não silencioso em um arquivo temporário
        if (( $(echo "$inicio_sec > $start" | bc -l) )); then
            temp_output="$dir_out/temp_$(date +%s).mp4"
            ffmpeg -hide_banner -loglevel panic -ss "$start" -to "$inicio_sec" -i "$video_out_temp" -c copy "$temp_output"
            echo "file '$temp_output'" >> "$temp_file_list"
        fi

        # Atualizar o ponto de início para o próximo segmento
        start=$fim_sec
    done

    # Adicionar o último segmento não silencioso (do último silêncio até o fim do vídeo)
    duration=$(ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 "$video_out_temp")
    duration=${duration%.*}
    if (( $(echo "$start < $duration" | bc -l) )); then
        temp_output="$dir_out/temp_$(date +%s).mp4"
        ffmpeg -hide_banner -loglevel panic -ss "$start" -i "$video_out_temp" -c copy "$temp_output"
        echo "file '$temp_output'" >> "$temp_file_list"
    fi

    # Concatenar todos os arquivos temporários em um único vídeo sem silêncio
    ffmpeg -hide_banner -loglevel panic -f concat -safe 0 -i "$temp_file_list" -c copy "$video_out_sem_silencio"

    # Equalizar o áudio do vídeo sem silêncio
    equalizar_audio "$video_out_sem_silencio" "$video_out_final"

    # Acelerar o vídeo final
    acelerar_video "$video_out_final" "$video_out_final" "$speed"

    # Remover arquivos temporários
    rm -f "$dir_out/temp_*.mp4"
    rm -f "$temp_file_list"
    rm -f "$video_out_temp"
    rm -f "$video_out_sem_silencio"

    echo "Vídeo processado e salvo em: $video_out_final"
}

# Configurações
video_path="video_In.mp4"
limite_amplitude=-30
min_duracao=0.5
speed=2  # Velocidade de aceleração

# Executar função principal para processar o vídeo
processar_video "$video_path" "$limite_amplitude" "$min_duracao" "$speed"
