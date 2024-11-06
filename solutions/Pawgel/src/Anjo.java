public class Anjo extends Pessoa {
    private String nomeAnimal;
    private int idadeAnimal;
    private boolean estadoAlimentacaoAnimal;
    private String sintomasAnimal;
    private boolean estadoVacinaAnimal;
    private boolean estadoMedicacaoAnimal;

    // Constructor
    public Anjo(String nome, int idade, String bairro, String nomeAnimal, int idadeAnimal) {
        this.setNome(nome);
        this.setIdade(idade);
        this.setBairro(bairro);
        this.nomeAnimal = nomeAnimal;
        this.idadeAnimal = idadeAnimal;
    }

    // Métodos específicos de Anjo
    public void cadastrarInformacoesAnimal(boolean estadoAlimentacaoAnimal, String sintomasAnimal, boolean estadoVacinaAnimal, boolean estadoMedicacaoAnimal) {
        this.estadoAlimentacaoAnimal = estadoAlimentacaoAnimal;
        this.sintomasAnimal = sintomasAnimal;
        this.estadoVacinaAnimal = estadoVacinaAnimal;
        this.estadoMedicacaoAnimal = estadoMedicacaoAnimal;
    }

    public boolean emitirEstadoSaudeAnimal() {
        // Implementar lógica para emitir estado de saúde do animal
        if (estadoAlimentacaoAnimal && !sintomasAnimal.isEmpty() && estadoVacinaAnimal && estadoMedicacaoAnimal) {
            return true; // Animal saudável
        } else {
            return false; // Animal não está em bom estado de saúde
        }
    }
}
