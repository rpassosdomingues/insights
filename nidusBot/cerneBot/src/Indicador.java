package src;

import java.util.HashMap;
import java.util.Map;

public class Indicador {
    private static final Map<Praticas, Indicador> indicadoresMap = new HashMap<>();

    private Praticas praticaChave;
    private String descricao;
    private double resultado;

    public Indicador(Praticas praticaChave, String descricao, double resultado) {
        this.praticaChave = praticaChave;
        this.descricao = descricao;
        this.resultado = resultado;

        // Adiciona o indicador ao mapa
        indicadoresMap.put(praticaChave, this);
    }

    public static Indicador getIndicador(Praticas pratica) {
        return indicadoresMap.get(pratica);
    }

    public Praticas getPraticaChave() {
        return praticaChave;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }
}
