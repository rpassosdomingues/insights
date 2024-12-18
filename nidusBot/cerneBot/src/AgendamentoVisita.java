package src;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class AgendamentoVisita {
    private LocalDateTime dataVisita;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private int numeroVisitantes;
    private String solicitante;

    // Getters e Setters
    public LocalDateTime getDataVisita() {
        return dataVisita;
    }

    public void setDataVisita(LocalDateTime dataVisita) {
        this.dataVisita = dataVisita;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public int getNumeroVisitantes() {
        return numeroVisitantes;
    }

    public void setNumeroVisitantes(int numeroVisitantes) {
        this.numeroVisitantes = numeroVisitantes;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }
}
