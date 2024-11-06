package src;

import java.util.ArrayList;
import java.util.List;

public class GestaoEstrategica {
    private String objetivo;

    // Lista estática para armazenar todas as gestões estratégicas
    private static List<GestaoEstrategica> gestoesEstrategicas = new ArrayList<>();

    // Construtor
    public GestaoEstrategica(String objetivo) {
        this.objetivo = objetivo;
    }

    // Getter para o método getObjetivo()
    public String getObjetivo() {
        return objetivo;
    }

    // Método para cadastrar uma nova gestão estratégica
    public void cadastrarGestaoEstrategica(GestaoEstrategica gestaoEstrategica) {
        gestoesEstrategicas.add(gestaoEstrategica);
        System.out.println("Gestão Estratégica cadastrada com sucesso!");
    }

    // Método para listar todas as gestões estratégicas cadastradas
    public static void listarGestoesEstrategicas() {
        for (GestaoEstrategica gestao : gestoesEstrategicas) {
            System.out.println("Objetivo: " + gestao.getObjetivo());
        }
    }
}
