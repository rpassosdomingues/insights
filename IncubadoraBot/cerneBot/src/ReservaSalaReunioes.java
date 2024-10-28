package src;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaSalaReunioes extends Reserva {
    private int numeroPessoas;
    private boolean necessitaTransmissao;

    // Lista estática para armazenar reservas de salas de reuniões
    private static List<ReservaSalaReunioes> reservasSalaReunioes = new ArrayList<>();

    public ReservaSalaReunioes(String solicitante, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, String descricaoReserva, int numeroPessoas, boolean necessitaTransmissao) {
        super(solicitante, dataHoraInicio, dataHoraFim, descricaoReserva);
        this.numeroPessoas = numeroPessoas;
        this.necessitaTransmissao = necessitaTransmissao;
    }

    public int getNumeroPessoas() {
        return numeroPessoas;
    }

    public boolean isNecessitaTransmissao() {
        return necessitaTransmissao;
    }

    // Método para cadastrar a reserva de sala de reuniões
    public void cadastrarReservaSalaReunioes() {
        reservasSalaReunioes.add(this); // Adiciona a instância atual à lista de reservas de sala de reuniões
        System.out.println("Reserva de sala de reuniões cadastrada com sucesso!");
    }

    // Método para obter todas as reservas de salas de reuniões
    public static List<ReservaSalaReunioes> getReservasSalaReunioes() {
        return reservasSalaReunioes;
    }
}
