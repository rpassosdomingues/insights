package src;

import java.util.ArrayList;
import java.util.List;

public class GestaoAmbiental {
    private String acao;     // Declare the acao variable
    private String impacto;  // Declare the impacto variable

    // Lista estática para armazenar todas as gestões ambientais
    private static List<GestaoAmbiental> gestoesAmbientais = new ArrayList<>();

    // Constructor
    public GestaoAmbiental(String acao, String impacto) {
        this.acao = acao;
        this.impacto = impacto;
    }

    // Método para cadastrar uma nova gestão ambiental
    public void cadastrarGestaoAmbiental(GestaoAmbiental gestaoAmbiental) {
        gestoesAmbientais.add(gestaoAmbiental);
        System.out.println("Gestão Ambiental cadastrada com sucesso!");
    }

    // Método para obter a ação
    public String getAcao() {
        return acao;
    }

    // Método para obter o impacto
    public String getImpacto() {
        return impacto;
    }

    // Método para agendar uma ação ambiental
    public void agendarAcao(String data) {
        System.out.println("Ação ambiental '" + acao + "' agendada para " + data);
    }

    // Método para listar todas as gestões ambientais cadastradas
    public static void listarGestoesAmbientais() {
        for (GestaoAmbiental gestao : gestoesAmbientais) {
            System.out.println("Ação: " + gestao.getAcao() + ", Impacto: " + gestao.getImpacto());
        }
    }
}
