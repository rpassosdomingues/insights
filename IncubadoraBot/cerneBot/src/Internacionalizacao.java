package src;

import java.util.ArrayList;
import java.util.List;

public class Internacionalizacao {
    private String pais;
    private String projetoInternacional;

    // Lista estática para armazenar todas as internacionalizações
    private static List<Internacionalizacao> internacionalizacoes = new ArrayList<>();

    public Internacionalizacao(String pais, String projetoInternacional) {
        this.pais = pais;
        this.projetoInternacional = projetoInternacional;
    }

    // Método para cadastrar uma nova internacionalização
    public void cadastrarInternacionalizacao(Internacionalizacao internacionalizacao) {
        internacionalizacoes.add(internacionalizacao);
        System.out.println("Internacionalização cadastrada com sucesso!");
    }

    // Método para obter o país
    public String getPais() {
        return pais;
    }

    // Método para obter o projeto internacional
    public String getProjetoInternacional() {
        return projetoInternacional;
    }

    // Método para planejar colaboração
    public void planejarColaboracao(String data) {
        System.out.println("Colaboração internacional com o país '" + pais + "' planejada para " + data);
    }

    // Método para listar todas as internacionalizações cadastradas
    public static void listarInternacionalizacoes() {
        for (Internacionalizacao intern : internacionalizacoes) {
            System.out.println("País: " + intern.getPais() + ", Projeto: " + intern.getProjetoInternacional());
        }
    }
}
