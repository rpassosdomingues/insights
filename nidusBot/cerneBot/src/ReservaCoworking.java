package src;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaCoworking extends Reserva {
    private boolean necessitaProjetor;

    // Lista estática para armazenar reservas de coworking
    private static List<ReservaCoworking> reservasCoworking = new ArrayList<>();

    public ReservaCoworking(String solicitante, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, String descricaoReserva, boolean necessitaProjetor) {
        super(solicitante, dataHoraInicio, dataHoraFim, descricaoReserva);
        this.necessitaProjetor = necessitaProjetor;
    }

    public boolean isNecessitaProjetor() {
        return necessitaProjetor;
    }

    // Metodo para cadastrar a reserva de coworking
    public void cadastrarReservaCoworking() {
        reservasCoworking.add(this); // Adiciona a instância atual à lista de reservas de coworking
        System.out.println("Reserva de coworking cadastrada com sucesso!");
    }

    // Metodo para obter todas as reservas de coworking
    public static List<ReservaCoworking> getReservasCoworking() {
        return reservasCoworking;
    }
}
