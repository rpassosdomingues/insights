public class Alimentador extends Pessoa {
    private String tipoAlimento;
    private String prazoValidade;
    private int cargaAlimento;
    private String diaHoraRetirada;

    // Constructor
    public Alimentador(String nome, int idade, String bairro, String tipoAlimento, String prazoValidade, int cargaAlimento, String diaHoraRetirada) {
        this.setNome(nome);
        this.setIdade(idade);
        this.setBairro(bairro);
        this.tipoAlimento = tipoAlimento;
        this.prazoValidade = prazoValidade;
        this.cargaAlimento = cargaAlimento;
        this.diaHoraRetirada = diaHoraRetirada;
    }

    // Métodos específicos de Alimentador
    public void cadastrarAlimento() {
        Alimento alimento = new Alimento(this.tipoAlimento, this.prazoValidade, this.cargaAlimento, this.diaHoraRetirada);
        // Implementar lógica de cadastro do alimento
    }
}
