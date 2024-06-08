from PIL import Image, ImageDraw, ImageFont
import os

def mesclar_imagens(caminho_pasta, nome_imagem_fundo, nome_imagem_frente, ajuste, caminho_saida):
    # Carregar a imagem de fundo
    imagem_fundo = Image.open(os.path.join(caminho_pasta, nome_imagem_fundo))
    largura_fundo, altura_fundo = imagem_fundo.size

    # Tamanho constante para post no Instagram
    largura_post = 1080
    altura_post = 1080

    # Altura desejada para a imagem da frente
    altura_frente = 960

    # Verificar se a imagem existe na pasta
    caminho_imagem = os.path.join(caminho_pasta, nome_imagem_frente)
    if not os.path.isfile(caminho_imagem):
        print(f"A imagem '{nome_imagem_frente}' não foi encontrada na pasta '{caminho_pasta}'.")
        return

    # Carregar a imagem
    imagem = Image.open(caminho_imagem)

    # Redimensionar a imagem da frente mantendo proporções
    largura_original, altura_original = imagem.size
    nova_largura = int((altura_frente / altura_original) * largura_original)
    imagem_redimensionada = imagem.resize((nova_largura, altura_frente))

    # Calcular posição superior esquerda para colar a imagem ajustada verticalmente
    posicao_x = (largura_post - nova_largura) // 2
    posicao_y = (altura_post - altura_frente) // 2

    # Ajustar a posição vertical
    if ajuste < 0:
        posicao_y -= abs(ajuste)
    else:
        posicao_y += ajuste

    # Certificar-se de que a posição y não seja negativa
    posicao_y = max(posicao_y, 0)

    # Mesclar a imagem redimensionada com a imagem de fundo
    imagem_fundo_com_imagem = imagem_fundo.copy()
    imagem_fundo_com_imagem.paste(imagem_redimensionada, (posicao_x, posicao_y))

    """
    # Adicionar texto
    fonte = ImageFont.load_default(50)
    draw = ImageDraw.Draw(imagem_fundo_com_imagem)
    draw.text((10, 30), "OFICINA DE CORTE A LASER", font=fonte, fill=(255, 255, 255)) # fill=(R, G, B)
    """

    # Salvar a nova imagem na pasta de saída
    nome_arquivo_saida = os.path.join(caminho_saida, f'{nome_imagem_frente}_post_instagram.png')
    imagem_fundo_com_imagem.save(nome_arquivo_saida)

    print(f"A imagem foi salva como '{nome_arquivo_saida}'.")

# Caminho para a pasta com as imagens
caminho_pasta = './images'  # Alterado para um caminho relativo ao diretório do script

# Caminho para a pasta de saída
caminho_saida = './out'  # Pasta de saída no mesmo diretório do script

# Crie a pasta de saída se não existir
if not os.path.exists(caminho_saida):
    os.makedirs(caminho_saida)

# Constantes
nome_imagem_fundo = 'backImageWithLogos.png'
nome_imagem_frente = input("Digite o nome da imagem (com extensão) que você deseja mesclar: ")
ajuste = -60  # Ajuste de posição. Se negativo, sobe; se positivo, desce.

# Chamar a função para mesclar as imagens
mesclar_imagens(caminho_pasta, nome_imagem_fundo, nome_imagem_frente, ajuste, caminho_saida)
