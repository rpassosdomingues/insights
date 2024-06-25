import java.util.List;

public class Rota {
    private String nome;
    private double altimetriaAcumulada; // em metros
    private double distancia; // em km
    private double tempoEstimado; // em horas
    private List<String> pousadas;
    private List<String> paradasHidratacao;
    private List<String> paradasAlimentacao;
    private List<String> hospitais;
    private List<String> conveniencias;
    private List<String> bicicletarias;
    private List<String> mecanicos;
    private String outrasInformacoes;

    public Rota(String nome, double altimetriaAcumulada, double distancia, double tempoEstimado, List<String> pousadas, List<String> paradasHidratacao, List<String> paradasAlimentacao, List<String> hospitais, List<String> conveniencias, List<String> bicicletarias, List<String> mecanicos, String outrasInformacoes) {
        this.nome = nome;
        this.altimetriaAcumulada = altimetriaAcumulada;
        this.distancia = distancia;
        this.tempoEstimado = tempoEstimado;
        this.pousadas = pousadas;
        this.paradasHidratacao = paradasHidratacao;
        this.paradasAlimentacao = paradasAlimentacao;
        this.hospitais = hospitais;
        this.conveniencias = conveniencias;
        this.bicicletarias = bicicletarias;
        this.mecanicos = mecanicos;
        this.outrasInformacoes = outrasInformacoes;
    }

    // Getters e Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getAltimetriaAcumulada() {
        return altimetriaAcumulada;
    }

    public void setAltimetriaAcumulada(double altimetriaAcumulada) {
        this.altimetriaAcumulada = altimetriaAcumulada;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getTempoEstimado() {
        return tempoEstimado;
    }

    public void setTempoEstimado(double tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
    }

    public List<String> getPousadas() {
        return pousadas;
    }

    public void setPousadas(List<String> pousadas) {
        this.pousadas = pousadas;
    }

    public List<String> getParadasHidratacao() {
        return paradasHidratacao;
    }

    public void setParadasHidratacao(List<String> paradasHidratacao) {
        this.paradasHidratacao = paradasHidratacao;
    }

    public List<String> getParadasAlimentacao() {
        return paradasAlimentacao;
    }

    public void setParadasAlimentacao(List<String> paradasAlimentacao) {
        this.paradasAlimentacao = paradasAlimentacao;
    }

    public List<String> getHospitais() {
        return hospitais;
    }

    public void setHospitais(List<String> hospitais) {
        this.hospitais = hospitais;
    }

    public List<String> getConveniencias() {
        return conveniencias;
    }

    public void setConveniencias(List<String> conveniencias) {
        this.conveniencias = conveniencias;
    }

    public List<String> getBicicletarias() {
        return bicicletarias;
    }

    public void setBicicletarias(List<String> bicicletarias) {
        this.bicicletarias = bicicletarias;
    }

    public List<String> getMecanicos() {
        return mecanicos;
    }

    public void setMecanicos(List<String> mecanicos) {
        this.mecanicos = mecanicos;
    }

    public String getOutrasInformacoes() {
        return outrasInformacoes;
    }

    public void setOutrasInformacoes(String outrasInformacoes) {
        this.outrasInformacoes = outrasInformacoes;
    }
}
