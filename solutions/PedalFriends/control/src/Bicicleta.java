public class Bicicleta {
    private String tipo;
    private String raiamento;
    private String quadro;
    private String pneu;
    private boolean ferramentas;
    private boolean remendo;
    private String outrasInformacoes;

    public Bicicleta(String tipo, String raiamento, String quadro, String pneu, boolean ferramentas, boolean remendo, String outrasInformacoes) {
        this.tipo = tipo;
        this.raiamento = raiamento;
        this.quadro = quadro;
        this.pneu = pneu;
        this.ferramentas = ferramentas;
        this.remendo = remendo;
        this.outrasInformacoes = outrasInformacoes;
    }

    // Getters e Setters

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRaiamento() {
        return raiamento;
    }

    public void setRaiamento(String raiamento) {
        this.raiamento = raiamento;
    }

    public String getQuadro() {
        return quadro;
    }

    public void setQuadro(String quadro) {
        this.quadro = quadro;
    }

    public String getPneu() {
        return pneu;
    }

    public void setPneu(String pneu) {
        this.pneu = pneu;
    }

    public boolean isFerramentas() {
        return ferramentas;
    }

    public void setFerramentas(boolean ferramentas) {
        this.ferramentas = ferramentas;
    }

    public boolean isRemendo() {
        return remendo;
    }

    public void setRemendo(boolean remendo) {
        this.remendo = remendo;
    }

    public String getOutrasInformacoes() {
        return outrasInformacoes;
    }

    public void setOutrasInformacoes(String outrasInformacoes) {
        this.outrasInformacoes = outrasInformacoes;
    }
}
