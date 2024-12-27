import pandas as pd
import numpy as np
import tensorflow as tf
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler

class FrostPredictionModel:
    def __init__(self):
        self.model = None

    def build_model(self, input_dim):
        model = tf.keras.Sequential([
            tf.keras.layers.Input(shape=(input_dim,)),
            tf.keras.layers.Dense(64, activation='relu'),
            tf.keras.layers.Dense(32, activation='relu'),
            tf.keras.layers.Dense(1, activation='sigmoid')
        ])
        model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])
        self.model = model

    def train_model(self, X, y, epochs=100, batch_size=32, validation_split=0.2):
        self.model.fit(X, y, epochs=epochs, batch_size=batch_size, validation_split=validation_split)

    def evaluate_model(self, X_test, y_test):
        loss, accuracy = self.model.evaluate(X_test, y_test)
        return loss, accuracy

    def save_model(self, model_path):
        self.model.save(model_path)

    def load_model(self, model_path):
        self.model = tf.keras.models.load_model(model_path)

def main():
    # Carregar dados meteorológicos, do solo e das plantas usando o Pandas
    weather_data = pd.read_csv('weather_data.csv')
    soil_data = pd.read_csv('soil_data.csv')
    plant_data = pd.read_csv('plant_data.csv')
    frost_data = pd.read_csv('frost_data.csv')

    # Combinar os dados em um único DataFrame com base em IDs ou datas

    combined_data = pd.merge(weather_data, soil_data, on='id', how='inner')
    combined_data = pd.merge(combined_data, plant_data, on='id', how='inner')

    # Dividir os dados em recursos (X) e rótulos (y)

    X = combined_data.drop(columns=['frost'])
    y = combined_data['frost']

    # Dividir o conjunto de dados em treinamento e teste

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

    # Padronizar os recursos

    scaler = StandardScaler()
    X_train = scaler.fit_transform(X_train)
    X_test = scaler.transform(X_test)

    # Inicializar e treinar o modelo

    input_dim = X_train.shape[1]
    model = FrostPredictionModel()
    model.build_model(input_dim)
    model.train_model(X_train, y_train, epochs=100, batch_size=32, validation_split=0.2)

    # Avaliar o modelo

    loss, accuracy = model.evaluate_model(X_test, y_test)
    print(f"Loss: {loss}, Accuracy: {accuracy}")

    # Salvar o modelo treinado

    model.save_model('frost_prediction_model.h5')

if __name__ == '__main__':
    main()
