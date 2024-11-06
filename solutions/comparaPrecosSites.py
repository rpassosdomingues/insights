import requests
from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.webdriver.firefox.options import Options
from selenium.webdriver.common.keys import Keys


class SiteCompra:
    def __init__(self, nome):
        self.nome = nome
        self.options = Options()
        self.options.headless = True
        self.driver = webdriver.Firefox(options=self.options)

    def buscar_produto(self, produto):
        pass

    def extrair_dados(self, soup):
        pass

    def fechar_driver(self):
        self.driver.quit()


class Amazon(SiteCompra):
    def __init__(self):
        super().__init__('Amazon')

    def buscar_produto(self, produto):
        self.driver.get(f'https://www.amazon.com.br/s?k={produto}')
        soup = BeautifulSoup(self.driver.page_source, 'html.parser')
        return self.extrair_dados(soup)

    def extrair_dados(self, soup):
        nome_produto = soup.find('span', class_='a-size-medium').text.strip()
        preco_produto = soup.find('span', class_='a-offscreen').text.strip()
        return {'nome': nome_produto, 'preco': preco_produto}


class MercadoLivre(SiteCompra):
    def __init__(self):
        super().__init__('Mercado Livre')

    def buscar_produto(self, produto):
        self.driver.get(f'https://www.mercadolivre.com.br/{produto}')
        soup = BeautifulSoup(self.driver.page_source, 'html.parser')
        return self.extrair_dados(soup)

    def extrair_dados(self, soup):
        nome_produto = soup.find('h1', class_='ui-pdp-title').text.strip()
        preco_produto = soup.find('span', class_='price-tag-fraction').text.strip()
        return {'nome': nome_produto, 'preco': preco_produto}


class ComparadorPrecos:
    def __init__(self):
        self.sites = [Amazon(), MercadoLivre()]

    def buscar_produto_em_sites(self, produto):
        resultados = []
        for site in self.sites:
            resultado = site.buscar_produto(produto)
            resultados.append(resultado)
            site.fechar_driver()
        return resultados

    def comparar_precos(self, resultados):
        menor_preco = float('inf')
        site_menor_preco = None

        for resultado in resultados:
            preco = float(resultado['preco'].replace('R$', '').replace('.', '').replace(',', '.'))
            if preco < menor_preco:
                menor_preco = preco
                site_menor_preco = resultado['site']

        return site_menor_preco, menor_preco


# Produto a ser buscado
produto = input('Digite o nome do produto: ')

# Realizar a busca nos sites e obter os resultados
comparador = ComparadorPrecos()
resultados = comparador.buscar_produto_em_sites(produto)

# Comparar preços e obter o site com o menor preço
site_menor_preco, menor_preco = comparador.comparar_precos(resultados)

print(f'O produto {produto} é mais barato no {site_menor_preco}. Preço: R${menor_preco:.2f}')
