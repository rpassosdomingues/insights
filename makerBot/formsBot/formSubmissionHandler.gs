/**
 * Este script automatiza o envio de e-mails em cópia com base nas respostas de um formulário do Google Forms.
 * Inclui o envio de um e-mail de confirmação para o usuário, com cópias condicionais
 * baseadas no campus e na resposta sobre o envolvimento com propriedade intelectual.
 *
 * Autor: Rafael Passos Domingues
 * Última atualização: 2024-07-06
 */

// Variáveis globais
var FORM_ID = '/d/FORM_ID/edit';
var SHEET_NAME = 'nome-aba'; // Nome da aba onde estão as respostas

/**
 * Função para obter a resposta de uma pergunta específica na última linha das respostas do formulário
 * @param {string} pergunta - A pergunta exata do formulário
 * @param {array} matrizRespostas - A matriz contendo todas as respostas do formulário
 * @return {string} - A resposta da pergunta na última linha
 */
function obtemResposta(pergunta, matrizRespostas) {
  if (!matrizRespostas || matrizRespostas.length === 0) {
    Logger.log("Matriz de respostas não está definida ou vazia.");
    return "";
  }

  // Encontrar a posição da pergunta na primeira linha (rótulos de coluna)
  var indexPergunta = matrizRespostas[0].indexOf(pergunta);
  
  if (indexPergunta !== -1 && matrizRespostas.length > 1) {
    // Retornar a resposta da última linha para a pergunta específica
    return matrizRespostas[matrizRespostas.length - 1][indexPergunta];
  } else {
    return ""; // Retornar vazio se a pergunta não for encontrada ou se não houver respostas suficientes
  }
}

/**
 * Função para gerar um ID de protocolo baseado na data e hora da resposta
 * @param {array} matrizRespostas - A matriz contendo todas as respostas do formulário
 * @return {string} - O ID de protocolo gerado no formato yyyymmddhhmmss
 */
function geraProtocolo(matrizRespostas) {
  // Acessar a resposta da pergunta "Carimbo de data/hora"
  var carimboDataHora = obtemResposta("Carimbo de data/hora", matrizRespostas);

  if (carimboDataHora) {
    // Extrair componentes de data e hora
    var dataHora = new Date(carimboDataHora);

    var ano = dataHora.getFullYear();
    var mes = ('0' + (dataHora.getMonth() + 1)).slice(-2); // Mês de 0-11, ajustar para 01-12
    var dia = ('0' + dataHora.getDate()).slice(-2);
    var horas = ('0' + dataHora.getHours()).slice(-2);
    var minutos = ('0' + dataHora.getMinutes()).slice(-2);
    var segundos = ('0' + dataHora.getSeconds()).slice(-2);

    // Construir o formato de protocolo yyyymmddhhmmss
    var protocolo = `${ano}${mes}${dia}${horas}${minutos}${segundos}`;
    return protocolo;
  } else {
    Logger.log("Não foi possível obter o carimbo de data/hora para gerar o protocolo.");
    return ""; // Retornar vazio se não houver resposta disponível
  }
}

/**
 * Função para enviar e-mails com cópias condicionais baseadas nas respostas do formulário
 * @param {string} email - O e-mail do destinatário principal
 * @param {string} campus - O campus de utilização do espaço NidusLab Maker
 * @param {string} isPatente - Indicação se o projeto está envolvido em propriedade intelectual
 * @param {array} matrizRespostas - A matriz contendo todas as respostas do formulário
 */
function enviaEmailsEmCopia(email, campus, isPatente, matrizRespostas) {
  var assunto = "[Formulário Maker] Protocolo da Solicitação de Serviços";
  var protocolo = geraProtocolo(matrizRespostas); // Gerar o protocolo
  
  // Verificar se campus e isPatente estão definidos
  var ccEmails = ["rafaelpassosdomingues@gmail.com"];

  if (campus && campus.toLowerCase().includes("Poços de Caldas - MG")) {
    ccEmails.push("rpassosdomingues@gmail.com");
  } else if (campus && campus.toLowerCase().includes("Varginha - MG")) {
    ccEmails.push("rafael.domingues@sou.unifal-mg.edu.br");
  }

  // Verificar se isPatente está definido
  if (isPatente && (isPatente.toLowerCase().includes("Sim") || isPatente.toLowerCase().includes("Talvez"))) {
    ccEmails.push("rafaelpassosdomingues@outlook.com");
  }

  // Corpo do e-mail com o protocolo
  var corpo = `Olá!\n\nSua solicitação de serviços do Laboratório NidusLab Maker foi registrada com sucesso!\n\nO protocolo da sua solicitação é: ${protocolo}.\n\nAtenciosamente,\nRafael Passos Domingues\nBolsista de Desenvolvimento em Ciência, Tecnologia e Inovação`;

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
function main(e) {
  // Verificar se o objeto de evento e as respostas nomeadas estão presentes
  if (!e || !e.namedValues) {
    Logger.log("Objeto de evento inválido, sem respostas.");
    return;
  }
  
  try {
    // Carregar a planilha de respostas
    var URL_PLANILHA_RESPOSTAS = 'link-aqui' // Substitua pelo URL da sua planilha
    var planilha = SpreadsheetApp.openByUrl(URL_PLANILHA_RESPOSTAS);
    var abaRespostas = planilha.getSheetByName(SHEET_NAME);
    
    if (!abaRespostas) {
      Logger.log("Aba de respostas não encontrada na planilha.");
      return;
    }
    
    // Obter todas as respostas da planilha
    var matrizRespostas = abaRespostas.getDataRange().getValues();
    
    // Gerar protocolo
    var protocolo = geraProtocolo(matrizRespostas);
    
    // Acessar as respostas específicas usando a matriz de respostas
    var email = obtemResposta("Endereço de e-mail", matrizRespostas);
    var campus = obtemResposta("[2.1]  Campus de utilização do espaço NidusLab Maker", matrizRespostas);
    var isPatente = obtemResposta("[4.1]  O projeto a ser desenvolvido está envolvido em processo de propriedade intelectual? (Se a resposta da pergunta acima for sim, a Agência de Inovação e Empreendedorismo entrará em contato com instruções através do contato disponibilizado)", matrizRespostas);
    
    // Verificar se os dados necessários foram obtidos
    if (!email || !campus || !isPatente) {
      Logger.log("Dados essenciais não encontrados nas respostas.");
      return;
    }
    
    // Enviar e-mail com cópias condicionais
    enviaEmailsEmCopia(email, campus, isPatente, matrizRespostas);
    
  } catch (error) {
    Logger.log("Ocorreu um erro ao processar o formulário: " + error.message);
  }
}
