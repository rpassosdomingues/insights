import matplotlib.pyplot as plt
import numpy as np

# Habilidades e valores para 2015 e 2020
habilidades = [
    "Scrum Knowledge",
     "Meeting Facilitation",
     "Communication",
     "Conflict resolution",
     "Leadership",
     "Technical knowledge",
     "Team empowerment",
     "Removing obstacles",
     "Customer Focus",
     "Continuous improvement"
]

valores_2015 = [8, 7, 8, 7, 6, 5, 6, 7, 6, 6]  # Valores em 2015
valores_2020 = [9, 8, 8, 8, 7, 6, 7, 8, 7, 7]  # Valores em 2020

# Número de habilidades
num_habilidades = len(habilidades)

# Ângulos para o gráfico de radar
angulos = np.linspace(0, 2 * np.pi, num_habilidades, endpoint=False).tolist()
angulos += angulos[:1]  # Feche o gráfico

# Valores para as duas séries de dados (2015 e 2020)
valores_2015 += valores_2015[:1]
valores_2020 += valores_2020[:1]

# Crie o gráfico de radar
fig, ax = plt.subplots(subplot_kw={'projection': 'polar'})
ax.fill(angulos, valores_2015, 'b', alpha=0.1, label='2015')
ax.fill(angulos, valores_2020, 'r', alpha=0.1, label='2023')
ax.set_xticks(angulos[:-1])
ax.set_xticklabels(habilidades)
ax.set_title('Scrum Skills Comparison (2015 vs. 2023)')

# Adicione uma legenda
plt.legend(loc='upper right')

# Mostrar o gráfico
plt.show()
