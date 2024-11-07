package src;

import java.util.Arrays;
import java.util.List;

public enum Praticas {
    SENSIBILIZACAO("1.1.1 Sensibilização", Arrays.asList("Tag1", "Tag2", "Tag3")),
    PROSPECCAO("1.1.2 Prospecção", Arrays.asList("Tag1", "Tag4", "Tag5")),
    QUALIFICACAO_POTENCIAIS("1.1.3 Qualificação de Potenciais Empreendedores", Arrays.asList("Tag2", "Tag6")),
    RECEPCAO_PROPOSTAS("1.2.1 Recepção de Propostas", Arrays.asList("Tag3", "Tag7")),
    AVALIACAO("1.2.2 Avaliação", Arrays.asList("Tag4", "Tag8")),
    CONTRATACAO("1.2.3 Contratação", Arrays.asList("Tag5", "Tag9")),
    PLANEJAMENTO("1.3.1 Planejamento", Arrays.asList("Tag6", "Tag10")),
    AGREGACAO_VALOR("1.3.2 Agregação de Valor", Arrays.asList("Tag7", "Tag11")),
    MONITORAMENTO("1.3.3 Monitoramento", Arrays.asList("Tag8", "Tag12")),
    GRADUACAO("1.4.1 Graduação", Arrays.asList("Tag9", "Tag13")),
    RELACIONAMENTO_GRADUADAS("1.4.2 Relacionamento com Graduadas", Arrays.asList("Tag10", "Tag14")),
    ESTRUTURA_ORGANIZACIONAL("1.5.1 Estrutura Organizacional", Arrays.asList("Tag11", "Tag15")),
    OPERACAO_INCUBADORA("1.5.2 Operação da Incubadora", Arrays.asList("Tag12", "Tag16")),
    COMUNICACAO_MARKETING("1.5.3 Comunicação e Marketing", Arrays.asList("Tag13", "Tag17")),
    PLANEJAMENTO_ESTRATEGICO("2.1.1 Planejamento Estratégico", Arrays.asList("Tag14", "Tag18")),
    ADMINISTRACAO_ESTRATEGICA("2.1.2 Administração Estratégica", Arrays.asList("Tag15", "Tag19")),
    AMBIENTES_IDEACAO("2.2.1 Ambientes de Ideação", Arrays.asList("Tag16", "Tag20")),
    SERVICOS_ORGANIZACOES("2.2.2 Serviços a Organizações", Arrays.asList("Tag17", "Tag21")),
    AVALIACAO_QUALIDADE("2.3.1 Avaliação da Qualidade", Arrays.asList("Tag18", "Tag22")),
    AVALIACAO_IMPACTOS("2.3.2 Avaliação dos Impactos", Arrays.asList("Tag19", "Tag23")),
    INTERACAO_ENTORNO("3.1.1 Interação com o Entorno", Arrays.asList("Tag20", "Tag24")),
    PARTICIPACAO_POLITICAS("3.1.2 Participação na Proposição de Políticas Públicas", Arrays.asList("Tag21", "Tag25")),
    REDE_MENTORES("3.2.1 Rede de Mentores", Arrays.asList("Tag22", "Tag26")),
    GESTAO_OFERTAS_DEMANDAS("3.2.2 Gestão de Ofertas e Demandas", Arrays.asList("Tag23", "Tag27")),
    INCUBACAO_VIRTUAL("3.2.3 Incubação Virtual", Arrays.asList("Tag24", "Tag28")),
    GESTAO_AMBIENTAL("3.3.1 Gestão Ambiental", Arrays.asList("Tag25", "Tag29")),
    RESPONSABILIDADE_SOCIAL("3.3.2 Responsabilidade Social", Arrays.asList("Tag26", "Tag30")),
    INTERNACIONALIZACAO_INCUBADORA("4.1.1 Internacionalização da Incubadora", Arrays.asList("Tag26", "Tag30")),
    INTERNACIONALIZACAO_EMPREENDIMENTOS("4.1.2 Internacionalização dos Empreendimentos", Arrays.asList("Tag27", "Tag31"));

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
