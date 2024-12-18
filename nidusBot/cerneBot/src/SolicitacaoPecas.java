package src;

import java.time.LocalDateTime;

public class SolicitacaoPecas {
    private String solicitante;
    private String campus;
    private String areaOrigem;
    private String material;
    private String equipamento;
    private LocalDateTime dataSolicitacao;
    private String status;
    private String andamento;
    private String conclusao;

    // Construtor
    public SolicitacaoPecas(String solicitante, String campus, String areaOrigem, String material,
                            String equipamento, LocalDateTime dataSolicitacao, String status,
                            String andamento, String conclusao) {
        this.solicitante = solicitante;
        this.campus = campus;
        this.areaOrigem = areaOrigem;
        this.material = material;
        this.equipamento = equipamento;
        this.dataSolicitacao = dataSolicitacao;
        this.status = status;
        this.andamento = andamento;
        this.conclusao = conclusao;
    }

    // Getters e Setters
    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getAreaOrigem() {
        return areaOrigem;
    }

    public void setAreaOrigem(String areaOrigem) {
        this.areaOrigem = areaOrigem;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(String equipamento) {
        this.equipamento = equipamento;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAndamento() {
        return andamento;
    }

    public void setAndamento(String andamento) {
        this.andamento = andamento;
    }

    public String getConclusao() {
        return conclusao;
    }

    public void setConclusao(String conclusao) {
        this.conclusao = conclusao;
    }

    // Método para exibir informações da solicitação
    public void exibirInformacoes() {
        System.out.println("Solicitação de Peças:");
        System.out.println("Solicitante: " + solicitante);
        System.out.println("Campus: " + campus);
        System.out.println("Área de Origem: " + areaOrigem);
        System.out.println("Material: " + material);
        System.out.println("Equipamento: " + equipamento);
        System.out.println("Data da Solicitação: " + dataSolicitacao);
        System.out.println("Status: " + status);
        System.out.println("Andamento: " + andamento);
        System.out.println("Conclusão: " + conclusao);
    }

    // Método para obter uma descrição resumida da solicitação
    public String getDescricao() {
        return "Solicitação de " + material + " para " + equipamento + " - Solicitante: " + solicitante;
    }
}
