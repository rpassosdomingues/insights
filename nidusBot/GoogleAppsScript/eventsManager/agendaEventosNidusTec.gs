/**
 * Este script automatiza a criação e remoção de eventos no Google Calendar a partir de dados na planilha,
 * com cache de memória para acompanhar o status de criação dos eventos.
 *
 * Autor: Rafael Passos Domingues
 * Última atualização: 2024-11-14
 */

var SHEET_NAME = 'EVENTOS'; 
var SPREADSHEET_URL = 'https://docs.google.com/spreadsheets/d/42/edit?gid=42#gid=42'; 
var CALENDAR_ID = 'c_42@group.calendar.google.com'; 
var STATUS_COLUMN_NAME = 'Google Agenda';

/**
 * Função principal executada quando a planilha é editada.
 * Ela percorre a planilha, verificando se os eventos devem ser lançados ou removidos do Google Agenda.
 */
function main() {
  var planilha = SpreadsheetApp.openByUrl(SPREADSHEET_URL); 
  var aba = planilha.getSheetByName(SHEET_NAME);

  if (!aba) {
    Logger.log("Aba não encontrada.");
    return;
  }

  var cabecalhoProjeto = "Projeto";
  var cabecalhoEvento = "Evento";
  var cabecalhoLocal = "Local";
  var cabecalhoData = "Data";
  var cabecalhoHorarioInicio = "Horário de Início";
  var cabecalhoHorarioTermino = "Horário de Término";
  var cabecalhoStatus = "Google Agenda";

  var cabecalhos = aba.getRange(7, 2, 1, aba.getLastColumn() - 1).getValues()[0];
  var indiceProjeto = cabecalhos.indexOf(cabecalhoProjeto);
  var indiceEvento = cabecalhos.indexOf(cabecalhoEvento);
  var indiceLocal = cabecalhos.indexOf(cabecalhoLocal);
  var indiceData = cabecalhos.indexOf(cabecalhoData);
  var indiceHorarioInicio = cabecalhos.indexOf(cabecalhoHorarioInicio);
  var indiceHorarioTermino = cabecalhos.indexOf(cabecalhoHorarioTermino);
  var indiceStatus = cabecalhos.indexOf(cabecalhoStatus);

  if ([indiceProjeto, indiceEvento, indiceLocal, indiceData, indiceHorarioInicio, indiceHorarioTermino, indiceStatus].includes(-1)) {
    Logger.log("Um ou mais cabeçalhos não foram encontrados.");
    return;
  }

  var rangeDados = aba.getRange(8, 2, aba.getLastRow() - 7, aba.getLastColumn() - 1);
  var matrizDados = rangeDados.getValues();
  var calendar = CalendarApp.getCalendarById(CALENDAR_ID);

  if (!calendar) {
    Logger.log("Erro: Calendário não encontrado. Verifique o ID do calendário.");
    return;
  }

  // Chama a função que gerencia a criação e remoção de eventos
  atualizaAgenda(matrizDados, aba, indiceProjeto, indiceEvento, indiceLocal, indiceData, indiceHorarioInicio, indiceHorarioTermino, indiceStatus, calendar);
}

/**
 * Função responsável por gerenciar a criação e remoção de eventos no Google Agenda.
 * Ela verifica se o evento deve ser criado ou removido de acordo com o status.
 */
function atualizaAgenda(matrizDados, aba, indiceProjeto, indiceEvento, indiceLocal, indiceData, indiceHorarioInicio, indiceHorarioTermino, indiceStatus, calendar) {
  for (var i = 0; i < matrizDados.length; i++) {
    var linha = matrizDados[i];
    var projeto = linha[indiceProjeto];
    var evento = linha[indiceEvento];
    var local = linha[indiceLocal];
    var data = linha[indiceData];
    var horarioInicio = linha[indiceHorarioInicio];
    var horarioTermino = linha[indiceHorarioTermino];
    var status = linha[indiceStatus];

    // Verificar se o evento já existe no Google Calendar antes de criar
    var inicio = new Date(data + " " + horarioInicio);
    var termino = new Date(data + " " + horarioTermino);
    var eventosExistentes = calendar.getEvents(inicio, termino, {location: local, search: evento});

    if (status === "Evento Confirmado" && eventosExistentes.length === 0) {
      // Se não há eventos com o mesmo intervalo e local, cria o evento
      var idEventoCriado = criaEventoAgenda(calendar, projeto, evento, local, data, horarioInicio, horarioTermino);
      
      if (idEventoCriado.startsWith("Erro")) {
        Logger.log("Erro ao criar evento: " + idEventoCriado);
      } else {
        // Após a criação do evento, atualiza o status para "Lançado na Agenda"
        aba.getRange(i + 8, indiceStatus + 2).setValue("Lançado na Agenda");
      }
    } else if (status === "Evento Cancelado" && eventosExistentes.length > 0) {
      // Se o evento foi cancelado e existe, remove o evento
      removeEventoAgenda(calendar, projeto, evento, local, data, horarioInicio, horarioTermino, planilha, linha);
    }
  }
}

/**
 * Função para criar um evento no Google Calendar e atualizar o status na planilha.
 * @param {CalendarApp} calendar - O objeto do calendário do Google.
 * @param {String} projeto - Nome do projeto relacionado ao evento.
 * @param {String} evento - Descrição da ação do evento.
 * @param {String} local - Local onde o evento ocorrerá.
 * @param {Date} data - Data do evento.
 * @param {Date} horarioInicio - Hora de início do evento.
 * @param {Date} horarioTermino - Hora de término do evento.
 * @returns {String} - Retorna o ID do Evento Confirmado ou uma mensagem de erro.
 */
function criaEventoAgenda(calendar, projeto, evento, local, data, horarioInicio, horarioTermino) {
  // Encapsula data e horários para compatibilidade com Google Agenda
  var dataInicio = converteParaData(data, horarioInicio);
  var dataFim = converteParaData(data, horarioTermino);

  // Verificar se as datas foram corretamente convertidas para objetos Date
  if (!dataInicio || !dataFim) {
    Logger.log("Erro: Datas ou horários inválidos. Verifique os dados de entrada.");
    return "Erro: Datas ou horários inválidos.";
  }

  // Cria o evento no Google Calendar
  try {
    var evento = calendar.createEvent(`[${evento}] ${projeto}`,
      dataInicio,
      dataFim,
      {
        description: `O evento: ${evento} acontece no(a) ${local}, dia ${dataInicio}.`,
        location: local
      }
    );

    Logger.log(`Evento criado no Google Agenda: A ação: ${evento}, do projeto: ${projeto} acontece no(a) ${local}, dia ${dataInicio}`);

    return evento.getId();  // Retorna o ID do evento para referência futura
  } catch (e) {
    Logger.log("Erro ao criar o evento: " + e.message);
    return "Erro ao criar o evento: " + e.message;
  }
}

/**
 * Converte uma data e horário em um objeto Date para o Google Calendar.
 * @param {Date} data - Objeto Date com a data completa no formato Google Spreadsheet.
 * @param {Date} horario - Objeto Date com o horário.
 * @returns {Object} - Objeto contendo a data e horários encapsulados.
 */
function converteParaData(data, horario) {
  // Verifica se 'data' e 'horario' são objetos Date válidos
  if (!(data instanceof Date) || isNaN(data.getTime()) || !(horario instanceof Date) || isNaN(horario.getTime())) {
    Logger.log("Erro: Data ou horário inválidos.");
    return null;
  }

  // Extrai a data (dia, mês e ano) do objeto 'data'
  var dia = data.getDate();
  var mes = data.getMonth(); // Meses começam do zero (0-11)
  var ano = data.getFullYear();

  // Extrai hora e minuto do objeto 'horario'
  var hora = horario.getHours();
  var minuto = horario.getMinutes();

  // Cria um novo objeto Date com a data completa, incluindo hora e minuto
  var dataComHorario = new Date(ano, mes, dia, hora, minuto, 0, 0); // Aqui usamos 0 para os segundos e milissegundos

  // Verifique se a conversão foi bem-sucedida
  if (isNaN(dataComHorario.getTime())) {
    Logger.log("Erro: A conversão da data e horário falhou.");
    return null;
  }

  return dataComHorario;
}

/**
 * Gera um ID único para o evento, com base nos parâmetros fornecidos.
 * @param {Date} dataInicio - Data e hora de início do evento.
 * @returns {String} - ID único do evento no formato yyyymmddhhmm.
 */
function geraIdEvento(dataInicio) {
  // Formata a data no formato yyyymmddhhmm
  var ano = dataInicio.getFullYear();
  var mes = String(dataInicio.getMonth() + 1).padStart(2, '0');  // Mês começa do 0, então somamos 1
  var dia = String(dataInicio.getDate()).padStart(2, '0');
  var hora = String(dataInicio.getHours()).padStart(2, '0');
  var minuto = String(dataInicio.getMinutes()).padStart(2, '0');
  
  // Criar o ID com base no formato desejado
  var id = ano + mes + dia + hora + minuto;

  return id;
}
