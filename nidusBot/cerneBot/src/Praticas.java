package src;

public enum Praticas {
    SENSIBILIZACAO("1.1.1 Sensibilização"),
    PROSPECCAO("1.1.2 Prospecção"),
    QUALIFICACAO_POTENCIAIS("1.1.3 Qualificação de Potenciais Empreendedores"),
    RECEPCAO_PROPOSTAS("1.2.1 Recepção de Propostas"),
    AVALIACAO("1.2.2 Avaliação"),
    CONTRATACAO("1.2.3 Contratação"),
    PLANEJAMENTO("1.3.1 Planejamento"),
    AGREGACAO_VALOR("1.3.2 Agregação de Valor"),
    MONITORAMENTO("1.3.3 Monitoramento"),
    GRADUACAO("1.4.1 Graduação"),
    RELACIONAMENTO_GRADUADAS("1.4.2 Relacionamento com Graduadas"),
    ESTRUTURA_ORGANIZACIONAL("1.5.1 Estrutura Organizacional"),
    OPERACAO_INCUBADORA("1.5.2 Operação da Incubadora"),
    COMUNICACAO_MARKETING("1.5.3 Comunicação e Marketing"),
    PLANEJAMENTO_ESTRATEGICO("2.1.1 Planejamento Estratégico"),
    ADMINISTRACAO_ESTRATEGICA("2.1.2 Administração Estratégica"),
    AMBIENTES_IDEACAO("2.2.1 Ambientes de Ideação"),
    SERVIÇOS_ORGANIZACOES("2.2.2 Serviços a Organizações"),
    AVALIACAO_QUALIDADE("2.3.1 Avaliação da Qualidade"),
    AVALIACAO_IMPACTOS("2.3.2 Avaliação dos Impactos"),
    INTERACAO_ENTORNO("3.1.1 Interação com o Entorno"),
    PARTICIPACAO_POLITICAS("3.1.2 Participação na Proposição de Políticas Públicas"),
    REDE_MENTORES("3.2.1 Rede de Mentores"),
    GESTAO_OFERTAS_DEMANDAS("3.2.2 Gestão de Ofertas e Demandas"),
    INCUBACAO_VIRTUAL("3.2.3 Incubação Virtual"),
    GESTAO_AMBIENTAL("3.3.1 Gestão Ambiental"),
    RESPONSABILIDADE_SOCIAL("3.3.2 Responsabilidade Social");

    private final String descricao;

    Praticas(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
