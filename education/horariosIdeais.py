import pandas as pd
import matplotlib.pyplot as plt

class Disciplina:
    def __init__(self, nome, media_esperada):
        self.nome = nome
        self.media_esperada = media_esperada

class Aluno:
    def __init__(self, nome):
        self.nome = nome
        self.disciplinas = {}

    def adicionar_disciplina(self, disciplina, horario):
        self.disciplinas[disciplina] = horario

    def calcular_media_bimestral(self):
        total_media = 0.0
        for disciplina, horario in self.disciplinas.items():
            total_media += disciplina.media_esperada
        return total_media / len(self.disciplinas)

class OtimizadorHorario:
    def __init__(self, alunos, disciplinas, horarios_disponiveis):
        self.alunos = alunos
        self.disciplinas = disciplinas
        self.horarios_disponiveis = horarios_disponiveis

    def otimizar_horarios(self):
        # Implemente aqui o código de otimização com aprendizado de máquina.

    def criar_tabela_horarios(self):
        dados_alunos = []
        for aluno in self.alunos:
            dados_aluno = {
                "Aluno": aluno.nome
            }
            for disciplina, horario in aluno.disciplinas.items():
                dados_aluno[disciplina.nome] = horario

            media = aluno.calcular_media_bimestral()
            dados_aluno["Média Bimestral"] = media
            dados_alunos.append(dados_aluno)

            print(f"Tabela de horários para {aluno.nome}:")
            for disciplina, horario in aluno.disciplinas.items():
                print(f"{disciplina.nome}: {horario}")
            print(f"Média Bimestral: {media}\n")

        # Criar um DataFrame pandas para armazenar os dados dos alunos
        df = pd.DataFrame(dados_alunos)

        # Gerar um gráfico de barras da média bimestral
        plt.figure(figsize=(10, 5))
        plt.bar(df["Aluno"], df["Média Bimestral"])
        plt.xlabel("Aluno")
        plt.ylabel("Média Bimestral")
        plt.title("Média Bimestral dos Alunos")
        plt.xticks(rotation=45)
        plt.tight_layout()

        # Mostrar o gráfico
        plt.show()

# Exemplo de uso:
matematica = Disciplina("Matemática", 8.5)
historia = Disciplina("História", 7.0)
ciencias = Disciplina("Ciências", 8.0)
ingles = Disciplina("Inglês", 9.0)

aluno1 = Aluno("João")
aluno2 = Aluno("Maria")

aluno1.adicionar_disciplina(matematica, "Segunda 9:00-10:30")
aluno1.adicionar_disciplina(historia, "Terça 14:00-15:30")

aluno2.adicionar_disciplina(ciencias, "Quarta 10:00-11:30")
aluno2.adicionar_disciplina(ingles, "Segunda 14:00-15:30")

alunos = [aluno1, aluno2]
disciplinas = [matematica, historia, ciencias, ingles]
horarios_disponiveis = ["Segunda 9:00-10:30", "Terça 14:00-15:30", "Quarta 10:00-11:30"]

otimizador = OtimizadorHorario(alunos, disciplinas, horarios_disponiveis)
otimizador.otimizar_horarios()
otimizador.criar_tabela_horarios()

def main():
    # Exemplo de uso
    matematica = Disciplina("Matemática", 8.5)
    historia = Disciplina("História", 7.0)
    ciencias = Disciplina("Ciências", 8.0)
    ingles = Disciplina("Inglês", 9.0)

    aluno1 = Aluno("João")
    aluno2 = Aluno("Maria")

    aluno1.adicionar_disciplina(matematica, "Segunda 9:00-10:30")
    aluno1.adicionar_disciplina(historia, "Terça 14:00-15:30")

    aluno2.adicionar_disciplina(ciencias, "Quarta 10:00-11:30")
    aluno2.adicionar_disciplina(ingles, "Segunda 14:00-15:30")

    alunos = [aluno1, aluno2]

    # Criando um DataFrame com os dados
    dados_alunos = []
    for aluno in alunos:
        dados_aluno = {
            "Aluno": aluno.nome
        }
        for disciplina, horario in aluno.disciplinas.items():
            dados_aluno[disciplina.nome] = horario
        media = aluno.calcular_media_bimestral()
        dados_aluno["Média Bimestral"] = media
        dados_alunos.append(dados_aluno)

    df = pd.DataFrame(dados_alunos)

    # 1. Média bimestral VS disciplina (barras)
    plt.figure(figsize=(10, 5))
    df.plot(x='Média Bimestral', y='Aluno', kind='bar', rot=0)
    plt.xlabel("Média Bimestral")
    plt.ylabel("Aluno")
    plt.title("Média Bimestral VS Aluno (Barras)")
    plt.tight_layout()
    plt.show()

    # 2. Média bimestral VS disciplina (barras) VS dia da semana (dispersão)
    dia_semana = ["Segunda", "Terça", "Quarta", "Quinta", "Sexta"]

    plt.figure(figsize=(10, 5))
    for dia in dia_semana:
        filtered_data = df[df.columns.difference(dia_semana + ["Aluno"])]
        filtered_data.plot(x='Média Bimestral', y=dia, kind='scatter', label=dia)
    plt.xlabel("Média Bimestral")
    plt.ylabel("Disciplina")
    plt.title("Média Bimestral VS Disciplina (Barras) VS Dia da Semana (Dispersão)")
    plt.legend()
    plt.tight_layout()
    plt.show()

    # 3. Média bimestral VS disciplina (barras) (todos os bimestres) mostrando a evolução de um bimestre a outro
    bimestres = ["Bimestre 1", "Bimestre 2", "Bimestre 3", "Bimestre 4"]

    plt.figure(figsize=(10, 5))
    for bimestre in bimestres:
        filtered_data = df[df.columns.difference(dia_semana + ["Aluno"])]
        filtered_data.plot(x='Média Bimestral', y='Disciplina', kind='bar', label=bimestre, rot=0)
    plt.xlabel("Média Bimestral")
    plt.ylabel("Disciplina")
    plt.title("Média Bimestral VS Disciplina (Barras) - Evolução dos Bimestres")
    plt.legend()
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    main()