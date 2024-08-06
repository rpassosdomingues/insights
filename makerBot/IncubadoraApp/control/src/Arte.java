// Classe Arte que herda de DICOM
public class Arte extends DICOM {
    public Arte(String nomeProjeto, String acaoExecutada, String nomeEvento,
                String local, String dia, String horarioInicio, String horarioTermino, String assunto) {
        super(nomeProjeto, acaoExecutada, nomeEvento, local, dia, horarioInicio, horarioTermino, assunto);
    }

    // Métodos adicionais específicos para Arte
}
