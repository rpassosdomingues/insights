package src;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Reserva {
    protected String solicitante;
    protected LocalDateTime dataHoraInicio;
    protected LocalDateTime dataHoraFim;
    protected String descricaoReserva;

    // Lista privada para armazenar as reservas
    private static List<Reserva> reservas = new ArrayList<>();

    public Reserva(String solicitante, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, String descricaoReserva) {
        this.solicitante = solicitante;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.descricaoReserva = descricaoReserva;
    }

    // Método para cadastrar a reserva
    public void cadastrarReserva() {
        reservas.add(this); // Adiciona a instância atual à lista de reservas
        System.out.println("Reserva cadastrada com sucesso!");
    }

    public String getSolicitante() {
        return solicitante;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public String getDescricaoReserva() {
        return descricaoReserva;
    }

    // Metodo para obter todas as reservas
    public static List<Reserva> getReservas() {
        return reservas;
    }
}
