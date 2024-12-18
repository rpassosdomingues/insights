import subprocess
import sys

def instalar_pacote(pacote):
    subprocess.check_call([sys.executable, "-m", "pip", "install", pacote])

# Verificando e instalando pacotes necessários
try:
    import pandas as pd
except ImportError:
    instalar_pacote('pandas')

try:
    import smtplib
except ImportError:
    instalar_pacote('smtplib')

try:
    from email.mime.text import MIMEText
except ImportError:
    instalar_pacote('email')

try:
    from datetime import datetime
except ImportError:
    instalar_pacote('datetime')

try:
    import os
except ImportError:
    instalar_pacote('os')

try:
    import logging
except ImportError:
    instalar_pacote('logging')

import time
import subprocess
import pandas as pd
import smtplib
from email.mime.text import MIMEText
from datetime import datetime
import os
import logging

"""
# Função para garantir que o diretório de logs exista
def garantir_diretorio_logs(diretorio):
    if not os.path.exists(diretorio):
        os.makedirs(diretorio)
        logging.info(f"Diretório de logs criado: {diretorio}")

# Configuração de logging
diretorio_logs = '/home/pi/logs/'
garantir_diretorio_logs(diretorio_logs)
logging.basicConfig(filename=os.path.join(diretorio_logs, 'internet_monitor.log'),
                    level=logging.INFO,
                    format='%(asctime)s - %(message)s')
"""

# Configuração de logging
diretorio_logs = os.getcwd()  # Diretório atual onde o script está sendo executado
arquivo_log = os.path.join(diretorio_logs, 'internet_monitor.log')

def conectar_rede_wifi(ssid):
    """
    Conecta o Raspberry Pi a uma rede Wi-Fi específica.

    Parâmetros:
    ssid (str): Nome da rede Wi-Fi (SSID) à qual se conectar.

    Retorna:
    None
    """
    try:
        cmd = f'nmcli dev wifi connect "{ssid}"'
        subprocess.run(cmd, shell=True, check=True)
        logging.info(f"Conectado à rede Wi-Fi {ssid} com sucesso.")
    except Exception as e:
        logging.error(f"Erro ao conectar à rede Wi-Fi {ssid}: {e}")

def medir_velocidade(interface):
    """
    Mede a velocidade de internet (ping, upload, download) usando o comando speedtest-cli
    para uma interface de rede específica.

    Parâmetros:
    interface (str): Interface de rede a ser medida (ex: 'wlan0').

    Retorna:
    tuple: Contendo ping, upload e download como floats. Se houver erro, retorna (None, None, None).
    """
    try:
        cmd = f'speedtest-cli --interface={interface} --simple'
        result = subprocess.run(cmd, shell=True, capture_output=True, text=True, check=True)
        ping, upload, download = result.stdout.strip().split('\n')
        return float(ping.split(':')[1].strip()), float(upload.split(':')[1].strip()), float(download.split(':')[1].strip())
    except Exception as e:
        logging.error(f"Erro ao medir a velocidade da interface {interface}: {e}")
        return None, None, None

def calcular_status(ping, upload, download, ping_ideal, upload_ideal, download_ideal):
    """
    Calcula um índice de desempenho baseado nas velocidades de ping, upload e download.

    Parâmetros:
    ping (float): Valor medido de ping.
    upload (float): Valor medido de upload.
    download (float): Valor medido de download.
    ping_ideal (float): Valor ideal de ping.
    upload_ideal (float): Valor ideal de upload.
    download_ideal (float): Valor ideal de download.

    Retorna:
    float: Índice de desempenho calculado.
    """
    try:
        performance = (ping_ideal / ping) ** 0.4 * (upload / upload_ideal) ** 0.4 * (download / download_ideal) ** 0.2
        return performance
    except ZeroDivisionError:
        logging.error("Erro de divisão por zero ao calcular o status.")
        return 0

def enviar_email(destinatarios, assunto, corpo):
    """
    Envia um email com os resultados do monitoramento.

    Parâmetros:
    destinatarios (list): Lista de emails para os quais o email será enviado.
    assunto (str): Assunto do email.
    corpo (str): Corpo do email.

    Retorna:
    None
    """
    try:
        remetente = 'rementente@sou.unifal-mg.edu.br'
        senha = 'senha'  # Substitua pela sua senha do email

        mensagem = MIMEText(corpo)
        mensagem['From'] = remetente
        mensagem['To'] = ', '.join(destinatarios)
        mensagem['Subject'] = assunto

        with smtplib.SMTP_SSL('smtp.gmail.com', 465) as server:
            server.login(remetente, senha)
            server.sendmail(remetente, destinatarios, mensagem.as_string())
        logging.info("Email enviado com sucesso.")
    except Exception as e:
        logging.error(f"Erro ao enviar email: {e}")

def monitorar_internet():
    """
    Função principal de monitoramento da internet. Executa o teste de velocidade na interface de rede
    wlan0, armazena os resultados em um arquivo CSV, verifica o status da internet, e envia um email
    se a qualidade estiver abaixo do limiar definido.
    """
    agora = datetime.now()
    if agora.hour == 6:
        conectar_rede_wifi('Nome-Rede_WLan0')

        ping, upload, download = medir_velocidade('wlan0')
        if ping is None or upload is None or download is None:
            logging.error("Não foi possível medir a velocidade da internet. O monitoramento será retomado mais tarde.")
            return

        ping_ideal = 20.0  # Defina o valor ideal de ping
        upload_ideal = 10.0  # Defina o valor ideal de upload (em Mbps)
        download_ideal = 50.0  # Defina o valor ideal de download (em Mbps)

        performance = calcular_status(ping, upload, download, ping_ideal, upload_ideal, download_ideal)

        df = pd.DataFrame([[agora, 'wlan0', ping, upload, download, performance]], 
                          columns=['Data', 'Interface', 'Ping', 'Upload', 'Download', 'Status'])
        df.to_csv('/home/pi/internet_monitor.csv', mode='a', header=not os.path.exists('/home/pi/internet_monitor.csv'), index=False)

        if performance < 0.2:
            destinatarios = ['destinatario@unifal-mg.edu.br']
            corpo = f"O sinal da internet está ruim. Considere trabalhar em home-office hoje.\n\n{df.to_string(index=False)}"
            enviar_email(destinatarios, '[Monitor de Internet] Sinal insuficiente', corpo)
        logging.info("Monitoramento realizado com sucesso.")

def configurar_servico():
    """
    Configura o script como um serviço do systemd no Raspberry Pi para que seja executado automaticamente no boot.

    Parâmetros:
    None

    Retorna:
    None
    """
    service_file = """
    [Unit]
    Description=Monitor de Internet
    After=network.target

    [Service]
    ExecStart=/usr/bin/python3 /home/pi/internet_monitor.py
    WorkingDirectory=/home/pi/
    User=pi
    Restart=always

    [Install]
    WantedBy=multi-user.target
    """
    try:
        with open('/etc/systemd/system/internet_monitor.service', 'w') as f:
            f.write(service_file)

        os.system('sudo systemctl daemon-reload')
        os.system('sudo systemctl enable internet_monitor.service')
        os.system('sudo systemctl start internet_monitor.service')
        logging.info("Serviço do systemd configurado com sucesso.")
    except Exception as e:
        logging.error(f"Erro ao configurar o serviço do systemd: {e}")

def main():
    """
    Função principal que organiza a execução do monitoramento e configuração do serviço.

    Parâmetros:
    None

    Retorna:
    None
    """
    logging.info("Iniciando monitoramento de internet...")
    configurar_servico()
    while True:
        monitorar_internet()
        time.sleep(86400)  # Aguarda 1 dia antes de realizar o próximo monitoramento

if __name__ == '__main__':
    main()