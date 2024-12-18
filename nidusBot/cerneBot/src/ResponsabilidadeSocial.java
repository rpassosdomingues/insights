package src;

import java.util.ArrayList;
import java.util.List;

public class ResponsabilidadeSocial {
    private String projetoSocial;
    private String impacto;

    // Lista estática para armazenar todas as responsabilidades sociais
    private static List<ResponsabilidadeSocial> responsabilidadesSociais = new ArrayList<>();

    // Construtor
    public ResponsabilidadeSocial(String projetoSocial, String impacto) {
        this.projetoSocial = projetoSocial;
        this.impacto = impacto;
    }

    // Método para cadastrar uma nova responsabilidade social
    public void cadastrarResponsabilidadeSocial(ResponsabilidadeSocial responsabilidadeSocial) {
        responsabilidadesSociais.add(responsabilidadeSocial);
        System.out.println("Responsabilidade Social cadastrada com sucesso!");
    }

    // Método para obter o projeto social
    public String getProjetoSocial() {
        return projetoSocial;
    }

    // Método para obter o impacto
    public String getImpacto() {
        return impacto;
    }

    // Método para agendar ação social
    public void agendarAcaoSocial(String data) {
        System.out.println("Ação social '" + projetoSocial + "' agendada para " + data);
    }

    // Método para listar todas as responsabilidades sociais cadastradas
    public static void listarResponsabilidadesSociais() {
        for (ResponsabilidadeSocial resp : responsabilidadesSociais) {
            System.out.println("Projeto Social: " + resp.getProjetoSocial() + ", Impacto: " + resp.getImpacto());
        }
    }
}
