import requests

base_url = 'https://graph.instagram.com/v12.0'  # Atualize para a versão mais recente da API

# Substitua 'YOUR_ACCESS_TOKEN' pelo token de acesso gerado durante a autenticação
access_token = 'YOUR_ACCESS_TOKEN'

def get_media_data(media_id):
    media_url = f'{base_url}/{media_id}?fields=id,caption,media_type,thumbnail_url,permalink,timestamp,like_count,comment_count&access_token={access_token}'
    response = requests.get(media_url)
    return response.json()

def main():
    # Substitua 'MEDIA_ID' pelo ID da postagem do Instagram que você deseja analisar
    media_id = 'MEDIA_ID'
    media_data = get_media_data(media_id)

    if 'error' in media_data:
        print(f'Erro: {media_data["error"]["message"]}')
    else:
        print(f'Engajamento da Postagem:')
        print(f'Likes: {media_data["like_count"]}')
        print(f'Comentários: {media_data["comment_count"]}')

if __name__ == "__main__":
    main()
