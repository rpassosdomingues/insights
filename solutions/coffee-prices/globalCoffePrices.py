import pandas as pd
import matplotlib.pyplot as plt

# Carregar os dados a partir do arquivo CSV
file_path = "insights\solucoes_locais\coffee-prices\globalCoffeePrices_data.csv"
df = pd.read_csv(file_path, parse_dates=["DATE"])

# Configurar o tamanho do gráfico
plt.figure(figsize=(12, 6))

# Plotar o gráfico de séries temporais
plt.plot(df["DATE"], df["PCOFFOTMUSDM"], label="Preço do Café (USDM)")
plt.title("Preço do Café ao Longo do Tempo")
plt.xlabel("Data")
plt.ylabel("Preço (USDM)")
plt.legend()

# Exibir o gráfico
plt.grid(True)
plt.tight_layout()
plt.show()
