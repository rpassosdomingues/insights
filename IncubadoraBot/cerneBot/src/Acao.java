package src;

import java.time.LocalDate; // Importar para usar LocalDate
import java.time.format.DateTimeFormatter; // Importar para formatação de data

public class Acao extends Projeto {
    private String local;
    private LocalDate dia; // Declare a variável dia aqui
    private String horarioInicio;
    private String horarioTermino;
    private String assunto;

    public Acao(String nomeProjeto, String descricao, String local, String diaInput,
                String horarioInicio, String horarioTermino, String assunto) {
        super(nomeProjeto, descricao, diaInput); // Passa a string diretamente para o construtor de Projeto
        this.local = local;

        // Armazenar a data como LocalDate
        this.dia = LocalDate.parse(diaInput, DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Converter diaInput para LocalDate

        this.horarioInicio = horarioInicio;
        this.horarioTermino = horarioTermino;
        this.assunto = assunto;
    }

    // Getters e Setters
    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public LocalDate getDia() {
        return dia; // Retorna o dia como LocalDate
    }

    public void setDia(String diaInput) {
        // Aceita uma string e converte para LocalDate
        this.dia = LocalDate.parse(diaInput, DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Converte e armazena
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioTermino() {
        return horarioTermino;
    }

    public void setHorarioTermino(String horarioTermino) {
        this.horarioTermino = horarioTermino;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }
}
