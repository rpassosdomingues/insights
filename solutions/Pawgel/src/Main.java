import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            exibirMenu();

            // Ler a opção do usuário
            System.out.print("Escolha uma opção (1 a 5, 0 para sair): ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            switch (opcao) {
                case 1:
                    cadastrarComoCacador(scanner);
                    break;
                case 2:
                    cadastrarComoAlimentador(scanner);
                    break;
                case 3:
                    cadastrarComoEntregador(scanner);
                    break;
                case 4:
                    cadastrarComoDoutor(scanner);
                    break;
                case 5:
                    cadastrarComoAnjo(scanner);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        } while (opcao != 0);

        scanner.close();
    }

    // Método para exibir o menu de opções
    private static void exibirMenu() {
        System.out.println("=== Menu de Opções ===");
        System.out.println("1. Cadastrar como Caçador");
        System.out.println("2. Cadastrar como Alimentador");
        System.out.println("3. Cadastrar como Entregador");
        System.out.println("4. Cadastrar como Doutor");
        System.out.println("5. Cadastrar como Anjo");
        System.out.println("0. Sair");
    }

    // Método para cadastrar como Caçador
    private static void cadastrarComoCacador(Scanner scanner) {
        System.out.println("== Cadastro como Caçador ==");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Idade: ");
        int idade = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha

        System.out.print("Bairro: ");
        String bairro = scanner.nextLine();

        System.out.print("Tipo de animal: ");
        String tipoAnimal = scanner.nextLine();

        System.out.print("Localização CEP: ");
        String localizacaoCep = scanner.nextLine();

        // Criar objeto Caçador e cadastrar o animal
        Cacador cacador = new Cacador(nome, idade, bairro, tipoAnimal, localizacaoCep);
        cacador.cadastrarAnimal();

        System.out.println("Cadastro como Caçador realizado com sucesso.\n");
    }

    // Método para cadastrar como Alimentador
    private static void cadastrarComoAlimentador(Scanner scanner) {
        System.out.println("== Cadastro como Alimentador ==");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Idade: ");
        int idade = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha

        System.out.print("Bairro: ");
        String bairro = scanner.nextLine();

        System.out.print("Tipo de alimento: ");
        String tipoAlimento = scanner.nextLine();

        System.out.print("Prazo de validade: ");
        String prazoValidade = scanner.nextLine();

        System.out.print("Carga de alimento: ");
        int cargaAlimento = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha

        System.out.print("Dia e horário de retirada: ");
        String diaHoraRetirada = scanner.nextLine();

        // Criar objeto Alimentador e cadastrar o alimento
        Alimentador alimentador = new Alimentador(nome, idade, bairro, tipoAlimento, prazoValidade, cargaAlimento, diaHoraRetirada);
        alimentador.cadastrarAlimento();

        System.out.println("Cadastro como Alimentador realizado com sucesso.\n");
    }

    // Método para cadastrar como Entregador
    private static void cadastrarComoEntregador(Scanner scanner) {
        System.out.println("== Cadastro como Entregador ==");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Idade: ");
        int idade = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha

        System.out.print("Bairro: ");
        String bairro = scanner.nextLine();

        System.out.print("Tipo de transporte: ");
        String tipoTransporte = scanner.nextLine();

        System.out.print("Carga máxima: ");
        int cargaMaxima = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha

        // Criar objeto Entregador
        Entregador entregador = new Entregador(nome, idade, bairro, tipoTransporte, cargaMaxima);
        entregador.cadastrarTransporte();

        System.out.println("Cadastro como Entregador realizado com sucesso.\n");
    }

    // Método para cadastrar como Doutor
    private static void cadastrarComoDoutor(Scanner scanner) {
        System.out.println("== Cadastro como Doutor ==");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Idade: ");
        int idade = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha

        System.out.print("Bairro: ");
        String bairro = scanner.nextLine();

        // Criar objeto Doutor
        Doutor doutor = new Doutor(nome, idade, bairro);

        // Cadastro e modificação do estado de saúde do animal
        System.out.print("Tipo de animal: ");
        String tipoAnimal = scanner.nextLine();

        System.out.print("Localização CEP do animal: ");
        String localizacaoCep = scanner.nextLine();

        Animal animal = new Animal(tipoAnimal, localizacaoCep);

        System.out.print("Estado de alimentação do animal (true/false): ");
        boolean estadoAlimentacao = scanner.nextBoolean();
        scanner.nextLine(); // Consumir a quebra de linha

        System.out.print("Sintomas do animal: ");
        String sintomas = scanner.nextLine();

        System.out.print("Estado de vacinação do animal (true/false): ");
        boolean estadoVacina = scanner.nextBoolean();
        scanner.nextLine(); // Consumir a quebra de linha

        System.out.print("Estado de saúde do animal (true/false): ");
        boolean estadoSaude = scanner.nextBoolean();
        scanner.nextLine(); // Consumir a quebra de linha

        doutor.tratarAnimal(animal, estadoAlimentacao, sintomas, estadoVacina, estadoSaude);

        System.out.println("Cadastro como Doutor realizado com sucesso.\n");
    }

    // Método para cadastrar como Anjo
    private static void cadastrarComoAnjo(Scanner scanner) {
        System.out.println("== Cadastro como Anjo ==");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Idade: ");
        int idade = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha

        System.out.print("Bairro: ");
        String bairro = scanner.nextLine();

        // Criar objeto Anjo
        Anjo anjo = new Anjo(nome, idade, bairro);

        // Cadastro e modificação do estado de saúde do animal
        System.out.print("Nome do animal: ");
        String nomeAnimal = scanner.nextLine();

        System.out.print("Idade do animal: ");
        int idadeAnimal = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha

        System.out.print("Estado de alimentação do animal (true/false): ");
        boolean estadoAlimentacaoAnimal = scanner.nextBoolean();
        scanner.nextLine(); // Consumir a quebra de linha

        System.out.print("Sintomas do animal: ");
        String sintomasAnimal = scanner.nextLine();

        System.out.print("Estado de vacinação do animal (true/false): ");
        boolean estadoVacinaAnimal = scanner.nextBoolean();
        scanner.nextLine(); // Consumir a quebra de linha

        System.out.print("Estado de medicação do animal (true/false): ");
        boolean estadoMedicacaoAnimal = scanner.nextBoolean();
        scanner.nextLine(); // Consumir a quebra de linha

        anjo.cuidarAnimal(nomeAnimal, idadeAnimal, estadoAlimentacaoAnimal, sintomasAnimal, estadoVacinaAnimal, estadoMedicacaoAnimal);

        System.out.println("Cadastro como Anjo realizado com sucesso.\n");
    }
}
