// Classe principal para testar a funcionalidade
public class Main {
    public static void main(String[] args) {
        Incubadora incubadora = new Incubadora();

        // Adiciona alguns projetos à incubadora
        incubadora.adicionarProjeto(new Acao("Projeto A", "Ação 1", "Evento 1", "Local 1", "Dia 1", "08:00", "10:00", "Assunto 1"));
        incubadora.adicionarProjeto(new Acao("Projeto B", "Ação 2", "Evento 2", "Local 2", "Dia 2", "10:00", "12:00", "Assunto 2"));
        incubadora.adicionarProjeto(new Acao("Projeto C", "Ação 1", "Evento 3", "Local 1", "Dia 3", "14:00", "16:00", "Assunto 1"));

        // Lista todos os projetos
        incubadora.listarProjetos();

        // Conta ações específicas
        int count = incubadora.contaAcao("Ação 1");
        System.out.println("Número de Ações 1: " + count);

        // Classifica práticas por local
        Map<String, List<Projeto>> classificacaoPorLocal = incubadora.classificaPratica("local");
        System.out.println("Classificação por Local:");
        for (Map.Entry<String, List<Projeto>> entry : classificacaoPorLocal.entrySet()) {
            System.out.println("Local: " + entry.getKey());
            for (Projeto projeto : entry.getValue()) {
                System.out.println("  - " + projeto.getNomeProjeto());
            }
        }

        // Gera um relatório com todas as ações
        incubadora.geraRelatorio();
    }
}