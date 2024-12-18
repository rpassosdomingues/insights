/**
 * Este script automatiza o envio de feedback instantâneo
 * quanto à disponibilidade de salas na Incubadora e a criação de eventos no Google Agenda.
 *
 * Autor: Rafael Passos Domingues
 * Última atualização: 2024-07-12
 */

// Identificador do Formulário
var FORM_ID = 'form-id';
// URL da planilha de respostas
var URL_PLANILHA_RESPOSTAS = 'https://docs.google.com/spreadsheets/d/form-id/edit?resourcekey=&gid=42#gid=42';
// Nome da aba onde estão as respostas
var SHEET_NAME = 'Reserva_Salas';
// Identificador da Google Agenda
var CALENDAR_ID = 'c_blablabla@group.calendar.google.com';

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

  var indicePergunta = encontraPergunta(pergunta, matrizRespostas);

  if (indicePergunta !== -1) {
    // Percorrer da última linha para a primeira para encontrar a resposta mais recente
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
 * Função para obter as respostas de uma coluna específica (pergunta) da matriz de respostas do formulário
 * @param {string} pergunta - A pergunta exata do formulário
 * @param {array} matrizRespostas - A matriz contendo todas as respostas do formulário
 * @return {array} - Um array com todas as respostas para a pergunta específica
 */
function obtemColunaRespostas(pergunta, matrizRespostas) {
  var indicePergunta = encontraPergunta(pergunta, matrizRespostas);

  if (indicePergunta !== -1) {
    var respostas = [];
    for (var i = 0; i < matrizRespostas.length - 1; i++) { 
      var resposta = matrizRespostas[i][indicePergunta];
      respostas.push(resposta);
    }
    return respostas;
  } else {
    Logger.log("Pergunta não encontrada na matriz de respostas.");
    return [];
  }
}

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
 * Função para verificar a disponibilidade de uma sala no dia e horário solicitados
 * @param {string} sala - A sala solicitada
 * @param {string} perguntaSala - Pergunta correspondente à sala no formulário
 * @param {string} dia - O dia da reserva
 * @param {string} perguntaDia - Pergunta correspondente ao dia no formulário
 * @param {string} horarioInicio - Horário de início da reserva (HH:mm)
 * @param {string} perguntaHorarioInicio - Pergunta correspondente ao horário de início no formulário
 * @param {string} horarioTermino - Horário de término da reserva (HH:mm)
 * @param {string} perguntaHorarioTermino - Pergunta correspondente ao horário de término no formulário
 * @param {array} matrizRespostas - A matriz contendo todas as respostas do formulário
 * @return {boolean} - True se a sala estiver disponível, False se não estiver
 */
function verificaDisponibilidade(sala, perguntaSala, dia, perguntaDia, horarioInicio, perguntaHorarioInicio, horarioTermino, perguntaHorarioTermino, matrizRespostas) {
  // Obtém o vetor inteiro de respostas para cada pergunta específica
  var respostasSalas = obtemColunaRespostas(perguntaSala, matrizRespostas);
  var respostasDias = obtemColunaRespostas(perguntaDia, matrizRespostas);
  var respostasInicios = obtemColunaRespostas(perguntaHorarioInicio, matrizRespostas);
  var respostasTerminos = obtemColunaRespostas(perguntaHorarioTermino, matrizRespostas);

  // Percorre todas as respostas para verificar disponibilidade
  for (var i = respostasSalas.length - 1; i >= 0; i--) {
    // Verifica se é a mesma sala e o mesmo dia
    if (respostasSalas[i] === sala && respostasDias[i] === dia) {
      // Verifica se há sobreposição de horários
      if (verificaHorarios(horarioInicio, horarioTermino, respostasInicios[i], respostasTerminos[i])) {
        return false; // A sala já está reservada para o dia e horário solicitados
      }
    }
  }
  
  return true; // A sala está disponível
}

/**
 * Função auxiliar para verificar a sobreposição de horários
 * @param {string} novoHorarioInicio - Horário de início da nova reserva
 * @param {string} novoHorarioTermino - Horário de término da nova reserva
 * @param {array} horariosExistenteInicio - Vetor de horários de início das reservas existentes
 * @param {array} horariosExistenteTermino - Vetor de horários de término das reservas existentes
 * @return {boolean} - True se houver sobreposição, False se não houver
 */
function verificaHorarios(novoHorarioInicio, novoHorarioTermino, horariosExistenteInicio, horariosExistenteTermino) {
  // Converte o novo horário de início para minutos
  var novoInicioMinutos = convertTimeToHHMM(converteHoraParaMinutos(novoHorarioInicio));
  var novoTerminoMinutos = convertTimeToHHMM(converteHoraParaMinutos(novoHorarioTermino));

  // Percorre os horários existentes para verificar sobreposição
  for (var i = horariosExistenteInicio.length - 1; i >= 0; i--) {
    var existenteInicioMinutos = convertTimeToHHMM(converteHoraParaMinutos(horariosExistenteInicio[i]));
    var existenteTerminoMinutos = convertTimeToHHMM(converteHoraParaMinutos(horariosExistenteTermino[i]));

    // Verifica se há sobreposição de horários
    if ((novoInicioMinutos <= existenteTerminoMinutos && novoTerminoMinutos >= existenteInicioMinutos) ||
        (existenteInicioMinutos <= novoTerminoMinutos && existenteTerminoMinutos >= novoInicioMinutos)) {
      return true; // Há sobreposição de horários
    }
  }

  return false; // Não há sobreposição de horários
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
  return (protocolo);
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
  var ccEmails = ["rafaelpassosdomingues@gmail.com", "rpassosdomingues@gmail.com"];

  // Corpo do e-mail com o protocolo
  var corpo;
  if (disponibilidade) {
    corpo = `Olá, ${nome}.\n\nSua reserva para utilização da ${sala}, no dia ${dia} das ${horarioInicio} às ${horarioTermino} horas, foi registrada com sucesso!\n\nO protocolo da sua solicitação é: ${protocolo}.\n\nApós utilizar o espaço, por favor, não se esqueça de fechar todas as janelas, desligar o ar condicionado e demais equipamentos e informar o responsável quanto ao fim da utilização do espaço.\n\nAtenciosamente,\nEquipe da Incubadora de Empresas de Base Tecnológica da UNIFAL-MG`;
  } else {
    corpo = `Olá, ${nome}.\n\nInfelizmente a sala solicitada estará ocupada no dia ${dia} a partir das ${horarioInicio} horas.\n\nPor favor, escolha outro horário e faça uma nova solicitação.\n\nAtenciosamente,\nEquipe da Incubadora de Empresas de Base Tecnológica da UNIFAL-MG`;
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

  return (formattedDate);
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
 * Função para converter uma string de hora (HH:mm) em minutos
 * @param {string} hora - Hora no formato HH:mm
 * @return {number} - Valor em minutos
 */
function converteHoraParaMinutos(hora) {
  var partes = hora.split(":");
  return parseInt(partes[0], 10) * 60 + parseInt(partes[1], 10);
}

/**
 * Cria um evento no Google Agenda
 * @param {Object} calendar - O objeto do Google Calendar
 * @param {string} sala - Sala a ser reservada
 * @param {string} dia - Dia da reserva no formato dd/mm/yyyy
 * @param {string} horarioInicio - Horário de início da reserva no formato HH:MM
 * @param {string} horarioTermino - Horário de término da reserva no formato HH:MM
 * @param {string} nome - Nome do solicitante
 */
function criaEventoNaAgenda(calendar, nome, sala, dia, horarioInicio, horarioTermino) {
  var dataInicio = converteParaData(dia, horarioInicio);
  var dataFim = converteParaData(dia, horarioTermino);

  calendar.createEvent(`[${sala}] (${nome})`,
    dataInicio,
    dataFim,
    {
      description: `${nome} reservou a sala, ${sala}, no dia ${dia} das ${horarioInicio} às ${horarioTermino} horas.`,
      location: sala
    }
  );

  Logger.log(`Evento criado no Google Agenda: ${nome} reservou a sala, ${sala}, no dia ${dia} das ${horarioInicio} às ${horarioTermino} horas.`);
}

/**
 * Converte uma data e horário para um objeto Date
 * @param {string} dia - Dia no formato dd/mm/yyyy
 * @param {string} horario - Horário no formato HH:MM
 * @returns {Date} - Objeto Date representando a data e horário
 */
function converteParaData(dia, horario) {
  var partesDia = dia.split("/");
  var partesHorario = horario.split(":");
  var data = new Date(partesDia[2], partesDia[1] - 1, partesDia[0]);
  data.setHours(parseInt(partesHorario[0]), parseInt(partesHorario[1]));
  return data;
}

/**
 * Formata uma hora no formato HH:MM
 * @param {Date} data - Objeto Date contendo a hora a ser formatada
 * @returns {string} - Hora formatada no formato HH:MM
 */
function formatarHora(data) {
  var horas = data.getHours();
  var minutos = data.getMinutes();
  return padZero(horas) + ":" + padZero(minutos);
}

/**
 * Adiciona um zero à esquerda se o número for menor que 10
 * @param {number} numero - Número a ser formatado
 * @returns {string} - Número formatado como string
 */
function padZero(numero) {
  if (numero < 10) {
    return "0" + numero;
  }
  return numero.toString();
}

/**
 * Função principal que coordena a execução das outras funções
 */
function main() {
  var planilha = SpreadsheetApp.openByUrl(URL_PLANILHA_RESPOSTAS);
  var aba = planilha.getSheetByName(SHEET_NAME);
  var calendar = CalendarApp.getCalendarById(CALENDAR_ID);
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
  var disponibilidade = verificaDisponibilidade(sala, perguntaSala, day, perguntaDia, startHour, perguntaHorarioInicio, endHour, perguntaHorarioTermino, matrizRespostas);

  // Gerar o protocolo com base no carimbo de data e hora
  var protocolo = geraProtocolo(carimboDataHora);

  // Enviar e-mails com cópias condicionais
  enviaEmailsEmCopia(protocolo, nome, email, sala, dia, horarioInicio, horarioTermino, disponibilidade);

  // Criar evento no Google Agenda se a sala estiver disponível
  if (disponibilidade) {
    criaEventoNaAgenda(calendar, nome, sala, dia, horarioInicio, horarioTermino);
  }
}
