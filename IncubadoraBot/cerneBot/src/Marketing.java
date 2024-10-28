package src;

public class Marketing {
    private Projeto projeto;      // Agora faz referência a Projeto
    private DICOM evento;         // Agora faz referência a DICOM
    private String impacto;        // Mantido

    // Construtor atualizado para aceitar Projeto e DICOM
    public Marketing(Projeto projeto, DICOM evento, String impacto) {
        this.projeto = projeto;
        this.evento = evento;
        this.impacto = impacto;
    }

    public Projeto getProjeto() {
        return projeto; // Retorna o projeto associado
    }

    public DICOM getEvento() {
        return evento; // Retorna o evento associado
    }

    public String getImpacto() {
        return impacto; // Retorna o impacto
    }

    public void agendarCampanha(String data) {
        System.out.println("Campanha de marketing para o projeto '" + projeto.getNomeProjeto() + "' agendada para " + data);
    }
}
