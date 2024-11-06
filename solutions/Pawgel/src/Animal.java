public class Animal {
    private String nome;
    private String tipo;
    private String raca;
    private int idade;
    private boolean estadoAlimentacao;
    private boolean estadoVacina;
    private boolean estadoSaude;
    private String sintomas;

    // Constructor
    public Animal(String nome, String tipo, String raca, int idade, String localizacaoCep) {
        this.nome = nome;
        this.tipo = tipo;
        this.raca = raca;
        this.idade = idade;
        this.localizacaoCep = localizacaoCep;
        this.estadoAlimentacao = false; // Estado inicial de alimentação é false (não alimentado)
        this.estadoVacina = false; // Estado inicial de vacinação é false (não vacinado)
        this.estadoSaude = true; // Estado inicial de saúde é true (saudável)
        this.sintomas = ""; // Inicialmente não há sintomas
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public boolean isEstadoAlimentacao() {
        return estadoAlimentacao;
    }

    public void setEstadoAlimentacao(boolean estadoAlimentacao) {
        this.estadoAlimentacao = estadoAlimentacao;
    }

    public boolean isEstadoVacina() {
        return estadoVacina;
    }

    public void setEstadoVacina(boolean estadoVacina) {
        this.estadoVacina = estadoVacina;
    }

    public boolean isEstadoSaude() {
        return estadoSaude;
    }

    public void setEstadoSaude(boolean estadoSaude) {
        this.estadoSaude = estadoSaude;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getLocalizacaoCep() {
        return localizacaoCep;
    }

    public void setLocalizacaoCep(String localizacaoCep) {
        this.localizacaoCep = localizacaoCep;
    }
}
