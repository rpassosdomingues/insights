import matplotlib.pyplot as plt
import numpy as np

# Habilidades e valores para 2015
habilidades_2015 = ["Coordenação com Outros", "Criatividade", "Negociação", "Flexibilidade", "Inteligência Emocional", "Julgamento", "Tomada de Decisões Críticas", "Oferta de Benefícios Corporativos"]
valores_2015 = [5, 6, 4, 5, 4, 5, 5, 6]

# Habilidades e valores para 2033
habilidades_2033 = ["Coordenação com Outros", "Criatividade", "Negociação", "Flexibilidade", "Inteligência Emocional", "Julgamento", "Tomada de Decisões Críticas", "Oferta de Benefícios Corporativos", "Machine Learning", "Processamento de Linguagem Natural", "Visão Computacional"]
valores_2033 = [9, 9, 7, 7, 8, 7, 7, 7, 8, 8, 8]

# Número de habilidades
num_habilidades = len(habilidades_2033)

# Ângulos para o gráfico de radar
angulos = np.linspace(0, 2 * np.pi, num_habilidades, endpoint=False).tolist()
angulos += angulos[:1]  # Feche o gráfico

# Crie o gráfico de radar
fig, ax = plt.subplots(subplot_kw={'projection': 'polar'})
ax.set_xticks(angulos[:-1])
ax.set_xticklabels(habilidades_2033)

# Plote os valores para 2015 e 2033
valores_2015 += valores_2015[:1]
valores_2033 += valores_2033[:1]
ax.fill(angulos, valores_2015, 'b', alpha=0.2, label='2015')
ax.fill(angulos, valores_2033, 'r', alpha=0.2, label='2033')

# Adicione uma legenda
plt.legend(loc='upper right', bbox_to_anchor=(1.3, 1.1))

# Mostrar o gráfico
plt.title('Evolução das Habilidades do Scrum Master (2015 vs. 2033)')
plt.show()
