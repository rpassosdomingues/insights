import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import tensorflow as tf
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import LSTM, Dense

# Carregar os dados a partir do arquivo CSV
file_path = "insights/solucoes_locais/coffee-prices/globalCoffeePrices_data.csv"
df = pd.read_csv(file_path, parse_dates=["DATE"])

# Selecionar a coluna de preços
data = df["PCOFFOTMUSDM"].values
data = data.reshape(-1, 1)

# Normalizar os dados entre 0 e 1
data_min = np.min(data)
data_max = np.max(data)
data = (data - data_min) / (data_max - data_min)

# Dividir os dados em treinamento e teste
train_size = int(len(data) * 0.67)
train_data, test_data = data[0:train_size, :], data[train_size:len(data), :]

# Função para criar sequências de dados com base no histórico
def create_sequences(data, seq_length):
    X, y = [], []
    for i in range(len(data) - seq_length):
        X.append(data[i:i + seq_length])
        y.append(data[i + seq_length])
    return np.array(X), np.array(y)

# Definir o comprimento das sequências
seq_length = 12

# Criar sequências de treinamento
X_train, y_train = create_sequences(train_data, seq_length)

# Criar sequências de teste
X_test, y_test = create_sequences(test_data, seq_length)

# Construir o modelo de rede neural
model = Sequential()
model.add(LSTM(50, input_shape=(X_train.shape[1], X_train.shape[2])))
model.add(Dense(1))
model.compile(loss="mean_squared_error", optimizer="adam")

# Treinar o modelo
model.fit(X_train, y_train, epochs=100, batch_size=32)

# Realizar previsões
train_predict = model.predict(X_train)
test_predict = model.predict(X_test)

# Inverter a escala das previsões para obter valores reais
train_predict = train_predict * (data_max - data_min) + data_min
test_predict = test_predict * (data_max - data_min) + data_min

# Plotar os resultados
plt.figure(figsize=(12, 6))
plt.plot(df["DATE"].values[seq_length:train_size], train_predict, label="Previsões de Treinamento", color="blue")
plt.plot(df["DATE"].values[train_size + seq_length:], test_predict, label="Previsões de Teste", color="green")
plt.plot(df["DATE"].values[seq_length:train_size], df["PCOFFOTMUSDM"].values[seq_length:train_size], label="Dados Reais de Treinamento", color="gray")
plt.plot(df["DATE"].values[train_size + seq_length:], df["PCOFFOTMUSDM"].values[train_size + seq_length:], label="Dados Reais de Teste", color="red")
plt.title("Previsão de Preços do Café")
plt.xlabel("Data")
plt.ylabel("Preço (USDM)")
plt.legend()
plt.show()
