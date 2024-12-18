package src;

import java.io.FileWriter; // Importar para gravar o arquivo
import java.io.IOException; // Importar para tratar exceções
import java.time.LocalDate; 
import java.time.format.DateTimeFormatter; 
import java.util.ArrayList; 
import java.util.List;

public class Projeto {
    private String nomeProjeto;
    private String descricao;
    private LocalDate dataInicio;

    private static List<Projeto> projetos = new ArrayList<>();

    public Projeto(String nomeProjeto, String descricao, String dataInicioInput) {
        this.nomeProjeto = nomeProjeto;
        this.descricao = descricao;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.dataInicio = LocalDate.parse(dataInicioInput, formatter);

        projetos.add(this);
    }

    public String getNomeProjeto() {
        return nomeProjeto;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public String formatarData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dataInicio.format(formatter);
    }

    public static List<Projeto> listarProjetos() {
        return projetos;
    }

}
