// Função para registrar o último acesso à planilha
function registrarUltimoAcesso() {
  // Obtém a planilha ativa
  var planilha = SpreadsheetApp.getActiveSpreadsheet().getActiveSheet();
  // Obtém a data e hora atual formatada
  var dataHoraAtual = Utilities.formatDate(new Date(), 'GMT-3', 'dd/MM/yyyy HH:mm:ss');
  // Obtém a célula onde será registrado o último acesso (por exemplo, A1)
  var celulaUltimoAcesso = planilha.getRange("I2");
  // Define o valor da célula como "Último acesso em:" seguido da data e hora atual
  celulaUltimoAcesso.setValue(dataHoraAtual);
}