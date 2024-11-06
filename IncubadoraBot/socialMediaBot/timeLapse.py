from moviepy.editor import VideoFileClip, concatenate_videoclips, vfx
import os
from PIL import Image
import numpy as np
from concurrent.futures import ThreadPoolExecutor

# Função para redimensionar um vídeo
def resize_clip(clip, new_size):
    def resize_frame(frame):
        pil_image = Image.fromarray(frame)
        resized_pil_image = pil_image.resize(new_size, Image.Resampling.LANCZOS)
        return np.array(resized_pil_image)
    return clip.fl_image(resize_frame)

# Função para mutar um vídeo
def mute_clip(clip):
    return clip.volumex(0)

# Função para acelerar um vídeo
def speed_up_clip(clip, factor):
    return clip.fx(vfx.speedx, factor)

# Função para processar um vídeo
def process_video(video_path, new_size, speed_factor):
    video = VideoFileClip(video_path)
    video_mutado = mute_clip(video)
    video_acelerado = speed_up_clip(video_mutado, speed_factor)
    video_redimensionado = resize_clip(video_acelerado, new_size)
    return video_redimensionado

# Função para concatenar vídeos processados e salvar o vídeo final
def concatenate_and_save_videos(videos, output_path, output_filename):
    video_final = concatenate_videoclips(videos)
    output_video_path = os.path.join(output_path, output_filename)
    video_final.write_videofile(output_video_path, fps=video_final.fps)
    print(f'Vídeo final salvo em: {output_video_path}')

# Caminhos para os diretórios de entrada e saída
input_path = 'videos'
output_path = 'out'

# Verifique se o diretório de entrada existe
if not os.path.isdir(input_path):
    raise FileNotFoundError(f"O diretório especificado para os vídeos não foi encontrado: {input_path}")

# Tamanho ideal para Instagram Reels e Stories
new_size = (1080, 1080)
speed_factor = 4
output_filename = 'timelapse.mp4'

# Obter a lista de vídeos e ordenar por nome (data e hora)
video_files = sorted([os.path.join(input_path, f) for f in os.listdir(input_path) if f.endswith('.mp4')])

# Processar os vídeos em paralelo
with ThreadPoolExecutor() as executor:
    futures = [executor.submit(process_video, video_file, new_size, speed_factor) for video_file in video_files]
    videos_processados = [future.result() for future in futures]

# Concatenar os vídeos processados e salvar o vídeo final
concatenate_and_save_videos(videos_processados, output_path, output_filename)
