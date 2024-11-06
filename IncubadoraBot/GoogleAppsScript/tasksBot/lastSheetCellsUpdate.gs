// Este Script automatiza o processo de registro de último acesso, última edição de células
// da planilha e e-mail do último editor
// Autor: Rafael Passos Domingues
// Última Atualização: 2024 - 07 - 23

// Função para registrar o último acesso à planilha
function registraUltimoAcesso() {
  // Obtém a planilha ativa
  var planilha = SpreadsheetApp.getActiveSpreadsheet().getActiveSheet();
  // Obtém a data e hora atual formatada
  var dataHoraAtual = Utilities.formatDate(new Date(), 'GMT-3', 'dd/MM/yyyy HH:mm:ss');
  // Obtém a célula onde será registrado o último acesso (por exemplo, J6)
  var celulaUltimoAcesso = planilha.getRange("J6");
  // Define o valor da célula como "Último acesso em:" seguido da data e hora atual
  celulaUltimoAcesso.setValue(dataHoraAtual);
}

// Função para registrar a última atualização
function registraUltimaAtualizacao(e) {
  // Verifica se o objeto de evento está definido e se contém as informações necessárias
  if (e && e.range && e.source) {
    // Obtém a planilha ativa
    var planilha = e.source.getActiveSheet();
    // Obtém o intervalo que foi editado
    var intervalo = e.range;
    // Obtém o número da linha editada
    var linhaEditada = intervalo.getRow();
    // Obtém a célula onde será registrada a última atualização (na coluna L)
    var celulaTimestamp = planilha.getRange(linhaEditada, 12); // Coluna L
    // Obtém a data e hora atual formatada
    var dataHoraAtual = Utilities.formatDate(new Date(), 'GMT-3', 'dd/MM/yyyy HH:mm:ss');
    // Define o valor da célula como a data e hora atual
    celulaTimestamp.setValue(dataHoraAtual);
  } else {
    console.log("Objeto de evento não está definido ou não contém informações necessárias.");
  }
}

// Função para registrar o e-mail do último editor
function registraEmail(e) {
  // Verifica se o objeto de evento está definido e se contém as informações necessárias
  if (e && e.range && e.source) {
    // Obtém a planilha ativa
    var planilha = e.source.getActiveSheet();
    // Obtém o intervalo que foi editado
    var intervalo = e.range;
    // Obtém o número da linha editada
    var linhaEditada = intervalo.getRow();
    // Obtém a célula onde será registrado o e-mail do editor (na coluna M)
    var celulaEmail = planilha.getRange(linhaEditada, 13); // Coluna M
    // Obtém o e-mail do editor
    var emailEditor = Session.getActiveUser().getEmail();
    // Define o valor da célula como o e-mail do editor
    celulaEmail.setValue(emailEditor);
  } else {
    console.log("Objeto de evento não está definido ou não contém informações necessárias.");
  }
}

// Função principal para atribuir as funções de atualização e registro de e-mail
function main(e) {
  // Define as colunas permitidas para edição (colunas B, C, D, E , F, G, H, I, J e K)
  var colunasPermitidas = [2, 3, 4, 5, 6, 7, 8, 9, 10, 11];
  // Verifica se a edição foi feita dentro das colunas permitidas
  if (e && e.range && e.range.getColumn() >= 2 && e.range.getColumn() <= 11) {
    // Chama a função para registrar a última atualização, passando o objeto de evento
    registraUltimaAtualizacao(e);
    // Chama a função para registrar o e-mail do editor, passando o objeto de evento
    registraEmail(e);
  }
}

// Função de gatilho para registrar o último acesso
function onOpen() {
  registraUltimoAcesso();
}

// Função de gatilho para registrar a última edição
function onEdit(e) {
  main(e);
}
