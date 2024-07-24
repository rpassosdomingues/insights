public class Cacador extends Pessoa {
    private String tipoAnimal;
    private String localizacaoCep;

    // Constructor
    public Cacador(String nome, int idade, String bairro, String tipoAnimal, String localizacaoCep) {
        this.setNome(nome);
        this.setIdade(idade);
        this.setBairro(bairro);
        this.tipoAnimal = tipoAnimal;
        this.localizacaoCep = localizacaoCep;
    }

    // Métodos específicos de Caçador
    public void cadastrarAnimal() {
        Animal animal = new Animal(this.tipoAnimal, this.localizacaoCep);
        // Implementar lógica de cadastro do animal
    }
}
