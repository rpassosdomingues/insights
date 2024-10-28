package src;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVHandler {

    // Método genérico para salvar qualquer tipo de dado no CSV
    public static <T> void salvarDadosCSV(String nomeArquivo, List<T> dados, String[] cabecalhos) {
        try (FileWriter writer = new FileWriter(nomeArquivo, true)) {
            // Se o arquivo estiver vazio, escrever o cabeçalho
            if (dados.size() == 1) {
                for (String cabecalho : cabecalhos) {
                    writer.append(cabecalho).append(";");
                }
                writer.append("\n");
            }

            // Escrever os dados
            for (T dado : dados) {
                writer.append(dado.toString()).append("\n");
            }

        } catch (IOException e) {
            System.out.println("Erro ao salvar dados no CSV: " + e.getMessage());
        }
    }
}
