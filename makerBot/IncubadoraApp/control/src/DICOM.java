// Classe DICOM que herda de Evento
public class DICOM extends Evento {
    public DICOM(String nomeProjeto, String acaoExecutada, String nomeEvento,
                 String local, String dia, String horarioInicio, String horarioTermino, String assunto) {
        super(nomeProjeto, acaoExecutada, nomeEvento, local, dia, horarioInicio, horarioTermino, assunto);
    }

    // Métodos adicionais específicos para DICOM
}
