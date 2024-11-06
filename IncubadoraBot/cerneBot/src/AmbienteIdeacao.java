package src;

import java.util.ArrayList;
import java.util.List;

public class AmbienteIdeacao {
    private String nomeEspaco;

    // Lista estática para armazenar todos os ambientes de ideação
    private static List<AmbienteIdeacao> ambientesIdeacao = new ArrayList<>();

    // Construtor
    public AmbienteIdeacao(String nomeEspaco) {
        this.nomeEspaco = nomeEspaco;
    }

    // Getter para o método getNomeEspaco()
    public String getNomeEspaco() {
        return nomeEspaco;
    }

    // Método para cadastrar um novo ambiente de ideação
    public void cadastrarAmbienteIdeacao(AmbienteIdeacao ambienteIdeacao) {
        ambientesIdeacao.add(ambienteIdeacao); // Adiciona o ambiente à lista
        System.out.println("Ambiente de Ideação cadastrado com sucesso!");
    }

    // Método para listar todos os ambientes de ideação cadastrados
    public static void listarAmbientesIdeacao() {
        for (AmbienteIdeacao ambiente : ambientesIdeacao) {
            System.out.println(ambiente.getNomeEspaco());
        }
    }
}
