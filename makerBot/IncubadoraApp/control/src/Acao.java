// Classe Acao que herda de Projeto
public class Acao extends Projeto {
    private String local;
    private String dia;
    private String horarioInicio;
    private String horarioTermino;
    private String assunto;

    public Acao(String nomeProjeto, String acaoExecutada, String nomeEvento,
                String local, String dia, String horarioInicio, String horarioTermino, String assunto) {
        super(nomeProjeto, acaoExecutada, nomeEvento);
        this.local = local;
        this.dia = dia;
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

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
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
