import matplotlib.pyplot as plt

# Anos de 2000 a 2021
anos = list(range(2000, 2022))

# Valores para as habilidades ao longo dos anos
coordenacao_com_outros = [5, 6, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9]
criatividade = [6, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8]
negociacao = [4, 5, 5, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7]
flexibilidade = [5, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7]
inteligencia_emocional = [4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7]
julgamento = [5, 5, 5, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7]
tomada_de_decisoes_criticas = [5, 5, 5, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7]
oferta_de_beneficios_corporativos = [6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7]

# Crie o gráfico de linha
plt.figure(figsize=(12, 6))
plt.plot(anos, coordenacao_com_outros, label='Coordenação com Outros', marker='o')
plt.plot(anos, criatividade, label='Criatividade', marker='o')
plt.plot(anos, negociacao, label='Negociação', marker='o')
plt.plot(anos, flexibilidade, label='Flexibilidade', marker='o')
plt.plot(anos, inteligencia_emocional, label='Inteligência Emocional', marker='o')
plt.plot(anos, julgamento, label='Julgamento', marker='o')
plt.plot(anos, tomada_de_decisoes_criticas, label='Tomada de Decisões Críticas', marker='o')
plt.plot(anos, oferta_de_beneficios_corporativos, label='Oferta de Benefícios Corporativos', marker='o')

# Adicione rótulos e título
plt.xlabel('Anos')
plt.ylabel('Pontuação das Habilidades')
plt.title('Comparativo de Habilidades Scrum (2000-2021)')

# Adicione uma legenda
plt.legend(loc='best')

# Mostrar o gráfico
plt.grid(True)
plt.show()
