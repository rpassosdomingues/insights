public class Doutor extends Pessoa {
    private boolean estadoAlimentacao;
    private String sintomas;
    private boolean estadoVacina;
    private boolean estadoMedicacao;

    // Constructor
    public Doutor(String nome, int idade, String bairro) {
        this.setNome(nome);
        this.setIdade(idade);
        this.setBairro(bairro);
    }

    // Métodos específicos de Doutor
    public void cadastrarInformacoesAnimal(boolean estadoAlimentacao, String sintomas, boolean estadoVacina, boolean estadoMedicacao) {
        this.estadoAlimentacao = estadoAlimentacao;
        this.sintomas = sintomas;
        this.estadoVacina = estadoVacina;
        this.estadoMedicacao = estadoMedicacao;
    }

    public void modificarEstadoSaude(boolean estadoAlimentacao, boolean estadoVacina, boolean estadoMedicacao) {
        this.estadoAlimentacao = estadoAlimentacao;
        this.estadoVacina = estadoVacina;
        this.estadoMedicacao = estadoMedicacao;
        // Implementar lógica de modificação do estado de saúde do animal
    }
}
