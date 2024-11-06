package src;

import java.time.LocalDate; // Importa a classe LocalDate
import java.time.LocalTime; // Importa a classe LocalTime
import java.util.ArrayList;
import java.util.List;

public class Arte extends DICOM {
    private String instagram; // Pergunta sobre Instagram
    private String linkedin;  // Pergunta sobre LinkedIn
    private boolean artePronta; // Indica se a arte já está pronta
    private boolean legendaPronta; // Indica se a legenda já está pronta
    private List<String> arteAnexo; // Lista para anexar arte
    private List<String> legendaAnexo; // Lista para anexar legenda

    // Construtor
    public Arte(String nomeProjeto, String acaoExecutada, String nomeEvento,
                String local, LocalDate dia, LocalTime horarioInicio, LocalTime horarioTermino, String assunto,
                String modo, String motivo, String depoimento,
                String instagram, String linkedin, boolean artePronta, boolean legendaPronta) {
        super(nomeProjeto, acaoExecutada, nomeEvento, local, dia, horarioInicio, horarioTermino, assunto, modo, motivo, depoimento);
        this.instagram = instagram;
        this.linkedin = linkedin;
        this.artePronta = artePronta;
        this.legendaPronta = legendaPronta;
        this.arteAnexo = new ArrayList<>(); // Inicializa a lista de anexos de arte
        this.legendaAnexo = new ArrayList<>(); // Inicializa a lista de anexos de legenda
    }

    // Getters e Setters
    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public boolean isArtePronta() {
        return artePronta;
    }

    public void setArtePronta(boolean artePronta) {
        this.artePronta = artePronta;
    }

    public boolean isLegendaPronta() {
        return legendaPronta;
    }

    public void setLegendaPronta(boolean legendaPronta) {
        this.legendaPronta = legendaPronta;
    }

    public List<String> getArteAnexo() {
        return arteAnexo;
    }

    public void setArteAnexo(List<String> arteAnexo) {
        this.arteAnexo = arteAnexo;
    }

    public List<String> getLegendaAnexo() {
        return legendaAnexo;
    }

    public void setLegendaAnexo(List<String> legendaAnexo) {
        this.legendaAnexo = legendaAnexo;
    }

    // Metodo para adicionar um anexo de arte
    public void adicionarArteAnexo(String arte) {
        arteAnexo.add(arte);
        System.out.println("Anexo de arte adicionado com sucesso!");
    }

    // Metodo para adicionar um anexo de legenda
    public void adicionarLegendaAnexo(String legenda) {
        legendaAnexo.add(legenda);
        System.out.println("Anexo de legenda adicionado com sucesso!");
    }
}
