/**
 * Este script automatiza a gestão de inscrições em eventos:
 * - Gerencia status de publicações;
 * - Cria pastas organizadas por evento e subpastas de evidências;
 * - Gera automaticamente um formulário Google associado ao evento;
 * - Registra URLs de evidências na planilha.
 *
 * Autor: Rafael Passos Domingues
 * Última atualização: 2024-11-15
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

    // Obter dados das colunas B, C e E (Evento, Legenda e Status da Publicação)
    const dadosArtes = abaArtes.getRange(7, 2, abaArtes.getLastRow() - 6, 3).getValues();

    // Iterar sobre as linhas para verificar o status "Aprovado"
    dadosArtes.forEach((linha, index) => {
      const [evento, legenda, statusArte , statusPublicacao, enderecoDrive] = linha;

      if (statusPublicacao === "Aprovado" && legenda && evento) {
        const linhaAtual = index + 7; // Ajusta para a linha real na planilha

        // Criar estrutura e formulário
        const pastaEvento = criarPastaEvento(pastaEventos, evento);
        criaFormulario(pastaEvento, legenda, evento);

        // Atualizar status para "Publicado"
        abaArtes.getRange(linhaAtual, 4).setValue("Publicado");

        // Registrar evidência
        registrarEvidencia(linhaAtual, pastaEvento.getUrl());
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
  // Criando o formulário com o nome ajustado
  const formulario = FormApp.create(`[Lista de Inscrição] ${nomeEvento}`).setDescription(legenda);

  // Pergunta de múltipla escolha para aceitar os termos
  formulario.addMultipleChoiceItem()
    .setTitle("Os dados pessoais envolvidos na inscrição e afins, relacionados à execução de quaisquer atividades ligadas a Agência de Inovação e Empreendedorismo - I9/Unifal e  Incubadora de Empresas de Base Tecnológica - NidusTec serão tratados única e exclusivamente para cumprir com a finalidade a que se destinam e em respeito a toda a legislação aplicável sobre segurança da informação, privacidade e proteção de dados, inclusive, mas não se limitando à Lei Geral de Proteção de Dados (Lei Federal n. 13.709/2018).")
    .setChoices([
      FormApp.createChoice("Eu aceito os termos acima")
    ]);

  // Perguntas do formulário
  formulario.addTextItem().setTitle("Nome Completo");
  formulario.addTextItem().setTitle("CPF");
  formulario.addTextItem().setTitle("e-mail");
  formulario.addTextItem().setTitle("WhatsApp");
  formulario.addTextItem().setTitle("Curso/Instituição");

  // Pergunta de múltipla escolha para saber como ficou sabendo do evento
  formulario.addMultipleChoiceItem()
    .setTitle("Como ficou sabendo do evento?")
    .setChoices([
      FormApp.createChoice("Redes Sociais"),
      FormApp.createChoice("e-mail"),
      FormApp.createChoice("Indicação")
    ]);

  // Adiciona o formulário à pasta do evento
  const arquivoFormulario = DriveApp.getFileById(formulario.getId());
  pastaEvento.addFile(arquivoFormulario);
  DriveApp.getRootFolder().removeFile(arquivoFormulario); // Remove o formulário da pasta raiz.
}

/**
 * Registra a URL da pasta criada na aba "ARTES".
 *
 * @param {number} linha - Índice da linha para registro.
 * @param {string} urlPasta - URL da pasta criada.
 */
function registrarEvidencia(linha, urlPasta) {
  const colunaEvidencia = 8; // Coluna "Evidência" (coluna H)
  abaParticipantes.getRange(linha, colunaEvidencia).setValue(urlPasta);
}
