/**
 * Este script automatiza a gestão de inscrições em eventos:
 * - Gerencia status de publicações;
 * - Cria pastas organizadas por evento e subpastas de evidências;
 * - Gera automaticamente um formulário Google da Lista de Inscrição associada ao evento;
 * - Registra URLs de evidências na planilha.
 *
 * Autor: Rafael Passos Domingues
 * Última atualização: 2024-11-18
 */

/**
 * Configurações iniciais:
 * URL da planilha Google e ID da pasta Drive onde os eventos serão organizados.
 */
var SPREADSHEET_URL = 'https://docs.google.com/spreadsheets/d/42/edit?gid=42#gid=42';
var PASTA_EVENTOS_ID = "42";

// Referências globais para as abas da planilha.
var planilha = SpreadsheetApp.openByUrl(SPREADSHEET_URL);
var abaArtes = planilha.getSheetByName("ARTES");
var abaParticipantes = planilha.getSheetByName("PARTICIPANTES");

/**
 * Função principal que verifica o status "Aprovado" na aba "ARTES" e executa ações relacionadas.
 */
function geraListaInscricao() {
  try {
    const pastaEventos = DriveApp.getFolderById(PASTA_EVENTOS_ID);
    const ultimaLinha = abaArtes.getLastRow();

    // Buscar índices das colunas dinamicamente na linha 7
    const cabecalhos = abaArtes.getRange(7, 1, 1, abaArtes.getLastColumn()).getValues()[0];
    const indiceEvento = cabecalhos.indexOf("Evento") + 1;
    const indiceLegenda = cabecalhos.indexOf("Legenda") + 1;
    const indiceStatusPublicacao = cabecalhos.indexOf("Status da Publicação") + 1;

    if (indiceEvento === 0 || indiceLegenda === 0 || indiceStatusPublicacao === 0) {
      throw new Error("Não foi possível encontrar todos os cabeçalhos necessários na linha 7.");
    }

    // Obter dados das colunas relevantes
    const dadosArtes = abaArtes.getRange(8, 1, ultimaLinha - 7, abaArtes.getLastColumn()).getValues();

    dadosArtes.forEach((linha, index) => {
      const evento = linha[indiceEvento - 1]; // Ajusta para índice base 0
      const legenda = linha[indiceLegenda - 1];
      const statusPublicacao = linha[indiceStatusPublicacao - 1];

      const linhaAtual = index + 8; // Ajusta para a linha real na planilha

      if (statusPublicacao === "Aprovado" && evento && legenda) {
        try {
          Logger.log(`Processando evento: ${evento}, linha: ${linhaAtual}`);
          const pastaEvento = criarPastaEvento(pastaEventos, evento);

          criaFormulario(pastaEvento, legenda, evento);

          // Atualizar status para "Publicado"
          abaArtes.getRange(linhaAtual, indiceStatusPublicacao).setValue("Publicado");

          // Registrar URL da pasta como evidência
          registraEvidencia(linhaAtual, pastaEvento.getUrl());
          Logger.log(`URL da pasta registrada: ${pastaEvento.getUrl()}`);
        } catch (erroInterno) {
          Logger.log(`Erro ao processar a linha ${linhaAtual}: ${erroInterno.message}`);
        }
      }
    });
  } catch (error) {
    Logger.log("Erro ao executar a função: " + error.message);
  }
}

/**
 * Cria a estrutura de pastas para o evento.
 *
 * @param {DriveApp.Folder} pastaRaiz - Pasta raiz onde os eventos serão armazenados.
 * @param {string} nomeEvento - Nome do evento.
 * @returns {DriveApp.Folder} Pasta criada para o evento.
 */
function criarPastaEvento(pastaRaiz, nomeEvento) {
  // Formato do timestamp ajustado para 'yyyymmdd'
  const dataAtual = new Date();
  const timestamp = dataAtual.getFullYear() + 
                    String(dataAtual.getMonth() + 1).padStart(2, '0') + 
                    String(dataAtual.getDate()).padStart(2, '0');
  
  const nomePastaEvento = `${timestamp}_${nomeEvento}`;
  const pastaEvento = pastaRaiz.createFolder(nomePastaEvento);

  const nomeSubpastaEvidencia = `[Evidência] ${nomeEvento}`;
  pastaEvento.createFolder(nomeSubpastaEvidencia);

  return pastaEvento;
}

/**
 * Cria um formulário Google associado ao evento.
 *
 * @param {DriveApp.Folder} pastaEvento - Pasta do evento.
 * @param {string} legenda - Descrição do formulário.
 * @param {string} nomeEvento - Nome do evento.
 */
function criaFormulario(pastaEvento, legenda, nomeEvento) {
  // Criando o formulário com o nome ajustado diretamente na pasta do evento
  const formulario = FormApp.create(`[Lista de Inscrição] ${nomeEvento}`)
    .setDescription(legenda);

  // Pergunta de múltipla escolha para aceitar os termos
  formulario.addMultipleChoiceItem()
    .setTitle("Os dados pessoais envolvidos na inscrição e afins, relacionados à execução de quaisquer atividades ligadas à Agência de Inovação e Empreendedorismo - I9/Unifal e Incubadora de Empresas de Base Tecnológica - NidusTec serão tratados única e exclusivamente para cumprir com a finalidade a que se destinam e em respeito a toda a legislação aplicável sobre segurança da informação, privacidade e proteção de dados, inclusive, mas não se limitando à Lei Geral de Proteção de Dados (Lei Federal n. 13.709/2018).")
    .setChoiceValues(["Eu aceito os termos acima"]);

  // Perguntas do formulário
  formulario.addTextItem().setTitle("Nome Completo");
  formulario.addTextItem().setTitle("CPF");
  formulario.addTextItem().setTitle("E-mail");
  formulario.addTextItem().setTitle("WhatsApp");
  formulario.addTextItem().setTitle("Curso/Instituição");

  // Pergunta de múltipla escolha para saber como ficou sabendo do evento
  formulario.addMultipleChoiceItem()
    .setTitle("Como ficou sabendo do evento?")
    .setChoiceValues(["Redes Sociais", "E-mail", "Indicação"]);

  // Adiciona o formulário à pasta do evento diretamente
  const arquivoFormulario = DriveApp.getFileById(formulario.getId());
  pastaEvento.addFile(arquivoFormulario);
  DriveApp.getRootFolder().removeFile(arquivoFormulario); // Remove da pasta raiz
}

/**
 * Registra a URL da pasta criada na primeira linha vazia da coluna "Evidência" (coluna H).
 *
 * @param {number} linha - Índice da linha para registro.
 * @param {string} urlPasta - URL da pasta criada.
 */
function registraEvidencia(linha, urlPasta) {
  const colunaEvidencia = 8; // Coluna "Evidência" (coluna H)
  
  // Obter a última linha preenchida na aba PARTICIPANTES
  const ultimaLinha = abaParticipantes.getLastRow();
  
  // Verificar se a célula da linha indicada já possui um valor
  let linhaDisponivel = linha;

  // Percorrer a coluna H para encontrar a primeira linha vazia
  for (let i = linha; i <= ultimaLinha; i++) {
    const valorAtual = abaParticipantes.getRange(i, colunaEvidencia).getValue();
    if (!valorAtual) {
      linhaDisponivel = i;
      break; // Encontrou a primeira linha vazia, sai do loop
    }
  }

  // Registrar a URL na primeira linha vazia encontrada
  abaParticipantes.getRange(linhaDisponivel, colunaEvidencia).setValue(urlPasta);
}
