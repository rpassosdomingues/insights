package src;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {
    private List<Projeto> projetos;
    private List<Evento> eventos;  
    private List<Monitoramento> monitoramentos;
    private List<Reserva> reservas;
    private List<SolicitacaoPecas> pecas;

    // Construtor
    public Main() {
        projetos = new ArrayList<>();
        eventos = new ArrayList<>();
        monitoramentos = new ArrayList<>();
        reservas = new ArrayList<>(); 
        pecas = new ArrayList<>(); 
    }

    // Método principal
    public static void main(String[] args) {
        Main main = new Main();
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("\nMenu Principal:");
            System.out.println("1. Cadastrar Projeto");
            System.out.println("2. Agendar Evento");
            System.out.println("3. Registrar Rodada de Monitoramento");
            System.out.println("4. Reservar uma Sala");
            System.out.println("5. Solicitar Fabricação de Peça Maker");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha após o número

            switch (opcao) {
                case 1:
                    main.cadastrarProjeto(scanner);
                    break;
                case 2:
                    main.agendarEvento(scanner);
                    break;
                case 3:
                    main.registrarRodadaMonitoramento(scanner);
                    break;
                case 4:
                    main.reservarSala(scanner);
                    break;
                case 5:
                    main.solicitarFabricacaoPecas(scanner);
                    break;
                case 0:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        scanner.close();
    }

    // Métodos de cadastro

    public void cadastrarProjeto(Scanner scanner) {
        System.out.print("Informe o nome do projeto: ");
        String nomeProjeto = scanner.nextLine();
        System.out.print("Informe a descrição do projeto: ");
        String descricaoProjeto = scanner.nextLine();
        System.out.print("Informe a data de início do projeto (yyyy-MM-dd): ");
        String dataInicioProjeto = scanner.nextLine();

        Projeto projeto = new Projeto(nomeProjeto, descricaoProjeto, dataInicioProjeto);
        projetos.add(projeto);  // Adicionar à lista de projetos

        // Salvar o projeto no arquivo CSV
        System.out.println("Projeto cadastrado com sucesso: " + projeto.getNomeProjeto());
    }

    public void agendarEvento(Scanner scanner) {
        System.out.print("Informe o nome do evento: ");
        String nomeEvento = scanner.nextLine();
        System.out.print("Informe os detalhes do evento: ");
        String detalhesEvento = scanner.nextLine();
        System.out.print("Informe a data do evento (yyyy-MM-dd): ");
        String dataEvento = scanner.nextLine();
        System.out.print("Informe a hora de início do evento (HH:mm): ");
        String horaInicioEvento = scanner.nextLine();
        System.out.print("Informe a hora de fim do evento (HH:mm): ");
        String horaFimEvento = scanner.nextLine();
        System.out.print("Informe o local do evento: ");
        String localEvento = scanner.nextLine();

        LocalDate dia = LocalDate.parse(dataEvento);
        LocalTime horarioInicio = LocalTime.parse(horaInicioEvento);
        LocalTime horarioTermino = LocalTime.parse(horaFimEvento);

        Evento evento = new Evento("Projeto Exemplo", "Execução do evento", nomeEvento, localEvento,
                dia, horarioInicio, horarioTermino, detalhesEvento);

        eventos.add(evento);

        System.out.println("Evento cadastrado com sucesso!");
    }

    public void registrarRodadaMonitoramento(Scanner scanner) {
        System.out.print("Escolha a empresa incubada (1 - Polygon, 2 - ICRO Digital, 3 - SMARTComerci, 4 - iLegis): ");
        int escolhaEmpresa = scanner.nextInt();
        scanner.nextLine();  // Consumir nova linha

        EmpresaIncubada empresaSelecionada;
        switch (escolhaEmpresa) {
            case 1:
                empresaSelecionada = EmpresaIncubada.POLYGON;
                break;
            case 2:
                empresaSelecionada = EmpresaIncubada.ICRO_DIGITAL;
                break;
            case 3:
                empresaSelecionada = EmpresaIncubada.SMARTCOMERCI;
                break;
            case 4:
                empresaSelecionada = EmpresaIncubada.ILEGIS;
                break;
            default:
                System.out.println("Opção inválida. Tente novamente.");
                return;  // Sai do método
        }

        System.out.print("Informe a data e hora do monitoramento (yyyy-MM-dd HH:mm): ");
        String dataHoraInput = scanner.nextLine();
        LocalDateTime dataHora = LocalDateTime.parse(dataHoraInput.replace(" ", "T"));

        System.out.print("Informe os documentos pendentes (separados por vírgula): ");
        String documentosInput = scanner.nextLine();
        List<String> documentosPendentes = List.of(documentosInput.split(","));

        System.out.print("Informe o caminho do arquivo Excel: ");
        String arquivoExcel = scanner.nextLine();

        Monitoramento monitoramento = new Monitoramento(empresaSelecionada, dataHora, documentosPendentes, arquivoExcel);
        monitoramentos.add(monitoramento);

        System.out.println("Monitoramento cadastrado com sucesso!");
    }

    public void reservarSala(Scanner scanner) {
        System.out.print("Escolha o tipo de reserva (1 - Sala de Reuniões, 2 - Coworking): ");
        int tipoReservaEscolha = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Informe o nome do solicitante: ");
        String solicitante = scanner.nextLine();

        System.out.print("Informe a data e hora de início (yyyy-MM-dd HH:mm): ");
        String inicioInput = scanner.nextLine();
        LocalDateTime dataHoraInicio = LocalDateTime.parse(inicioInput.replace(" ", "T"));

        System.out.print("Informe a data e hora de fim (yyyy-MM-dd HH:mm): ");
        String fimInput = scanner.nextLine();
        LocalDateTime dataHoraFim = LocalDateTime.parse(fimInput.replace(" ", "T"));

        if (tipoReservaEscolha == 1) {
            reservarSalaReunioes(scanner, solicitante, dataHoraInicio, dataHoraFim);
        } else if (tipoReservaEscolha == 2) {
            reservarCoworking(scanner, solicitante, dataHoraInicio, dataHoraFim);
        } else {
            System.out.println("Tipo de reserva inválido.");
        }
    }

    private void reservarSalaReunioes(Scanner scanner, String solicitante, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        System.out.print("Informe o número de pessoas: ");
        int numeroPessoas = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha

        System.out.print("Necessita transmissão? (true/false): ");
        boolean necessitaTransmissao = scanner.nextBoolean();
        scanner.nextLine(); // Consumir a nova linha

        Reserva reserva = new ReservaSalaReunioes(solicitante, dataHoraInicio, dataHoraFim, "Reserva de sala de reuniões", numeroPessoas, necessitaTransmissao);
        reservas.add(reserva);

        System.out.println("Reserva de sala de reuniões cadastrada com sucesso!");
    }

    private void reservarCoworking(Scanner scanner, String solicitante, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        System.out.print("Informe o número de mesas: ");
        int numeroMesas = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha

        System.out.print("Necessita projetor? (true/false): ");
        boolean necessitaProjetor = scanner.nextBoolean();
        scanner.nextLine(); // Consumir a nova linha

        String descricaoReserva = "Reserva de coworking para " + numeroMesas + " mesas";
        Reserva reserva = new ReservaCoworking(solicitante, dataHoraInicio, dataHoraFim, descricaoReserva, necessitaProjetor);
        reservas.add(reserva);

        System.out.println("Reserva de coworking cadastrada com sucesso!");
    }

    // Método para cadastrar solicitação de fabricação de peça
    public void solicitarFabricacaoPecas(Scanner scanner) {
        System.out.print("Informe o solicitante: ");
        String solicitante = scanner.nextLine();
        System.out.print("Informe o campus: ");
        String campus = scanner.nextLine();
        System.out.print("Informe a área de origem: ");
        String areaOrigem = scanner.nextLine();
        System.out.print("Informe o material: ");
        String material = scanner.nextLine();
        System.out.print("Informe o equipamento: ");
        String equipamento = scanner.nextLine();
        System.out.print("Informe o status: ");
        String status = scanner.nextLine();
        System.out.print("Informe o andamento: ");
        String andamento = scanner.nextLine();
        LocalDateTime dataSolicitacao = LocalDateTime.now();  // Data atual
        String conclusao = "Em aberto";

        SolicitacaoPecas solicitacao = new SolicitacaoPecas(solicitante, campus, areaOrigem, material, equipamento, dataSolicitacao, status, andamento, conclusao);
        pecas.add(solicitacao);

        System.out.println("Solicitação de peça cadastrada com sucesso!");
    }
}
