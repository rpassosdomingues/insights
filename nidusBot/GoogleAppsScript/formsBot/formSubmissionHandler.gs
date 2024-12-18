/**
 * Este script automatiza o envio de e-mails em cópia com base nas respostas de um formulário do Google Forms.
 * Inclui o envio de um e-mail de confirmação para o usuário, com cópias condicionais
 * baseadas no campus e na resposta sobre o envolvimento com propriedade intelectual.
 *
 * Autor: Rafael Passos Domingues
 * Última atualização: 2024-07-05
 */

// Identificador do Formulário
var FORM_ID = '42';
// URL da planilha de respostas
var URL_PLANILHA_RESPOSTAS = 'https://docs.google.com/spreadsheets/d/42/edit?resourcekey=&gid=42#gid=42';
// Nome da aba onde estão as respostas
var SHEET_NAME = 'Solicitacoes_Maker';

/**
 * Função para encontrar o índice da coluna correspondente à pergunta na primeira linha da matriz de respostas
 * @param {string} pergunta - A pergunta exata do formulário
 * @param {array} matrizRespostas - A matriz contendo todas as respostas do formulário
 * @return {number} - O índice da coluna onde a pergunta foi encontrada, ou -1 se não encontrada
 */
function encontraPergunta(pergunta, matrizRespostas) {
  if (!matrizRespostas || matrizRespostas.length === 0) {
    Logger.log("Matriz de respostas não está definida ou vazia.");
    return -1;
  }

  // Encontrar a posição da pergunta na primeira linha (rótulos de coluna)
  var primeiraLinha = matrizRespostas[0];
  for (var i = 0; i < primeiraLinha.length; i++) {
    if (primeiraLinha[i] === pergunta) {
      return i;
    }
  }

  // Retornar -1 se a pergunta não for encontrada
  return -1;
}

/**
 * Função para obter a resposta de uma pergunta específica na última linha das respostas do formulário
 * @param {string} pergunta - A pergunta exata do formulário
 * @param {array} matrizRespostas - A matriz contendo todas as respostas do formulário
 * @return {string} - A resposta da pergunta na última linha
 */
function obtemResposta(pergunta, matrizRespostas) {
  // Encontrar o índice da pergunta na matriz (linha de cabeçalhos)
  var indicePergunta = encontraPergunta(pergunta, matrizRespostas);

  // Verificar se o índice da pergunta é válido e se há pelo menos uma resposta
  if (indicePergunta !== -1 && matrizRespostas.length > 1) {
    // Última linha de respostas (a mais recente)
    var ultimaLinha = matrizRespostas[matrizRespostas.length - 1];
    
    // Retornar a resposta da pergunta na última linha
    return ultimaLinha[indicePergunta] || ""; // Retorna a resposta ou uma string vazia se não houver resposta
  }

  return ""; // Retornar vazio se a pergunta não for encontrada ou se não houver respostas suficientes
}

/**
 * Função para obter a resposta de uma pergunta específica na última linha das respostas do formulário
 * @param {string} pergunta - A pergunta exata do formulário
 * @param {array} matrizRespostas - A matriz contendo todas as respostas do formulário
 * @return {string} - A resposta da pergunta na última linha
 */
//function obtemResposta(pergunta, matrizRespostas) {
//  var indicePergunta = encontraPergunta(pergunta, matrizRespostas);
  
//  if (indicePergunta !== -1 && matrizRespostas.length > 1) {
    // Percorrer até a última linha para obter a resposta da pergunta específica
//    for (var i = matrizRespostas.length - 1; i > 0; i--) {
//      var resposta = matrizRespostas[i][indicePergunta];
//      if (resposta !== "") {
//        return resposta;
//      }
//    }
//  }
  
//  return ""; // Retornar vazio se a pergunta não for encontrada ou se não houver respostas suficientes
//}

/**
 * Função para gerar um ID de protocolo baseado na data e hora da resposta
 * @param {string} carimboDataHora - O carimbo de data e hora da resposta do formulário
 * @return {string} - O ID de protocolo gerado no formato yyyymmddhhmm
 */
function geraProtocolo(carimboDataHora) {
  if (!carimboDataHora) {
    Logger.log("Carimbo de data/hora não definido.");
    return "";
  }

  // Extrair componentes de data e hora
  var dataHora = new Date(carimboDataHora);

  var ano = dataHora.getFullYear();
  var mes = ('0' + (dataHora.getMonth() + 1)).slice(-2); // Mês de 0-11, ajustar para 01-12
  var dia = ('0' + dataHora.getDate()).slice(-2);
  var horas = ('0' + dataHora.getHours()).slice(-2);
  var minutos = ('0' + dataHora.getMinutes()).slice(-2);

  // Construir o formato de protocolo yyyymmddhhmm
  var protocolo = `${ano}${mes}${dia}${horas}${minutos}`;
  return protocolo;
}

/**
 * Função para enviar e-mails com cópias condicionais baseadas nas respostas do formulário
 * @param {string} protocolo - O protocolo com base no carimbo de data e hora da resposta do formulário
 * @param {string} nome - O nome do destinatário principal
 * @param {string} email - O e-mail do destinatário principal
 * @param {string} campus - O campus de utilização do espaço NidusLab Maker
 * @param {string} isPatente - Indicação se o projeto está envolvido em propriedade intelectual
 */
function enviaEmailsEmCopia(protocolo, nome, email, campus, isPatente) {
  var assunto = "[Formulário Maker] Solicitação de Serviços - Protocolo: " + protocolo.toString();
  
  var ccEmails = [];

  // Envia com cópia para diretoria
  ccEmails.push("izabella.carneiro@unifal-mg.edu.br");
  // Envia com cópia para coordenação do Maker
  ccEmails.push("niduslabmaker@unifal-mg.edu.br");

  // Envia com cópia para campus específico
  if (campus && campus.includes("Poços de Caldas - MG")) {
    ccEmails.push("cdtipocos@unifal-mg.edu.br");
  } else if (campus && campus.includes("Varginha - MG")) {
    ccEmails.push("pedro.papandrea@unifal-mg.edu.br");
  }

  // Envia com cópia para equipe PI
  if (isPatente && isPatente.includes("Sim")) {
    ccEmails.push("inovacao@unifal-mg.edu.br");
  }

  // Corpo do e-mail com o protocolo
  var corpo = `Olá, ${nome}.\n\nSua solicitação de serviços do Laboratório NidusLab Maker para o campus ${campus} foi registrada com sucesso! \n\nO protocolo da sua solicitação é: ${protocolo}.\n\nAtenciosamente,\nEquipe do Laboratório NidusLab Maker`;

  // Enviar e-mail com cópias atendendo cada caso
  MailApp.sendEmail({
    to: email,
    cc: ccEmails.join(","),
    subject: assunto,
    body: corpo
  });
}

/**
 * Função principal que coordena a execução das outras funções na ordem correta ao enviar o formulário
 * @param {object} e - O objeto de evento contendo as respostas do formulário
 */
function main() {
  var planilha = SpreadsheetApp.openByUrl(URL_PLANILHA_RESPOSTAS);
  var abaRespostas = planilha.getSheetByName(SHEET_NAME);
    
  if (!abaRespostas) {
    Logger.log("Aba de respostas não encontrada na planilha.");
    return;
  }

  // Adiciona um atraso de 60 segundos para garantir que as respostas sejam salvas na planilha
  Utilities.sleep(60000); // 60000 milissegundos = 60 segundos
    
  // Obter todas as respostas da planilha
  var matrizRespostas = abaRespostas.getDataRange().getValues();
    
  // Acessar as respostas específicas usando a matriz de respostas
  var carimboDataHora = obtemResposta("Carimbo de data/hora", matrizRespostas);
  var nome = obtemResposta("[1.1] Nome Completo do Solicitante (Servidor da UNIFAL)", matrizRespostas);
  var email = obtemResposta("Endereço de e-mail", matrizRespostas);
  var campus = obtemResposta("[2.1]  Campus de utilização do espaço NidusLab Maker", matrizRespostas);
  var isPatente = obtemResposta("[4.1]  O projeto a ser desenvolvido está envolvido em processo de propriedade intelectual? (Se a resposta da pergunta acima for sim, a Agência de Inovação e Empreendedorismo entrará em contato com instruções através do contato disponibilizado)", matrizRespostas);

  // Verificar se os dados necessários foram obtidos
  if (!carimboDataHora || !nome || !email || !campus || !isPatente) {
    Logger.log("Respostas essenciais não encontradas.");
    return;
  }

  // Gera o protocolo
  var protocolo = geraProtocolo(carimboDataHora);
    
  // Enviar e-mail com cópias condicionais
  enviaEmailsEmCopia(protocolo, nome, email, campus, isPatente);

}
