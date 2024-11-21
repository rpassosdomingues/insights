/**
 * Cria um link de videoconferência do Google Meet e associa ao evento.
 *
 * @param {DriveApp.Folder} pastaEvento - Pasta do evento.
 * @param {string} nomeEvento - Nome do evento.
 * @returns {string} - URL do link do Google Meet.
 */
function criaMeet(pastaEvento, nomeEvento) {
  // Criação do evento no Google Calendar (necessário para gerar o link do Meet)
  const eventoCalendar = CalendarApp.createEvent(nomeEvento, new Date(), new Date());
  
  // Recupera o link do Google Meet do evento criado
  const linkMeet = eventoCalendar.getHangoutLink();
  
  // Adiciona o link à pasta do evento
  const arquivoMeet = DriveApp.createFile(`LinkMeet_${nomeEvento}.txt`, linkMeet, MimeType.PLAIN_TEXT);
  pastaEvento.addFile(arquivoMeet);
  DriveApp.getRootFolder().removeFile(arquivoMeet); // Remove da pasta raiz

  return linkMeet;
}

/**
 * Envia um e-mail para os participantes do evento com o link do Google Meet.
 *
 * @param {string} nomeEvento - Nome do evento.
 * @param {string} linkMeet - URL do link do Google Meet.
 * @param {string} assunto - Assunto do e-mail.
 * @param {string} mensagem - Corpo do e-mail.
 */
function enviaEmails(nomeEvento, linkMeet, assunto, mensagem) {
  // Obter todas as respostas do formulário (aba PARTICIPANTES)
  const ultimaLinha = abaParticipantes.getLastRow();
  const participantes = abaParticipantes.getRange(2, 1, ultimaLinha - 1, abaParticipantes.getLastColumn()).getValues();

  participantes.forEach((linha, index) => {
    const nome = linha[0]; // Supondo que o nome está na primeira coluna
    const email = linha[2]; // Supondo que o e-mail está na terceira coluna (ajuste conforme sua planilha)

    if (email) {
      const corpoEmail = `
        Olá ${nome},\n\n
        Você está inscrito no evento: ${nomeEvento}.\n
        Acesse o evento através do link do Google Meet: ${linkMeet}\n\n
        Mensagem do organizador: ${mensagem}\n\n
        Aguardamos você no evento!
      `;
      
      // Envia o e-mail para o participante
      MailApp.sendEmail({
        to: email,
        subject: assunto,
        body: corpoEmail
      });
    }
  });
}
