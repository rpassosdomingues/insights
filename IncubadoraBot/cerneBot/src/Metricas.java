package src;

public class Metricas {
    // Variáveis para armazenar os resultados dos indicadores
    private double numeroSensibilizados;
    private double numeroPropostasProspectadas;
    private double numeroPotenciaisEmpreendedoresQualificados;
    private double numeroPropostasRegionaisRecebidas;
    private double numeroPropostasOutrasRegioesRecebidas;
    private double numeroEmpreendimentosSelecionados;
    private double percentualSelecionados;
    private double numeroContratacoes;
    private double numeroEmpreendimentosPreIncubacao;
    private double percentualPlanosAtualizados;
    private double numeroServicosOferecidos;
    private double numeroHorasConsultoria;
    private double numeroHorasCapacitacao;
    private double numeroEmpregosGerados;
    private double percentualNaoConclusao;
    private double percentualAltoImpacto;
    private double faturamentoTotal;
    private double totalImpostosGerados;
    private double investimentoAnjo;
    private double investimentosRecebidos;
    private double numeroGraduadas;
    private double percentualEstabelecidas;
    private double percentualAltoImpactoGraduadas;
    private double percentualAltoCrescimentoGraduadas;
    private double percentualPermanenciaMercado;
    private double numeroEmpregosGraduadas;
    private double faturamentoGraduadas;
    private double totalImpostosGraduadas;
    private double investimentosRecebidosGraduadas;
    private double percentualAdquiridas;
    private double percentualFusoes;
    private double percentualInteracaoIncubadora;
    private double numeroParceiros;
    private double numeroEventosOrganizados;
    private double totalReceitaFinanceira;
    private double totalReceitaEconomica;
    private double recursosCaptados;
    private double numeroPessoasAlcancadasMarketing;
    private double percentualGovernancaParticipacao;
    private double percentualAcoesPlanejamento;
    private double numeroAmbientesIdeacao;
    private double totalReceitaServicos;
    private double numeroPremiosRecebidos;
    private double numeroPessoasRelatorioImpactos;
    private double numeroProjetosParceria;
    private double numeroForaParticipacao;
    private double percentualEmpresasComMentor;
    private double numeroMentores;
    private double numeroHorasMentoria;
    private double numeroProjetosConexao;
    private double numeroEmpresasParticipantes;
    private double numeroEmpreendimentosIncubadosVirtualmente;
    private double numeroAcoesGestaoAmbiental;
    private double numeroAcoesResponsabilidadeSocial;

    // Construtor
    public Metricas() {
        // Inicializando os indicadores com os resultados fornecidos
        this.numeroSensibilizados = 2783;
        this.numeroPropostasProspectadas = 5;
        this.numeroPotenciaisEmpreendedoresQualificados = 81;
        this.numeroPropostasRegionaisRecebidas = 4;
        this.numeroPropostasOutrasRegioesRecebidas = 0;
        this.numeroEmpreendimentosSelecionados = 2;
        this.percentualSelecionados = 20;
        this.numeroContratacoes = 1;
        this.numeroEmpreendimentosPreIncubacao = 4;
        this.percentualPlanosAtualizados = 50;
        this.numeroServicosOferecidos = 44;
        this.numeroHorasConsultoria = 8;
        this.numeroHorasCapacitacao = 0;
        this.numeroEmpregosGerados = 41;
        this.percentualNaoConclusao = 29;
        this.percentualAltoImpacto = 21;
        this.faturamentoTotal = 400000; // R$ 400'000,00
        this.totalImpostosGerados = 2000; // R$ 2'000,00
        this.investimentoAnjo = 0; // R$ 0,00
        this.investimentosRecebidos = 0; // R$ 0,00
        this.numeroGraduadas = 7;
        this.percentualEstabelecidas = 29;
        this.percentualAltoImpactoGraduadas = 29;
        this.percentualAltoCrescimentoGraduadas = 29;
        this.percentualPermanenciaMercado = 29;
        this.numeroEmpregosGraduadas = 34;
        this.faturamentoGraduadas = 4000000; // R$ 4.000.000,00
        this.totalImpostosGraduadas = 480000; // R$ 480.000,00
        this.investimentosRecebidosGraduadas = 100000; // R$ 100.000,00
        this.percentualAdquiridas = 0;
        this.percentualFusoes = 0;
        this.percentualInteracaoIncubadora = 29;
        this.numeroParceiros = 8;
        this.numeroEventosOrganizados = 59;
        this.totalReceitaFinanceira = 241043.34; // R$ 241.043,34
        this.totalReceitaEconomica = 0;
        this.recursosCaptados = 1;
        this.numeroPessoasAlcancadasMarketing = 2280;
        this.percentualGovernancaParticipacao = 90;
        this.percentualAcoesPlanejamento = 50;
        this.numeroAmbientesIdeacao = 5;
        this.totalReceitaServicos = 0; // R$ 0,00
        this.numeroPremiosRecebidos = 1;
        this.numeroPessoasRelatorioImpactos = 28;
        this.numeroProjetosParceria = 21;
        this.numeroForaParticipacao = 3;
        this.percentualEmpresasComMentor = 0;
        this.numeroMentores = 11;
        this.numeroHorasMentoria = 6;
        this.numeroProjetosConexao = 5;
        this.numeroEmpresasParticipantes = 2;
        this.numeroEmpreendimentosIncubadosVirtualmente = 3;
        this.numeroAcoesGestaoAmbiental = 17;
        this.numeroAcoesResponsabilidadeSocial = 8;
    }

    // Método para obter resultados
    public double obterResultado(String indicador) {
        switch (indicador) {
            case "Sensibilizacao":
                return numeroSensibilizados;
            case "Prospeccao":
                return numeroPropostasProspectadas;
            case "Potenciais Empreendedores Qualificados":
                return numeroPotenciaisEmpreendedoresQualificados;
            case "Propostas Regionais Recebidas":
                return numeroPropostasRegionaisRecebidas;
            case "Propostas Outras Regioes Recebidas":
                return numeroPropostasOutrasRegioesRecebidas;
            case "Empreendimentos Selecionados":
                return numeroEmpreendimentosSelecionados;
            case "Percentual Selecionados":
                return percentualSelecionados;
            case "Contratacoes":
                return numeroContratacoes;
            case "Empreendimentos Pre Incubacao":
                return numeroEmpreendimentosPreIncubacao;
            case "Percentual Planos Atualizados":
                return percentualPlanosAtualizados;
            case "Servicos Oferecidos":
                return numeroServicosOferecidos;
            case "Horas Consultoria":
                return numeroHorasConsultoria;
            case "Horas Capacitação":
                return numeroHorasCapacitacao;
            case "Empregos Gerados":
                return numeroEmpregosGerados;
            case "Percentual Nao Conclusao":
                return percentualNaoConclusao;
            case "Percentual Alto Impacto":
                return percentualAltoImpacto;
            case "Faturamento Total":
                return faturamentoTotal;
            case "Total Impostos Gerados":
                return totalImpostosGerados;
            case "Investimento Anjo":
                return investimentoAnjo;
            case "Investimentos Recebidos":
                return investimentosRecebidos;
            case "Graduadas":
                return numeroGraduadas;
            case "Percentual Estabelecidas":
                return percentualEstabelecidas;
            case "Percentual Alto Impacto Graduadas":
                return percentualAltoImpactoGraduadas;
            case "Percentual Alto Crescimento Graduadas":
                return percentualAltoCrescimentoGraduadas;
            case "Percentual Permanencia Mercado":
                return percentualPermanenciaMercado;
            case "Empregos Graduadas":
                return numeroEmpregosGraduadas;
            case "Faturamento Graduadas":
                return faturamentoGraduadas;
            case "Total Impostos Graduadas":
                return totalImpostosGraduadas;
            case "Investimentos Recebidos Graduadas":
                return investimentosRecebidosGraduadas;
            case "Percentual Adquiridas":
                return percentualAdquiridas;
            case "Percentual Fusoes":
                return percentualFusoes;
            case "Percentual Interacao Incubadora":
                return percentualInteracaoIncubadora;
            case "Parceiros":
                return numeroParceiros;
            case "Eventos Organizados":
                return numeroEventosOrganizados;
            case "Total Receita Financeira":
                return totalReceitaFinanceira;
            case "Total Receita Economica":
                return totalReceitaEconomica;
            case "Recursos Captados":
                return recursosCaptados;
            case "Pessoas Alcançadas Marketing":
                return numeroPessoasAlcancadasMarketing;
            case "Percentual Governanca Participacao":
                return percentualGovernancaParticipacao;
            case "Percentual Acoes Planejamento":
                return percentualAcoesPlanejamento;
            case "Ambientes de Ideacao":
                return numeroAmbientesIdeacao;
            case "Total Receita Servicos":
                return totalReceitaServicos;
            case "Premios Recebidos":
                return numeroPremiosRecebidos;
            case "Pessoas Relatorio Impactos":
                return numeroPessoasRelatorioImpactos;
            case "Projetos Parceria":
                return numeroProjetosParceria;
            case "Fora Participacao":
                return numeroForaParticipacao;
            case "Empresas com Mentor":
                return percentualEmpresasComMentor;
            case "Mentores":
                return numeroMentores;
            case "Horas Mentoria":
                return numeroHorasMentoria;
            case "Projetos Conexao":
                return numeroProjetosConexao;
            case "Empresas Participantes":
                return numeroEmpresasParticipantes;
            case "Empreendimentos Incubados Virtualmente":
                return numeroEmpreendimentosIncubadosVirtualmente;
            case "Acoes Gestao Ambiental":
                return numeroAcoesGestaoAmbiental;
            case "Acoes Responsabilidade Social":
                return numeroAcoesResponsabilidadeSocial;
            default:
                throw new IllegalArgumentException("Indicador desconhecido: " + indicador);
        }
    }
}
