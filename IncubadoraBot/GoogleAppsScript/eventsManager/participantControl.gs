/**
 * Este script automatiza a gestão de inscrições em eventos, incluindo:
 * - Gerenciamento de status de publicações;
 * - Criação de pastas organizadas por evento, com subpastas para evidências;
 * - Criação automática de um formulário Google associado ao evento;
 * - Registro de URLs de evidências na planilha.
 *
 * Autor: Rafael Passos Domingues
 * Última atualização: 2024-11-15
 */

// URL da planilha Google e ID da pasta onde os eventos serão organizados
var SPREADSHEET_URL = 'https://docs.google.com/spreadsheets/d/42/edit?gid=42#gid=42';
var PASTA_EVENTOS_ID = "42";

// Declaração de variáveis globais para as abas relevantes
var planilha = SpreadsheetApp.openByUrl(SPREADSHEET_URL);
var abaArtes = planilha.getSheetByName("ARTES");
var abaParticipantes = planilha.getSheetByName("PARTICIPANTES");

/**
 * Função principal chamada ao editar a planilha.
 * Controla o fluxo de execução, verificando condições e delegando tarefas.
 *
 * @param {Object} event - Objeto de evento do Apps Script, contendo informações da célula editada.
 */
function geraListaInscricao(event) {
  const pastaEventos = DriveApp.getFolderById(PASTA_EVENTOS_ID);

  // Obter índices das colunas na aba ARTES
  const cabecalhoArtes = abaArtes.getDataRange().getValues()[0];
  const colunaLegenda = cabecalhoArtes.indexOf("Legenda") + 1;
  const colunaStatus = cabecalhoArtes.indexOf("Status da Publicação") + 1;

  // Identificar célula editada e valor associado
  const linhaEditada = event.range.getRow();
  const colunaEditada = event.range.getColumn();
  const statusEditado = abaArtes.getRange(linhaEditada, colunaEditada).getValue();

  // Disparar ação somente se a célula editada for a de Status e o valor for "Aprovado"
  if (colunaEditada === colunaStatus && statusEditado === "Aprovado") {
    const legenda = abaArtes.getRange(linhaEditada, colunaLegenda).getValue();
    const nomeEvento = obterNomeEvento(planilha, linhaEditada);

    if (!nomeEvento || !legenda) {
      Logger.log("Nome do evento ou legenda não encontrados.");
      return;
    }

    const pastaEvento = criarEstruturaDePasta(pastaEventos, nomeEvento);
    criarFormularioAssociado(pastaEvento, legenda, nomeEvento);
    atualizarStatusPublicado(linhaEditada, colunaStatus);
    registrarEvidencia(linhaEditada, pastaEvento.getUrl());
  }
}

/**
 * Obtém o nome do evento a partir da aba "ARTES".
 *
 * @param {SpreadsheetApp.Spreadsheet} planilha - A planilha ativa.
 * @param {number} linha - Linha correspondente à edição.
 * @returns {string} Nome do evento.
 */
function obterNomeEvento(planilha, linha) {
  const abaArtes = planilha.getSheetByName("EVENTOS");
  const cabecalhoEventos = abaArtes.getDataRange().getValues()[0];
  const colunaEvento = cabecalhoEventos.indexOf("Evento") + 1;
  return abaArtes.getRange(linha, colunaEvento).getValue();
}

/**
 * Cria uma estrutura de pastas para o evento, incluindo uma subpasta para evidências.
 *
 * @param {DriveApp.Folder} pastaEventos - Pasta raiz onde os eventos são armazenados.
 * @param {string} nomeEvento - Nome do evento.
 * @returns {DriveApp.Folder} Pasta criada para o evento.
 */
function criarEstruturaDePasta(pastaEventos, nomeEvento) {
  const timestamp = new Date().toISOString().replace(/[-:T]/g, "").substring(0, 12);
  const nomePastaEvento = `${timestamp}_${nomeEvento}`;
  const pastaEvento = pastaEventos.createFolder(nomePastaEvento);

  const nomeSubpastaEvidencia = `[Evidência] ${nomeEvento}`;
  pastaEvento.createFolder(nomeSubpastaEvidencia);

  return pastaEvento;
}

/**
 * Cria um formulário Google associado ao evento dentro da pasta do evento.
 *
 * @param {DriveApp.Folder} pastaEvento - Pasta do evento onde o formulário será salvo.
 * @param {string} legenda - Legenda usada como descrição do formulário.
 * @param {string} nomeEvento - Nome do evento.
 */
function criarFormularioAssociado(pastaEvento, legenda, nomeEvento) {
  const formulario = FormApp.create(`Inscrição - ${nomeEvento}`);
  formulario.setDescription(legenda);

  // Adicionar as perguntas ao formulário
  formulario.addMultipleChoiceItem()
    .setTitle("Os dados pessoais serão tratados conforme descrito na LGPD.")
    .setChoiceValues(["Eu aceito os termos acima"]);

  formulario.addTextItem().setTitle("Nome").setRequired(true);
  formulario.addTextItem().setTitle("CPF").setRequired(true);
  formulario.addTextItem().setTitle("E-mail").setRequired(true);

  // Salvar o formulário na pasta do evento
  const arquivoFormulario = DriveApp.getFileById(formulario.getId());
  arquivoFormulario.moveTo(pastaEvento);
}

/**
 * Atualiza o status na aba "ARTES" para "Publicado".
 *
 * @param {number} linha - Linha da célula editada.
 * @param {number} colunaStatus - Índice da coluna de status.
 */
function atualizarStatusPublicado(linha, colunaStatus) {
  abaArtes.getRange(linha, colunaStatus).setValue("Publicado");
}

/**
 * Registra a URL da pasta criada na aba "PARTICIPANTES".
 *
 * @param {number} linha - Linha da célula editada.
 * @param {string} urlPasta - URL da pasta criada.
 */
function registrarEvidencia(linha, urlPasta) {
  const cabecalho = abaParticipantes.getDataRange().getValues()[0];
  const colunaEvidencia = cabecalho.indexOf("Evidência") + 1;
  abaParticipantes.getRange(linha, colunaEvidencia).setValue(urlPasta);
}

/**
 * Configura o gatilho para disparar a função `geraListaInscricao` ao editar.
 */
function configurarGatilho() {
  ScriptApp.newTrigger("geraListaInscricao")
    .forSpreadsheet(planilha)
    .onEdit()
    .create();
}
