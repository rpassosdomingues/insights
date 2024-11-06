
# Documentação do Projeto: Sistema de Gerenciamento de Incubadora - CERNE
## Visão Geral

Este projeto consiste na criação de um Sistema de Gerenciamento de Incubadora, com foco nas práticas do modelo de certificação CERNE. O CERNE é um modelo de evolução de incubadoras de empresas que vai de CERNE 1 (nível básico) até CERNE 4 (nível internacional). Cada nível requer o cumprimento de determinadas práticas, sendo que muitas delas são transversais, ou seja, aplicam-se em múltiplos níveis.

O objetivo do sistema é automatizar o registro e a organização das ações realizadas pela incubadora, facilitando a centralização de evidências em um banco de dados. Com isso, o sistema permite rotular as ações realizadas e associá-las aos diferentes níveis do CERNE, cruzando dados para gerar relatórios detalhados.

Arquitetura do Sistema
O sistema é desenvolvido com base nos conceitos de Programação Orientada a Objetos (POO) em Java. Ele utiliza uma abordagem de herança para estruturar as classes que representam as práticas e as ações da incubadora. Cada prática é modelada como uma classe que herda de outra, promovendo a reutilização de código.

# Estrutura de Classes
## 1. Classe Incubadora
## Gerencia os projetos e as ações da incubadora.

Atributos:
projetos: Lista de projetos.
Métodos:
adicionarProjeto(Projeto projeto): Adiciona um projeto à incubadora.
listarProjetos(): Lista todos os projetos adicionados à incubadora.
contaAcao(String acaoExecutada): Conta quantas vezes uma determinada ação foi executada.
classificaPratica(String criterio): Classifica práticas com base em um critério (local, dia, assunto, etc.).
geraRelatorio(): Gera um relatório de todos os projetos e ações.

## 2. Classe Projeto
Classe base que representa um projeto na incubadora.
Atributos:
nomeProjeto: Nome do projeto.
acaoExecutada: Ação executada.
nomeEvento: Nome do evento associado ao projeto.
Métodos:
Getters e setters para os atributos.

## 3. Classe Acao
Herda de Projeto e adiciona detalhes específicos sobre a ação realizada.
Atributos:
local: Local onde a ação foi realizada.
dia: Data da ação.
horarioInicio: Horário de início.
horarioTermino: Horário de término.
assunto: Assunto relacionado à ação.
Métodos:
Getters e setters para os atributos.

## 4. Classes Herdeiras de Acao
Evento: Herda de Acao e adiciona métodos específicos para eventos.
DICOM: Herda de Evento e representa a Diretoria de Comunicação Social.
Arte: Herda de DICOM e se relaciona com a criação de artes para divulgação.
GestaoAmbiental: Herda de Interacao e lida com práticas de gestão ambiental.
Interacao: Herda de Servicos e trata da interação com o entorno.
Internacionalizacao: Herda de ResponsabilidadeSocial e foca em práticas de internacionalização.
Fluxo de Funcionamento
Cadastro de Projetos e Ações:
O sistema permite que o usuário cadastre projetos e ações associadas. Cada ação pode ser rotulada como pertencente ao CERNE 1, 2, 3 ou 4, ou a mais de um nível.
Classificação e Relatórios:
O sistema classifica as ações com base em diferentes critérios, como data, local, assunto, etc. Os usuários podem consultar dados cruzados entre diferentes níveis de práticas do CERNE.
Organização de Evidências:
O sistema centraliza e organiza as evidências (documentos, imagens, etc.) nas pastas corretas automaticamente, evitando a replicação manual de arquivos em diversos diretórios.

# Funcionalidades Principais

Interface de Usuário: A aplicação possui uma interface simples em JavaFX, com botões para reservar salas, agendar visitas, solicitar peças do Maker, entre outras ações.
Gerenciamento de Projetos e Ações: O sistema gerencia e classifica projetos, ações e práticas, permitindo a visualização detalhada de cada uma.
Geração de Relatórios: Gera relatórios baseados nos projetos e ações, organizando-os por critérios como local, dia, e assunto.
Centralização de Evidências: Automatiza a organização de evidências, eliminando a necessidade de upload manual para diferentes pastas.

## Requisitos
Linguagem de Programação: Java (JavaFX para interface gráfica).
Banco de Dados: Deve ser integrado com um banco de dados relacional para armazenar as evidências e metadados das ações (recomendado MySQL ou PostgreSQL).
Compatibilidade: O software deve ser multiplataforma (Windows, Linux, Mac, iOS, Android).
Segurança: Implementação de controle de acesso para diferentes perfis de usuários.
Escalabilidade: A solução deve suportar a expansão de práticas e ações à medida que novos níveis do CERNE forem alcançados.

## Possíveis Melhorias Futuras
Integração com APIs de armazenamento de arquivos para facilitar o upload e organização das evidências.
Implementação de relatórios gráficos para melhor visualização das práticas e ações.
Automação de processos de auditoria para verificar o cumprimento das práticas do CERNE.
Adaptação para um sistema web para maior acessibilidade.

## Conexão entre os Métodos
Cadastrar: Usado para registrar novos projetos, campanhas, monitoramentos, etc.
Consultar: Usado para consultar informações de um projeto ou campanha já cadastrada.
Agendar: Permite agendar reuniões, campanhas ou ações em diversas áreas.
Reservar: Usado para reservar salas ou espaços.
Solicitar: Pode ser incluído como uma funcionalidade para que os usuários façam pedidos de novas ações ou campanhas, dependendo do contexto.
Todos esses métodos são organizados de maneira que a classe principal Incubadora possa coordenar e utilizar os recursos de cada uma das classes especializadas.

## Práticas como Enum: 31 práticas constantes definidas em um enum.

- Tags Fixas: As tags serão pré-definidas e cadastradas no banco de dados, e você poderá selecionar várias tags usando caixas de seleção (checkboxes).

- Decisor Automático: O sistema irá automaticamente comparar as tags selecionadas com as práticas já cadastradas e decidir qual(is) prática(s) são mais adequadas para a ação, com base no grafo de relações entre as tags e práticas.

- Práticas Definidas: As práticas serão um enum de 31 valores constantes.
- Tags: Tags serão selecionadas por meio de caixas de seleção.
- Decisão Automática: Após selecionar as tags, o sistema compara as tags escolhidas com as associadas às práticas no grafo, e automaticamente decide qual(is) prática(s) está(ão) mais associada(s) às tags selecionadas.

## Etapas para Implementação

1. Definição das Práticas (Enum):

As práticas serão representadas por um enum.
Cada prática terá um conjunto de tags associadas a ela.

2. Banco de Tags:

Teremos um conjunto de tags previamente cadastradas no banco de dados.
As tags poderão ser selecionadas por caixas de seleção.

3. Decisor (Lógica de Vinculação):

O sistema irá comparar as tags selecionadas com as tags associadas às práticas no banco de dados e determinar qual prática (ou práticas) são as mais adequadas para a ação.