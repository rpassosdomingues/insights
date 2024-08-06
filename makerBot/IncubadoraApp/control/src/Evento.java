// Classe Evento que herda de Acao
public class Evento extends Acao {
    public Evento(String nomeProjeto, String acaoExecutada, String nomeEvento,
                  String local, String dia, String horarioInicio, String horarioTermino, String assunto) {
        super(nomeProjeto, acaoExecutada, nomeEvento, local, dia, horarioInicio, horarioTermino, assunto);
    }

    // Métodos adicionais específicos para Evento
}
