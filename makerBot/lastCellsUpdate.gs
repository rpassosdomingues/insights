// Função que será executada toda vez que houver uma edição na planilha
function registrarUltimaAtualizacao(e) {
  // Verifica se o objeto de evento está definido e se contém as informações necessárias
  if (e && e.range && e.source) {
    // Obtém a planilha ativa
    var planilha = e.source.getActiveSheet();
    // Obtém o intervalo que foi editado
    var intervalo = e.range;
    // Obtém o número da coluna que foi editada
    var colunaEditada = intervalo.getColumn();
    
    // Verifica se a edição foi feita em uma das colunas especificadas
    var colunasPermitidas = e.parameter.colunasPermitidas; // Obtém as colunas permitidas do objeto de evento
    if (colunasPermitidas.includes(colunaEditada)) {
      // Obtém o número da linha editada
      var linhaEditada = intervalo.getRow();
      // Obtém a célula onde será registrada a última atualização (na coluna H)
      var celulaTimestamp = planilha.getRange(linhaEditada, 8); // Coluna H
      // Obtém a data e hora atual formatada
      var dataHoraAtual = Utilities.formatDate(new Date(), 'GMT-3', 'dd/MM/yyyy HH:mm:ss');
      // Define o valor da célula como a data e hora atual
      celulaTimestamp.setValue(dataHoraAtual);
    }
  } else {
    console.log("Objeto de evento não está definido ou não contém informações necessárias.");
  }
}

// Atribui a função ao evento de edição
function onEdit(e) {
  // Define as colunas permitidas para edição (colunas de B até F)
  var colunasPermitidas = [2, 3, 4, 5, 6];
  // Verifica se a edição foi feita dentro das colunas permitidas
  if (e && e.range && e.range.getColumn() >= 2 && e.range.getColumn() <= 6) {
    // Chama a função para registrar a última atualização, passando o objeto de evento e as colunas permitidas como parâmetros
    registrarUltimaAtualizacao({...e, parameter: {colunasPermitidas: colunasPermitidas}});
  }
}
