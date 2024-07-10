/**
 * Este script automatiza o envio de feedback instantâneo
 * quanto à disponibilidade de salas na Incubadora e a criação de eventos no Google Agenda.
 *
 * Autor: Rafael Passos Domingues
 * Última atualização: 2024-07-09
 */

// Identificador do Formulário
var FORM_ID = '/d/id/edit';
// URL da planilha de respostas
var URL_PLANILHA_RESPOSTAS = 'https://docs.google.com/spreadsheets/d/id/edit?resourcekey=&gid=42#gid=42';
// Nome da aba onde estão as respostas
var SHEET_NAME = 'Reserva_Salas';
// Identificador da Google Agenda
var CALENDAR_ID = 'c_id@group.calendar.google.com';

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
  var indicePergunta = encontraPergunta(pergunta, matrizRespostas);
  
  if (indicePergunta !== -1 && matrizRespostas.length > 1) {
    // Percorrer até a última linha para obter a resposta da pergunta específica
    for (var i = matrizRespostas.length - 1; i > 0; i--) {
      var resposta = matrizRespostas[i][indicePergunta];
      if (resposta !== "") {
        return resposta;
      }
    }
  }
  
  return ""; // Retornar vazio se a pergunta não for encontrada ou se não houver respostas suficientes
}

/**
 * Função para verificar a disponibilidade de uma sala no dia e intervalo de horário solicitados
 * @param {string} sala - A sala solicitada
 * @param {string} dia - O dia da reserva (formato dd/mm/yyyy)
 * @param {string} horarioInicio - O horário de início da reserva no formato hh:mm
 * @param {string} horarioTermino - O horário de término da reserva no formato hh:mm
 * @param {array} matrizRespostas - A matriz contendo todas as respostas do formulário
 * @return {boolean} - True se a sala estiver disponível, False se não estiver
 */
function verificaDisponibilidade(sala, dia, horarioInicio, horarioTermino, matrizRespostas) {
  if (!Array.isArray(matrizRespostas) || matrizRespostas.length === 0) {
    return true; // Se não há respostas, a sala está disponível
  }

  // Percorrendo a matriz de respostas da linha mais recente para a mais antiga
  for (var i = matrizRespostas.length - 1; i > 0; i--) {
    var resposta = matrizRespostas[i];
    var respostaSala = resposta[encontraPergunta("Qual espaço deseja reservar?", matrizRespostas)];
    var respostaDia = resposta[encontraPergunta("Qual a data de sua reserva? (Mínimo 2 dias úteis de antecedência)", matrizRespostas)];
    var respostaHorarioInicio = resposta[encontraPergunta("Qual horário de início da sua reserva?", matrizRespostas)];
    var respostaHorarioTermino = resposta[encontraPergunta("Qual horário de término da sua reserva?", matrizRespostas)];

    // Verificar se a sala e o dia correspondem
    if (respostaSala === sala && respostaDia === dia) {
      var reservaInicio = respostaHorarioInicio;
      var reservaFim = respostaHorarioTermino;

      // Verificar se há sobreposição de horários
      if (
        (horarioInicio >= reservaInicio && horarioInicio < reservaFim) ||
        (horarioTermino > reservaInicio && horarioTermino <= reservaFim) ||
        (horarioInicio <= reservaInicio && horarioTermino >= reservaFim)
      ) {
        return false; // A sala já está reservada para o dia e intervalo de horário solicitados
      }
    }
  }

  return true; // A sala está disponível
}

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
 * @param {string} sala - A sala reservada
 * @param {string} dia - O dia da reserva
 * @param {string} horarioInicio - O horário de início da reserva
 * @param {string} horarioTermino - O horário de término da reserva
 * @param {boolean} disponibilidade - Indica se a sala está disponível ou não
 */
function enviaEmailsEmCopia(protocolo, nome, email, sala, dia, horarioInicio, horarioTermino, disponibilidade) {
  
  // Define o assunto base
  var assuntoBase = "[Reserva de Salas i9]";

  // Modifica o assunto base com base na disponibilidade
  var assunto;
  if (disponibilidade) {
    assunto = `${assuntoBase} Sala Disponível`;
  } else {
    assunto = `${assuntoBase} Sala Ocupada`;
  }
  
  // Define os e-mails em cópia
  var ccEmails = ["inovacao@unifal-mg.edu.br", "niduslabmaker@unifal-mg.edu.br"];

  // Corpo do e-mail com o protocolo
  var corpo;
  if (disponibilidade) {
    corpo = `Olá, ${nome}.\n\nSua reserva para utilização da ${sala}, no dia ${dia} das ${horarioInicio} às ${horarioTermino} horas, foi registrada com sucesso!\n\nO protocolo da sua solicitação é: ${protocolo}.\n\nApós utilizar o espaço, por favor, não se esqueça de fechar todas as janelas, desligar o ar condicionado e demais equipamentos e informar o responsável quanto ao fim da utilização do espaço.\n\nAtenciosamente,\nEquipe da Incubadora de Empresas de Base Tecnológica da UNIFAL-MG`;
  } else {
    corpo = `Olá, ${nome}.\n\nInfelizmente a sala solicitada estará ocupada no dia e horário informados.\n\nPor favor, escolha outro horário e faça uma nova solicitação.\n\nAtenciosamente,\nEquipe da Incubadora de Empresas de Base Tecnológica da UNIFAL-MG`;
  }

  // Enviar e-mail com cópias atendendo cada caso
  MailApp.sendEmail({
    to: email,
    cc: ccEmails.join(","),
    subject: assunto,
    body: corpo
  });
}

function convertDateToDDMMYYYY(dateString) {
  // Parse the date string into a Date object
  const date = new Date(dateString);

  // Check if the date is valid (optional)
  if (isNaN(date.getTime())) {
    return "Invalid Date";
  }

  // Format the date in dd/mm/yyyy format using toLocaleDateString() with 'en-GB' locale
  const formattedDate = date.toLocaleDateString('en-GB');

  return formattedDate;
}

function convertTimeToHHMM(timeString) {
  // Try parsing the string as a full date object
  const date = new Date(timeString);

  // Check if parsing was successful (full date format)
  if (!isNaN(date.getTime())) {
    // Extract hours and minutes using getHours() and getMinutes()
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    return `${hours}:${minutes}`;
  } else {
    // If parsing fails, assume it's just an hour in decimal format
    const hours = parseFloat(timeString);

    // Ensure hours are within valid range (0-23)
    const formattedHours = Math.floor(Math.max(0, Math.min(23, hours)));

    // Calculate minutes from decimal part (optional)
    const minutes = Math.round((hours - formattedHours) * 60);

    // Format minutes with leading zero
    const formattedMinutes = minutes.toString().padStart(2, '0');

    return `${formattedHours}:${formattedMinutes}`;
  }
}

/**
 * Função para criar um evento no Google Agenda
 * @param {string} nome - O nome do evento
 * @param {string} sala - A sala reservada
 * @param {string} dia - O dia da reserva
 * @param {string} horarioInicio - O horário de início da reserva
 * @param {string} horarioTermino - O horário de término da reserva
 */
function criaEventoNaAgenda(nome, sala, dia, horarioInicio, horarioTermino) {
  var calendar = CalendarApp.getCalendarById(CALENDAR_ID);
  var startDate = new Date(`${dia}T${horarioInicio}:00`);
  var endDate = new Date(`${dia}T${horarioTermino}:00`);

  calendar.createEvent(`Reserva de Sala: ${sala}`, startDate, endDate, {
    description: `Reserva feita por: ${nome}\nSala: ${sala}\nData: ${dia}\nHorário: ${horarioInicio} - ${horarioTermino}`
  });
}

/**
 * Função principal que coordena a execução das outras funções
 */
function main() {
  var planilha = SpreadsheetApp.openByUrl(URL_PLANILHA_RESPOSTAS);
  var aba = planilha.getSheetByName(SHEET_NAME);
  var matrizRespostas = aba.getDataRange().getValues();

  // Perguntas específicas do formulário
  var perguntaNome = "Nome do Solicitante";
  var perguntaEmail = "Endereço de e-mail";
  var perguntaSala = "Qual espaço deseja reservar?";
  var perguntaDia = "Qual a data de sua reserva? (Mínimo 2 dias úteis de antecedência)";
  var perguntaHorarioInicio = "Qual horário de início da sua reserva?";
  var perguntaHorarioTermino = "Qual horário de término da sua reserva?";
  var perguntaCarimboDataHora = "Carimbo de data/hora";

  // Obter as respostas da última linha da matriz
  var nome = obtemResposta(perguntaNome, matrizRespostas);
  var email = obtemResposta(perguntaEmail, matrizRespostas);
  var sala = obtemResposta(perguntaSala, matrizRespostas);
  var day = obtemResposta(perguntaDia, matrizRespostas);
  var startHour = obtemResposta(perguntaHorarioInicio, matrizRespostas);
  var endHour = obtemResposta(perguntaHorarioTermino, matrizRespostas);
  var carimboDataHora = obtemResposta(perguntaCarimboDataHora, matrizRespostas);

  var dia = convertDateToDDMMYYYY(day);
  var horarioInicio = convertTimeToHHMM(startHour);
  var horarioTermino = convertTimeToHHMM(endHour);

  // Verificar a disponibilidade da sala
  var disponibilidade = verificaDisponibilidade(sala, dia, horarioInicio, horarioTermino, matrizRespostas);

  // Gerar o protocolo com base no carimbo de data e hora
  var protocolo = geraProtocolo(carimboDataHora);

  // Enviar e-mails com cópias condicionais
  enviaEmailsEmCopia(protocolo, nome, email, sala, dia, horarioInicio, horarioTermino, disponibilidade);

  // Criar evento no Google Agenda se a sala estiver disponível
  if (disponibilidade) {
    criaEventoNaAgenda(nome, sala, dia, horarioInicio, horarioTermino);
  }
}
