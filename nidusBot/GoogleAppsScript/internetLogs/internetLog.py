import subprocess
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText

# Função para verificar a velocidade da internet
def verificar_internet():
    resultado = subprocess.run(['speedtest-cli', '--simple'], capture_output=True, text=True)
    return resultado.stdout

# Função para enviar o e-mail
def enviar_email(resultados):
    remetente = "origem@gmail.com"
    senha = "senha-app"
    destinatario = "destino@gmail.com"

    msg = MIMEMultipart()
    msg['From'] = remetente
    msg['To'] = destinatario
    msg['Subject'] = "Status da Internet"

    corpo = f"""
    Olá,

    Gostaria de informar o status atual da minha conexão de internet.

    {resultados}

    Atenciosamente,
    Rafael Passos Domingues
    """
    
    msg.attach(MIMEText(corpo, 'plain'))

    server = smtplib.SMTP('smtp.gmail.com', 587)
    server.starttls()
    server.login(remetente, senha)
    texto = msg.as_string()
    server.sendmail(remetente, destinatario, texto)
    server.quit()

# Executa as funções
resultados = verificar_internet()
enviar_email(resultados)
