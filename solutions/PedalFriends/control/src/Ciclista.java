public class Ciclista {
    private String nome;
    private int idade;
    private double altura; // em metros
    private double peso; // em kg
    private double maiorPercurso; // em km
    private String tipoSanguineo;
    private boolean fumante;
    private boolean cardiaco;
    private String outrasInformacoesSaude;
    private String nivelPedal;

    public Ciclista(String nome, int idade, double altura, double peso, double maiorPercurso, String tipoSanguineo, boolean fumante, boolean cardiaco, String outrasInformacoesSaude) {
        this.nome = nome;
        this.idade = idade;
        this.altura = altura;
        this.peso = peso;
        this.maiorPercurso = maiorPercurso;
        this.tipoSanguineo = tipoSanguineo;
        this.fumante = fumante;
        this.cardiaco = cardiaco;
        this.outrasInformacoesSaude = outrasInformacoesSaude;
        this.nivelPedal = calcularNivelPedal();
    }

    private String calcularNivelPedal() {
        if (maiorPercurso > 100 && !fumante && !cardiaco) {
            return "Avançado";
        } else if (maiorPercurso > 50) {
            return "Intermediário";
        } else {
            return "Iniciante";
        }
    }

    // Getters e Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getMaiorPercurso() {
        return maiorPercurso;
    }

    public void setMaiorPercurso(double maiorPercurso) {
        this.maiorPercurso = maiorPercurso;
    }

    public String getTipoSanguineo() {
        return tipoSanguineo;
    }

    public void setTipoSanguineo(String tipoSanguineo) {
        this.tipoSanguineo = tipoSanguineo;
    }

    public boolean isFumante() {
        return fumante;
    }

    public void setFumante(boolean fumante) {
        this.fumante = fumante;
    }

    public boolean isCardiaco() {
        return cardiaco;
    }

    public void setCardiaco(boolean cardiaco) {
        this.cardiaco = cardiaco;
    }

    public String getOutrasInformacoesSaude() {
        return outrasInformacoesSaude;
    }

    public void setOutrasInformacoesSaude(String outrasInformacoesSaude) {
        this.outrasInformacoesSaude = outrasInformacoesSaude;
    }

    public String getNivelPedal() {
        return nivelPedal;
    }

    public void setNivelPedal(String nivelPedal) {
        this.nivelPedal = nivelPedal;
    }
}
