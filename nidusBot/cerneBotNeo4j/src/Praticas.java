package src;

import java.util.Arrays;
import java.util.List;

public enum Praticas {
    SENSIBILIZACAO("1.1.1 Sensibilização", Arrays.asList("Divulgação", "Visita")),
    PROSPECCAO("1.1.2 Prospecção", Arrays.asList("Evento", "Convite", "Lista de Presença")),
    QUALIFICACAO_POTENCIAIS("1.1.3 Qualificação de Potenciais Empreendedores", Arrays.asList("Capacitação", "Curso")),
    RECEPCAO_PROPOSTAS("1.2.1 Recepção de Propostas", Arrays.asList("Edital", "Pitch")),
    AVALIACAO("1.2.2 Avaliação", Arrays.asList("Banca", "Edital")),
    CONTRATACAO("1.2.3 Contratação", Arrays.asList("Contrato", "Incubação", "Pré")),
    PLANEJAMENTO("1.3.1 Planejamento", Arrays.asList("Planejamento", "Canvas")),
    AGREGACAO_VALOR("1.3.2 Agregação de Valor", Arrays.asList("Conexão", "Networking")),
    MONITORAMENTO("1.3.3 Monitoramento", Arrays.asList("Monitoramento", "Acompanhento", "Incubado")),
    GRADUACAO("1.4.1 Graduação", Arrays.asList("Graduado")),
    RELACIONAMENTO_GRADUADAS("1.4.2 Relacionamento com Graduadas", Arrays.asList("Relacionamento")),
    ESTRUTURA_ORGANIZACIONAL("1.5.1 Estrutura Organizacional", Arrays.asList("Infraestrutura", "Regimento")),
    OPERACAO_INCUBADORA("1.5.2 Operação da Incubadora", Arrays.asList("Financeiro", "Sustentabilidade")),
    COMUNICACAO_MARKETING("1.5.3 Comunicação e Marketing", Arrays.asList("Divulgação", "Arte", "Matéria")),
    PLANEJAMENTO_ESTRATEGICO("2.1.1 Planejamento Estratégico", Arrays.asList("Estratégia", "Plano")),
    ADMINISTRACAO_ESTRATEGICA("2.1.2 Administração Estratégica", Arrays.asList("Administração", "Status")),
    AMBIENTES_IDEACAO("2.2.1 Ambientes de Ideação", Arrays.asList("Startup Run", "Inovação")),
    SERVICOS_ORGANIZACOES("2.2.2 Serviços a Organizações", Arrays.asList("Serviços", "Maker", "Fabricação de Peça", "Reserva de Sala")),
    AVALIACAO_QUALIDADE("2.3.1 Avaliação da Qualidade", Arrays.asList("Premiação", "Reconhecimento")),
    AVALIACAO_IMPACTOS("2.3.2 Avaliação dos Impactos", Arrays.asList("Relatório", "Gestão", "Impacto")),
    INTERACAO_ENTORNO("3.1.1 Interação com o Entorno", Arrays.asList("Interação", "Conexões", "Externo")),
    PARTICIPACAO_POLITICAS("3.1.2 Participação na Proposição de Políticas Públicas", Arrays.asList("Política", "Público", "Participação")),
    REDE_MENTORES("3.2.1 Rede de Mentores", Arrays.asList("Conselheiro", "Rede", "Mentoria")),
    GESTAO_OFERTAS_DEMANDAS("3.2.2 Gestão de Ofertas e Demandas", Arrays.asList("Oferta", "Demanda", "Resolução de Problema")),
    INCUBACAO_VIRTUAL("3.2.3 Incubação Virtual", Arrays.asList("Virtual", "Incubação")),
    GESTAO_AMBIENTAL("3.3.1 Gestão Ambiental", Arrays.asList("Meio Ambiente", "Sustentabilidade")),
    RESPONSABILIDADE_SOCIAL("3.3.2 Responsabilidade Social", Arrays.asList("Educação", "Sociedade", "Retorno para Sociedade")),
    INTERNACIONALIZACAO_INCUBADORA("4.1.1 Internacionalização da Incubadora", Arrays.asList("Exterior", "Internacional")),
    INTERNACIONALIZACAO_EMPREENDIMENTOS("4.1.2 Internacionalização dos Empreendimentos", Arrays.asList("Exterior", "Internacional"));

    private final String practice;
    private final List<String> tags;

    // Construtor para associar a prática com as tags
    Praticas(String practice, List<String> tags) {
        this.practice = practice;
        this.tags = tags;
    }

    public String getKeyPractice() {
        return practice;
    }

    public List<String> getTags() {
        return tags;
    }

    public static List<String> getAllTags() {
        // Utiliza o stream para coletar todas as tags de todas as práticas e as adiciona em uma lista
        return Arrays.stream(Praticas.values())
                     .flatMap(pratica -> pratica.getTags().stream())  // Achata as listas de tags
                     .distinct()  // Elimina duplicatas
                     .toList();  // Converte para uma lista
    }

    // Método que verifica se uma prática possui uma ou mais tags associadas
    public static List<Praticas> findByTags(List<String> selectedTags) {
        return Arrays.stream(Praticas.values())
                     .filter(pratica -> pratica.getTags().stream().anyMatch(selectedTags::contains))
                     .toList();
    }
}
