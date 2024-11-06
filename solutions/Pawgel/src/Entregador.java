public class Entregador extends Pessoa {
    private String tipoTransporte;
    private int cargaMaxima;

    // Constructor
    public Entregador(String nome, int idade, String bairro, String tipoTransporte, int cargaMaxima) {
        this.setNome(nome);
        this.setIdade(idade);
        this.setBairro(bairro);
        this.tipoTransporte = tipoTransporte;
        this.cargaMaxima = cargaMaxima;
    }

    // Métodos específicos de Entregador
    public void cadastrarTransporte() {
        // Implementar lógica de cadastro do tipo de transporte
    }

    public void definirCargaMaxima(int cargaMaxima) {
        this.cargaMaxima = cargaMaxima;
        // Implementar lógica para definir a carga máxima do transporte
    }
}
