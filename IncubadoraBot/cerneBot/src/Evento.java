package src;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Evento {
    private String nomeProjeto;
    private String acaoExecutada;
    private String nomeEvento;
    private String local;
    private LocalDate dia; // Data do evento
    private LocalTime horarioInicio; // Horário de início do evento
    private LocalTime horarioTermino; // Horário de término do evento
    private String assunto;

    // Lista estática para armazenar todos os eventos
    private static List<Evento> eventos = new ArrayList<>();

    // Construtor
    public Evento(String nomeProjeto, String acaoExecutada, String nomeEvento, String local,
                  LocalDate dia, LocalTime horarioInicio, LocalTime horarioTermino, String assunto) {
        // Validação: o horário de término deve ser após o horário de início
        if (horarioTermino.isBefore(horarioInicio)) {
            throw new IllegalArgumentException("O horário de término deve ser após o horário de início.");
        }
        this.nomeProjeto = nomeProjeto;
        this.acaoExecutada = acaoExecutada;
        this.nomeEvento = nomeEvento;
        this.local = local;
        this.dia = dia;
        this.horarioInicio = horarioInicio;
        this.horarioTermino = horarioTermino;
        this.assunto = assunto;
    }

    // Getters e Setters
    public String getNomeProjeto() {
        return nomeProjeto;
    }

    public void setNomeProjeto(String nomeProjeto) {
        this.nomeProjeto = nomeProjeto;
    }

    public String getAcaoExecutada() {
        return acaoExecutada;
    }

    public void setAcaoExecutada(String acaoExecutada) {
        this.acaoExecutada = acaoExecutada;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioTermino() {
        return horarioTermino;
    }

    public void setHorarioTermino(LocalTime horarioTermino) {
        this.horarioTermino = horarioTermino;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }
}
