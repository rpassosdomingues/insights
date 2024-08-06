import java.util.ArrayList;
import java.util.List;

// Classe Incubadora para gerenciar projetos e ações
class Incubadora {
    private List<Projeto> projetos;

    public Incubadora() {
        this.projetos = new ArrayList<>();
    }

    // Adiciona um projeto à lista de projetos
    public void adicionarProjeto(Projeto projeto) {
        projetos.add(projeto);
    }

    // Lista todos os projetos adicionados
    public void listarProjetos() {
        for (Projeto projeto : projetos) {
            System.out.println("Nome do Projeto: " + projeto.getNomeProjeto());
            System.out.println("Ação Executada: " + projeto.getAcaoExecutada());
            System.out.println("Nome do Evento: " + projeto.getNomeEvento());
            System.out.println("--------------");
        }
    }

    // Conta o número de ações executadas
    public int contaAcao(String acaoExecutada) {
        int count = 0;
        for (Projeto projeto : projetos) {
            if (projeto.getAcaoExecutada().equalsIgnoreCase(acaoExecutada)) {
                count++;
            }
        }
        return count;
    }

    // Classifica práticas com base em um critério
    public Map<String, List<Projeto>> classificaPratica(String criterio) {
        Map<String, List<Projeto>> classificacao = new HashMap<>();
        for (Projeto projeto : projetos) {
            String chave;
            switch (criterio.toLowerCase()) {
                case "local":
                    chave = ((Acao) projeto).getLocal();
                    break;
                case "dia":
                    chave = ((Acao) projeto).getDia();
                    break;
                case "assunto":
                    chave = ((Acao) projeto).getAssunto();
                    break;
                default:
                    chave = "Outro";
                    break;
            }
            classificacao.putIfAbsent(chave, new ArrayList<>());
            classificacao.get(chave).add(projeto);
        }
        return classificacao;
    }

    // Gera um relatório com todas as ações
    public void geraRelatorio() {
        System.out.println("Relatório de Projetos e Ações:");
        for (Projeto projeto : projetos) {
            System.out.println("Nome do Projeto: " + projeto.getNomeProjeto());
            System.out.println("Ação Executada: " + projeto.getAcaoExecutada());
            System.out.println("Nome do Evento: " + projeto.getNomeEvento());
            if (projeto instanceof Acao) {
                Acao acao = (Acao) projeto;
                System.out.println("Local: " + acao.getLocal());
                System.out.println("Dia: " + acao.getDia());
                System.out.println("Horário de Início: " + acao.getHorarioInicio());
                System.out.println("Horário de Término: " + acao.getHorarioTermino());
                System.out.println("Assunto: " + acao.getAssunto());
            }
            System.out.println("--------------");
        }
    }
}