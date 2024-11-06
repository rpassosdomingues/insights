package src;

import java.util.ArrayList;
import java.util.List;

public class Incubadora {
    private List<Projeto> projetos; // Lista de projetos
    private List<SolicitacaoPecas> solicitacoes; // Lista para as solicitações
    private List<Reserva> reservas; // Lista para as reservas

    // Construtor
    public Incubadora() {
        this.projetos = new ArrayList<>();
        this.solicitacoes = new ArrayList<>();
        this.reservas = new ArrayList<>();
    }

    // Método para cadastrar um projeto
    public void cadastrarProjeto(Projeto projeto) {
        projetos.add(projeto);
        System.out.println("Projeto cadastrado com sucesso!");
    }

    // Método para consultar um projeto pelo nome
    public Projeto consultarProjeto(String nome) {
        for (Projeto projeto : projetos) {
            if (projeto.getNomeProjeto().equalsIgnoreCase(nome)) {
                return projeto;
            }
        }
        return null; // Retorna null se o projeto não for encontrado
    }

    // Método para cadastrar uma solicitação
    public void cadastrarSolicitacao(SolicitacaoPecas solicitacao) {
        solicitacoes.add(solicitacao);
        System.out.println("Solicitação cadastrada com sucesso!");
    }

    // Método para consultar todas as solicitações
    public List<SolicitacaoPecas> consultarSolicitacoes() {
        return solicitacoes;
    }

    // Método para cadastrar uma reserva
    public void cadastrarReserva(Reserva reserva) {
        reservas.add(reserva);
        System.out.println("Reserva cadastrada com sucesso!");
    }

    // Método para consultar todas as reservas
    public List<Reserva> consultarReservas() {
        return reservas;
    }

    // Método para gerar relatório de todas as áreas
    public void gerarRelatorio() {
        System.out.println("Relatório de todas as áreas:");

        // Relatório de projetos
        for (Projeto projeto : projetos) {
            System.out.println("Projeto: " + projeto.getNomeProjeto());
        }

        // Relatório de solicitações
        for (SolicitacaoPecas solicitacao : solicitacoes) {
            System.out.println("Solicitação: " + solicitacao.getDescricao());
        }

        // Relatório de reservas
        for (Reserva reserva : reservas) {
            System.out.println("Reserva: " + reserva.getDescricaoReserva()); // Alterado para chamar o método correto
        }
    }
}
