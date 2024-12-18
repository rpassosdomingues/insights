package src;

import java.time.LocalDate; // Importa a classe LocalDate
import java.time.LocalTime; // Importa a classe LocalTime
import java.util.ArrayList;
import java.util.List;

// Classe DICOM que herda de Evento
public class DICOM extends Evento {
    private String modo; // Como a atividade ou projeto foi idealizado e como está sendo desenvolvido
    private String motivo; // Objetivos da atividade ou projeto, expectativas, justificativa, resultados, etc.
    private String depoimento; // Depoimento da organização e/ou participantes sobre a importância da atividade
    private List<String> anexo; // Lista de anexos (fotos)

    // Construtor
    public DICOM(String nomeProjeto, String acaoExecutada, String nomeEvento,
                 String local, LocalDate dia, LocalTime horarioInicio, LocalTime horarioTermino, String assunto,
                 String modo, String motivo, String depoimento) {
        super(nomeProjeto, acaoExecutada, nomeEvento, local, dia, horarioInicio, horarioTermino, assunto);
        this.modo = modo;
        this.motivo = motivo;
        this.depoimento = depoimento;
        this.anexo = new ArrayList<>(); // Inicializa a lista de anexos
    }

    // Getters e Setters
    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDepoimento() {
        return depoimento;
    }

    public void setDepoimento(String depoimento) {
        this.depoimento = depoimento;
    }

    public List<String> getAnexo() {
        return anexo;
    }

    public void setAnexo(List<String> anexo) {
        this.anexo = anexo;
    }

    // Metodo para adicionar um anexo
    public void adicionarAnexo(String foto) {
        anexo.add(foto);
        System.out.println("Anexo adicionado com sucesso!");
    }
}
