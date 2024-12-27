#!/bin/bash

# Nome do commit inicial
commit_message="$(date +'%b %d %a')"

# Verifica se o diretório atual é um repositório Git
if [ ! -d ".git" ]; then
  echo "Erro: Este diretório não é um repositório Git."
  exit 1
fi

echo "Criando um branch órfão para limpar o histórico..."
git checkout --orphan novo-branch

echo "Adicionando todos os arquivos ao novo branch..."
git add .

echo "Fazendo um commit inicial..."
git commit -m "$commit_message"

echo "Excluindo o branch principal antigo..."
git branch -D main

echo "Renomeando o novo branch para 'main'..."
git branch -m main

echo "Forçando o envio para o repositório remoto (excluindo histórico anterior)..."
git push origin main --force

echo "Histórico do repositório reiniciado com sucesso!"

