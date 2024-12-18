/**
 * Este script automatiza a criação de eventos no Google Calendar a partir de dados na planilha.
 *
 * Autor: Rafael Passos Domingues
 * Última atualização: 2024-08-01
 * 
 * Erro: Corrigir formato de dia e horários
 */

// Nome da aba onde estão os dados dos eventos
var SHEET_NAME = 'Eventos i9';
// Identificador da Google Agenda
var CALENDAR_ID = 'c_calendarId@group.calendar.google.com';
// Nome da coluna de status
var STATUS_COLUMN_NAME = 'Google Agenda';

/**
 * Converte uma data para o formato dd/mm/yyyy
 * @param {Date} date - Objeto Date representando a data
 * @returns {string} - Data formatada no formato dd/mm/yyyy
 */
function convertDateToDDMMYYYY(date) {
  if (isNaN(date.getTime())) {
    return "Data Inválida";
  }
  return date.toLocaleDateString('pt-BR');
}

/**
 * Converte um horário para o formato HH:MM
 * @param {string} timeString - Horário no formato HH:MM:SS
 * @returns {string} - Horário formatado no formato HH:MM
 */
function convertTimeToHHMM(timeString) {
  // Verifica se o horário está no formato esperado
  const timeParts = timeString.split(':');
  
  if (timeParts.length < 2) {
    throw new Error("Formato de horário inválido.");
  }
  
  const hours = timeParts[0].padStart(2, '0'); // Hora
  const minutes = timeParts[1].padStart(2, '0'); // Minuto

  // Retorna o horário formatado
  return `${hours}:${minutes}`;
}

/**
 * Converte uma data e horário para um objeto Date
 * @param {string} dia - Dia no formato dd/mm/yyyy
 * @param {string} horario - Horário no formato HH:MM
 * @returns {Date} - Objeto Date representando a data e horário
 */
function converteParaData(dia, horario) {
  // Verifica se a data e horário estão no formato esperado
  if (!/^\d{2}\/\d{2}\/\d{4}$/.test(dia) || !/^\d{2}:\d{2}$/.test(horario)) {
    throw new Error("Formato de data ou horário inválido.");
  }

  var partesDia = dia.split("/");
  var partesHorario = horario.split(":");

  // Cria o objeto Date com base no formato dd/mm/yyyy e HH:MM
  var data = new Date(
    parseInt(partesDia[2]), // Ano
    parseInt(partesDia[1], 10) - 1, // Mês (0 baseado)
    parseInt(partesDia[0]), // Dia
    parseInt(partesHorario[0]), // Hora
    parseInt(partesHorario[1]) // Minuto
  );

  // Subtrai 8 horas do horário UTC para corrigir o fuso horário
  data.setHours(data.getHours() - 8);

  // Verifica se a data é válida
  if (isNaN(data.getTime())) {
    throw new Error("Data inválida.");
  }

  // Retorna o objeto Date
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
 * Converte um horário do formato Date String para o formato HH:MM
 * @param {string} dateString - Horário no formato Date String (ex: "Sat Dec 30 1899 13:53:32 GMT-0306 (Brasilia Standard Time)")
 * @returns {string} - Horário formatado no formato HH:MM
 */
function convertDateStringToHHMM(dateString) {
  // Cria um objeto Date a partir da string fornecida
  const date = new Date(dateString);

  // Verifica se a data é válida
  if (isNaN(date.getTime())) {
    throw new Error("Formato de data inválido.");
  }

  // Extrai horas e minutos do objeto Date
  const hours = date.getUTCHours().toString().padStart(2, '0');
  const minutes = date.getUTCMinutes().toString().padStart(2, '0');

  // Retorna o horário formatado
  return `${hours}:${minutes}`;
}

/**
 * Função para criar um evento no Google Calendar
 * @param {Object} calendar - O objeto do Google Calendar
 * @param {string} projeto - Nome do projeto
 * @param {string} acao - Descrição da ação
 * @param {string} local - Local do evento
 * @param {Date} dataInicio - Data e hora de início
 * @param {Date} dataFim - Data e hora de término
 */
function criaEventoNaAgenda(calendar, projeto, acao, local, dataInicio, dataFim) {
  try {

    calendar.createEvent(`[${projeto}] ${acao}`,
      dataInicio,
      dataFim,
      {
        description: `No dia ${convertDateToDDMMYYYY(dataInicio)} das ${formatarHora(dataInicio)} às ${formatarHora(dataFim)} horas, acontece a ação: ${acao}, do projeto: ${projeto}`,
        location: local
      }
    );

    Logger.log(`Evento criado no Google Calendar: Projeto - [${projeto}] | Ação - [${acao}]`);
  } catch (e) {
    Logger.log(`Erro ao criar evento: ${e}`);
  }
}

/**
 * Função principal que coordena a execução das outras funções
 * e cria eventos no Google Calendar a partir das respostas na planilha
 */
function main() {

  var planilha = SpreadsheetApp.getActiveSpreadsheet();
  var aba = planilha.getSheetByName(SHEET_NAME);

  // Verifica se a aba foi carregada corretamente
  if (!aba) {
    Logger.log("Aba não encontrada.");
    return;
  }

  // Obtém todos os dados na aba a partir da linha 8 e coluna B (2), até a última linha e coluna
  var rangeDados = aba.getRange(8, 2, aba.getLastRow() - 7, aba.getLastColumn() - 1);
  var matrizDados = rangeDados.getValues();

  // Verifica se a matriz de dados foi carregada corretamente
  if (!matrizDados || matrizDados.length === 0) {
    Logger.log("Matriz de dados não está definida ou vazia.");
    return;
  }

  // Cabeçalhos específicos do formulário
  var cabecalhoProjeto = "Projeto";
  var cabecalhoAcao = "Ação";
  var cabecalhoLocal = "Local";
  var cabecalhoDia = "Dia";
  var cabecalhoHorarioInicio = "Horário de Início";
  var cabecalhoHorarioTermino = "Horário de Término";

  // Obter índices das colunas relevantes (começando da coluna B)
  var cabecalhos = aba.getRange(7, 2, 1, aba.getLastColumn() - 1).getValues()[0];
  var indiceProjeto = cabecalhos.indexOf(cabecalhoProjeto);
  var indiceAcao = cabecalhos.indexOf(cabecalhoAcao);
  var indiceLocal = cabecalhos.indexOf(cabecalhoLocal);
  var indiceDia = cabecalhos.indexOf(cabecalhoDia);
  var indiceHorarioInicio = cabecalhos.indexOf(cabecalhoHorarioInicio);
  var indiceHorarioTermino = cabecalhos.indexOf(cabecalhoHorarioTermino);
  var indiceStatus = cabecalhos.indexOf(STATUS_COLUMN_NAME);

  // Verifica se todos os cabeçalhos necessários foram encontrados
  if ([indiceProjeto, indiceAcao, indiceLocal, indiceDia, indiceHorarioInicio, indiceHorarioTermino, indiceStatus].includes(-1)) {
    Logger.log("Um ou mais cabeçalhos não foram encontrados.");
    return;
  }

  // Ajusta índices para corresponder ao início da matriz (coluna B)
  indiceProjeto += 0;
  indiceAcao += 0;
  indiceLocal += 0;
  indiceDia += 0;
  indiceHorarioInicio += 0;
  indiceHorarioTermino += 0;
  indiceStatus += 0;

  var calendar = CalendarApp.getCalendarById(CALENDAR_ID);

  // Percorre cada linha de dados e cria um evento no Google Calendar se ainda não tiver sido criado
  for (var i = 0; i < matrizDados.length; i++) {
    var linha = matrizDados[i];

    var projeto = linha[indiceProjeto];
    var acao = linha[indiceAcao];
    var local = linha[indiceLocal];
    
    var day = linha[indiceDia];
    var dia = convertDateToDDMMYYYY(day);

    var startHour = linha[indiceHorarioInicio];
    var endHour = linha[indiceHorarioTermino];
    
    var horarioInicio = convertDateStringToHHMM(startHour);
    var horarioTermino = convertDateStringToHHMM(endHour);

    var status = linha[indiceStatus];

    // Verifica se todas as variáveis necessárias estão definidas e se o evento ainda não foi criado
    if (projeto && acao && local && dia && horarioInicio && horarioTermino && status !== "Evento Criado") {

      // Converte as datas e horários para objetos Date
      var dataInicio = converteParaData(dia, horarioInicio);
      var dataFim = converteParaData(dia, horarioTermino);

      // Atualiza o status para "Evento Criado"
      aba.getRange(i + 8, indiceStatus + 2).setValue("Evento Criado na Agenda");
      } else if (!projeto && !acao && !local && !dia && !horarioInicio && !horarioTermino) {
        Logger.log("Linha vazia encontrada. Parando a execução.");
        break;
    }

    // Cria o evento se o status for "Evento Pendente"
      if (status === "Evento Pendente") {

        criaEventoNaAgenda(calendar, projeto, acao, local, dataInicio, dataFim);

        // Atualiza o status para "Evento Criado"
        aba.getRange(i + 8, indiceStatus + 2).setValue("Evento Criado");
      } else if (!projeto && !acao && !local && !dia && !horarioInicio && !horarioTermino) {
      Logger.log("Linha vazia encontrada. Parando a execução.");
      break;
    }
  }
}
