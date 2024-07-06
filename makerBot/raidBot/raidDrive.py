import os
import datetime
import threading
from zipfile import ZipFile
from google.oauth2 import service_account
from googleapiclient.discovery import build
from tqdm import tqdm  # Biblioteca para barras de progresso

# Configuração do Google Drive API
client_secrets_path = 'path/to/credentials.json'  # Local do arquivo de credenciais
credentials = service_account.Credentials.from_service_account_file(client_secrets_path)
drive_service = build('drive', 'v3', credentials=credentials)

# Função para obter o tamanho do arquivo
def obter_tamanho_arquivo(arquivo_id):
    arquivo_metadata = drive_service.files().get(fileId=arquivo_id).execute()
    return int(arquivo_metadata['size'])

# Função para obter a última modificação do arquivo
def obter_ultima_modificacao_arquivo(arquivo_id):
    arquivo_metadata = drive_service.files().get(fileId=arquivo_id).execute()
    ultima_modificacao = arquivo_metadata['modifiedTime']
    return datetime.datetime.strptime(ultima_modificacao, '%Y-%m-%dT%H:%M:%S.%fZ')

# Função para copiar um arquivo
def copiar_arquivo(arquivo_id, pasta_destino_id, nome_arquivo):
    print(f"Copiando arquivo: {nome_arquivo}")

    # Cria um novo arquivo na pasta de destino
    novo_arquivo = drive_service.files().copy(fileId=arquivo_id, body={'parents': [pasta_destino_id]}).execute()
    novo_arquivo_id = novo_arquivo['id']

    # Renomeia o novo arquivo
    drive_service.files().update(fileId=novo_arquivo_id, body={'name': nome_arquivo}).execute()

# Função para mover um arquivo para uma pasta
def mover_arquivo(arquivo_id, pasta_destino_id):
    print(f"Movendo arquivo: {arquivo_id}")

    drive_service.files().update(fileId=arquivo_id, addParents=pasta_destino_id).execute()

# Função para compactar uma pasta recursivamente
def compactar_pasta(pasta_id, nome_arquivo, progress_bar):
    print(f"Compactando pasta: {pasta_id}")

    # Cria um objeto ZipFile
    with ZipFile(f"{nome_arquivo}.zip", 'w') as zip_file:
        # Recupera os metadados da pasta
        pasta_metadata = drive_service.files().get(fileId=pasta_id).execute()

        # Lista os arquivos e subpastas dentro da pasta
        lista_conteudos = drive_service.files().list(q=f"'{pasta_id}' in parents").execute()
        for conteudo in lista_conteudos['files']:
            # Verifica se o conteúdo é um arquivo
            if conteudo['mimeType'] != 'application/vnd.google-apps.folder':
                arquivo_id = conteudo['id']
                nome_arquivo_conteudo = conteudo['name']

                # Baixa o arquivo e adiciona-o ao arquivo zip
                request = drive_service.files().get_media(fileId=arquivo_id)
                arquivo_conteudo = request.execute()
                zip_file.writestr(nome_arquivo_conteudo, arquivo_conteudo)

                # Atualiza a barra de progresso
                progress_bar.update(1)

    print(f"Pasta compactada com sucesso: {nome_arquivo}.zip")

# Função para baixar recursivamente uma pasta compactada
def baixar_pasta_compactada(nome_arquivo, progress_bar):
    print(f"Baixando pasta compactada: {nome_arquivo}")

    # Cria o diretório para a pasta compactada no disco local
    diretorio_local = f"baixados/{nome_arquivo}"
    os.makedirs(diretorio_local, exist_ok=True)

    # Extrai o conteúdo do arquivo zip para o diretório local
    with ZipFile(f"{nome_arquivo}.zip", 'r') as zip_file:
        zip_file.extractall(diretorio_local)
        for _ in range(len(zip_file.namelist())):
            progress_bar.update(1)

# Função para organizar os arquivos em pastas de cache, main e buffers
def organizar_arquivos(pasta_id, cache_id, main_id, buffer_id):
    print(f"Organizando arquivos na pasta: {pasta_id}")

    # Lista os arquivos e subpastas dentro da pasta
    lista_conteudos = drive_service.files().list(q=f"'{pasta_id}' in parents").execute()
    for conteudo in lista_conteudos['files']:
        arquivo_id = conteudo['id']
        nome_arquivo = conteudo['name']
        ultima_modificacao = obter_ultima_modificacao_arquivo(arquivo_id)

        # Organiza os arquivos com base na última modificação
        if ultima_modificacao > datetime.datetime.now() - datetime.timedelta(days=30):
            mover_arquivo(arquivo_id, cache_id)
        else:
            mover_arquivo(arquivo_id, main_id)

        # Arquivos muito recentes vão para o buffer
        if ultima_modificacao > datetime.datetime.now() - datetime.timedelta(days=7):
            mover_arquivo(arquivo_id, buffer_id)

# Função principal
def main():
    # Define a pasta raiz a ser processada
    pasta_raiz_id = 'pasta_raiz_id'  # ID da pasta raiz no Google Drive
    cache_id = 'cache_folder_id'  # ID da pasta cache
    main_id = 'main_folder_id'  # ID da pasta main
    buffer_id = 'buffer_folder_id'  # ID da pasta buffer

    # Cria uma nova pasta de backup com a data e hora atuais
    pasta_backup_nome = f"raid_{datetime.datetime.now().strftime('%Y-%m-%d_%H-%M-%S')}"
    pasta_backup = drive_service.files().create(body={'name': pasta_backup_nome, 'mimeType': 'application/vnd.google-apps.folder'}).execute()
    pasta_backup_id = pasta_backup['id']

    # Copia e organiza os arquivos da pasta raiz
    organizar_arquivos(pasta_raiz_id, cache_id, main_id, buffer_id)

    # Barras de progresso para cada processo
    total_files_cache = len(drive_service.files().list(q=f"'{cache_id}' in parents").execute()['files'])
    total_files_main = len(drive_service.files().list(q=f"'{main_id}' in parents").execute()['files'])
    total_files_buffer = len(drive_service.files().list(q=f"'{buffer_id}' in parents").execute()['files'])

    progress_bar_cache = tqdm(total=total_files_cache, desc="Compactando cache")
    progress_bar_main = tqdm(total=total_files_main, desc="Compactando main")
    progress_bar_buffer = tqdm(total=total_files_buffer, desc="Compactando buffer")

    # Compacta as pastas de cache, main e buffer
    threading.Thread(target=compactar_pasta, args=(cache_id, 'cache_backup', progress_bar_cache)).start()
    threading.Thread(target=compactar_pasta, args=(main_id, 'main_backup', progress_bar_main)).start()
    threading.Thread(target=compactar_pasta, args=(buffer_id, 'buffer_backup', progress_bar_buffer)).start()

    # Baixa as pastas compactadas para o disco local
    threading.Thread(target=baixar_pasta_compactada, args=('cache_backup', progress_bar_cache)).start()
    threading.Thread(target=baixar_pasta_compactada, args=('main_backup', progress_bar_main)).start()
    threading.Thread(target=baixar_pasta_compactada, args=('buffer_backup', progress_bar_buffer)).start()

if __name__ == '__main__':
    main()
