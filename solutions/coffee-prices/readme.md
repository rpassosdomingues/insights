# Previsão de Preços do Café com Redes Neurais Usando TensorFlow

## Introdução

O café é uma das commodities mais consumidas em todo o mundo, e os preços do café são influenciados por uma série de fatores, incluindo condições climáticas, oferta e demanda global, câmbio e muito mais. Prever os preços do café é um desafio significativo devido à natureza complexa e volátil desse mercado. Neste artigo, exploramos como as redes neurais podem ser usadas para prever os preços do café, com um foco especial em utilizar a biblioteca TensorFlow em Python.

## Conjunto de Dados

Para realizar nossa análise, usamos um conjunto de dados de preços globais do café. Os dados incluem informações sobre os preços mensais do café ao longo de várias décadas. Inicialmente, carregamos os dados de um arquivo CSV usando a biblioteca Pandas e os apresentamos em uma série temporal.

## CEPEA-USP
## INPE
## Portal Notícias Agrícolas Sul de Minas
## Global Coffee Prices

## Pré-Processamento dos Dados

Antes de alimentar os dados em uma rede neural, realizamos algumas etapas importantes de pré-processamento. Primeiro, normalizamos os preços do café para garantir que estejam na mesma escala. Isso é feito usando o MinMaxScaler do Scikit-Learn. Em seguida, dividimos os dados em conjuntos de treinamento e teste. A parte inicial dos dados é usada para treinar o modelo, enquanto a parte final é reservada para testar as previsões.
Modelagem da Rede Neural

Utilizamos uma rede neural recorrente (RNN) com uma camada LSTM (Long Short-Term Memory) para modelar a série temporal dos preços do café. A rede é implementada usando o TensorFlow e a biblioteca Keras, que fornece uma interface amigável para construir modelos de aprendizado profundo.

A arquitetura da rede neural consiste em uma camada LSTM com 50 unidades, seguida por uma camada densa que gera a previsão final. O otimizador "adam" é usado para minimizar a função de perda "mean_squared_error". Treinamos o modelo por 100 épocas com um tamanho de lote de 32.
Treinamento e Avaliação

Após treinar o modelo com os dados de treinamento, avaliamos o desempenho usando os dados de teste. Para isso, fazemos previsões em ambos os conjuntos de treinamento e teste e calculamos o erro médio quadrado (MSE) das previsões. O MSE é uma medida de quão bem o modelo está se saindo na tarefa de previsão.
Previsões Futuras

Uma das partes mais interessantes desse projeto é a capacidade do modelo treinado de fazer previsões para o futuro. Usamos os dados mais recentes para prever os preços do café para os próximos 20 anos, gerando uma série temporal de previsões.

## Resultados e Visualização

Finalmente, visualizamos os resultados das previsões e os comparamos com os dados reais. O gráfico resultante mostra as previsões de treinamento e teste, bem como as previsões futuras em relação aos dados reais de preços do café.

## Conclusão

A capacidade de fazer previsões precisas sobre os preços das commodities como o café pode ser valiosa para produtores, comerciantes e investidores. No entanto, é importante observar que as previsões são baseadas em dados históricos e não levam em consideração eventos imprevisíveis que podem afetar os preços, como condições climáticas extremas ou crises econômicas.

Em resumo, este trabalho ilustra como a combinação de ciência de dados, aprendizado de máquina e bibliotecas como TensorFlow pode ser aplicada para abordar problemas complexos, como a previsão de preços de commodities, e fornece uma visão fascinante sobre o mundo do café.